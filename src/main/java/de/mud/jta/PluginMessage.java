package de.mud.jta;

/**
 * The base interface for a plugin message.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: PluginMessage.java 499 2005-09-29 08:24:54Z leo $
 */
public interface PluginMessage {
    /**
     * Fire the message to all listeners that are compatible with this
     * message and return the result.
     *
     * @param pl the list of plugin message listeners
     * @return the result message
     */
    public Object firePluginMessage(PluginListener pl);
}
