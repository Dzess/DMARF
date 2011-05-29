package org.put.hd.dmarf.algorithms.factories;

import java.util.LinkedList;
import java.util.List;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.WekaAlgorithm;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryApriori;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryRuleEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.BinarySetsEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.InjectableSetsEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.JOCLSetsEngine;
import org.put.hd.dmarf.algorithms.apriori.binary.JOCLSetsEngine.DeviceTypeSelector;
import org.put.hd.dmarf.algorithms.apriori.nst.AprioriNST;

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

		// Standard non optimized algorithm for apriori with quite inefficient
		// implementation
		algorithms.add(new AprioriNST());

		// Standard CPU implementation of binary apriori
		algorithms.add(new BinaryApriori(new BinaryRuleEngine(),new BinarySetsEngine(),512));

		// CPU based apriori with support mining with OpenCL
		InjectableSetsEngine GPUsetsEngine = new InjectableSetsEngine(
				new BinarySetsEngine(), new JOCLSetsEngine(DeviceTypeSelector.Any));
		//will prefer GPU
		algorithms.add(new BinaryApriori(new BinaryRuleEngine(), GPUsetsEngine,4));

		// CPU based apriori with support mining with OpenCL
		InjectableSetsEngine CPUsetsEngine = new InjectableSetsEngine(
				new BinarySetsEngine(), new JOCLSetsEngine(DeviceTypeSelector.CPU));
		
		algorithms.add(new BinaryApriori(new BinaryRuleEngine(), CPUsetsEngine,4));
		
	}

	public int getNumberOfAlgorithms() {
		return algorithms.size();
	}

	public IAlgorithm getAlgorithm(int number) {
		return algorithms.get(number);
	}

}
