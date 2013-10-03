package org.put.hd.dmarf;

import java.util.List;

import org.junit.Assert;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.Rule;
import org.put.hd.dmarf.algorithms.WekaAlgorithm;
import org.put.hd.dmarf.algorithms.factories.IAlgorithmFactory;
import org.put.hd.dmarf.algorithms.factories.ProductionAlgorithmFactory;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.AlgorithmBasedBuilderFactory;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;
import org.put.hd.dmarf.stopwatches.StopWatch;

public class AgainstWekaTestingBase {

	protected double minSupport;
	protected double minConfidence;
	protected IDataLoader loader;
	protected IDataFormatter fomratter;
	protected IDataRepresentationBuilder builder;
	protected IAlgorithmFactory factory;
	protected IAlgorithm wekaProvider;

	public AgainstWekaTestingBase() {
		super();
	}

	protected void runTestingForDataSet(String fileName) {
		StopWatch sw = new StopWatch();
		
		sw.start();
		DataRepresentationBase wekaData = getWekaData(fileName);

		// get the Weka results - expected ones
		wekaProvider.start(wekaData, minSupport, minConfidence);
		List<Rule> wekaResults = wekaProvider.getRules();
		sw.stop();
		
		System.out.println("Weka preRun time [s]: "+ sw.getElapsedTimeSecs());

		// run the tests for each algorithm
		for (int i = 0; i < factory.getNumberOfAlgorithms(); i++) {

			// out part of algorithms
			IAlgorithm algorithm = factory.getAlgorithm(i);

			// get data from algorithm
			getAlgorithmData(algorithm);

			// get data
			loader.setInputFileName(fileName);
			DataRepresentationBase data = loader.loadData();

			// go with the algorithms
			sw.start();
			algorithm.start(data, minSupport, minConfidence);
			sw.stop();

			System.out.println("Algorithm: " + algorithm.toString()
					+ " took [s]: " + sw.getElapsedTimeSecs());
			// ignore the time just get output
			List<Rule> result = algorithm.getRules();

			sw.start();
			// verify that result is good
			verifyOutput(result, wekaResults);
			sw.stop();
			System.out.println("Verify took [s]: "+sw.getElapsedTimeSecs());
		}
	}

	private void getAlgorithmData(IAlgorithm algorithm) {
		builder = new AlgorithmBasedBuilderFactory(algorithm);
		fomratter = new SimpleDataFormatter(builder);
		loader = new SimpleDataLoader(fomratter);
	}

	private DataRepresentationBase getWekaData(String fileName) {
		IDataRepresentationBuilder wekaBuilder = new AlgorithmBasedBuilderFactory(
				wekaProvider);
		IDataFormatter wekaFormatter = new SimpleDataFormatter(wekaBuilder);
		SimpleDataLoader wekaLoader = new SimpleDataLoader(wekaFormatter);
		wekaLoader.setInputFileName(fileName);
        return wekaLoader.loadData();
	}

	private void verifyOutput(List<Rule> result, List<Rule> expectedRules) {

		// check the sizes - must be the same
		if (result.size() != expectedRules.size()) {
			//showRulesDifferences(result, expectedRules);
			Assert.fail("The both result rules should be of equal lenght");
		}

		// for each rule in the rule set search it in the output rule set
		for (Rule rule : result) {

			if (!expectedRules.contains(rule)) {
				//showRulesDifferences(result, expectedRules);
				Assert.fail("The rule" + rule
						+ "could not be found in expected rule set.");
			}
		}

	}

	private void showRulesDifferences(List<Rule> result,
			List<Rule> expectedRules) {

		// Show what went wrong
		System.err.println("Outcome: ");

		for (Rule r : result) {
			System.err.println(r);
		}

		System.err.flush();

		System.err.println("Expected outcome: ");
		for (Rule r : expectedRules) {
			System.err.println(r);
		}

		System.err.flush();
	}

	public void setUp() {

		// factory to be used - target: production level factory
		factory = new ProductionAlgorithmFactory();

		// get the Weka provider
		wekaProvider = new WekaAlgorithm();

		// set the parameters of the algorithms
		minSupport = 0.6; // meaning 3 item must support (0*6 * 4 = 2,4 -> 3
							// rules i guess)
		minConfidence = 0.75;
	}

}