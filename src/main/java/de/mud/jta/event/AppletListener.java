package de.mud.jta.event;

import de.mud.jta.PluginListener;

import javax.swing.*;

/**
 * This is the interface is for applet listeners, plugins that
 * want to know the applet object.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: AppletListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface AppletListener extends PluginListener {
    /**
     * Return the current window size of the terminal in rows and columns.
     */
    public void setApplet(JApplet applet);
}
