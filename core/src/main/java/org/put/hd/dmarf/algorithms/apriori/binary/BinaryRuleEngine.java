package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Class encapsulates the whole logic of the rule harvesting. Quite the fast
 * uses {@link BinaryItemSet}
 * 
 * @author Piotr
 * 
 */
public class BinaryRuleEngine {

	private int ruleCounter = 0;
	private double minCredibility;

	/**
	 * Number of bits per chunk. Using the datatype {@link Character} the number
	 * of bits is 16.
	 */
	private final int bitsFactor = 16;

	public List<Rule> getRules(BinaryItemSet itemSet,
			DataRepresentationBase data, double minCredibility,
			SortedMap<BinaryItemSet, Integer> frequentSet) {

		// save some of the features
		this.minCredibility = minCredibility;
		List<Rule> itemSetRules = new LinkedList<Rule>();

		// all rule item set support (FOR THE WHOLE RULE)
		int suportXY = frequentSet.get(itemSet);

		// create the veto list of elements that can be placed in conditional
		// part of the rule
		Collection<BinaryItemSet> vetoSets = new LinkedList<BinaryItemSet>();
		Collection<BinaryItemSet> nextSets =  BinaryItemSet.divideSet(itemSet);

		// to search for all the combinations of the frequency sets
		// but only n-1 goes down is possible up to the two element sets
		for (int i = 0; i < itemSet.getAttributeVector().length * bitsFactor
				- 1; i++) {

			// FIXME: this can be faster here i guess
			// get the all (n-1 elemented) sets, from the accepted set elements
			List<BinaryItemSet> smallerSets = new LinkedList<BinaryItemSet>(nextSets);

			// checking if the set in the next is not in the veto list
			// FIXME: it is optimization possibility

			// check if they are eligible from confidence measure point of view
			// MT: this can also be parallel
			for (BinaryItemSet currentSet : smallerSets) {

				int supportX = frequentSet.get(currentSet);
				double confidanceXY = suportXY / supportX;

				if (confidanceXY >= minCredibility) {

					// delegate creation of the rule to someone else
					Rule rule = createRuleFromItemSet(itemSet, currentSet,
							confidanceXY, suportXY);

					itemSetRules.add(rule);

					// mark the set as the one to further rule implications
					Collection<BinaryItemSet> sets = BinaryItemSet.divideSet(currentSet);
					if (sets != null)
						nextSets.addAll(sets);
				} else {

					// add all n-1 sets created from this set to the veto guys,
					// those wont be checked against the data
					Collection<BinaryItemSet> sets = BinaryItemSet.divideSet(currentSet);

					if (sets != null)
						vetoSets.addAll(sets);
				}
			}
		}

		return itemSetRules;
	}

	private Rule createRuleFromItemSet(BinaryItemSet itemSet,
			BinaryItemSet currentSet, double confidance, int suportXY) {

		List<Integer> condPart = new LinkedList<Integer>();
		List<Integer> exePart = new LinkedList<Integer>();

		char[] vectorAll = itemSet.getAttributeVector();
		char[] vectorPremise = currentSet.getAttributeVector();

		// FIXME: this method sucks, could be used faster approach
		// write reading the attribute vector and putting those elements
		// into the lists
		for (int i = 0; i < vectorPremise.length; i++) {

			// this gets the binary representation - can be fasten using some
			// bitwise tricks
			char[] binaryAll = Integer.toBinaryString(vectorAll[i])
					.toCharArray();
			char[] binaryPremise = Integer.toBinaryString(vectorPremise[i])
					.toCharArray();

			for (int j = 0; j < binaryAll.length; j++) {
				if (binaryAll[j] == 1) {
					Integer value = (int) Math.pow(2, j + i);
					if (binaryPremise[j] == 1) {
						condPart.add(value);
					} else {
						exePart.add(value);
					}
				}

			}
		}

		Rule rule = new Rule(this.ruleCounter++, condPart, exePart,
				(int) (confidance * 100), suportXY);

		return rule;
	}


}
