package de.mud.jta.event;

import de.mud.jta.PluginConfig;
import de.mud.jta.PluginListener;


/**
 * Configuration listener will be notified of configuration events.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: ConfigurationListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface ConfigurationListener extends PluginListener {
    /**
     * Called for configuration changes.
     */
    public void setConfiguration(PluginConfig config);
}
