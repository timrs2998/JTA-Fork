package de.mud.flash;

import de.mud.terminal.VDUBuffer;
import de.mud.terminal.VDUDisplay;
import de.mud.terminal.VDUInput;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlashTerminal implements VDUDisplay, Runnable {
    private final static Logger logger = Logger.getLogger(FlashTerminal.class.getName());

    private boolean simpleMode = true;
    private boolean terminalReady = false;
    private VDUBuffer buffer;
    private BufferedWriter writer;
    private BufferedReader reader;

    /**
     * A list of colors used for representation of the display
     */
    private final String[] color = {"#000000", "#ff0000", "#00ff00", "#ffff00", "#0000ff", "#ff00ff", "#00ffff",
            "#ffffff", null, // bold color
            null, // inverted color
    };

    public void start(Socket flashSocket) {
        try {
            logger.warning("FlashTerminal: got connection: " + flashSocket);
            writer = new BufferedWriter(new OutputStreamWriter(flashSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(flashSocket.getInputStream()));
            (new Thread(this)).run();
        } catch (IOException e) {
            logger.severe("FlashTerminal: unable to accept connection.");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void updateScrollBar() {
        // dont do anything... or?
    }

    protected void disconnect() {
        // do nothing by default
    }

    /**
     * Output performance information
     *
     * @param msg message from the flash client
     */
    private void perf(String msg) {
        logger.log(Level.WARNING, String.valueOf(System.currentTimeMillis() + msg));
    }

    public void run() {
        char[] buf = new char[1024];
        int n = 0;
        do {
            try {
                logger.fine("FlashTerminal: waiting for keyboard input ...");

                // read from flash frontend
                n = reader.read(buf);
                if (n > 0 && buf[0] == '<') {
                    handleXMLCommand(new String(buf, 0, n - 1));
                    continue;
                }

                logger.fine("FlashTerminal: got " + n + " keystokes: " + (n > 0 ? new String(buf, 0, n) : ""));

                if (n > 0 && (buffer instanceof VDUInput)) {
                    if (simpleMode) {
                        // in simple mode simply write the data to the remote host
                        // we have to convert the chars to bytes ...
                        byte[] tmp = new byte[n];
                        for (int i = 0; i < n - 1; i++) {
                            tmp[i] = (byte) buf[i];
                        }
                        ((VDUInput) buffer).write(tmp);
                    } else {
                        // write each key for it's own
                        for (int i = 0; i < n - 1; i++) {
                            ((VDUInput) buffer).keyTyped((int) buf[i], buf[i], 0);
                        }
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "FlashTerminal: i/o exception reading keyboard input");
                logger.log(Level.SEVERE, e.toString(), e);
            }
        } while (n >= 0);
        logger.fine("FlashTerminal: end of keyboard input");
        disconnect();
    }

    private final SAXBuilder builder = new SAXBuilder();

    /**
     * Handle XML Commands sent by the remote host.
     *
     * @param xml string containing the xml commands
     */
    private void handleXMLCommand(String xml) throws IOException {
        logger.fine("handleXMLCommand(" + xml + ")");
        StringReader src = new StringReader("<root>" + xml.replace('\0', ' ') + "</root>");
        try {
            Element root = builder.build(src).getRootElement();
            for (Object o : root.getChildren()) {
                Element command = (Element) o;
                String name = command.getName();
                if ("mode".equals(name)) {
                    simpleMode = "true".equals(command.getAttribute("simple").getValue().toLowerCase());
                } else if ("timestamp".equals(name)) {
                    perf(command.getAttribute("msg").getValue());
                } else if ("start".equals(name)) {
                    terminalReady = true;
                    buffer.update[0] = true;
                    redraw();
                }
            }
        } catch (JDOMException e) {
            logger.log(Level.SEVERE, "Error reading command");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    // placeholder for the terminal element and the xml outputter
    private final Element terminal = new Element("terminal");
    private final XMLOutputter xmlOutputter = new XMLOutputter();

    /**
     * Redraw terminal (send new/changed terminal lines to flash frontend).
     */
    public void redraw() {
        logger.fine("FlashTerminal: redraw()");

        if (terminalReady && writer != null) {
            try {
                // remove children from terminal
                for (Object obj : terminal.getChildren()) {
                    Element child = (Element) obj;
                    terminal.removeChild(child.getName());
                    terminal.removeChild(child.getName(), child.getNamespace());
                }

                if (simpleMode) {
                    Element result = redrawSimpleTerminal(terminal);
                    if (!result.getChildren().isEmpty()) {
                        xmlOutputter.output(result, writer);
                    }
                } else {
                    xmlOutputter.output(redrawFullTerminal(terminal), writer);
                }
                writer.write(0);
                writer.flush();
                logger.fine("FlashTerminal: flushed data ...");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "FlashTerminal: error writing to client");
                logger.log(Level.SEVERE, e.toString(), e);
                writer = null;
            }
        }
    }

    /**
     * The simple terminal only draws new lines and ignores
     * changes on lines aready written.
     *
     * @param terminal
     * @return
     */
    private Element redrawSimpleTerminal(Element terminal) {
        terminal.setAttribute("simple", "true");

        int checkPoint = buffer.scrollMarker < 0 ? 0 : buffer.scrollMarker;

        // first check whether our check point is in the back buffer
        while (checkPoint < buffer.screenBase) {
            terminal.addContent(redrawLine(0, checkPoint++));
        }

        // then dive into the screen area ...
        while (checkPoint < buffer.bufSize) {
            int line = checkPoint - (buffer.screenBase - 1);
            if (line > buffer.getCursorRow()) {
                break;
            }
            terminal.addContent(redrawLine(0, checkPoint++));
        }
        // update scroll marker
        buffer.scrollMarker = checkPoint;

        buffer.update[0] = false;
        return terminal;
    }

    /**
     * Redraw a complete terminal with updates on all visible lines.
     *
     * @param terminal the root terminal tag to add changed lines to
     * @return the final terminal tag
     */
    private Element redrawFullTerminal(Element terminal) {
        // cycle through buffer and create terminal update ...
        for (int l = 0; l < buffer.height; l++) {
            if (!buffer.update[0] && !buffer.update[l + 1]) {
                continue;
            }
            buffer.update[l + 1] = false;
            terminal.addContent(redrawLine(l, buffer.windowBase));
        }
        buffer.update[0] = false;
        return terminal;
    }

    /**
     * Redraw a sinle line by looking at chunks and formatting them.
     *
     * @param l    the current line
     * @param base the "window"-base within the buffer
     * @return an element with the formatted line
     */
    private Element redrawLine(int l, int base) {
        Element line = new Element("line");
        line.setAttribute("row", "" + l);

        // determine the maximum of characters we can print in one go
        for (int c = 0; c < buffer.width; c++) {
            int addr = 0;
            int currAttr = buffer.charAttributes[base + l][c];

            while ((c + addr < buffer.width) && ((buffer.charArray[base + l][c + addr] < ' ') || (buffer
                    .charAttributes[base + l][c + addr] == currAttr))) {
                if (buffer.charArray[base + l][c + addr] < ' ') {
                    buffer.charArray[base + l][c + addr] = ' ';
                    buffer.charAttributes[base + l][c + addr] = 0;
                    continue;
                }
                addr++;
            }

            if (addr > 0) {
                String tmp = new String(buffer.charArray[base + l], c, addr);
                // create new text node and make sure we insert &nbsp; (160)
                Text text = new Text(tmp.replace(' ', (char) 160));
                Element chunk = null;
                if ((currAttr & 0xfff) != 0) {
                    if ((currAttr & VDUBuffer.BOLD) != 0) {
                        chunk = addChunk(new Element("B"), chunk, text);
                    }
                    if ((currAttr & VDUBuffer.UNDERLINE) != 0) {
                        chunk = addChunk(new Element("U"), chunk, text);
                    }
                    if ((currAttr & VDUBuffer.INVERT) != 0) {
                        chunk = addChunk(new Element("I"), chunk, text);
                    }
                    if ((currAttr & VDUBuffer.COLOR_FG) != 0) {
                        String fg = color[((currAttr & VDUBuffer.COLOR_FG) >> 4) - 1];
                        Element font = new Element("FONT").setAttribute("COLOR", fg);
                        chunk = addChunk(font, chunk, text);
                    }
          /*
          if ((currAttr & buffer.COLOR_BG) != 0) {
            Color bg = color[((currAttr & buffer.COLOR_BG) >> 8) - 1];
          }
          */
                }
                if (chunk == null) {
                    line.addContent(text);
                } else {
                    line.addContent(chunk);
                }
            }
            c += addr - 1;
        }
        return line;
    }

    /**
     * Helper method to wrap a chunk or piece of text in another element.
     *
     * @param el    the element to put the text or chunk into
     * @param chunk a chunk of elements
     * @param text  a text element
     * @return a new chunk made up from the element
     */
    private Element addChunk(Element el, Element chunk, Text text) {
        if (chunk == null) {
            return el.addContent(text);
        } else {
            return el.addContent(chunk);
        }
    }

    /**
     * Set the VDUBuffer that contains the terminal screen and back-buffer
     *
     * @param buffer the terminal buffer
     */
    public void setVDUBuffer(VDUBuffer buffer) {
        this.buffer = buffer;
        if (simpleMode) {
            this.buffer.setCursorPosition(0, 23);
        }
        this.buffer.setDisplay(this);
        this.buffer.update[0] = true;
    }

    /**
     * Get the current buffer.
     *
     * @return the VDUBuffer
     */
    public VDUBuffer getVDUBuffer() {
        return buffer;
    }
}
