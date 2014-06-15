package de.mud.jta;

import java.util.Properties;

/**
 * Plugin configuration container. This class extends the Properties
 * to allow specific duplications of plugins. To get the value of a
 * property for a plugin simply call getProperty() with the plugin name,
 * the unique id (which may be null) and the key you look for. A fallback
 * value will be returned if it exists.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: PluginConfig.java 499 2005-09-29 08:24:54Z leo $
 */
public class PluginConfig extends Properties {

    public PluginConfig(Properties props) {
        super(props);
    }

    /**
     * Get property value for a certain plugin with the specified id.
     * This method will return the default value if no value for the specified
     * id exists.
     *
     * @param plugin the plugin to get the setup for
     * @param id     plugin id as specified in the config file
     * @param key    the property key to search for
     * @return the property value or null
     */
    public String getProperty(String plugin, String id, String key) {
        if (id == null) {
            id = "";
        } else {
            id = "(" + id + ")";
        }
        String result = getProperty(plugin + id, key);
        if (result == null) {
            result = getProperty(plugin, key);
        }
        return result;
    }

    /**
     * Get the property value for a certain plugin.
     *
     * @param plugin the plugin to get setup for
     * @param key    the property key to search for
     */
    public String getProperty(String plugin, String key) {
        return getProperty(plugin + "." + key);
    }

    /**
     * Set the property value for a certain plugin and id.
     *
     * @param plugin the name of the plugin
     * @param id     the unique id of the plugin
     * @param key    the property key
     * @param value  the new value
     */

    public void setProperty(String plugin, String id, String key, String value) {
        if (id == null) {
            id = "";
        } else {
            id = "(" + id + ")";
        }
        setProperty(plugin + id, key, value);
    }

    /**
     * Set the property value for a certain plugin.
     *
     * @param plugin the name of the plugin
     * @param key    the property key
     * @param value  the new value
     */
    public void setProperty(String plugin, String key, String value) {
        setProperty(plugin + "." + key, value);
    }

}
