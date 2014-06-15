package de.mud.jta;

import java.io.IOException;

/**
 * The filter plugin is the base interface for plugins that want to intercept
 * the communication between front end and back end plugins. Filters and
 * protocol handlers are a good example.
 * <p>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @author Matthias L. Jugel, Marcus Mei�ner
 * @version $Id: FilterPlugin.java 499 2005-09-29 08:24:54Z leo $
 */
public interface FilterPlugin {
    /**
     * Set the source plugin where we get our data from and where the data
     * sink (write) is. The actual data handling should be done in the
     * read() and write() methods.
     *
     * @param source the data source
     */
    public void setFilterSource(FilterPlugin source) throws IllegalArgumentException;

    public FilterPlugin getFilterSource();

    /**
     * Read a block of data from the back end.
     *
     * @param b the buffer to read the data into
     * @return the amount of bytes actually read
     */
    public int read(byte[] b) throws IOException;

    /**
     * Write a block of data to the back end.
     *
     * @param b the buffer to be sent
     */
    public void write(byte[] b) throws IOException;
}
