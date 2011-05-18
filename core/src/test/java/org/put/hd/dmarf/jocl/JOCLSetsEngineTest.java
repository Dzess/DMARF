package org.put.hd.dmarf.jocl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.JOCLSetsEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

public class JOCLSetsEngineTest {

	private IDataLoader dataloader;
	private IDataFormatter formatter;
	private IDataRepresentationBuilder builder;

	private DataRepresentationBase data;
	private JOCLSetsEngine joclEngine;

	@Before
	public void setUp() {

		// use all the normal classes
		builder = new BasicDataBuilder();
		formatter = new SimpleDataFormatter(builder);
		dataloader = new SimpleDataLoader(formatter);

		joclEngine = new JOCLSetsEngine();

	}

	@Test
	public void upload_real_set() {

		// perform test
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "alignedSet.data";
		dataloader.setInputFileName(pathToFile);
		data = dataloader.loadData();

		joclEngine.initCL(data);

		/*
		 * for (int i = 0; i < 10000; i++) { joclEngine.runCL(); }
		 */

		char[] candidateSet = { 32768, 0, 0, 0 };
		int supp = joclEngine.getSupport(candidateSet);

		System.out.println(supp);
		joclEngine.cleanupCL();
		// joclEngine.verifyOutputCL();

	}
}
