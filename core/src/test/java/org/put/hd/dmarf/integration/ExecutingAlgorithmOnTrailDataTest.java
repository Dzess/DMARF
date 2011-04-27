package org.put.hd.dmarf.integration;

import java.util.LinkedList;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.algorithms.factories.IAlgorithmFactory;
import org.put.hd.dmarf.algorithms.factories.TestAlgorithmFactory;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.builders.IDataReprsentatinoBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

/**
 * Runs all the tests for each of the algorithms in the
 * {@link IAlgorithmFactory}. Passing this tests ensures that all algorithms in
 * the factory are implemented properly. Validation is made against the
 * resources/trail.dat file.
 * 
 * @author Piotr
 * 
 */
public class ExecutingAlgorithmOnTrailDataTest {

	private double minSupport;
	private double minConfidance;

	/*
	 * Data loader initializations
	 */
	private IDataLoader loader;
	private IDataFormatter fomratter;
	private IDataReprsentatinoBuilder builder;
	private TestAlgorithmFactory factory;

	@Before
	public void set_up() {

		// TODO: change into production factory
		// place here factory to be used
		factory = new TestAlgorithmFactory();

		// set the loaders for the algorithms
		builder = new BasicDataBuilder();
		fomratter = new SimpleDataFormatter(builder);
		loader = new SimpleDataLoader(fomratter);

		// set the parameters of the algorithms
		minSupport = 0.3;
		minConfidance = 0.6;
	}

	@Test
	public void run_all_tests_from_simple_algorithm_factory_trial_1() {

		// path to the resources data
		String fileName = "resources/data/trail.dat";

		// get data
		loader.setInputFileName(fileName);
		DataRepresentationBase data = loader.loadData();

		// run the tests for each algorithm
		for (int i = 0; i < factory.getNumberOfAlgorithms(); i++) {
			IAlgorithm algorithm = factory.getAlgorithm(i);
			algorithm.start(data, minSupport, minConfidance);

			// ignore the time just get output
			List<Rule> result = algorithm.getRules();

			// verify that result is good
			verifyOutput(result, getRulesForTrail_1());
		}

	}

	private List<Rule> getRulesForTrail_1() {
		List<Rule> list = new LinkedList<Rule>();

		// TODO: mark the rules which should be used 
		// TODO: use Weka for that ? - thats the tricky idea
		
		return list;
	}

	private void verifyOutput(List<Rule> result, List<Rule> expectedRules) {

		// check the sizes - must be the same
		if(result.size() != expectedRules.size())
			Assert.fail("The both result rules should be the equal lenght");
		
		
		// for each rule in the rule set search it in the output rule set
		for (Rule rule : result) {
			// TODO: get the validation properly
		}
		
	}

	// TODO: another files with some nice input for validation
}
