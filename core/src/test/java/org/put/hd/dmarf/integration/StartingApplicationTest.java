package org.put.hd.dmarf.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.ArgumentParser;

/**
 * Unit level testing of starting application and string parsing
 * 
 * @author Piotr
 * 
 */
public class StartingApplicationTest {

	private ArgumentParser parser;
	private String inputFileName;
	private String outputFileName;
	private String minSupport;
	private String minCredibility;
	private String algorithm;

	@Before
	public void set_up() {
		parser = new ArgumentParser();
		inputFileName = "inputFile";
		outputFileName = "outputFile";
		minSupport = " 30";
		minCredibility = "60";
		algorithm = "1";
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
		String result = parser.setInputArguments(new String[] { inputFileName,
				outputFileName, minSupport, minCredibility, algorithm });
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
}
