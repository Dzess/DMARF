package org.put.hd.dmarf.unit;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.put.hd.dmarf.SimpleDataLoader;
import org.put.hd.dmarf.IDataLoader;

/**
 * Testing data loading. Using IDataLoader classes.
 */
public class DataLoading {

	private IDataLoader loader;
	private IDataFormatter mockFormatter;

	@Before
	public void set_up() {
		mockFormatter = Mockito.mock(IDataFormatter.class);
		loader = new SimpleDataLoader(mockFormatter);
	}

	@Test
	public void providing_no_data_file_results_in_exception() {
		String name = "non_existing_file_name";
		try {
			loader.setInputFileName(name);
		} catch (UnsupportedOperationException e) {

			return;
		}

		Assert.fail("No exception has been catched, but should");
	}

	@Test
	public void providing_null_as_file_results_in_exception() {
		String name = null;

		try {
			loader.setInputFileName(name);
		} catch (NullPointerException e) {
			return;
		}
		Assert.fail("No exception has been catched, but should");
	}

	@Test
	public void providing_normal_file_gets_the_data_formater_being_used() {

		// create the stub file
		String fileName = "testFileName";
		File f = new File(fileName);
		if (f.exists())
			f.delete();

		try {
			f.createNewFile();
		} catch (IOException e) {

		}

		try {

			// perform test
			loader.setInputFileName(fileName);
			loader.loadData();

			// assert that good formatter was used
			Mockito.verify(mockFormatter).getFormattedData(
					Mockito.any(Reader.class));

		} finally {
			// clean up after this fileName
			if (f.exists())
				f.delete();
		}
	}
}
