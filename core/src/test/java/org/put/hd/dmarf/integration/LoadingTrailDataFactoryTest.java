package org.put.hd.dmarf.integration;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.AlgorithmBasedBuilderFactory;
import org.put.hd.dmarf.data.builders.BinaryDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.builders.IntegerDataBuilder;
import org.put.hd.dmarf.data.builders.StringDataBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

/**
 * Test loading the data using the hierarchy of the
 * {@link IDataRepresentationBuilder} based on the algorithm demands.
 * 
 * @author Piotr
 * 
 */
public class LoadingTrailDataFactoryTest {

	private IDataRepresentationBuilder builder;
	private IDataLoader dataLoader;

	@Mock
	private IAlgorithm algorithmMock;
	private Integer fiveAttribute;
	private Integer fourAttribute;
	private Integer threeAttribute;
	private Integer twoAttribute;
	private Integer oneAttribute;
	private int numberOfTransactions;
	private int numberOfAttributes;

	@Before
	public void set_up() {
		MockitoAnnotations.initMocks(this);

		// expected values of attributes
		oneAttribute = 3;
		twoAttribute = 3;
		threeAttribute = 3;
		fourAttribute = 3;
		fiveAttribute = 2;

		numberOfTransactions = 4;
		numberOfAttributes = 5;

	}

	private IDataLoader setUpDataSource() {
		String pathToFile = "src/test/resources/data/trail.dat";
		IDataFormatter fomratter = new SimpleDataFormatter(this.builder);
		IDataLoader dataloader = new SimpleDataLoader(fomratter);
		dataloader.setInputFileName(pathToFile);
		return dataloader;
	}

	@Test
	public void algorithm_must_be_defined_otherwise_exception() {
		try {
			this.builder = new AlgorithmBasedBuilderFactory(null);
		} catch (Exception e) {
			return;
		}

		Assert.fail("The exception should have been thrown");
	}

	@Test
	public void algorithm_defines_no_representation_throw_an_exception() {

		try {
			this.builder = new AlgorithmBasedBuilderFactory(algorithmMock);
		} catch (Exception e) {
			Mockito.verify(algorithmMock).getRequiredBuilders();
			return;
		}

		Assert.fail("The exception should have been thrown");
	}

	@Test
	public void algorithm_defines_empty_representation_throw_an_exception() {

		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(
				new LinkedList<IDataRepresentationBuilder>());

		try {
			this.builder = new AlgorithmBasedBuilderFactory(algorithmMock);
		} catch (Exception e) {
			Mockito.verify(algorithmMock).getRequiredBuilders();
			return;
		}

		Assert.fail("The exception should have been thrown");
	}

	@Test
	public void algorithm_defines_string_representation() {

		List<IDataRepresentationBuilder> list = new LinkedList<IDataRepresentationBuilder>();
		list.add(new StringDataBuilder());

		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(list);

		this.builder = new AlgorithmBasedBuilderFactory(algorithmMock);

		// prepare the set of class for loadings
		dataLoader = setUpDataSource();

		// get the test working
		DataRepresentationBase result = dataLoader.loadData();

		// assert that created representations is good
		Assert.assertEquals(numberOfTransactions, result.getTransactions()
				.size());

		Assert.assertEquals(oneAttribute, result.getAttributes().get("1"));
		Assert.assertEquals(twoAttribute, result.getAttributes().get("2"));
		Assert.assertEquals(threeAttribute, result.getAttributes().get("3"));
		Assert.assertEquals(fourAttribute, result.getAttributes().get("4"));
		Assert.assertEquals(fiveAttribute, result.getAttributes().get("5"));

		Assert.assertEquals(numberOfAttributes, result.getAttributes().keySet()
				.size());

	}

	@Test
	public void algorithm_defines_integer_representation() {
		List<IDataRepresentationBuilder> list = new LinkedList<IDataRepresentationBuilder>();
		list.add(new IntegerDataBuilder());

		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(list);

		this.builder = new AlgorithmBasedBuilderFactory(algorithmMock);

		// prepare the set of class for loadings
		dataLoader = setUpDataSource();

		// get the test working
		DataRepresentationBase result = dataLoader.loadData();

		// assert that created representations is good
		Assert.assertEquals(numberOfTransactions, result.getTransactionsList()
				.size());

		Assert.assertEquals(oneAttribute, result.getAttributesCounter().get(1));
		Assert.assertEquals(twoAttribute, result.getAttributesCounter().get(2));
		Assert.assertEquals(threeAttribute, result.getAttributesCounter()
				.get(3));
		Assert.assertEquals(fourAttribute, result.getAttributesCounter().get(4));
		Assert.assertEquals(fiveAttribute, result.getAttributesCounter().get(5));

		Assert.assertEquals(numberOfAttributes, result.getAttributesCounter()
				.keySet().size());
	}

	@Test
	public void algorithm_defines_char_map_representation() {
		List<IDataRepresentationBuilder> list = new LinkedList<IDataRepresentationBuilder>();
		list.add(new BinaryDataBuilder());

		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(list);

		this.builder = new AlgorithmBasedBuilderFactory(algorithmMock);

		// prepare the set of class for loadings
		dataLoader = setUpDataSource();

		// get the test working
		DataRepresentationBase result = dataLoader.loadData();

		// expected char map
		// 4 rows with 1 chunk each
		char[] map = new char[4];
		map[0] = 63488; // 11111 rest 0 (16 bit)
		map[1] = 49152; // 11 rest 0 (16 bit)
		map[2] = 12288; // 0011 rest 0 (1b bit)
		map[3] = map[0]; // the same value

		// assert the result
		Assert.assertArrayEquals(map, result.getTransactionsCharMap());
		Assert.assertEquals(16, result.getMaxAttAligned());
		Assert.assertEquals(5, result.getMaxAttIndex());
		Assert.assertEquals(1, result.getNumberOfAttributesClusters());
		Assert.assertEquals(4, result.getNumberOfTransactions());

	}

}
