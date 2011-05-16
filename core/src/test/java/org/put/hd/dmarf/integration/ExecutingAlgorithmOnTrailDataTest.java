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
	private IAlgorithmFactory factory;
	private IAlgorithm wekaProvider;

	@Before
	public void set_up() {

		// TODO: change into production factory
		// place here factory to be used
		factory = new ProductionAlgorithmFactory();

		// set the loaders for the algorithms
		builder = new BasicDataBuilder();
		fomratter = new SimpleDataFormatter(builder);
		loader = new SimpleDataLoader(fomratter);

		// get the Weka provider
		wekaProvider = new WekaAlgorithm();
		
		// set the parameters of the algorithms
		minSupport = 0.6;
		minConfidance = 0.75;
	}
	
	@Test
	public void run_all_test_on_sample_data_from_lectures(){
		// path to the resources data
		String fileName = "resources/data/lecture.dat";

		// get data
		loader.setInputFileName(fileName);
		DataRepresentationBase data = loader.loadData();
		
		wekaProvider.start(data, 0.7, 0.5);
		List<Rule> wekaRules = wekaProvider.getRules();
		System.out.println("WEKAs in (" + wekaProvider.getElapsedTimeOverall() + ")");
		for (Rule rule : wekaRules) {
			System.out.println(rule);
		}
		
		
		// get standard apriori working
		IAlgorithm algorithm = factory.getAlgorithm(1);
		
		algorithm.start(data, 0.7, 0.5);
		List<Rule> aprioriRules = algorithm.getRules();
		System.out.println("Apriori ST in (" + algorithm.getElapsedTimeOverall() + ")");
		for (Rule rule : aprioriRules) {
			System.out.println(rule);
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
		if(result.size() != expectedRules.size())
			Assert.fail("The both result rules should be of equal lenght");
		
		
		// for each rule in the rule set search it in the output rule set
		for (Rule rule : result) {
				
			if(!expectedRules.contains(rule))
				Assert.fail("The rule" + rule + "could not be found in expected rule set.");
		}
		
	}

	// TODO: another files with some nice input for validation
}
