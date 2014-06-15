package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

/**
 * Notification of the end of record event
 * <p>
 * <B>Maintainer:</B> Marcus Meissner
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: EndOfRecordRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class EndOfRecordRequest implements PluginMessage {
    /**
     * Create a new local echo request with the specified value.
     */
    public EndOfRecordRequest() {
    }

    /**
     * Notify all listeners about the end of record message
     *
     * @param pl the list of plugin message listeners
     * @return always null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof EndOfRecordListener) {
            ((EndOfRecordListener) pl).EndOfRecord();
        }
        return null;
    }
}
