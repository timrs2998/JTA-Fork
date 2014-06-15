package de.mud.jta;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

/**
 * Help display for JTA.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: Help.java 499 2005-09-29 08:24:54Z leo $
 */
public class Help {

    public static final JEditorPane helpText = new JEditorPane();

    public static void show(Component parent, String url) {
        BufferedReader reader = null;
        System.err.println("Help: " + url);

        try {
            helpText.setPage(Help.class.getResource(url));
        } catch (IOException e) {
            try {
                helpText.setPage(new URL(url));
            } catch (Exception ee) {
                System.err.println("unable to load help");
                JOptionPane.showMessageDialog(parent, "JTA - Telnet/SSH for the JAVA(tm) platform\r\n(c) 1996-2005 " +
                        "Matthias L. Jugel, Marcus Meißner\r\n\r\n", "jta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        helpText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(helpText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setSize(800, 600);

        final JFrame frame = new JFrame("HELP");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        JPanel panel = new JPanel();
        JButton close = new JButton("Close Help");
        panel.add(close);
        close.addActionListener(e -> frame.setVisible(false));
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
        frame.getContentPane().add(BorderLayout.SOUTH, close);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

}
