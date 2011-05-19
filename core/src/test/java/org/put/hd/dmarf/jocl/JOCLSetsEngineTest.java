package org.put.hd.dmarf.jocl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.JOCLSetsEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;
import org.put.hd.dmarf.stopwatches.StopWatch;

public class JOCLSetsEngineTest {

	private IDataLoader dataloader;
	private IDataFormatter formatter;
	private BasicDataBuilder builder;

	private DataRepresentationBase data;
	private JOCLSetsEngine joclEngine;

	private StopWatch sw;

	@Before
	public void setUp() {

		// use all the normal classes
		builder = new BasicDataBuilder();
		formatter = new SimpleDataFormatter(builder);
		dataloader = new SimpleDataLoader(formatter);

		joclEngine = new JOCLSetsEngine();

		sw = new StopWatch();

	}

	@Test
	public void jocl_speedup_test() {

		// perform test
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "mushroom.dat";
		dataloader.setInputFileName(pathToFile);
		data = dataloader.loadData();
		int maxAtt = 10 % (data.getMaxAttAligned());
		Random r = new Random();
		// >>>
		int howMany = 100;
		// >>>
		List<Integer> set = new LinkedList<Integer>();

		for (int l = 0; l <1; l++) {
			set.add(r.nextInt(maxAtt)+1);
		}	
		char[] candidateSet = BasicDataBuilder.generateCharArray(set,
				data.getMaxAttAligned());

		System.out.println("CPU ST Test");
		int numberOfAttClusters = data.getNumberOfAttributesClusters();
		int supportInData = 0;
		sw.start();
		for (int k = 0; k < howMany; k++) {
		
			supportInData = 0;
			char[] transactionsMap = data.getTransactionsCharMap();
			for (int i = 0; i < data.getNumberOfTransactions(); i++) {
				char flag = 0;
				for (int j = 0; j < numberOfAttClusters; j++) {
					flag = (char) (flag | (((candidateSet[j] & transactionsMap[i
							* numberOfAttClusters + j]) ^ candidateSet[j])));
				}
				if (flag == 0) {
					supportInData++;
				}
			}
		}
		sw.stop();
		System.out.println("CPU found supporting transactions: "
				+ supportInData);
		System.out.println("CPU running time: " + sw.getElapsedTime() / 1000.0);

		
		System.out.println("GPU Test");

		sw.start();
		joclEngine.initCL(data);
		sw.stop();
		System.out.println(sw.getElapsedTime() / 1000.0
				+ " <- Time to initialize and upload transactions Matrix.");

		int supp = 0;
		sw.start();
		for (int i = 0; i < howMany; i++) {
			supp = joclEngine.getSupport(candidateSet);
		}
		sw.stop();
		System.out.println(sw.getElapsedTime() / 1000.0
				+ " <- Time to find support " + howMany + " times.");
		System.out.println("Supporting transactions = " + supp);
		joclEngine.cleanupCL();

	}
}
