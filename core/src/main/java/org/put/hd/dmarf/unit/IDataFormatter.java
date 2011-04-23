package org.put.hd.dmarf.unit;

import java.io.Reader;

/**
 * Gets the data stream from various sources and reformats it into
 * specified format. To be used with variety of representation forms.
 * @author Piotr Jessa
 *
 */
public interface IDataFormatter {

	
	void getFormattedData(Reader reader);

}
