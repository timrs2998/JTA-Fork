package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Notify all listeners that we on or offline.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: OnlineStatus.java 499 2005-09-29 08:24:54Z leo $
 */
public class OnlineStatus implements PluginMessage {
    protected final boolean online;

    /**
     * Create a new online status message with the specified value.
     */
    public OnlineStatus(boolean online) {
        this.online = online;
    }

    /**
     * Notify the listers about the online status.
     *
     * @param pl the list of plugin message listeners
     * @return the window size or null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof OnlineStatusListener) {
            if (online) {
                ((OnlineStatusListener) pl).online();
            } else {
                ((OnlineStatusListener) pl).offline();
            }
        }
        return null;
    }
}
