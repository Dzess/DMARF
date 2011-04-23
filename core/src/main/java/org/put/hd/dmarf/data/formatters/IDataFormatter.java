package org.put.hd.dmarf.data.formatters;

import java.io.Reader;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Gets the data stream from various sources and reformats it into
 * specified format. To be used with variety of representation forms.
 * @author Piotr Jessa
 *
 */
public interface IDataFormatter {

	/**
	 * Gets the formatted data from reader.
	 * @param reader. Input stream for reading data.
	 * @return The one of the provided data implementations.
	 */
	public DataRepresentationBase getFormattedData(Reader reader);

}
