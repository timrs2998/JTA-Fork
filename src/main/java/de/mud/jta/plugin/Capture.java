package de.mud.jta.plugin;

import de.mud.jta.*;
import de.mud.jta.event.ConfigurationListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A capture plugin that captures data and stores it in a
 * defined location. The location is specified as a plugin
 * configuration option Capture.url and can be used in
 * conjunction with the UploadServlet from the tools directory.
 * <p>
 * Parametrize the plugin carefully:<br>
 * <b>Capture.url</b> should contain a unique URL can may have
 * parameters for identifying the upload.<br>
 * <i>Example:</i> http://mg.mud.de/servlet/UpladServlet?id=12345
 * <p>
 * The actually captured data will be appended as the parameter
 * <b>content</b>.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: Capture.java 499 2005-09-29 08:24:54Z leo $
 */
public class Capture extends Plugin
        implements FilterPlugin, VisualPlugin, ActionListener {

    // this enables or disables the compilation of menu entries
    private final static boolean personalJava = false;

    // for debugging output
    private final static Logger logger = Logger.getLogger(Capture.class.getName());

    /**
     * The remote storage URL
     */
    protected final Hashtable remoteUrlList = new Hashtable();

    /**
     * The plugin menu
     */
    protected JMenu menu;
    protected JDialog errorDialog;
    protected JDialog fileDialog;
    protected JDialog doneDialog;

    /**
     * Whether the capture is currently enabled or not
     */
    protected boolean captureEnabled = false;

    // menu entries and the viewing frame/textarea
    private final JMenuItem start, stop, clear;
    private final JFrame frame;
    private final JTextArea textArea;
    private JTextField fileName;

    /**
     * Initialize the Capture plugin. This sets up the menu entries
     * and registers the plugin on the bus.
     */
    public Capture(final PluginBus bus, final String id) {
        super(bus, id);

        if (!personalJava) {

            // set up viewing frame
            frame = new JFrame("Java Telnet Applet: Captured Text");
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(textArea = new JTextArea(24, 80), BorderLayout.CENTER);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    frame.setVisible(false);
                }
            });
            frame.pack();

            // an error dialogue, in case the upload fails
            errorDialog = new JDialog(frame, "Error", true);
            errorDialog.getContentPane().setLayout(new BorderLayout());
            errorDialog.getContentPane().add(new JLabel("Cannot store data on remote server!"), BorderLayout.NORTH);
            JPanel panel = new JPanel();
            JButton button = new JButton("Close Dialog");
            panel.add(button);
            errorDialog.getContentPane().add(panel, BorderLayout.SOUTH);
            button.addActionListener(e -> errorDialog.setVisible(false));

            // an error dialogue, in case the upload fails
            doneDialog = new JDialog(frame, "Success", true);
            doneDialog.getContentPane().setLayout(new BorderLayout());
            doneDialog.getContentPane().add(new JLabel("Successfully saved data!"), BorderLayout.NORTH);
            panel = new JPanel();
            button = new JButton("Close Dialog");
            panel.add(button);
            doneDialog.getContentPane().add(panel, BorderLayout.SOUTH);
            button.addActionListener(e -> errorDialog.setVisible(false));


            fileDialog = new JDialog(frame, "Enter File Name", true);
            fileDialog.getContentPane().setLayout(new BorderLayout());
            ActionListener saveFileListener = e -> {
                String params = (String) remoteUrlList.get("URL.file.params.orig");
                params = params == null ? "" : params + "&";
                try {
                    remoteUrlList.put("URL.file.params", params + "file=" + URLEncoder.encode(fileName.getText(), "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                    logger.log(Level.SEVERE, e1.toString(), e1);
                }
                saveFile("URL.file");
                fileDialog.setVisible(false);
            };
            panel = new JPanel();
            panel.add(new JLabel("File Name: "));
            panel.add(fileName = new JTextField(30));
            fileName.addActionListener(saveFileListener);
            fileDialog.getContentPane().add(panel, BorderLayout.CENTER);
            panel = new JPanel();
            panel.add(button = new JButton("Cancel"));
            button.addActionListener(e -> fileDialog.setVisible(false));
            panel.add(button = new JButton("Save File"));
            button.addActionListener(saveFileListener);
            fileDialog.getContentPane().add(panel, BorderLayout.SOUTH);
            fileDialog.pack();

            // set up menu entries
            menu = new JMenu("Capture");
            start = new JMenuItem("Start");
            start.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logger.warning("Capture: start capturing");
                    captureEnabled = true;
                    start.setEnabled(false);
                    stop.setEnabled(true);
                }
            });
            menu.add(start);

            stop = new JMenuItem("Stop");
            stop.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logger.warning("Capture: stop capturing");
                    captureEnabled = false;
                    start.setEnabled(true);
                    stop.setEnabled(false);

                }
            });
            stop.setEnabled(false);
            menu.add(stop);

            clear = new JMenuItem("Clear");
            clear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logger.warning("Capture: cleared captured text");
                    textArea.setText("");
                }
            });
            menu.add(clear);
            menu.addSeparator();

            JMenuItem view = new JMenuItem("View/Hide Text");
            view.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logger.fine("view/hide text: " + frame.isVisible());
                    if (frame.isVisible()) {
                        frame.setVisible(false);
                    } else {
                        frame.setVisible(true);
                    }
                }
            });
            menu.add(view);


        } // !personalJava


        // configure the remote URL
        bus.registerPluginListener((ConfigurationListener) config -> {
            String tmp;

            JMenuItem save = new JMenuItem("Save As File");
            menu.add(save);

            if ((tmp = config.getProperty("Capture", id, "file.url")) != null) {
                try {
                    remoteUrlList.put("URL.file", new URL(tmp));
                    if ((tmp = config.getProperty("Capture", id, "file.params")) != null) {
                        remoteUrlList.put("URL.file.params.orig", tmp);
                    }

                    save.addActionListener(e -> fileDialog.setVisible(true));
                    save.setActionCommand("URL.file");
                } catch (MalformedURLException e) {
                    System.err.println("capture url invalid: " + e);
                }

            } else {
                save.setEnabled(false);
            }

            int i = 1;
            while ((tmp = config.getProperty("Capture", id, i + ".url")) != null) {
                try {
                    String urlID = "URL." + i;
                    URL remoteURL = new URL(tmp);
                    remoteUrlList.put(urlID, remoteURL);
                    if ((tmp = config.getProperty("Capture", id, i + ".params")) != null) {
                        remoteUrlList.put(urlID + ".params", tmp);
                    }
                    // use name if applicable or URL
                    if ((tmp = config.getProperty("Capture", id, i + ".name")) != null) {
                        save = new JMenuItem("Save As " + tmp);
                    } else {
                        save = new JMenuItem("Save As " + remoteURL.toString());
                    }
                    // enable menu entry
                    save.setEnabled(true);
                    save.addActionListener(Capture.this);
                    save.setActionCommand(urlID);
                    menu.add(save);
                    // count up
                    i++;
                } catch (MalformedURLException e) {
                    System.err.println("capture url invalid: " + e);
                }
            }
        });

        if (!personalJava) {
        }
    }

    public void actionPerformed(ActionEvent e) {
        String urlID = e.getActionCommand();
        logger.fine("Capture: storing text: "
                + urlID + ": "
                + remoteUrlList.get(urlID));
        saveFile(urlID);
    }

    private void saveFile(String urlID) {
        URL url = (URL) remoteUrlList.get(urlID);
        try {
            URLConnection urlConnection = url.openConnection();
            DataOutputStream out;
            BufferedReader in;

            // Let the RTS know that we want to do output.
            urlConnection.setDoInput(true);
            // Let the RTS know that we want to do output.
            urlConnection.setDoOutput(true);
            // No caching, we want the real thing.
            urlConnection.setUseCaches(false);
            // Specify the content type.
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // retrieve extra arguments
            // Send POST output.
            // send the data to the url receiver ...
            out = new DataOutputStream(urlConnection.getOutputStream());
            String content = (String) remoteUrlList.get(urlID + ".params");
            content = (content == null ? "" : content + "&") + "content=" + URLEncoder.encode(textArea.getText(), "UTF-8");
            logger.warning("Capture: " + content);
            out.writeBytes(content);
            out.flush();
            out.close();

            // retrieve response from the remote host and display it.
            logger.warning("Capture: reading response");
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String str;
            while (null != ((str = in.readLine()))) {
                System.out.println("Capture: " + str);
            }
            in.close();

            doneDialog.pack();
            doneDialog.setVisible(true);

        } catch (IOException ioe) {
            System.err.println("Capture: cannot store text on remote server: " + url);
            ioe.printStackTrace();
            JTextArea errorMsg = new JTextArea(ioe.toString(), 5, 30);
            errorMsg.setEditable(false);
            errorDialog.add(errorMsg, BorderLayout.CENTER);
            errorDialog.pack();
            errorDialog.setVisible(true);
        }
        logger.warning("Capture: storage complete: " + url);
    }

    // this is where we get the data from (left side in plugins list)
    protected FilterPlugin source;

    /**
     * The filter source is the plugin where Capture is connected to.
     * In the list of plugins this is the one to the left.
     *
     * @param source the next plugin
     */
    public void setFilterSource(FilterPlugin source) {
        logger.warning("Capture: connected to: " + source);
        this.source = source;
    }

    public FilterPlugin getFilterSource() {
        return source;
    }

    /**
     * Read data from the left side plugin, capture the content and
     * pass it on to the next plugin which called this method.
     *
     * @param b the buffer to store data into
     */
    public int read(byte[] b) throws IOException {
        int size = source.read(b);
        if (captureEnabled && size > 0) {
            String tmp = new String(b, 0, size);
            textArea.append(tmp);
        }
        return size;
    }

    /**
     * Write data to the backend but also append it to the capture buffer.
     *
     * @param b the buffer with data to write
     */
    public void write(byte[] b) throws IOException {
        if (captureEnabled) {
            textArea.append(new String(b));
        }
        source.write(b);
    }

    /**
     * The Capture plugin has no visual component that is embedded in
     * the JTA main frame, so this returns null.
     *
     * @return always null
     */
    public JComponent getPluginVisual() {
        return null;
    }

    /**
     * The Capture menu for the menu bar as configured in the constructor.
     *
     * @return the drop down menu
     */
    public JMenu getPluginMenu() {
        return menu;
    }

}
