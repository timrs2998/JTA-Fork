package de.mud.jta;

import javax.swing.*;

/**
 * To show data on-screen a plugin may have a visible component. That component
 * may either be a single awt component or a container with severel elements.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: VisualPlugin.java 499 2005-09-29 08:24:54Z leo $
 */
public interface VisualPlugin {
    /**
     * Get the visible components from the plugin.
     *
     * @return a component that represents the plugin
     */
    public JComponent getPluginVisual();

    /**
     * Get the menu entry for this component.
     *
     * @return a menu that can be used to change the plugin state
     */
    public JMenu getPluginMenu();
}
