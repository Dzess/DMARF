package org.put.hd.dmarf.data.formatters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.IDataReprsentatinoBuilder;

/**
 * Gets the data from stream and reads the input providing output in a nice
 * format. In future there is eventuality for changing the output data format.
 * 
 * @author Piotr
 * 
 */
public class SimpleDataFormatter implements IDataFormatter {

	private final IDataReprsentatinoBuilder builder;

	public SimpleDataFormatter(IDataReprsentatinoBuilder builder) {
		this.builder = builder;

	}

	public DataRepresentationBase getFormattedData(Reader someReader) {

		// parsing the data
		BufferedReader buffer = new BufferedReader(someReader);
		int counter = 0;

		// invoking the dataRepresentation factory
		while (true) {

			String line = null;
			try {
				line = buffer.readLine();
			} catch (IOException e) {
				// this should never happen though, (we do not want to show
				// throwable)
				throw new RuntimeException(
						"Some problem with reading the file", e);
			}

			// null is only when EOS
			if (line == null)
				break;

			builder.addTransaction(counter);
			counter++;

			// read one line
			StringTokenizer tokenizer = new StringTokenizer(line);
			while (tokenizer.hasMoreElements()) {

				// get single string item and make it number then pass to
				// builder
				String singleItemString = (String) tokenizer.nextElement();
				Integer item = Integer.parseInt(singleItemString);
				builder.addItemInTransactino(item);
			}

		}

		// just read it from builder
		return builder.getDataRepresentation();
	}

}
