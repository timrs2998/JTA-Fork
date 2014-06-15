package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

import javax.swing.*;


/**
 * Tell listeners the applet object.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: AppletRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class AppletRequest implements PluginMessage {
    protected final JApplet applet;

    public AppletRequest(JApplet applet) {
        this.applet = applet;
    }

    /**
     * Notify all listeners of a configuration event.
     *
     * @param pl the list of plugin message listeners
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof AppletListener) {
            ((AppletListener) pl).setApplet(applet);
        }
        return null;
    }
}
