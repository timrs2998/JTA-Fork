package de.mud.jta.event;

import de.mud.jta.PluginListener;

/**
 * This interface should be used by plugins who would like to be notified
 * about the end of record event
 * <p>
 * <B>Maintainer:</B> Marcus Meissner
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: EndOfRecordListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface EndOfRecordListener extends PluginListener {
    /**
     * Called if the end of record event appears
     */
    public void EndOfRecord();
}
