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
	public void dzess_want_test_0() {
		// 1000 0000 0000 0000 => 32768
		char[] vectorMine = new char[] { 32768 };
		List<Integer> list = new LinkedList<Integer>();
		list.add(1);
		char[] mikiVector = BasicDataBuilder.generateCharArray(list, 16);

		Assert.assertArrayEquals(vectorMine, mikiVector);
	}

	@Test
	public void dzess_want_test_1() {
		// 0000 0001 0000 0000 => 256
		char[] vectorMine = new char[] { 256 };
		List<Integer> list = new LinkedList<Integer>();
		list.add(8);
		char[] mikiVector = BasicDataBuilder.generateCharArray(list, 16);

		Assert.assertArrayEquals(vectorMine, mikiVector);
	}

	@Test
	public void dzess_want_test_2() {
		// 0000 0000 0000 0001 => 1
		char[] vectorMine = new char[] { 1 };
		List<Integer> list = new LinkedList<Integer>();
		list.add(16);
		char[] mikiVector = BasicDataBuilder.generateCharArray(list, 16);

		Assert.assertArrayEquals(vectorMine, mikiVector);
	}

	// TODO: write the same in the way for TWO chunks

	@Test
	public void sample_aligned_table() {

		// perform test
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "alignedSet.data";
		dataloader.setInputFileName(pathToFile);
		data = dataloader.loadData();

		char[] testCharMap = data.getTransactionsCharMap();

		// 1 4 8 16 	 => 37121 0 0 0	=> 1001 0001 0000 0001 0x 0x 0x
		// 1 2 8 16 	 => 49409 0 0 0	=> 1100 0001 0000 0001 0x 0x 0x
		// 4 8 16 32 	 => 4353  1 0 0	=> 0001 0001 0000 0001 | 0000 0000 0000 0001 | 0x 0x
		// 1 2 4 8 16 32 => 53505 1 0 0 => 1101 0001 0000 0001 | 0000 0000 0000 0001 | 0x 0x
		// 48 			 => 	0 0 1 0 => 0x 0x | 0000 0000 0000 0001 | 0x

		char[] expectedCharMap = { 37121, 0, 0, 0, 49409, 0, 0, 0, 4353,
				1, 0, 0, 53505, 1, 0, 0, 0, 0, 1, 0 };

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

		// 1 4 15      => 36866 0 0 0 	=> 1001 0000 0000 0010 0x 0x 0x
		// 1 2 15 	   => 49154 0 0 0 	=> 1100 0000 0000 0010 0x 0x 0x
		// 4 15 31 	   => 4098  2 0 0 	=> 0001 0000 0000 0010 | 0000 0000 0000 0010 0x 0x
		// 1 2 4 15 31 => 53250 2 0 0 	=> 1101 0000 0000 0010 | 0000 0000 0000 0010 0x 0x
		// 47		
		
		char[] expectedCharMap = { 36866, 0, 0, 0, 49154, 0, 0, 0, 4098,
				2, 0, 0, 53250, 2, 0, 0, 0, 0, 2, 0 };

		for (int i = 0; i < testCharMap.length; i++) {
			System.out.println((int) testCharMap[i] + " "
					+ Integer.toBinaryString(testCharMap[i]));
		}		
		
		Assert.assertArrayEquals(expectedCharMap, testCharMap);

	}

}
