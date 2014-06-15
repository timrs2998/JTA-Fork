package de.mud.jta.event;

import de.mud.jta.PluginListener;

/**
 * This interface should be used by plugins who would like to be notified
 * about the local echo property.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: LocalEchoListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface LocalEchoListener extends PluginListener {
    /**
     * Called if the local echo property changes.
     */
    public void setLocalEcho(boolean echo);
}
