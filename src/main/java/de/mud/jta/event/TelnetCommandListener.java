package de.mud.jta.event;

import de.mud.jta.PluginListener;

import java.io.IOException;

/**
 * This interface should be used by plugins who would like to be notified
 * about the end of record event
 * <p>
 * <B>Maintainer:</B> Marcus Meissner
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: TelnetCommandListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface TelnetCommandListener extends PluginListener {
    /**
     * Called by code in the terminal interface or somewhere for sending
     * telnet commands
     */
    public void sendTelnetCommand(byte command) throws IOException;
}
