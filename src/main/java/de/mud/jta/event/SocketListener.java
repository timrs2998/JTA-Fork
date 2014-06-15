package de.mud.jta.event;

import de.mud.jta.PluginListener;

import java.io.IOException;

/**
 * The socket listener should be implemented by plugins that want to know
 * when the whole systems connects or disconnects.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: SocketListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface SocketListener extends PluginListener {
    /**
     * Called if a connection should be established.
     */
    public void connect(String host, int port);

    /**
     * Called if the connection should be stopped.
     */
    public void disconnect() throws IOException;
}
