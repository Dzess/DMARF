package org.put.hd.dmarf.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.put.hd.dmarf.ArgumentParser;
import org.put.hd.dmarf.algorithms.IAlgorithmFactory;
import org.put.hd.dmarf.data.loaders.IDataLoader;

/**
 * Unit level testing of starting application and string parsing.
 * 
 * @author Piotr
 * 
 */
public class ArgumentParserInputArgumentsTest {

	private ArgumentParser parser;
	private String inputFileName;
	private String outputFileName;
	private String minSupport;
	private String minCredibility;
	private String algorithm;

	@Mock private IDataLoader mockDataLoader;
	@Mock private IAlgorithmFactory mockAlgorithmFactory;
	private String[] list;
	
	@Before
	public void set_up() {
		
		MockitoAnnotations.initMocks(this);
		
		parser = new ArgumentParser(mockDataLoader,mockAlgorithmFactory);
		inputFileName = "inputFile";
		outputFileName = "outputFile";
		minSupport = "30";
		minCredibility = "60";
		algorithm = "1";
		
		Mockito.when(mockAlgorithmFactory.getNumberOfAlgorithms()).thenReturn(3);
	}

	@Test
	public void providing_empty_string_resutls_in_exceptino() {
		String arguments = null;

		try {
			parser.setInputArguments(arguments);
		} catch (Exception e) {
			Assert.assertEquals(parser.usageMessage, e.getMessage());
			return;
		}

		Assert.fail("The exception should be thrown");
	}

	@Test
	public void providing_non_good_list_results_in_exception() {
		String arguments = "wrong arguments";

		try {
			parser.setInputArguments(arguments);
		} catch (Exception e) {
			Assert.assertEquals(parser.usageMessage, e.getMessage());
			return;
		}

		Assert.fail("The exception should be thrown");
	}

	@Test
	public void providing_good_list_works_wtih_confirmation_message() {

		// wait for confirmation message
		list = new String[] { inputFileName,
				outputFileName, minSupport, minCredibility, algorithm };
		
		String result = parser.setInputArguments(list);
		Assert.assertEquals(parser.confirmationMessage, result);
	}

	@Test
	public void providing_good_list_works_with_confirmation_message() {

		String arguments = inputFileName + " " + outputFileName + " "
				+ minSupport + " " + minCredibility + " " + algorithm;

		// well this should work as assert.doesNotThrow
		String result = parser.setInputArguments(arguments);
		Assert.assertEquals(parser.confirmationMessage, result);

	}
	
	@Test
	public void providing_no_good_algorithm_results_in_exception(){
		// lets put something like algorithm number 5 
		algorithm = "5";

		// when we only have 3
		Mockito.when(mockAlgorithmFactory.getNumberOfAlgorithms()).thenReturn(3);

		// test
		list = new String[] { inputFileName,
				outputFileName, minSupport, minCredibility, algorithm };
		
		try {
			parser.setInputArguments(list);
		} catch (Exception e) {
			Assert.assertEquals(parser.usageMessage, e.getMessage());
			return;
		}
		Assert.fail("The exception should be thrown");
	}
}
