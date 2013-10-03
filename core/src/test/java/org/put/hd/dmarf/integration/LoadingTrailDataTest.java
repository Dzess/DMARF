package org.put.hd.dmarf.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

/**
 * Integration level test for loading all data. Acceptance level of test for
 * data loading. Using the default {@link IDataRepresentationBuilder}
 * 
 * @author Piotr
 * 
 */
public class LoadingTrailDataTest {

	private IDataLoader dataLoader;
	private IDataFormatter formatter;
	private IDataRepresentationBuilder builder;

	private DataRepresentationBase result;

	@Before
	public void set_up() {

		// use all the normal classes
		builder = new BasicDataBuilder();
		formatter = new SimpleDataFormatter(builder);
		dataLoader = new SimpleDataLoader(formatter);
	}

	@Test
	public void providing_trial_data_results_in_proper_data_representation() {

		// expected values of attributes
		Integer oneAttribute = 3;
		Integer twoAttribute = 3;
		Integer threeAttribute = 3;
		Integer fourAttribute = 3;
		Integer fiveAttribute = 2;

		int numberOfTransactions = 4;
		int numberOfAttributes = 5;

		// perform test
		String pathToFile = "src/test/resources/data/trail.dat";
		dataLoader.setInputFileName(pathToFile);
		result = dataLoader.loadData();

		// verify the result of the attributes
		Assert.assertEquals(oneAttribute, result.getAttributesCounter().get(1));
		Assert.assertEquals(twoAttribute, result.getAttributesCounter().get(2));
		Assert.assertEquals(threeAttribute, result.getAttributesCounter()
				.get(3));
		Assert.assertEquals(fourAttribute, result.getAttributesCounter().get(4));
		Assert.assertEquals(fiveAttribute, result.getAttributesCounter().get(5));

		// verify that the number of transaction is correct
		Assert.assertEquals(numberOfTransactions, result.getTransactionsList()
				.size());
		Assert.assertEquals(numberOfAttributes, result.getAttributesCounter()
				.keySet().size());

		// verify the structure of the results presented ?? because

	}

}
