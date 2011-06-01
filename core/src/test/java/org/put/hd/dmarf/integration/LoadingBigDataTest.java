package org.put.hd.dmarf.integration;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.AlgorithmBasedBuilderFactory;
import org.put.hd.dmarf.data.builders.BinaryDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.builders.StringDataBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

/**
 * Loading the big data test. Especially accidents data set and kosarak data
 * ser.
 * 
 * @author Peter Jessa 
 * 
 */
public class LoadingBigDataTest {

	@Mock
	private IAlgorithm algorithmMock;
	private IDataRepresentationBuilder builder;
	private IDataFormatter formatter;
	private IDataLoader dataloader;

	@Before
	public void set_up() {
		MockitoAnnotations.initMocks(this);
	}

	public void setUpLoader() {

		// use all the normal classes
		builder = new AlgorithmBasedBuilderFactory(algorithmMock);
		formatter = new SimpleDataFormatter(builder);
		dataloader = new SimpleDataLoader(formatter);
	}

	@Test
	@Ignore("This test will use too much time and memory")
	public void loading_kosarak() {
		String fileName = "resources/data/kosarak.dat";

		// we want to use all types of loader
		List<IDataRepresentationBuilder> list = new LinkedList<IDataRepresentationBuilder>();
		list.add(new StringDataBuilder());
		list.add(new BinaryDataBuilder());

		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(list);

		setUpLoader();
		this.dataloader.setInputFileName(fileName);
		DataRepresentationBase data = this.dataloader.loadData();
		System.out
				.println("Transactions: " + data.getTransactionsList().size());
		System.out.println("Attributes clusters:"
				+ data.getNumberOfAttributesClusters());
	}

	@Test
	@Ignore("This test will use too much time and memory")
	public void loading_accidenst() {
		String fileName = "resources/data/accidents.dat";

		// we want to use all types of loader
		List<IDataRepresentationBuilder> list = new LinkedList<IDataRepresentationBuilder>();
		list.add(new StringDataBuilder());
		list.add(new BinaryDataBuilder());

		Mockito.when(algorithmMock.getRequiredBuilders()).thenReturn(list);

		setUpLoader();
		this.dataloader.setInputFileName(fileName);
		DataRepresentationBase data = this.dataloader.loadData();
		System.out
				.println("Transactions: " + data.getTransactionsList().size());
		System.out.println("Attributes clusters:"
				+ data.getNumberOfAttributesClusters());
	}
}
