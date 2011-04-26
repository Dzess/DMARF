package org.put.hd.dmarf;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.IAlgorithmFactory;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;
import org.put.hd.dmarf.output.OutputFormatter;

/**
 * Entry point for DMARF application.
 */
public class App {
	public static void main(String[] args) {
		IAlgorithmFactory algorithmFactory = null;

		// use parser
		ArgumentParser parser = new ArgumentParser(algorithmFactory);
		parser.setInputArguments(args);

		// loading data phase
		IDataFormatter fomratter = null;
		IDataLoader loader = new SimpleDataLoader(fomratter);
		loader.setInputFileName(parser.getInputFileName());
		DataRepresentationBase data = loader.loadData();

		// running the algorithm
		IAlgorithm algorithm = parser.getAlgorithm();
		algorithm.start(data);

		// saving the output
		OutputFormatter outputFormatter = new OutputFormatter();
		outputFormatter.setMinSupport(parser.getMinSupport());
		outputFormatter.setMinCredibility(parser.getMinCredibility());
		outputFormatter.setInputFileName(parser.getInputFileName());
		outputFormatter.setAlgorithm(algorithm);

		String outputString = outputFormatter.getFormattedOutputString();

		// write the output to the standard output
		System.out.println(outputString);
	}
}
