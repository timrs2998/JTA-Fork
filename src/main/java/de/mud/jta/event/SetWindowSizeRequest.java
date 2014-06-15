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
public class SetWindowSizeRequest implements PluginMessage {

    private final int columns;
    private final int rows;

    public SetWindowSizeRequest(int c, int r) {
        rows = r;
        columns = c;
    }

    /**
     * Set the new size of the window
     *
     * @param pl the list of plugin message listeners
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof SetWindowSizeListener) {
            ((SetWindowSizeListener) pl).setWindowSize(columns, rows);
        }
        return null;
    }
}
