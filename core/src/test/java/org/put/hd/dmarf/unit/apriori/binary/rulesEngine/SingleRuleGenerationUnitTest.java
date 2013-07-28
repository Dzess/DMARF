package org.put.hd.dmarf.unit.apriori.binary.rulesEngine;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryRuleEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.IRulesEngine;

/**
 * Testing rule harvesting from the passed item set.
 * 
 * @author Piotr
 * 
 */
public class SingleRuleGenerationUnitTest {

	private IRulesEngine engine;
	private int totalSupport;
	private double confidence;
	private BinaryItemSet premiseSet;
	private BinaryItemSet totalSet;

	@Before
	public void set_up() {
		engine = new BinaryRuleEngine();

		this.totalSupport = 10;
		this.confidence = 0.8;
	}

	@Test
	public void sample_case_1() {

		// chunk 1 : 100000000000
		char premiseVector = 32768;
		List<Integer> premise = new LinkedList<Integer>();
		premise.add(1);

		// chunk 1 : 100000000000001
		char totalVector = 32769;
		List<Integer> executive = new LinkedList<Integer>();
		executive.add(16);

		premiseSet = new BinaryItemSet(new char[] { premiseVector });
		totalSet = new BinaryItemSet(new char[] { totalVector });

		Rule rule = engine.createRuleFromItemSet(totalSet, premiseSet,
				this.confidence, this.totalSupport);

		Assert.assertTrue((int) (this.confidence * 100) == rule
				.getConfidence());
		Assert.assertEquals(this.totalSupport, rule.getSupport());

		// assert the premise part
		AssertLists(premise, rule.getConditionalPart());

		// assert the executive part
		AssertLists(executive, rule.getExecutivePart());
	}

	private void AssertLists(List<Integer> premise, List<Integer> list) {
		for (Integer integer : premise) {
			if (!list.contains(integer))
				Assert.fail("The elements should be the same");
		}
	}

	@Test
	public void sample_case_2() {
		// chunk 1 : 100000000000
		// chunk 2 : 100000000001
		char premiseVector_1 = 32768;
		char premiseVector_2 = 32769;
		List<Integer> premise = new LinkedList<Integer>();
		premise.add(1);
		premise.add(17);
		premise.add(32);

		// chunk 1 : 10000000..0000001
		// chunk 2 : 11111111..1111111
		char totalVector_1 = 32769;
		char totalVector_2 = 65535;
		List<Integer> executive = new LinkedList<Integer>();
		executive.add(16);
		executive.add(18);
		executive.add(19);
		executive.add(20);
		executive.add(21);
		executive.add(22);
		executive.add(23);
		executive.add(24);
		executive.add(25);
		executive.add(26);
		executive.add(27);
		executive.add(28);
		executive.add(29);
		executive.add(30);
		executive.add(31);

		premiseSet = new BinaryItemSet(new char[] { premiseVector_1,premiseVector_2 });
		totalSet = new BinaryItemSet(new char[] { totalVector_1,totalVector_2  });

		Rule rule = engine.createRuleFromItemSet(totalSet, premiseSet,
				this.confidence, this.totalSupport);

		Assert.assertTrue((int) (this.confidence * 100) == rule
				.getConfidence());
		Assert.assertEquals(this.totalSupport, rule.getSupport());

		// assert the premise part
		AssertLists(premise, rule.getConditionalPart());

		// assert the executive part
		AssertLists(executive, rule.getExecutivePart());
	}

	@Test
	public void passing_empty_item_set_invokes_exception() {

		premiseSet = new BinaryItemSet(new char[] {});
		totalSet = new BinaryItemSet(new char[] {});

		try {
			engine.createRuleFromItemSet(totalSet, premiseSet, this.confidence,
					this.totalSupport);
		} catch (Exception e) {
			return;
		}

		Assert.fail("The exception should have been thrown");

	}

	@Test
	public void premises_set_must_be_in_total_set() {

		// chunk 1 : 100000000000
		char premiseVector = 32768;

		// chunk 1 : 000000000000001
		char totalVector = 1;

		premiseSet = new BinaryItemSet(new char[] { premiseVector });
		totalSet = new BinaryItemSet(new char[] { totalVector });

		try {
			engine.createRuleFromItemSet(totalSet, premiseSet, this.confidence,
					this.totalSupport);
		} catch (Exception e) {
			return;
		}

		Assert.fail("The exception should have been thrown");
	}
}
