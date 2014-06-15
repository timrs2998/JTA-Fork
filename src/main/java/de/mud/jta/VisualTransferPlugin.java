package de.mud.jta;

import java.awt.datatransfer.Clipboard;

/**
 * A visual plugin that also allows to copy and paste data.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: VisualTransferPlugin.java 499 2005-09-29 08:24:54Z leo $
 */
public interface VisualTransferPlugin extends VisualPlugin {
    /**
     * Copy currently selected text into the clipboard.
     *
     * @param clipboard the clipboard
     */
    public void copy(Clipboard clipboard);

    /**
     * Paste text from clipboard to the plugin.
     *
     * @param clipboard the clipboard
     */
    public void paste(Clipboard clipboard);
}
