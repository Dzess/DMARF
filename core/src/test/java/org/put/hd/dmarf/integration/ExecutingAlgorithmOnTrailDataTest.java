package org.put.hd.dmarf.integration;

import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.AgainstWekaTestingBase;
import org.put.hd.dmarf.algorithms.factories.IAlgorithmFactory;

/**
 * Runs all the tests for each of the algorithms in the
 * {@link IAlgorithmFactory}. Passing this tests ensures that all algorithms in
 * the factory are implemented properly. Validation is made against the
 * resources/trail.dat file.
 * 
 * @author Piotr
 * 
 */
public class ExecutingAlgorithmOnTrailDataTest extends AgainstWekaTestingBase {

	@Before
	public void set_up(){
		this.setUp();
	}
	
	@Test
	public void run_all_tests_from_simple_algorithm_factory_trial_2() {
		// path to the resources data
		String fileName = "resources/data/lecture.dat";

		double confidance = 0.5;
		double support = 0.7;

		runTestingForDataSet(fileName);
	}

	@Test
	public void run_all_tests_from_simple_algorithm_factory_trial_1() {

		// path to the resources data
		String fileName = "resources/data/trail.dat";

		runTestingForDataSet(fileName);

	}
}
