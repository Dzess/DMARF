package org.put.hd.dmarf.algorithms;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple class where algorithms will be placed
 * 
 * @author Piotr
 * 
 */
public class SimpleAlgorithmFactory implements IAlgorithmFactory {

	private List<IAlgorithm> algorithms;

	public SimpleAlgorithmFactory() {

		algorithms = new LinkedList<IAlgorithm>();
		algorithms.add(new RandomAlgorithm());
	}

	public int getNumberOfAlgorithms() {
		return algorithms.size();
	}

	public IAlgorithm getAlgorithm(int number) {
		return algorithms.get(number);
	}

}
