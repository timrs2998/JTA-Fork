package de.mud.jta;

/**
 * A plugin bus is used for communication between plugins. The interface
 * describes the broadcast method that should broad cast the message
 * to all plugins known and return an answer message immediatly.<P>
 * The functionality is just simuliar to a bus, but depends on the
 * actual implementation of the bus.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: PluginBus.java 499 2005-09-29 08:24:54Z leo $
 */
public interface PluginBus {
    /**
     * Broadcast a plugin message to all listeners.
     */
    public Object broadcast(PluginMessage message);

    /**
     * Register a plugin listener with this bus object
     */
    public void registerPluginListener(PluginListener listener);
}
