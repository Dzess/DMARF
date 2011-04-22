package org.put.hd.dmarf.unit;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.DataLoader;
import org.put.hd.dmarf.IDataLoader;

/**
 * Testing data loading. Using IDataLoader classes.
 */
public class DataLoading {

	private IDataLoader loader;

	@Before
	public void set_up() {
		loader = new DataLoader();
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

		// perform test
		try {
			loader.setInputFileName(fileName);
			loader.loadData();
		} finally {
			// clean up after this fileName
			if (f.exists())
				f.delete();
		}
	}
}
