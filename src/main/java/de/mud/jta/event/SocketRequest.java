package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Notification of a socket request. Send this message if the system
 * should connect or disconnect.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: SocketRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class SocketRequest implements PluginMessage {
    final String host;
    int port;

    /**
     * Create a new disconnect message
     */
    public SocketRequest() {
        host = null;
    }

    /**
     * Create a new connect message
     */
    public SocketRequest(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Tell all listeners that we would like to connect.
     *
     * @param pl the list of plugin message listeners
     * @return the terminal type or null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof SocketListener) {
            try {
                if (host != null) {
                    ((SocketListener) pl).connect(host, port);
                } else {
                    ((SocketListener) pl).disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
