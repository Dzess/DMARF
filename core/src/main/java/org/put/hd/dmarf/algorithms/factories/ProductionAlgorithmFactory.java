package org.put.hd.dmarf.algorithms.factories;

import java.util.LinkedList;
import java.util.List;

import org.put.hd.dmarf.algorithms.IAlgorithm;

/**
 * Production factory for code.
 * NOTE: each algorithm in this factory will be tested at integration level by 
 * maven.
 * @author Piotr
 *
 */
public class ProductionAlgorithmFactory implements IAlgorithmFactory {

	private List<IAlgorithm> algorithms;
	
	public ProductionAlgorithmFactory(){
		algorithms = new LinkedList<IAlgorithm>();
	}
	
	public int getNumberOfAlgorithms() {
		return algorithms.size(); 
	}

	public IAlgorithm getAlgorithm(int number) {
		return algorithms.get(number);
	}

}
