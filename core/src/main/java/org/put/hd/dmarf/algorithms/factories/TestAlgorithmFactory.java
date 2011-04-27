package org.put.hd.dmarf.algorithms.factories;

import java.util.LinkedList;
import java.util.List;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.RandomAlgorithm;

/**
 * Simple class where algorithms will be placed for testing purposes.
 * NOTE: algorithms from this class will NOT be invoked during testing.
 * @author Piotr
 * 
 */
public class TestAlgorithmFactory implements IAlgorithmFactory {

	private List<IAlgorithm> algorithms;

	public TestAlgorithmFactory() {

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
