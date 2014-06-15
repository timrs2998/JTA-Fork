package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

import java.net.URL;

/**
 * Play a sound.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: SoundRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class SoundRequest implements PluginMessage {
    protected final URL audioClip;

    public SoundRequest(URL audioClip) {
        this.audioClip = audioClip;
    }

    /**
     * Notify all listeners that they may play the sound.
     *
     * @param pl the list of plugin message listeners
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof SoundListener) {
            ((SoundListener) pl).playSound(audioClip);
        }
        return null;
    }
}
