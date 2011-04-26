package org.put.hd.dmarf;

import java.util.StringTokenizer;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.data.loaders.IDataLoader;

/**
 * Parses the arguments from the command line. Shows the proper messages upon
 * wrong data etc. Initializes classes responsible for each of the tasks.
 * 
 * @author Piotr
 * 
 */
public class ArgumentParser {

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
		return confirmationMessage;
	}

	public IDataLoader getDataLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAlgorithm getAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

}
