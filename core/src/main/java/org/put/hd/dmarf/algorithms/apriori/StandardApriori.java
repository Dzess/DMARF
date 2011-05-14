package org.put.hd.dmarf.algorithms.apriori;

import java.util.List;

import org.put.hd.dmarf.algorithms.AlgorithmBase;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Classic implementation of the apriori algorithm. 
 * Single threaded, no optimization.
 * @author Piotr
 *
 */
public class StandardApriori extends AlgorithmBase {

	@Override
	protected void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Rule> getRules() {
		// TODO Auto-generated method stub
		return null;
	}

}
