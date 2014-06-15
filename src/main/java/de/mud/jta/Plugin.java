package de.mud.jta;


/**
 * Plugin base class for the JTA. A plugin is a component
 * for the PluginBus and may occur several times. If we have more than one
 * plugin of the same type the protected value id contains the unique plugin
 * id as configured in the configuration.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: Plugin.java 499 2005-09-29 08:24:54Z leo $
 */
public class Plugin {
    /**
     * holds the plugin bus used for communication between plugins
     */
    protected final PluginBus bus;
    /**
     * in case we have several plugins of the same type this contains their
     * unique id
     */
    protected final String id;

    /**
     * Create a new plugin and set the plugin bus used by this plugin and
     * the unique id. The unique id may be null if there is only one plugin
     * used by the system.
     *
     * @param bus the plugin bus
     * @param id  the unique plugin id
     */
    public Plugin(PluginBus bus, String id) {
        this.bus = bus;
        this.id = id;
    }

    /**
     * Return identifier for this plugin.
     *
     * @return id string
     */
    public String getId() {
        return id;
    }

    /**
     * Print an error message to stderr prepending the plugin name. This method
     * is public due to compatibility with Java 1.1
     *
     * @param msg the error message
     */
    public void error(String msg) {
        String name = getClass().toString();
        name = name.substring(name.lastIndexOf('.') + 1);
        System.err.println(name + (id != null ? "(" + id + ")" : "") + ": " + msg);
    }
}
