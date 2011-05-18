package org.put.hd.dmarf.unit.apriori.binary.setsEngine;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;
import org.put.hd.dmarf.algorithms.apriori.binary.BinarySetsEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Big class for generating the candidate sets alongside with apriori
 * methodology
 * 
 * @author Piotr
 * 
 */
public class BinaryCanidateGenerator extends BinarySetsEngineTestBase {

	private SortedMap<BinaryItemSet, Integer> frequentSupportMap;
	private TreeSet<BinaryItemSet> expecetCandidates;

	@Before
	public void set_up() {
		engine = new BinarySetsEngine();
		expecetCandidates = new TreeSet<BinaryItemSet>();
	}

	@Test
	public void set_partition(){
		
		// input data
		// 1000 0000 0000 0001 chunk 1 => 32769
		// 0000 0000 0000 0001 chunk 2 => 1
		char chunk1 = 32769;
		char chunk2 = 1;
		BinaryItemSet inputSet = new BinaryItemSet(new char[]{chunk1,chunk2},3);
		
		// expected output (3 sets with 2 chunks)
		List<BinaryItemSet> expected = new LinkedList<BinaryItemSet>();
		
		// set 1
		// 0000 0000 0000 0001 => C1
		// 0000 0000 0000 0001 => C2
		chunk1 = 1;
		chunk2 = 1;
		BinaryItemSet set1 = new BinaryItemSet(new char[]{chunk1,chunk2},2);
		
		// set 2
		// 1000 0000 0000 0000 => C1
		// 0000 0000 0000 0001 => C2
		chunk1 = 32768;
		chunk2 = 1;
		BinaryItemSet set2 = new BinaryItemSet(new char[]{chunk1,chunk2},2);
		
		// set 3
		// 1000 0000 0000 0001 chunk 1 => 32769
		// 0000 0000 0000 0000 chunk 2 => 0
		chunk1 = 32769;
		chunk2 = 0;
		BinaryItemSet set3 = new BinaryItemSet(new char[]{chunk1,chunk2},2);
		
		expected.add(set3);
		expected.add(set2);
		expected.add(set1);
		
		// use this feature from BinaryItemSet
		Collection<BinaryItemSet> result = BinaryItemSet.divideSet(inputSet);
		
		// assert the output
		for (BinaryItemSet binaryItemSet : result) {
			if(!expected.contains(binaryItemSet))
				Assert.fail("The set should be found in the expected sets");
		}
	}
	
	@Test
	public void generatin_level_two_sets_from_one_level_sets() {

		// the cut off level
		Integer support = 0; // at this level ignore support
		String dataString = "1 2 3";

		// expected candidates
		// 1100 0000 0000 0000 => 49152
		char vector = 49152;
		BinaryItemSet set1 = new BinaryItemSet(new char[] { vector });

		// 1010 0000 0000 0000 => 40960
		vector = 40960;
		BinaryItemSet set2 = new BinaryItemSet(new char[] { vector });

		// 0110 0000 0000 0000 => 24576
		vector = 24576;
		BinaryItemSet set3 = new BinaryItemSet(new char[] { vector });

		expecetCandidates.add(set1);
		expecetCandidates.add(set2);
		expecetCandidates.add(set3);

		// generate data
		DataRepresentationBase data = getDataFromString(dataString);
		frequentSupportMap = engine.getSingleCandidateSets(data, support);

		// go with test
		Set<BinaryItemSet> result = engine.getCandidateSets(frequentSupportMap,1);

		Assert.assertTrue(expecetCandidates.equals(result));
	}

	@Test
	public void generating_level_three_sets_from_level_two() {

		// already generated frequent sets
		frequentSupportMap = new TreeMap<BinaryItemSet, Integer>();
		char setVector = 0;

		// a1,a2
		// 1100 0000 0000 0000 => 49152
		setVector = 49152;
		frequentSupportMap.put(new BinaryItemSet(new char[] { setVector }), 1);

		// a1,a3
		// 1010 0000 0000 0000 => 40960
		setVector = 40960;
		frequentSupportMap.put(new BinaryItemSet(new char[] { setVector }), 1);

		// a2,a3
		// 0110 0000 0000 0000 => 24576
		setVector = 24576;
		frequentSupportMap.put(new BinaryItemSet(new char[] { setVector }), 1);

		// expected candidateL: (a1,a2,a3)
		// 1110 0000 0000 0000
		char vector = 57344;
		BinaryItemSet set1 = new BinaryItemSet(new char[] { vector });
		expecetCandidates.add(set1);

		// go with test
		Set<BinaryItemSet> result = engine.getCandidateSets(frequentSupportMap,1);

		Assert.assertTrue(expecetCandidates.equals(result));
	}

	@Test
	public void no_set_can_be_generated_from_current_stage_maximum_generation() {
		// already generated frequent sets
		frequentSupportMap = new TreeMap<BinaryItemSet, Integer>();
		
		// 1110 0000 0000 0000
		char vector = 57344;
		frequentSupportMap.put(new BinaryItemSet(new char[]{vector}), 1);

		// expected candidate: no candidates

		// go with test
		Set<BinaryItemSet> result = engine.getCandidateSets(frequentSupportMap,1);

		Assert.assertTrue(expecetCandidates.equals(result));
	}
}
