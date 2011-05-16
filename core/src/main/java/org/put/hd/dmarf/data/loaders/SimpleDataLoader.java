package org.put.hd.dmarf.data.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.formatters.IDataFormatter;

/**
 * Sample file data loader. Injected with IDataFormatter class.
 * 
 * @author Piotr
 */
public class SimpleDataLoader implements IDataLoader {

	private File inputFile;
	private final IDataFormatter fomratter;

	public SimpleDataLoader(IDataFormatter fomratter) {
		if (fomratter == null)
			throw new NullPointerException("formatter parameter cannot be null");

		this.fomratter = fomratter;
	}

	public void setInputFileName(String name) {

		// constraints
		if (name == null)
			throw new NullPointerException("Name parameter cannot be null");

		inputFile = new File(name);

		if (!inputFile.exists()) {
			inputFile = null;
			throw new UnsupportedOperationException("No such file exists");
		}

	}

	public DataRepresentationBase loadData() {
		Reader fileReader = null;

		DataRepresentationBase data = null;
		try {
			fileReader = new FileReader(inputFile);
			data = fomratter.getFormattedData(fileReader);
			fileReader.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

}
