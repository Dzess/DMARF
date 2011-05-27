package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.naming.directory.InvalidAttributesException;

import org.put.hd.dmarf.algorithms.AlgorithmBase;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BinaryDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;

/**
 * Apriori working on the nice features using bit level optimization and
 * lightweight collections. Main use here are classes: {@link BinaryItemSet} and
 * engines for various operations. Engine for sets creations:
 * {@link BinarySetsEngine} Engine for rule generation: {@link BinaryRuleEngine}
 * 
 * @author Piotr
 * 
 */
public class BinaryApriori extends AlgorithmBase {

	private List<Rule> rules;
	private int supportThreshold;

	private ISetsEngine binaryEngine;
	private SortedMap<BinaryItemSet, Integer> frequentSet;
	private IRulesEngine binaryRuleEngine;
	private List<IDataRepresentationBuilder> passedBuilders;

	/**
	 * Constructor. Initializes the default engines for data mining. Mainly
	 * support mining and structures for mining the data sets.
	 * 
	 * @param binaryRuleEngine
	 *            :engine that will be used for creating rules
	 * 
	 * @param binaryEngine
	 *            : engine that will be used for creating sets
	 */
	public BinaryApriori(IRulesEngine binaryRuleEngine, ISetsEngine binaryEngine) {
		this.binaryEngine = binaryEngine;
		this.binaryRuleEngine = binaryRuleEngine;

		// TODO: change here passing the builders
		this.passedBuilders = new LinkedList<IDataRepresentationBuilder>();
		this.passedBuilders.add(new BinaryDataBuilder());
	}

	@Override
	protected void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {

		rules = new LinkedList<Rule>();

		// for each frequent set
		// MT: this can be paralyzed quite easy
		for (Map.Entry<BinaryItemSet, Integer> entry : frequentSet.entrySet()) {

			// generate possible rules for this frequent set
			List<Rule> frequentSetRules = this.binaryRuleEngine.getRules(
					entry.getKey(), minCredibility, frequentSet);

			rules.addAll(frequentSetRules);
		}

	}

	@Override
	protected void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {

		// get the threshold for the minimal support of the frequent set =
		// ceiling { transactions * percent }
		this.supportThreshold = (int) Math.ceil(data.getTransactionsList()
				.size() * minSupport);

		// first level frequent sets (based on the maps) - quite a nice feature
		SortedMap<BinaryItemSet, Integer> frequentSuppMap = this.binaryEngine
				.getSingleCandidateSets(data, supportThreshold);

		Set<BinaryItemSet> approvedCandidates = frequentSuppMap.keySet();

		// generate the frequent sets starting from generation two
		int generation = 1;
		while (true) {

			// generate candidates with generation (with the generation)
			// number of elements in them
			Set<BinaryItemSet> candidates = this.binaryEngine.getCandidateSets(
					approvedCandidates, ++generation);

			// verify that all elements in candidate set are eligible for
			// being the frequent set
			SortedMap<BinaryItemSet, Integer> candidatesAccepted = this.binaryEngine
					.verifyCandidatesInData(data, candidates, supportThreshold);

			// mark for the next elements
			approvedCandidates = candidatesAccepted.keySet();

			// add to frequent sets
			frequentSuppMap.putAll(candidatesAccepted);

			// if zero then break the algorithm
			if (approvedCandidates.size() == 0)
				break;
		}

		// make the frequent set visible to the other part of algorithm
		frequentSet = frequentSuppMap;
	}

	@Override
	public List<Rule> getRules() {
		return rules;
	}

	@Override
	protected void initMemory(DataRepresentationBase data) {
		try {
			binaryEngine.initEngine(data);
		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void cleanupMemory() {
		binaryEngine.cleanupEngine();

	}

	public List<IDataRepresentationBuilder> getRequiredBuilders() {
		List<IDataRepresentationBuilder> builders = new LinkedList<IDataRepresentationBuilder>(
				passedBuilders);

		return builders;
	}

}
