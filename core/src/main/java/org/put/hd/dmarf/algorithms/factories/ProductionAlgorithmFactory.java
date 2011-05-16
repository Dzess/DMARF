package org.put.hd.dmarf.algorithms.factories;

import java.util.LinkedList;

import java.util.List;
import jcuda.*;
import jcuda.driver.JCudaDriver;
import jcuda.runtime.*;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.WekaAlgorithm;
import org.put.hd.dmarf.algorithms.apriori.AprioriCudaAlgorithm;
import org.put.hd.dmarf.algorithms.apriori.StandardApriori;

/**
 * Production factory for code. NOTE: each algorithm in this factory will be
 * tested at integration level by maven.
 * 
 * @author Piotr
 * 
 */
public class ProductionAlgorithmFactory implements IAlgorithmFactory {

	private List<IAlgorithm> algorithms;

	public ProductionAlgorithmFactory() {
		algorithms = new LinkedList<IAlgorithm>();

		// Weka algorithm is the first one to be implemented in production
		// phase.
		algorithms.add(new WekaAlgorithm());
		
		// Standard non optimized algorithm for apriori with quite inefficient implementation
		algorithms.add(new StandardApriori());

	}

	public int getNumberOfAlgorithms() {
		return algorithms.size();
	}

	public IAlgorithm getAlgorithm(int number) {
		return algorithms.get(number);
	}

}
