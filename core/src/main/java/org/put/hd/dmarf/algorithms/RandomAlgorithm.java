package org.put.hd.dmarf.algorithms;

import java.util.LinkedList;
import java.util.List;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Simple random algorithm class that uses the base of the algorithm, to show how to use the 
 * {@link AlgorithmBase} class mechanisms.
 * @author Piotr
 *
 */
public class RandomAlgorithm extends AlgorithmBase {

	private List<Rule> rules = new LinkedList<Rule>();
	
	@Override
	public List<Rule> getRules() {
		return rules;
	}

	@Override
	protected void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {
		// get some random rules out of data
	}

	@Override
	protected void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility) {
		// NOTE: in this method there is no such step
	}


}
