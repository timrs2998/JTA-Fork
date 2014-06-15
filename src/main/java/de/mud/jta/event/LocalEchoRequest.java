package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Notification of the local echo property. The terminal should echo all
 * typed in characters locally of this is true.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: LocalEchoRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class LocalEchoRequest implements PluginMessage {
    protected boolean xecho = false;

    /**
     * Create a new local echo request with the specified value.
     */
    public LocalEchoRequest(boolean echo) {
        xecho = echo;
    }

    /**
     * Notify all listeners about the status of local echo.
     *
     * @param pl the list of plugin message listeners
     * @return always null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof LocalEchoListener) {
            ((LocalEchoListener) pl).setLocalEcho(xecho);
        }
        return null;
    }
}
