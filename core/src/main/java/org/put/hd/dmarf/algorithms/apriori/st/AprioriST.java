package org.put.hd.dmarf.algorithms.apriori.st;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.put.hd.dmarf.algorithms.AlgorithmBase;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Much faster version of apriori algorithm using optimized Java structures for
 * data storage and some fancy features for being faster. Still though this code
 * is single threaded.
 * 
 * @author Piotr
 * 
 */
public class AprioriST extends AlgorithmBase {

	/**
	 * Created rules for apriori.
	 */
	private List<Rule> rules;

	private SortedMap<FastItemSet, Integer> frequentSet;
	private int ruleCounter = 0;

	private int supportThreshold;
	private double minSupport;
	private double minCredibility;

	@Override
	protected void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {

		rules = new LinkedList<Rule>();

		// for each frequent set
		// MT: this can be paralyzed quite easy
		for (Map.Entry<FastItemSet, Integer> entry : frequentSet.entrySet()) {

			// one element sets are bad ;) so avoid them
			if (entry.getKey().getElements().size() < 2)
				continue;

			// generate possible rules for this frequent set
			List<Rule> frequentSetRules = generateRulesForItemSet(
					entry.getKey(), data, minCredibility);

			rules.addAll(frequentSetRules);
		}
	}

	private List<Rule> generateRulesForItemSet(FastItemSet itemSet,
			DataRepresentationBase data, double minCredibility2) {

		List<Rule> itemSetRules = new LinkedList<Rule>();

		// all rule item set support (FOR THE WHOLE RULE)
		int suportXY = frequentSet.get(itemSet);

		// create the veto list of elements that can be placed in conditional
		// part of the rule
		List<FastItemSet> vetoSets = new LinkedList<FastItemSet>();
		List<FastItemSet> nextSets = getAllSets(itemSet.getElements());

		// to search for all the combinations of the frequency sets
		// but only n-1 goes down is possible up to the two element sets
		for (int i = 0; i < itemSet.getElements().size() - 1; i++) {

			// FIXME: this can be faster here i guess
			// get the all (n-1 elemented) sets, from the accepted set elements
			List<FastItemSet> smallerSets = new LinkedList<FastItemSet>(
					nextSets);

			// checking if the set in the next is not in the veto list
			// FIXME: it is optimization possibility

			// check if they are eligible from confidence measure point of view
			// MT: this can also be parallel
			for (FastItemSet currentSet : smallerSets) {

				int supportX = frequentSet.get(currentSet);
				double confidance = suportXY / supportX;

				if (confidance >= minCredibility) {

					// create the production rule here if the set is nice
					List<Integer> exectuivePart = new LinkedList<Integer>();
					List<Integer> conditionalPart = new LinkedList<Integer>();

					// MT: can be parallel
					for (Integer attribute : itemSet.getElements()) {

						if (currentSet.getElements().contains(attribute)) {
							// it gets to the conditional part
							conditionalPart.add(attribute);
						} else {
							exectuivePart.add(attribute);
						}
					}

					// the rule must have confidence in percents provided
					Rule rule = new Rule(ruleCounter++, conditionalPart,
							exectuivePart, (int) (confidance * 100), supportX);

					itemSetRules.add(rule);

					// mark the set as the one to further rule implications
					List<FastItemSet> sets = getAllSets(currentSet
							.getElements());
					if (sets != null)
						nextSets.addAll(sets);
				} else {

					// add all n-1 sets created from this set to the veto guys,
					// those wont be checked against the data
					List<FastItemSet> sets = getAllSets(currentSet
							.getElements());
					if (sets != null)
						vetoSets.addAll(sets);
				}
			}
		}

		return itemSetRules;
	}

	private List<FastItemSet> getAllSets(SortedSet<Integer> inputSet) {

		if (inputSet.size() == 1) {
			return null;
		}

		List<FastItemSet> outputSets = new LinkedList<FastItemSet>();

		// for each element, create exactly the same set as input, but remove
		// one element. during the iterations. Creating
		// number of n sets with n-1 elements.
		for (Integer element : inputSet) {
			FastItemSet smallerSet = new FastItemSet(inputSet);
			smallerSet.getElements().remove(element);

			outputSets.add(smallerSet);
		}
		return outputSets;
	}

	@Override
	protected void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {

		// assign some values
		this.minSupport = minSupport;
		this.minCredibility = minCredibility;

		// get the threshold for the minimal support of the frequent set =
		// ceiling { transactions * percent }
		supportThreshold = (int) Math.ceil(data.getTransactionsList().size()
				* minSupport);

		// first level frequent sets (based on the maps) - quite a nice feature
		SortedMap<FastItemSet, Integer> frequentSuppMap = generateLevelOneFreqent(data);

		// generate the frequent sets
		int generation = 0;
		while(true)
		{
			// generate candidates with generation (with the generation)
			// number of elements in them
			Set<FastItemSet> candidate = generateCandidates(frequentSuppMap,generation++);
			
			// verify that all elements in candidate set are eligible for 
			// being the frequent set
			// TODO: write this verification
			
			break;
		}

		// make the frequent set visible to the other part of algorithm
		frequentSet = frequentSuppMap;
	}

	private Set<FastItemSet> generateCandidates(
			SortedMap<FastItemSet, Integer> frequentSuppMap, int i) {
		
		Set<FastItemSet> candidateSet = new TreeSet<FastItemSet>();
		
		// TODO: write this generation here
		
		return candidateSet;
	}

	private SortedMap<FastItemSet, Integer> generateLevelOneFreqent(
			DataRepresentationBase data) {

		// MT: get the each attribute independently
		SortedMap<FastItemSet, Integer> frequentSets = new TreeMap<FastItemSet, Integer>();
		for (Map.Entry<Integer, Integer> entry : data.getAttributesCounter()
				.entrySet()) {

			Integer value = entry.getValue();
			Integer key = entry.getKey();

			// add elements only with proper support
			if (value >= supportThreshold) {
				FastItemSet itemSet = new FastItemSet(key);
				frequentSets.put(itemSet, value);
			}
		}
		return frequentSets;
	}

	@Override
	public List<Rule> getRules() {
		return rules;
	}

}
