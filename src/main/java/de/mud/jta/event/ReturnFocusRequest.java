package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Notify listeners that the focus is to be returned to whoever wants it.
 * <p>
 * Implemented after a suggestion by Dave &lt;david@mirrabooka.com&gt;
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 */
public class ReturnFocusRequest implements PluginMessage {
    /**
     * Create a new return focus request.
     */
    public ReturnFocusRequest() {
    }

    /**
     * Notify all listeners about return focus message.
     *
     * @param pl the list of plugin message listeners
     * @return always null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof ReturnFocusListener) {
            ((ReturnFocusListener) pl).returnFocus();
        }
        return null;
    }
}
