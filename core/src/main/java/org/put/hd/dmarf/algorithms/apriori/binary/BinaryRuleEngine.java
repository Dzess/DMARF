package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import org.put.hd.dmarf.algorithms.Rule;

/**
 * Class encapsulates the whole logic of the rule harvesting. Quite the fast
 * uses {@link BinaryItemSet}
 * 
 * @author Piotr
 * 
 */
public class BinaryRuleEngine implements IRulesEngine {

	private int ruleCounter = 0;

	/**
	 * Number of bits per chunk. Using the datatype {@link Character} the number
	 * of bits is 16.
	 */
	private final int bitsFactor = 16;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.put.hd.dmarf.algorithms.apriori.binary.IRulesEngine#getRules(org.
	 * put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet,
	 * org.put.hd.dmarf.data.DataRepresentationBase, double,
	 * java.util.SortedMap)
	 */
	public List<Rule> getRules(BinaryItemSet itemSet, double minCredibility,
			SortedMap<BinaryItemSet, Integer> frequentSet) {

		List<Rule> itemSetRules = new LinkedList<Rule>();

		// all rule item set support (FOR THE WHOLE RULE)
		int suportXY = frequentSet.get(itemSet);

		// check if the set passed is the at least two element set
		for (char elements : itemSet.getAttributeVector()) {
			int count = bitcount(elements);

			// for one and two element sets return the empty list
			if (count == 0 || count == 1)
				return itemSetRules;
		}

		// create the veto list of elements that can be placed in conditional
		// part of the rule
		Collection<BinaryItemSet> vetoSets = new LinkedList<BinaryItemSet>();
		Collection<BinaryItemSet> nextSets = BinaryItemSet.divideSet(itemSet);

		// to search for all the combinations of the frequency sets
		// but only n-1 goes down is possible up to the two element sets
		for (int i = 0; i < itemSet.getAttributeVector().length * 16; i++) {

			// this shallow copy CAN be faster
			List<BinaryItemSet> smallerSets = new LinkedList<BinaryItemSet>(
					nextSets);

			// reset the next sets
			nextSets.clear();

			// checking if the set in the next is not in the veto list
			// FIXME: it is optimization possibility

			// check if they are eligible from confidence measure point of view
			// checking the premise set (n-1) from the current iteration
			// MT: this can also be parallel
			for (BinaryItemSet currentSet : smallerSets) {

				int supportX = frequentSet.get(currentSet);
				double confidanceXY = suportXY / (double) supportX;

				if (confidanceXY >= minCredibility) {

					// delegate creation of the rule to someone else
					Rule rule = createRuleFromItemSet(itemSet, currentSet,
							confidanceXY, suportXY);

					itemSetRules.add(rule);

					// mark the set as the one to further rule implications
					Collection<BinaryItemSet> sets = BinaryItemSet
							.divideSet(currentSet);
					if (sets.size() != 0)
						nextSets.addAll(sets);
				} else {

					// add all n-1 sets created from this set to the veto guys,
					// those wont be checked against the data
					Collection<BinaryItemSet> sets = BinaryItemSet
							.divideSet(currentSet);

					if (sets.size() != 0)
						vetoSets.addAll(sets);
				}
			}
		}

		return itemSetRules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.put.hd.dmarf.algorithms.apriori.binary.IRulesEngine#getRules(org.
	 * put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet,
	 * org.put.hd.dmarf.data.DataRepresentationBase, double,
	 * java.util.SortedMap)
	 */
	public Rule createRuleFromItemSet(BinaryItemSet itemSet,
			BinaryItemSet currentSet, double confidance, int suportXY) {

		if (itemSet.getNumberOfAttributes() == 0
				|| currentSet.getNumberOfAttributes() == 0)
			throw new RuntimeException("The empty set cannot be passed");

		List<Integer> condPart = new LinkedList<Integer>();
		List<Integer> exePart = new LinkedList<Integer>();

		char[] vectorAll = itemSet.getAttributeVector();
		char[] vectorPremise = currentSet.getAttributeVector();

		// FIXME: this method sucks, could be used faster approach (using
		// bitwise
		// operations such as AND,XOR
		// write reading the attribute vector and putting those elements
		// into the lists
		for (int i = 0; i < vectorPremise.length; i++) {

			// this gets the binary representation
			char[] binaryAll = getBinaryString(vectorAll[i]);
			char[] binaryPremise = getBinaryString(vectorPremise[i]);

			for (int j = 0; j < binaryAll.length; j++) {

				if (binaryAll[j] == '1') {
					Integer value = 16 * i + j + 1;
					if (binaryPremise[j] == '1') {
						condPart.add(value);
					} else {
						exePart.add(value);
					}
				} else {
					if (binaryPremise[j] == '1')
						throw new RuntimeException(
								"Total set is not super set of the passed permise");
				}

			}
		}

		Rule rule = new Rule(this.ruleCounter++, condPart, exePart,
				(int) (confidance * 100), suportXY);

		return rule;
	}

	/**
	 * Get the number of '1' in the binary representation number.
	 * 
	 * @param n
	 *            : number to be looked into.
	 * @return the number of '1' in the binary representation.
	 */
	private int bitcount(char x) {
		// classic shift method
		int result = 0;
		for (int i = 0; i < 16; i++) {
			result += x & 1;
			x >>>= 1;
		}
		return result;
	}

	/**
	 * Gets the same as the Integer.toBinaryString, but does it with trailing
	 * zeros at the beginning.
	 * 
	 * @param number
	 *            : the value to be changed into binary representation.
	 * @return the char[] representation of the passed value.
	 */
	private char[] getBinaryString(char number) {

		char value = number;

		char[] output = new char[16];

		// 'read from behind' the standard algorithm
		for (int i = 0; i < 16; i++) {
			if (value % 2 == 1) {
				output[15 - i] = '1';
			} else {
				output[15 - i] = '0';
			}
			value = (char) (value / 2);
		}

		return output;
	}

}
