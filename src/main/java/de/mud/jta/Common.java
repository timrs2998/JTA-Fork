package de.mud.jta;

import de.mud.jta.event.ConfigurationRequest;

import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The common part of the <B>JTA - Telnet/SSH for the JAVA(tm) platform</B>
 * is handled here. Mainly this includes the loading of the plugins and
 * the screen setup of the visual plugins.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Meissner
 * @version $Id: Common.java 499 2005-09-29 08:24:54Z leo $
 */
public class Common extends PluginLoader {
    private static final Logger logger = Logger.getLogger(Common.class.getName());
    public static final String DEFAULT_PATH = "de.mud.jta.plugin";

    public Common(Properties config) {
        // configure the plugin path
        super(getPluginPath(config.getProperty("pluginPath")));

        logger.log(Level.FINE, "** JTA - Telnet/SSH for the JAVA(tm) platform");
        logger.log(Level.FINE, "** Version 2.6 for Java 2+");
        logger.log(Level.FINE, "** Copyright (c) 1996-2005 Matthias L. Jugel, "
                + "Marcus Meissner");

        try {
            Version build =
                    (Version) Class.forName("de.mud.jta.Build").newInstance();
            logger.log(Level.FINE, "** Build: " + build.getDate());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "** Build: patched or selfmade, no date");
            logger.log(Level.SEVERE, e.toString(), e);
        }

        Vector names = split(config.getProperty("plugins"), ',');
        if (names == null) {
            logger.log(Level.SEVERE, "jta: no plugins found! aborting ...");
            return;
        }

        Enumeration e = names.elements();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String id = null;
            int idx;
            if ((idx = name.indexOf("(")) > 1) {
                if (name.indexOf(")", idx) > idx) {
                    id = name.substring(idx + 1, name.indexOf(")", idx));
                } else {
                    System.err.println("jta: missing ')' for plugin '" + name + "'");
                }
                name = name.substring(0, idx);
            }
            logger.log(Level.FINE, "jta: loading plugin '" + name + "'"
                    + (id != null && !id.isEmpty() ?
                    ", ID: '" + id + "'" : ""));
            Plugin plugin = addPlugin(name, id);
            if (plugin == null) {
                logger.log(Level.SEVERE, "jta: ignoring plugin '" + name + "'"
                        + (id != null && !id.isEmpty() ?
                        ", ID: '" + id + "'" : ""));
                continue;
            }
        }

        broadcast(new ConfigurationRequest(new PluginConfig(config)));
    }

    /**
     * Get the list of visual components currently registered.
     *
     * @return a map of components
     */

    public Map getComponents() {
        Map plugins = getPlugins();
        Iterator pluginIt = plugins.keySet().iterator();
        Map components = new HashMap();
        while (pluginIt.hasNext()) {
            String name = (String) pluginIt.next();
            Plugin plugin = (Plugin) plugins.get(name);
            if (plugin instanceof VisualPlugin) {
                JComponent c = ((VisualPlugin) plugin).getPluginVisual();
                if (c != null) {
                    String id = plugin.getId();
                    components.put(name + (id != null ? "(" + id + ")" : ""), c);
                }
            }
        }
        return components;
    }

    public Map getMenus() {
        Map plugins = getPlugins();
        Iterator pluginIt = plugins.keySet().iterator();
        Map menus = new HashMap();
        while (pluginIt.hasNext()) {
            String name = (String) pluginIt.next();
            Plugin plugin = (Plugin) plugins.get(name);
            if (plugin instanceof VisualPlugin) {
                JMenu menu = ((VisualPlugin) plugin).getPluginMenu();
                if (menu != null) {
                    String id = plugin.getId();
                    menus.put(name + (id != null ? "(" + id + ")" : ""), menu);
                }
            }
        }
        return menus;
    }

    /**
     * Convert the plugin path from a separated string list to a Vector.
     *
     * @param path the string path
     * @return a vector containing the path
     */
    private static Vector getPluginPath(String path) {
        if (path == null) {
            path = DEFAULT_PATH;
        }
        return split(path, ':');
    }

    /**
     * Split up comma separated lists of strings. This is quite strict, no
     * whitespace characters are allowed.
     *
     * @param s the string to be split up
     * @return an array of strings
     */
    public static Vector split(String s, char separator) {
        if (s == null) {
            return null;
        }
        Vector v = new Vector();
        int old = -1, idx = s.indexOf(separator);
        while (idx >= 0) {
            v.addElement(s.substring(old + 1, idx));
            old = idx;
            idx = s.indexOf(separator, old + 1);
        }
        v.addElement(s.substring(old + 1));
        return v;
    }
}
