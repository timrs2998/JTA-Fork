package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Request message for the current terminal type.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: TerminalTypeRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class TerminalTypeRequest implements PluginMessage {
    /**
     * Ask all terminal type listener about the terminal type and return
     * the first answer.
     *
     * @param pl the list of plugin message listeners
     * @return the terminal type or null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof TerminalTypeListener) {
            Object ret = ((TerminalTypeListener) pl).getTerminalType();
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
}
