package org.put.hd.dmarf.algorithms;

import java.util.List;

/**
 * Represents the class for association rule. Immutable type.
 * 
 * @author Piotr
 * 
 */
public class Rule {

	private int id;

	public Rule(int id, List<Integer> conditionalPart,
			List<Integer> exectuivePart, int confidance, int support) {
		this.id = id;
		this.conditionalPart = conditionalPart;
		this.exectuivePart = exectuivePart;
		this.confidance = confidance;
		this.support = support;
	}

	private List<Integer> conditionalPart;
	private List<Integer> exectuivePart;
	private int confidance;
	private int support;

	public int getId() {

		return id;
	}

	public List<Integer> getConditionalPart() {
		return conditionalPart;
	}

	public List<Integer> getExecutivePart() {
		return exectuivePart;
	}

	public int getSupport() {
		return support;
	}

	public int getConfidance() {
		return confidance;
	}
}
