package de.mud.jta.event;

import de.mud.jta.PluginListener;

/**
 * This interface should be used by plugins who would like to be notified
 * about the return of the focus from another plugin.
 * <p>
 * Implemented after a suggestion by Dave &lt;david@mirrabooka.com&gt;
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: ReturnFocusListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface ReturnFocusListener extends PluginListener {
    /**
     * Called if the end of return focus message is sent.
     */
    public void returnFocus();
}
