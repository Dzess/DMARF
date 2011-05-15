package org.put.hd.dmarf.algorithms.apriori;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.put.hd.dmarf.algorithms.AlgorithmBase;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Classic implementation of the apriori algorithm. Single threaded, no
 * optimization. Data structures used to it - suck all the way. Uses
 * {@link String} version of the {@link DataRepresentationBase} for the
 * generation of the elements.
 * 
 * @author Piotr
 * 
 */
public class StandardApriori extends AlgorithmBase {

	/**
	 * Nested class for representing the frequent set. Used another class for
	 * that because of the {@link Comparable} interface which is must have
	 * feature for {@link SortedList}
	 * 
	 * @author Piotr
	 * 
	 */
	class ItemSet implements Comparable<ItemSet> {
		private SortedSet<String> elements;

		public SortedSet<String> getElements() {
			return elements;
		}

		public ItemSet() {
			elements = new TreeSet<String>();
		}

		public ItemSet(ItemSet set1, ItemSet set2) {

		}

		public ItemSet(String string) {
			elements = new TreeSet<String>();
			elements.add(string);
		}

		public ItemSet(SortedSet<String> set) {
			elements = set;
		}

		public int compareTo(ItemSet o) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private List<Rule> rules;
	private int supportThreshold;
	private SortedMap<ItemSet, Integer> frequentSet;

	@Override
	protected void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {

		rules = new LinkedList<Rule>();

		// for each frequent set 
		// MT: this can be paralyzed quite easy
		for (Map.Entry<ItemSet, Integer> entry : frequentSet.entrySet()) {

			// generate possible rules for this frequent set
			List<Rule> frequentSetRules = getRulesFromItemSet(entry.getKey(),
					data, minCredibility);
			rules.addAll(frequentSetRules);
		}

	}

	/**
	 * Goes for the data and checks if the items in frequent sets candidate are
	 * eligible for frequent set name.
	 * 
	 * @param data
	 * @param frequentSets
	 * @param candidateKeys
	 */
	private void verifyCandidatesForFrequentSets(DataRepresentationBase data,
			SortedMap<ItemSet, Integer> frequentSets,
			List<ItemSet> candidateKeys) {
		
		// FIXME: this can be fasten
		// well how can this fasten ? - forgot the main idea behind it in ST version
		// MT: each thread can verify totally independently without the others
		for (ItemSet candidate : candidateKeys) {

			// for each itemSet check all transactions and get the support
			int supportInData = 0;
			for (List<String> transaction : data.getTransactions()) {
				boolean hasTransactionItems = true;

				for (String string : candidate.getElements()) {
					if (!transaction.contains(string))
						hasTransactionItems = false;
					break;
				}
				if (hasTransactionItems)
					supportInData++;
			}

			// if support condition is fulfilled add the candidate to the
			// frequent set
			if (supportInData >= supportThreshold) {
				frequentSets.put(candidate, supportInData);
			}
		}
	}

	/**
	 * Generates the candidate for the frequent sets using avaliable frequent
	 * sets.
	 * 
	 * @param frequentSets
	 *            : the sets used to generate
	 * @param generation
	 *            : the number of the generation used to create the sets. This
	 *            will allow for generating only the sets with exactly
	 *            <i>generation + generations</i>.
	 * @return candidates for frequent sets.
	 */
	private List<ItemSet> getCandidateFromFrequentSet(
			SortedMap<ItemSet, Integer> frequentSets, int generation) {

		List<ItemSet> candidateKeys = new LinkedList<ItemSet>();
		// FIXME: this can be fasten x2 (but still O(n^2))
		// try generating the element not by two big for, but use tree like connecting,
		// the number of candidates is crucial for the algorithms
		for (Map.Entry<ItemSet, Integer> entry : frequentSets.entrySet()) {
			for (Map.Entry<ItemSet, Integer> secondEntry : frequentSets
					.entrySet()) {
				
				if (!entry.getKey().equals(secondEntry.getKey())) {
					ItemSet itemSet = new ItemSet(entry.getKey(),
							secondEntry.getKey());

					candidateKeys.add(itemSet);
				}
			}
		}

		return candidateKeys;
	}

	/**
	 * Generates the first level of the frequent sets using only the
	 * {@link DataRepresentationBase}. Uses the {@link TreeMap}
	 * 
	 * @param data
	 *            : data passed to apriori
	 * @return frequent sets for the first level.
	 */
	private SortedMap<ItemSet, Integer> getLevelOneFrequentSets(
			DataRepresentationBase data) {
		
		// MT: get the each attribute independently
		SortedMap<ItemSet, Integer> frequentSets = new TreeMap<ItemSet, Integer>();
		for (Map.Entry<String, Integer> entry : data.getAttributes().entrySet()) {
			Integer value = entry.getValue();
			if (value >= supportThreshold) {
				frequentSets.put(new ItemSet(entry.getKey()), value);
			}
		}

		return frequentSets;
	}

	@Override
	protected void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {

		// get the threshold for the minimal support of the frequent set =
		// ceiling { transactions * percent }
		supportThreshold = (int) Math.ceil(data.getTransactionsList().size()
				* minSupport);

		// first level frequent sets (based on the maps) - quite a nice feature
		SortedMap<ItemSet, Integer> frequentSuppMap = getLevelOneFrequentSets(data);

		// this loop will go up to the number of all attributes
		// FIXME: this can be fasten up to the moment when nothings change in
		// the map
		for (int i = 0; i < frequentSuppMap.size(); i++) {
			// build the candidates for the frequent sets
			List<ItemSet> candidateKeys = getCandidateFromFrequentSet(frequentSuppMap, 1);

			// verify all the elements in the candidate set
			verifyCandidatesForFrequentSets(data, frequentSuppMap, candidateKeys);
		}

		// make the frequent set visible to the other part of algorithm
		frequentSet = frequentSuppMap;

	}

	/**
	 * Gets the rules out of the passed itemSet
	 * 
	 * @param minCredibility
	 * @param data
	 * @param itemSet
	 *            : one of the frequent items.
	 * @return List of rules which satisfy minimal credibility.
	 */
	private List<Rule> getRulesFromItemSet(ItemSet itemSet,
			DataRepresentationBase data, double minCredibility) {

		List<Rule> itemSetRules = new LinkedList<Rule>();
		// TODO: make this counter global
		int ruleCounter = 0;

		// all rule item set support (FOR THE WHOLE RULE)
		int suportXY = frequentSet.get(itemSet);

		// create the veto list of elements that can be placed in conditional
		// part of the rule
		List<ItemSet> vetoSets = new LinkedList<ItemSet>();

		// TODO: warp this code into loop - to search for all the combinations of the frequency sets
		{

			// get the all (n-1 elemented) sets,
			List<ItemSet> smallerSets = getAllSets(itemSet.getElements());

			// check if they are eligible from confidence measure point of view
			// MT: this can also be parallel
			for (ItemSet currentSet : smallerSets) {

				int supportX = frequentSet.get(currentSet);
				double confidance = suportXY / supportX;

				if (confidance >= minCredibility) {

					// create the production rule here if the set is nice
					List<Integer> exectuivePart = new LinkedList<Integer>();
					List<Integer> conditionalPart = new LinkedList<Integer>();

					// MT: can be parallel 
					for (String attribute : itemSet.getElements()) {
						// FIXME: this is super slow to integer parsing -
						// totally senseless
						if (currentSet.getElements().contains(attribute)) {
							// it gets to the conditional part
							conditionalPart.add(Integer.parseInt(attribute));
						} else {
							exectuivePart.add(Integer.parseInt(attribute));
						}
					}

					// the rule must have confidence in percents provided
					Rule rule = new Rule(ruleCounter++, conditionalPart,
							exectuivePart, (int) (confidance * 100), supportX);

					itemSetRules.add(rule);
				} else {
					// add all n-1 sets created from this set to the veto guys,
					// those wont be checked against the data
					vetoSets.addAll(getAllSets(currentSet.getElements()));
				}
			}
		}

		return itemSetRules;
	}

	/**
	 * Returns <b>all</b> subsets of the following passed sets with <b>n-1
	 * elements.</b>.
	 * 
	 * @param inputSet
	 * @return
	 */
	private List<ItemSet> getAllSets(SortedSet<String> inputSet) {

		if (inputSet.size() == 1)
			throw new RuntimeException("Set must have at least two elements");

		List<ItemSet> outputSets = new LinkedList<ItemSet>();

		// for each element, create exactly the same set as input, but remove
		// one element. during the iterations. Creating
		// number of n sets with n-1 elements.
		for (String element : inputSet) {
			ItemSet smallerSet = new ItemSet(inputSet);
			smallerSet.getElements().remove(element);

			outputSets.add(smallerSet);
		}
		return outputSets;
	}

	@Override
	public List<Rule> getRules() {
		return rules;
	}

}
