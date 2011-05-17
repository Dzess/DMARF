package org.put.hd.dmarf.unit.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

public class TransactionsCharMapTest {

	private IDataLoader dataloader;
	private IDataFormatter formatter;
	private IDataRepresentationBuilder builder;

	private DataRepresentationBase data;

	@Before
	public void setUp() {

		// use all the normal classes
		builder = new BasicDataBuilder();
		formatter = new SimpleDataFormatter(builder);
		dataloader = new SimpleDataLoader(formatter);
	}

	@Test
	public void empty_table() {

		try {
			char[] emptyTable = builder.getDataRepresentation()
					.getTransactionsCharMap();
		} catch (RuntimeException e) {
			return;
		}
		Assert.fail();
	}

	@Test
	public void sample_aligned_table() {

		// perform test
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "alignedSet.data";
		dataloader.setInputFileName(pathToFile);
		data = dataloader.loadData();

		char[] testCharMap = data.getTransactionsCharMap();

		char[] expectedCharMap = { 
				32905, 0, 0, 0,
				32899, 0, 0, 0,
				32904, 32768, 0, 0,
				32907, 32768, 0, 0,
				0, 0, 32768, 0};

		/*
		 * for (int i = 0; i < testCharMap.length; i++) { for (int j = 0; j <
		 * testCharMap[i].length; j++) { System.out.println((int)
		 * testCharMap[i][j] + " " + Integer.toBinaryString(testCharMap[i][j]));
		 * } }
		 */
		Assert.assertArrayEquals(expectedCharMap, testCharMap);

	}

	@Test
	public void sample_non_aligned_table() {

		// perform test
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "nonAlignedSet.data";
		dataloader.setInputFileName(pathToFile);
		data = dataloader.loadData();

		char[] testCharMap = data.getTransactionsCharMap();

		char[] expectedCharMap = { 
				16393, 0, 0, 0,
				16387, 0, 0, 0,
				16392, 16384, 0, 0,
				16395, 16384, 0, 0,
				0, 0, 16384, 0 };

		Assert.assertArrayEquals(expectedCharMap, testCharMap);

	}

}
