package de.mud.jta.event;

import de.mud.jta.PluginListener;
import de.mud.jta.PluginMessage;

import java.io.IOException;

/**
 * Notification of the end of record event
 * <p>
 * <B>Maintainer:</B> Marcus Meissner
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: TelnetCommandRequest.java 499 2005-09-29 08:24:54Z leo $
 */
public class TelnetCommandRequest implements PluginMessage {
    /**
     * Create a new telnet command request with the specified value.
     */
    final byte cmd;

    public TelnetCommandRequest(byte command) {
        cmd = command;
    }

    /**
     * Notify all listeners about the end of record message
     *
     * @param pl the list of plugin message listeners
     * @return always null
     */
    public Object firePluginMessage(PluginListener pl) {
        if (pl instanceof TelnetCommandListener) {
            try {
                ((TelnetCommandListener) pl).sendTelnetCommand(cmd);
            } catch (IOException io) {
                System.err.println("io exception caught:" + io);
                io.printStackTrace();
            }
        }
        return null;
    }
}
