package de.mud.jta;

import de.mud.telnet.ScriptHandler;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * To write a program using the wrapper
 * you may use the following piece of code as an example:
 * <PRE>
 * TelnetWrapper telnet = new TelnetWrapper();
 * try {
 * telnet.connect(args[0], 23);
 * telnet.login("user", "password");
 * telnet.setPrompt("user@host");
 * telnet.waitfor("Terminal type?");
 * telnet.send("dumb");
 * System.out.println(telnet.send("ls -l"));
 * } catch(java.io.IOException e) {
 * e.printStackTrace();
 * }
 * </PRE>
 * Please keep in mind that the password is visible for anyone who can
 * download the class file. So use this only for public accounts or if
 * you are absolutely sure nobody can see the file.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei\u00dfner
 * @version $Id: Wrapper.java 499 2005-09-29 08:24:54Z leo $
 */


public class Wrapper {
    private static final Logger logger = Logger.getLogger(Wrapper.class.getName());

    protected ScriptHandler scriptHandler = new ScriptHandler();
    private Thread reader;

    protected InputStream in;
    protected OutputStream out;
    protected Socket socket;
    protected String host;
    protected int port = 23;
    protected Vector script = new Vector();

    /**
     * Connect the socket and open the connection.
     */
    public void connect(String host, int port) throws IOException {
        logger.log(Level.WARNING, "Wrapper: connect(" + host + "," + port + ")");
        try {
            socket = new java.net.Socket(host, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (Exception e) {
            System.err.println("Wrapper: " + e);
            disconnect();
            throw ((IOException) e);
        }
    }

    /**
     * Disconnect the socket and close the connection.
     */
    public void disconnect() throws IOException {
        logger.log(Level.WARNING, "Wrapper: disconnect()");
        if (socket != null) {
            socket.close();
        }
    }

    /**
     * Login into remote host. This is a convenience method and only
     * works if the prompts are "login:" and "Password:".
     *
     * @param user the user name
     * @param pwd  the password
     */
    public void login(String user, String pwd) throws IOException {
        waitfor("login:");        // throw output away
        send(user);
        waitfor("Password:");    // throw output away
        send(pwd);
    }

    /**
     * Set the prompt for the send() method.
     */
    private String prompt = null;

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    /**
     * Send a command to the remote host. A newline is appended and if
     * a prompt is set it will return the resulting data until the prompt
     * is encountered.
     *
     * @param cmd the command
     * @return output of the command or null if no prompt is set
     */
    public String send(String cmd) throws IOException {
        return null;
    }

    /**
     * Wait for a string to come from the remote host and return all
     * that characters that are received until that happens (including
     * the string being waited for).
     *
     * @param match the string to look for
     * @return skipped characters
     */

    public String waitfor(String[] searchElements) throws IOException {
        ScriptHandler[] handlers = new ScriptHandler[searchElements.length];
        for (int i = 0; i < searchElements.length; i++) {
            // initialize the handlers
            handlers[i] = new ScriptHandler();
            handlers[i].setup(searchElements[i]);
        }

        byte[] b1 = new byte[1];
        int n = 0;
        StringBuilder ret = new StringBuilder();
        String current;

        while (n >= 0) {
            n = read(b1);
            if (n > 0) {
                current = new String(b1, 0, n);
                logger.log(Level.FINE, current);
                ret.append(current);
                for (ScriptHandler handler : handlers) {
                    if (handler.match(ret.toString().getBytes(), ret.length())) {
                        return ret.toString();
                    } // if
                } // for
            } // if
        } // while
        return null; // should never happen
    }

    public String waitfor(String match) throws IOException {
        String[] matches = new String[1];

        matches[0] = match;
        return waitfor(matches);
    }

    /**
     * Read data from the socket and use telnet negotiation before returning
     * the data read.
     *
     * @param b the input buffer to read in
     * @return the amount of bytes read
     */
    public int read(byte[] b) throws IOException {
        return -1;
    }

    /**
     * Write data to the socket.
     *
     * @param b the buffer to be written
     */
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    public String getTerminalType() {
        return "dumb";
    }

    public Dimension getWindowSize() {
        return new Dimension(80, 24);
    }

    public void setLocalEcho(boolean echo) {
        logger.log(Level.WARNING, "local echo " + (echo ? "on" : "off"));
    }
}
