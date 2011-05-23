package org.put.hd.dmarf.unit.apriori.binary.rulesEngine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryRuleEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.IRulesEngine;

/**
 * Testing rule harvesting from the passed item set.
 * 
 * @author Piotr
 * 
 */
public class RuleGeneration {

	private IRulesEngine engine;
	private int totalSupport;
	private double confidance;
	private BinaryItemSet premiseSet;
	private BinaryItemSet totalSet;

	@Before
	public void set_up() {
		engine = new BinaryRuleEngine();

		this.totalSupport = 10;
		this.confidance = 0.8;
	}

	@Test
	public void sample_case_1() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public void sample_case_2() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public void passing_empty_item_set_invokes_exception() {

		premiseSet = new BinaryItemSet(new char[] {});
		totalSet = new BinaryItemSet(new char[] {});

		try {
			engine.createRuleFromItemSet(totalSet, premiseSet, this.confidance,
					this.totalSupport);
		} catch (Exception e) {
			return;
		}

		Assert.fail("The exception should have been thrown");

	}

	@Test
	public void premises_set_must_be_in_total_set() {
		// TODO: write this vectors for the proper initialization
		char premiseVector = 0;
		char totalVector = 0;

		premiseSet = new BinaryItemSet(new char[] { premiseVector });
		totalSet = new BinaryItemSet(new char[] { totalVector });

		try {
			engine.createRuleFromItemSet(totalSet, premiseSet, this.confidance,
					this.totalSupport);
		} catch (Exception e) {
			return;
		}

		Assert.fail("The exception should have been thrown");
	}
}
