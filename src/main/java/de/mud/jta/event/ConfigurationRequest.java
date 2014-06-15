package de.mud.jta.event;

import de.mud.jta.PluginConfig;
import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;


/**
 * Configuration request message. Subclassing this message can be used to
 * make the configuration more specific and efficient.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: ConfigurationRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class ConfigurationRequest implements PluginMessage {
    final PluginConfig config;

    public ConfigurationRequest(PluginConfig config) {
        this.config = config;
    }

    /**
     * Notify all listeners of a configuration event.
     *
     * @param pl the list of plugin message listeners
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof ConfigurationListener) {
            ((ConfigurationListener) pl).setConfiguration(config);
        }
        return null;
    }
}
