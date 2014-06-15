package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Request the current window size of the terminal.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: WindowSizeRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class WindowSizeRequest implements PluginMessage {
    /**
     * Return the size of the window
     *
     * @param pl the list of plugin message listeners
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof WindowSizeListener) {
            Object ret = ((WindowSizeListener) pl).getWindowSize();
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
}
