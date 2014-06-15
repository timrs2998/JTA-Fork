package de.mud.jta.event;

import de.mud.jta.Plugin;
import de.mud.jta.PluginListener;

/**
 * This is the interface for a focus status listener.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: FocusStatusListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface FocusStatusListener extends PluginListener {
    /**
     * Called if a plugin gained the input focus.
     */
    public void pluginGainedFocus(Plugin plugin);

    /**
     * Called if a plugin lost the input focus.
     */
    public void pluginLostFocus(Plugin plugin);
}
