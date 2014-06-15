package de.mud.jta.event;

import de.mud.jta.PluginListener;

/**
 * This is the interface for a terminal type listener. It should return
 * the terminal type id as a string. Valid terminal types include
 * VT52, VT100, VT200, VT220, VT320, ANSI etc.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: TerminalTypeListener.java 499 2005-09-29 08:24:54Z leo $
 */
public interface TerminalTypeListener extends PluginListener {
    /**
     * Return the terminal type string
     */
    public String getTerminalType();
}
