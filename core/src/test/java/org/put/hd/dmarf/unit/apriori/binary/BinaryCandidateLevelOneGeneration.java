package org.put.hd.dmarf.unit.apriori.binary;

import java.io.Reader;
import java.io.StringReader;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;
import org.put.hd.dmarf.algorithms.apriori.binary.BinarySetsEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;

public class BinaryCandidateLevelOneGeneration {

	private BinarySetsEngine engine;

	@Before
	public void set_up() {
		engine = new BinarySetsEngine();
	}

	@Test
	public void two_chunks_with_all_vectors() {
		// create input data (in the .dat format as string)
		Integer supportThreshold = 0; // all data should get through this
										// threshold
		String dataString = "1 32" + '\n' + "22 8" + '\n';

		// create expected data (the candidate sets for such elements) sic 16
		// bit integers
		SortedMap<BinaryItemSet, Integer> expetedCandidates = new TreeMap<BinaryItemSet, Integer>();
		
		// Transaction: 1,32 => chunk 1 chunk 2
		// 1000 0000 0000 0000 => 32768 (chunk 1)
		char chunk1 = 32768;
		// 0000 0000 0000 0001 => 1 (chunk 2)
		char chunk2 = 1;
		expetedCandidates.put(new BinaryItemSet(new char[] { chunk1,chunk2 }), 1);
		
		
		// 0000 0001 0000 0000 => 256 (chunk 1)
		chunk1 = 256;
		// 0000 0100 0000 0000 => 2048 (chunk 2)
		chunk2 = 2048;
		expetedCandidates.put(new BinaryItemSet(new char[] { chunk1,chunk2 }), 1);

		// get from string to the data representation base
		DataRepresentationBase data = getDataFromString(dataString);

		// test
		SortedMap<BinaryItemSet, Integer> result = engine
				.getSingleCandidateSets(data, supportThreshold);

		// assert results (the equality of empty maps)
		Assert.assertTrue(expetedCandidates.equals(result));
	}

	@Test
	public void one_chunk_with_all_vectors() {

		// create input data (in the .dat format as string)
		Integer supportThreshold = 0; // all data should get through this
										// threshold
		String dataString = "1 2 3" + '\n' + "8" + '\n';

		// create expected data (the candidate sets for such elements) sic 16
		// bit integers
		SortedMap<BinaryItemSet, Integer> expetedCandidates = new TreeMap<BinaryItemSet, Integer>();
		// 1000 0000 0000 0000 => 32768
		expetedCandidates.put(new BinaryItemSet(new char[] { 32768 }), 1);
		// 0100 0000 0000 0000 => 16384
		expetedCandidates.put(new BinaryItemSet(new char[] { 16384 }), 1);
		// 0010 0000 0000 0000 => 8192
		expetedCandidates.put(new BinaryItemSet(new char[] { 8192 }), 1);
		// 0000 0001 0000 0000 => 256
		expetedCandidates.put(new BinaryItemSet(new char[] { 256 }), 1);

		// get from string to the data representation base
		DataRepresentationBase data = getDataFromString(dataString);

		// test
		SortedMap<BinaryItemSet, Integer> result = engine
				.getSingleCandidateSets(data, supportThreshold);

		// assert results (the equality of empty maps)
		Assert.assertTrue(expetedCandidates.equals(result));
	}

	@Test
	public void one_chunk_empty_set() {

		// create input data (in the .dat format as string)
		Integer supportThreshold = 2; // no data will fulfill this support
		String dataString = "1 2 3" + '\n' + "8" + '\n';

		// create expected data (the candidate sets for such elements)
		SortedMap<BinaryItemSet, Integer> expetedCandidates = new TreeMap<BinaryItemSet, Integer>();

		// get from string to the data representation base
		DataRepresentationBase data = getDataFromString(dataString);

		// test
		SortedMap<BinaryItemSet, Integer> result = engine
				.getSingleCandidateSets(data, supportThreshold);

		// assert results (the equality of empty maps)
		Assert.assertTrue(expetedCandidates.equals(result));

	}

	private DataRepresentationBase getDataFromString(String dataString) {
		Reader stringReader = new StringReader(dataString);
		SimpleDataFormatter formatter = new SimpleDataFormatter(
				new BasicDataBuilder());
		DataRepresentationBase data = formatter.getFormattedData(stringReader);
		return data;
	}
}
