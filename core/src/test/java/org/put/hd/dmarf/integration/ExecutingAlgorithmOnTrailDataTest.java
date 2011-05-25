package org.put.hd.dmarf.integration;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.algorithms.WekaAlgorithm;
import org.put.hd.dmarf.algorithms.factories.IAlgorithmFactory;
import org.put.hd.dmarf.algorithms.factories.ProductionAlgorithmFactory;
import org.put.hd.dmarf.algorithms.factories.TestAlgorithmFactory;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
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
	private IDataRepresentationBuilder builder;
	private IAlgorithmFactory factory;
	private IAlgorithm wekaProvider;

	@Before
	public void set_up() {

		// factory to be used - target: production level factory
		factory = new ProductionAlgorithmFactory();

		// set the loaders for the algorithms
		builder = new BasicDataBuilder();
		fomratter = new SimpleDataFormatter(builder);
		loader = new SimpleDataLoader(fomratter);

		// get the Weka provider
		wekaProvider = new WekaAlgorithm();

		// set the parameters of the algorithms
		minSupport = 0.6; // meaning 3 item must support (0*6 * 4 = 2,4 -> 3 rules i guess)
		minConfidance = 0.75;
	}

	@Test
	public void run_all_tests_from_simple_algorithm_factory_trial_2() {
		// path to the resources data
		String fileName = "resources/data/lecture.dat";

		double confidance = 0.5;
		double support = 0.7;

		// get data
		loader.setInputFileName(fileName);
		DataRepresentationBase data = loader.loadData();

		// get the weka results - expected ones
		wekaProvider.start(data, support, confidance);
		List<Rule> wekaResults = wekaProvider.getRules();

		// run the tests for each algorithm
		for (int i = 0; i < factory.getNumberOfAlgorithms(); i++) {

			// out part of algorithms
			IAlgorithm algorithm = factory.getAlgorithm(i);
			algorithm.start(data, support, confidance);

			// ignore the time just get output
			List<Rule> result = algorithm.getRules();

			// verify that result is good
			verifyOutput(result, wekaResults);
		}
	}

	@Test
	public void run_all_tests_from_simple_algorithm_factory_trial_1() {

		// path to the resources data
		String fileName = "resources/data/trail.dat";

		// get data
		loader.setInputFileName(fileName);
		DataRepresentationBase data = loader.loadData();

		// get the weka results - expected ones
		wekaProvider.start(data, minSupport, minConfidance);
		List<Rule> wekaResults = wekaProvider.getRules();

		// run the tests for each algorithm
		for (int i = 0; i < factory.getNumberOfAlgorithms(); i++) {

			// out part of algorithms
			IAlgorithm algorithm = factory.getAlgorithm(i);
			algorithm.start(data, minSupport, minConfidance);

			// ignore the time just get output
			List<Rule> result = algorithm.getRules();

			// verify that result is good
			verifyOutput(result, wekaResults);
		}

	}

	private void verifyOutput(List<Rule> result, List<Rule> expectedRules) {

		// check the sizes - must be the same
		if (result.size() != expectedRules.size()) {
			showRulesDifferences(result, expectedRules);
			Assert.fail("The both result rules should be of equal lenght");
		}

		// for each rule in the rule set search it in the output rule set
		for (Rule rule : result) {

			if (!expectedRules.contains(rule)) {
				showRulesDifferences(result, expectedRules);
				Assert.fail("The rule" + rule
						+ "could not be found in expected rule set.");
			}
		}

	}

	private void showRulesDifferences(List<Rule> result,
			List<Rule> expectedRules) {
		// Show what went wring
		System.err.println("OutCome: ");
		for (Rule r : result) {
			System.out.println(r);
		}

		System.err.println("Expected outcome: ");
		for (Rule r : expectedRules) {
			System.out.println(r);
		}

		System.err.flush();
	}
}
