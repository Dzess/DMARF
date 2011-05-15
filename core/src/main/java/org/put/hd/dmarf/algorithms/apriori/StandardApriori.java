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

		// get the threshold for the minimal support of the frequent set =
		// ceiling { transactions * percent }
		supportThreshold = (int) Math.ceil(data.getTransactionsList().size()
				* minSupport);

		// first level frequent sets (based on the maps) - quite a nice feature
		SortedMap<ItemSet, Integer> frequentSets = getLevelOneFrequentSets(data);

		// TODO: warp this code into loop
		{
			// build the candidates for the frequent sets
			List<ItemSet> candidateKeys = getCandidateFromFrequentSet(frequentSets,1);
	
			// verify all the elements in the candidate set
			verifyCandidatesForFrequentSets(data, frequentSets, candidateKeys);
		}
		
		// make the frequent set visible to the other part of algorithm
		frequentSet = frequentSets;
		
	}

	/**
	 * Goes for the data and checks if the items in frequent sets candidate are eligible for 
	 * frequent set name.
	 * @param data
	 * @param frequentSets
	 * @param candidateKeys
	 */
	private void verifyCandidatesForFrequentSets(DataRepresentationBase data,
			SortedMap<ItemSet, Integer> frequentSets,
			List<ItemSet> candidateKeys) {
		// FIXME: this can be fasten
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
	 * @param frequentSets: the sets used to generate
	 * @param generation: the number of the generation used to create the sets. This will allow for generating
	 * only the sets with exactly <i>generation + generations</i>.
	 * @return candidates for frequent sets.
	 */
	private List<ItemSet> getCandidateFromFrequentSet(
			SortedMap<ItemSet, Integer> frequentSets,
			int generation) {

		List<ItemSet> candidateKeys = new LinkedList<ItemSet>();
		// FIXME: this can be fasten x2 (but still O(n^2))
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

		rules = new LinkedList<Rule>();
		// TODO: fill the list from the generated sets
	}

	@Override
	public List<Rule> getRules() {
		return rules;
	}

}
