package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.List;
import java.util.SortedMap;

import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;

public interface IRulesEngine {

	/**
	 * Gets the list of rules inferred from provided <i>itemSet</i>.
	 * 
	 * @param itemSet
	 *            : the frequent set from which the rule has to be inferred.
	 * @param minCredibility
	 *            : Metrics of the rule, in our case also named
	 *            <i>Confidence</i>.
	 * @param frequentSet
	 *            : {@link SortedMap} used for storing frequent set values.
	 * @return list of rules inferred from this frequent set.
	 */
	public List<Rule> getRules(BinaryItemSet itemSet, double minCredibility,
			SortedMap<BinaryItemSet, Integer> frequentSet);

	/**
	 * Gets the {@link Rule} from the single frequent data set.
	 * 
	 * @param totalSet
	 *            : the whole frequent set.
	 * @param premiseSet
	 *            : conditional part of the set, which will be used to create
	 *            the rule.
	 * @param confidance
	 *            : metrics which will be passed to the rule
	 * @param totalSupport
	 *            : total support of the rule.
	 * @return Newly created rule.
	 */
	public Rule createRuleFromItemSet(BinaryItemSet totalSet,
			BinaryItemSet premiseSet, double confidance, int totalSupport);

}