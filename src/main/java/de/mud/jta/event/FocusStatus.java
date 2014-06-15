package de.mud.jta.event;

import de.mud.jta.Plugin;
import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

import java.awt.event.FocusEvent;

/**
 * Notify all listeners that a component has got the input focus.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: FocusStatus.java 499 2005-09-29 08:24:54Z leo $
 */
public class FocusStatus implements PluginMessage {
    protected final Plugin plugin;
    protected final FocusEvent event;

    /**
     * Create a new online status message with the specified value.
     */
    public FocusStatus(Plugin plugin, FocusEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    /**
     * Notify the listers about the focus status of the sending component.
     *
     * @param pl the list of plugin message listeners
     * @return null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof FocusStatusListener) {
            switch (event.getID()) {
                case FocusEvent.FOCUS_GAINED:
                    ((FocusStatusListener) pl).pluginGainedFocus(plugin);
                    break;
                case FocusEvent.FOCUS_LOST:
                    ((FocusStatusListener) pl).pluginLostFocus(plugin);
            }
        }
        return null;
    }
}
