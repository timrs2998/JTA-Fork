package de.mud.jta.plugin;

import de.mud.jta.Plugin;
import de.mud.jta.PluginBus;
import de.mud.jta.VisualPlugin;
import de.mud.jta.event.ConfigurationListener;
import de.mud.jta.event.OnlineStatusListener;
import de.mud.jta.event.SocketListener;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;


/**
 * A simple plugin showing the current status of the application whether
 * it is online or not.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: Status.java 499 2005-09-29 08:24:54Z leo $
 */
public class Status extends Plugin implements VisualPlugin, Runnable {

    private final static int debug = 0;

    private final JLabel status;
    private JLabel host;
    private final JPanel sPanel;

    private String address, port;

    private String infoURL;
    private int interval;
    private Thread infoThread;

    private final Hashtable ports = new Hashtable();

    public Status(PluginBus bus, final String id) {
        super(bus, id);

        // setup the info
        bus.registerPluginListener((ConfigurationListener) config -> {
            infoURL = config.getProperty("Status", id, "info");
            if (infoURL != null) {
                host.setAlignmentX(JLabel.CENTER);
            }
            String tmp;
            if ((tmp = config.getProperty("Status", id, "font")) != null) {
                String font = tmp;
                int style = Font.PLAIN, fsize = 12;
                if ((tmp = config.getProperty("Status", id, "fontSize")) != null) {
                    fsize = Integer.parseInt(tmp);
                }
                String fontStyle = config.getProperty("Status", id, "fontStyle");
                if (fontStyle == null || "plain".equals(fontStyle)) {
                    style = Font.PLAIN;
                } else if ("bold".equals(fontStyle)) {
                    style = Font.BOLD;
                } else if ("italic".equals(fontStyle)) {
                    style = Font.ITALIC;
                } else if ("bold+italic".equals(fontStyle)) {
                    style = Font.BOLD | Font.ITALIC;
                }
                host.setFont(new Font(font, style, fsize));
            }

            if ((tmp = config.getProperty("Status", id, "foreground")) != null) {
                host.setForeground(Color.decode(tmp));
            }

            if ((tmp = config.getProperty("Status", id, "background")) != null) {
                host.setBackground(Color.decode(tmp));
            }

            if (config.getProperty("Status", id, "interval") != null) {
                try {
                    interval = Integer.parseInt(config.getProperty("Status", id, "interval"));
                    infoThread = new Thread(Status.this);
                    infoThread.start();
                } catch (NumberFormatException e) {
                    Status.this.error("interval is not a number");
                }
            }
        });


        // fill port hashtable
        ports.put("22", "ssh");
        ports.put("23", "telnet");
        ports.put("25", "smtp");

        sPanel = new JPanel(new BorderLayout());

        host = new JLabel("Not connected.", JLabel.LEFT);

        bus.registerPluginListener(new SocketListener() {
            public void connect(String addr, int p) {
                address = addr;
                if (address == null || address.isEmpty()) {
                    address = "<unknown host>";
                }
                if (ports.get("" + p) != null) {
                    port = (String) ports.get("" + p);
                } else {
                    port = "" + p;
                }
                if (infoURL == null) {
                    host.setText("Trying " + address + " " + port + " ...");
                }
            }

            public void disconnect() {
                if (infoURL == null) {
                    host.setText("Not connected.");
                }
            }
        });

        sPanel.add("Center", host);

        status = new JLabel("offline", JLabel.CENTER);

        bus.registerPluginListener(new OnlineStatusListener() {
            public void online() {
                status.setText("online");
                status.setForeground(Color.green);
                if (infoURL == null) {
                    host.setText("Connected to " + address + " " + port);
                }
                status.repaint();
            }

            public void offline() {

                status.setText("offline");
                status.setForeground(Color.red);
                if (infoURL == null) {
                    host.setText("Not connected.");
                }
                status.repaint();
            }
        });

        sPanel.add("East", status);

    }

    public void run() {
        URL url = null;
        try {
            url = new URL(infoURL);
        } catch (Exception e) {
            error("infoURL is not valid: " + e);
            infoURL = null;
            return;
        }

        while (url != null && infoThread != null) {
            try {
                BufferedReader content = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    String line;
                    while ((line = content.readLine()) != null) {
                        if (line.startsWith("#")) {
                            String color = line.substring(1, 7);
                            line = line.substring(8);
                            host.setForeground(Color.decode("#" + color));
                        }
                        host.setText(line);
                        Thread.sleep(10 * interval);
                    }
                } catch (IOException e) {
                    error("error while loading info ...");
                }
                Thread.sleep(100 * interval);
            } catch (Exception e) {
                error("error retrieving info content: " + e);
                e.printStackTrace();
                host.setForeground(Color.red);
                host.setText("error retrieving info content");
                infoURL = null;
                return;
            }
        }
    }

    public JComponent getPluginVisual() {
        return sPanel;
    }

    public JMenu getPluginMenu() {
        return null;
    }
}
