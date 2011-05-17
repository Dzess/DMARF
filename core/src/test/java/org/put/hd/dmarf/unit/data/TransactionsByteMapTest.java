package org.put.hd.dmarf.unit.data;

import java.io.File;

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

public class TransactionsByteMapTest {

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
			Byte[][] emptyTable = builder.getDataRepresentation()
					.getTransactionsByteMap();
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

		Byte[][] testByteMap = data.getTransactionsByteMap();

		Byte[][] expectedByteMap = { { (byte) 137, 0, 0 },
				{ (byte) 131, 0, 0 }, { (byte) 136, (byte) 128, 0 },
				{ (byte) 139, (byte) 128, 0 }, { 0, 0, (byte)128 } };

		Assert.assertArrayEquals(expectedByteMap, testByteMap);

	}

	@Test
	public void sample_non_aligned_table() {

		// perform test
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "nonAlignedSet.data";
		dataloader.setInputFileName(pathToFile);
		data = dataloader.loadData();

		Byte[][] testByteMap = data.getTransactionsByteMap();

		Byte[][] expectedByteMap = { { (byte) 73, 0, 0 }, { (byte) 67, 0, 0 },
				{ (byte) 72, (byte) 64, 0 }, { (byte) 75, (byte) 64, 0 },
				{ 0, 0, 64 } };

		Assert.assertArrayEquals(expectedByteMap, testByteMap);

	}

}
