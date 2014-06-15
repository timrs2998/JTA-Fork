package de.mud.jta.event;

import de.mud.jta.PluginListener;

import java.net.URL;

/**
 * Play a sound when requested.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: SoundListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface SoundListener extends PluginListener {
    /**
     * Play a sound that is given as a URL
     */
    public void playSound(URL audioClip);
}
