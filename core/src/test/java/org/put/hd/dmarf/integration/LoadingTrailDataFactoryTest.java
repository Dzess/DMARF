package org.put.hd.dmarf.integration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.data.AlgorithmBasedBuilderFactory;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
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
	private IDataLoader dataloader;

	@Mock
	private IAlgorithm algorithmMock;

	@Before
	public void set_up() {
		MockitoAnnotations.initMocks(this);

	}

	private IDataLoader setUpDataSource() {
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "trail.dat";
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
	public void algorithm_defines_integer_and_string_representation() {
		List<IDataRepresentationBuilder> list = new LinkedList<IDataRepresentationBuilder>();
		list.add(new StringDataBuilder());
		
		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(list);

		this.builder = new AlgorithmBasedBuilderFactory(algorithmMock);

		// prepare the set of class for loadings
		dataloader = setUpDataSource();

		// get the test working
		DataRepresentationBase result = dataloader.loadData();

		// assert that created representations is good
		// TODO: write the assertions
		Assert.fail("Not yet implemented");
	}

	@Test
	public void algorithm_defines_char_map_representation() {
		// TODO: write this test
		Assert.fail("Not yet implemented");
	}

	@Test
	public void algorithm_defines_the_all_representations() {
		// TODO: write this test
		Assert.fail("Not yet implemented");
	}
}
