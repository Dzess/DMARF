package org.put.hd.dmarf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.factories.IAlgorithmFactory;
import org.put.hd.dmarf.algorithms.factories.ProductionAlgorithmFactory;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.AlgorithmBasedBuilderFactory;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;
import org.put.hd.dmarf.output.OutputFormatter;
import org.put.hd.dmarf.stopwatches.IStopWatch;
import org.put.hd.dmarf.stopwatches.StopWatch;

/**
 * Entry point for DMARF application.
 */
public class App {
	public static void main(String[] args) {
		
		// stopwatch element here
		IStopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// get some algorithm factory (use stable production factory)
		IAlgorithmFactory algorithmFactory = new ProductionAlgorithmFactory();

		ArgumentParser parser = null;
		try {
			// use parser
			parser = new ArgumentParser(algorithmFactory);
			parser.setInputArguments(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
		IAlgorithm algorithm = parser.getAlgorithm();

		// loading data phase
		// FIXME: change the data representation builder into the fancy one
		// using some kind of algorithms
		IDataRepresentationBuilder builder = new AlgorithmBasedBuilderFactory(algorithm);
		//IDataRepresentationBuilder builder = new BasicDataBuilder();
		IDataFormatter fomratter = new SimpleDataFormatter(builder);
		IDataLoader loader = new SimpleDataLoader(fomratter);
		loader.setInputFileName(parser.getInputFileName());
		DataRepresentationBase data = loader.loadData();

		// running the algorithm
		algorithm.start(data, parser.getMinSupport(),
				parser.getMinCredibility());

		// the main thread within algorithm should be sequential 
		stopWatch.stop();
		
		// saving the output
		OutputFormatter outputFormatter = new OutputFormatter();
		outputFormatter.setMinSupport(parser.getMinSupport());
		outputFormatter.setMinCredibility(parser.getMinCredibility());
		outputFormatter.setInputFileName(parser.getInputFileName());
		outputFormatter.setAlgorithm(algorithm);
		outputFormatter.setTotalTime(stopWatch.getElapsedTimeSecs());

		String outputString = outputFormatter.getFormattedOutputString();

		// write the output to the standard output
		System.out.println(outputString);
		
		// write the output the file name
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(parser.getOutputFileName()));
		    out.write(outputString);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
