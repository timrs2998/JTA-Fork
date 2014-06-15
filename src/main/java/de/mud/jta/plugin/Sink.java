package de.mud.jta.plugin;

import de.mud.jta.FilterPlugin;
import de.mud.jta.Plugin;
import de.mud.jta.PluginBus;
import de.mud.jta.event.OnlineStatusListener;

import java.io.IOException;

/**
 * The terminal plugin represents the actual terminal where the
 * data will be displayed and the gets the keyboard input to sent
 * back to the remote host.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: Sink.java 499 2005-09-29 08:24:54Z leo $
 */
public class Sink extends Plugin
        implements FilterPlugin, Runnable {

    private final static int debug = 0;

    private Thread reader = null;

    public Sink(final PluginBus bus, final String id) {
        super(bus, id);
        // register an online status listener
        bus.registerPluginListener(new OnlineStatusListener() {
            public void online() {
                if (debug > 0) {
                    System.err.println("Terminal: online " + reader);
                }
                if (reader == null) {
                    reader = new Thread();
                    reader.start();
                }
            }

            public void offline() {
                if (debug > 0) {
                    System.err.println("Terminal: offline");
                }
                if (reader != null) {
                    reader = null;
                }
            }
        });
    }

    /**
     * Continuously read from our back end and drop the data.
     */
    public void run() {
        byte[] t, b = new byte[256];
        int n = 0;
        while (n >= 0) {
            try {
                n = read(b);
      /* drop the bytes into the sink :) */
            } catch (IOException e) {
                reader = null;
                break;
            }
        }
    }

    protected FilterPlugin source;

    public void setFilterSource(FilterPlugin source) {
        if (debug > 0) {
            System.err.println("Terminal: connected to: " + source);
        }
        this.source = source;
    }

    public FilterPlugin getFilterSource() {
        return source;
    }

    public int read(byte[] b) throws IOException {
        return source.read(b);
    }

    public void write(byte[] b) throws IOException {
        source.write(b);
    }
}
