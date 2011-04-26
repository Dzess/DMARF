package org.put.hd.dmarf;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.algorithms.IAlgorithmFactory;
import org.put.hd.dmarf.data.loaders.IDataLoader;

/**
 * Parses the arguments from the command line. Shows the proper messages upon
 * wrong data etc. Initializes classes responsible for each of the tasks.
 * 
 * @author Piotr
 * 
 */
public class ArgumentParser {

	private String inputFileName;
	private String outputFileName;
	private double minSupport;
	private double minCredibility;
	private short algorithm;
	
	private IDataLoader dataLoader;
	private IAlgorithmFactory algorithmFactory;
	
	public ArgumentParser(IDataLoader dataLoader, IAlgorithmFactory algorithmFactory){
		this.dataLoader = dataLoader;
		this.algorithmFactory = algorithmFactory;
	}
	
	/**
	 * The message that will be shown upon the failure.
	 */
	public final String usageMessage = "Provide arguments in the following manner:"
			+ "\n"
			+ "inputFileName_outputFileName_minSupport_minCredibility_numberOfAlgorithm";
	
	/**
	 * The message that will be shown upon the successful read.
	 */
	public final String confirmationMessage = "Arguments has been parsed succesfully";

	public String setInputArguments(String arguments) {

		if (arguments == null)
			throw new RuntimeException(usageMessage);

		// parse the input
		String[] args = arguments.split("\\s");
		return setInputArguments(args);
	}
	
	public String setInputArguments(String[] args){
		
		// check for the number of arguments
		if( args.length < 5)
			throw new RuntimeException(usageMessage);
		
		// check for nulls
		for (String string : args) {
			if(string == null)
				throw new RuntimeException(usageMessage);
		}
		
		
		// try bind values
		inputFileName = args[0];
		outputFileName = args[1];
		
		int minSupportInt;
		int minCredibityInt;
		
		try {
			minSupportInt = Integer.parseInt(args[2]);
			minCredibityInt = Integer.parseInt(args[3]);
			algorithm = Short.parseShort(args[4]);
		} catch (NumberFormatException e) {
			throw new RuntimeException(usageMessage,e);
		}
		
		minCredibility = minCredibityInt/100.0;
		minSupport = minSupportInt/100.0;
		
		// checking the number of algorithms in the factory
		if(algorithmFactory.getNumberOfAlgorithms() < algorithm)
			throw new RuntimeException(usageMessage);
		
		return confirmationMessage;
	}

	public IDataLoader getDataLoader() {
		
		return null;
	}

	public IAlgorithm getAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

}
