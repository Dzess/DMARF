package org.put.hd.dmarf.profiling;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.put.hd.dmarf.AgainstWekaTestingBase;

/**
 * Time consuming tests checking Weka against the data sets on.
 * 
 * @author Piotr
 * 
 */
public class TimeConsumingTests extends AgainstWekaTestingBase {

	/**
	 * Entry point for starting time consuming tests.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TimeConsumingTests tests = new TimeConsumingTests();

		tests.set_up();
		tests.mushroom_testing();

		// TODO: check for the GC before testing (memory limits for this code is
		// pretty awesome)
		tests.set_up();
		tests.retail_testing();
	}

	@Before
	public void set_up() {
		this.setUp();
	}

	@Test
	public void mushroom_testing() {

		this.minConfidence = 0.9;
		this.minSupport = 0.9;
		
		// path to the resources data
		String fileName = "resources/data/mushroom.dat";

		// check for file existence
		File f = new File(fileName);
		if (!f.exists()) {
			System.err.println(getMessage(fileName));
		}

		runTestingForDataSet(fileName);
	}
	
	@Test
	@Ignore("Too time consuming")
	public void retail_testing() {
		// path to the resources data
		String fileName = "resources/data/retail.dat";

		// check for file existence
		File f = new File(fileName);
		if (!f.exists()) {
			System.err.println(getMessage(fileName));
		}

		runTestingForDataSet(fileName);
	}

	private String getMessage(String fileName) {
        return "No file '" + fileName
                + "' in your resources please be sure too have it";
	}

}
