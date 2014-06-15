package de.mud.jta.event;

import de.mud.jta.PluginListener;

/**
 * This is the interface for a online status listener.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: OnlineStatusListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface OnlineStatusListener extends PluginListener {
    /**
     * Called when the system is online.
     */
    public void online();

    /**
     * Called when the system is offline.
     */
    public void offline();
}
