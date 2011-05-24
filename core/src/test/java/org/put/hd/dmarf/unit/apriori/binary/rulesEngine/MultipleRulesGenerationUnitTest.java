package org.put.hd.dmarf.unit.apriori.binary.rulesEngine;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryRuleEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.IRulesEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;

import weka.associations.BinaryItem;

/**
 * Quite the short class testing the general rule harvesting from frequent set.
 * 
 * @author Piotr
 * 
 */
public class MultipleRulesGenerationUnitTest {

	private IRulesEngine engine;
	private SortedMap<BinaryItemSet, Integer> frequentSetsMap;
	private BinaryItemSet frequentSet;
	private double confidance;

	@Before
	public void set_up() {
		engine = new BinaryRuleEngine();

		// default values for some of the parameters
		this.confidance = 0.5;
		this.frequentSetsMap = new TreeMap<BinaryItemSet, Integer>();
	}

	@Test
	public void one_element_frequent_set() {

		List<Rule> expectedRules = new LinkedList<Rule>();
		frequentSet = new BinaryItemSet(new char[] { 1 });
		frequentSetsMap.put(frequentSet, 3);

		List<Rule> result = engine.getRules(frequentSet, this.confidance,
				frequentSetsMap);

		// get the assertion right
		assertLists(expectedRules, result);
	}

	public void no_frequent_set_no_rule() {
		// TODO: write this painful test
	}

	public void more_complicated_data_case() {
		// TODO: write this complcated data test
	}

	@Test
	public void get_sample_rules_from_data() {

		// frequent set (1,16)
		// chunk 1 : 1000000..00000001
		char totalVector = 32769;
		frequentSet = new BinaryItemSet(new char[] { totalVector });
		frequentSetsMap.put(frequentSet, 1);

		BinaryItemSet supportSet1 = new BinaryItemSet(new char[] { 1 });
		BinaryItemSet supportSet2 = new BinaryItemSet(new char[] { 32768 });
		frequentSetsMap.put(supportSet1, 2);
		frequentSetsMap.put(supportSet2, 2);

		// rule set up
		List<Rule> expectedRules = new LinkedList<Rule>();
		List<Integer> listOne = new LinkedList<Integer>();
		listOne.add(1);
		List<Integer> listTwo = new LinkedList<Integer>();
		listOne.add(16);

		// rule if 1 => 16
		expectedRules.add(new Rule(0, listOne, listTwo, 1, 1));
		// rule 16 => 1
		expectedRules.add(new Rule(0, listTwo, listOne, 1, 1));

		// pass confidence 0 for always true (harvest all possible rules)
		List<Rule> result = engine.getRules(frequentSet, 0, frequentSetsMap);

		// get the assertion right
		assertLists(expectedRules, result);
	}

	private void assertLists(List<Rule> expectedRules, List<Rule> result) {
		for (Rule rule : result) {
			if (!expectedRules.contains(rule))
				Assert.fail("Have not found element");
		}
	}
}
