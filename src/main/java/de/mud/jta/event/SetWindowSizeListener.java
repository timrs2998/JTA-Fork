package de.mud.jta.event;

import de.mud.jta.PluginListener;

/**
 * This is the interface for a window size listener.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: WindowSizeListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface SetWindowSizeListener extends PluginListener {
    /**
     * Set the current window size of the terminal in rows and columns.
     */
    public void setWindowSize(int columns, int rows);
}
