package org.put.hd.dmarf.jocl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.JOCLSetsEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.formatters.IDataFormatter;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.IDataLoader;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;
import org.put.hd.dmarf.data.utils.BinaryUtils;
import org.put.hd.dmarf.stopwatches.StopWatch;

public class JOCLSetsEngineTest {

	private IDataLoader dataLoader;
	private IDataFormatter formatter;
	private BasicDataBuilder builder;

	private DataRepresentationBase data;
	private JOCLSetsEngine joclEngine;

	private StopWatch sw;

	@Before
	public void setUp() {

		// use all the normal classes
		builder = new BasicDataBuilder(4);
		formatter = new SimpleDataFormatter(builder);
		dataLoader = new SimpleDataLoader(formatter);

		sw = new StopWatch();

	}

	/**
	 * Test against alignedSet.dat
	 */
	@Test
	public void jocl_support_test() {

		// perform test
		String filename = "alignedSet.data";
		String pathToFile = "src/test/resources/data/" + filename;
		dataLoader.setInputFileName(pathToFile);
		System.out.println("Loading data set: " + filename);
		sw.start();
		data = dataLoader.loadData();
		sw.stop();

		System.out.println("Loading took [s]: " + sw.getElapsedTimeSecs());
		System.out.println("Found clusters: "
				+ data.getNumberOfAttributesClusters());
		System.out.println("Found transactions: "
				+ data.getNumberOfTransactions());

		try {
			joclEngine = new JOCLSetsEngine();
			joclEngine.initEngine(data);
			System.out.println("Created OpenCL on: "+joclEngine.getSelectedDeviceType());
		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		char[] candidateSet;
		int gpuSupport;
		List<Integer> set = new LinkedList<Integer>();

		// testing against 1
		set.add(1);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 3, was: " + gpuSupport, gpuSupport == 3);

		// against 1,16
		set.add(16);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 3, was: " + gpuSupport, gpuSupport == 3);

		// against 1,16,32
		set.add(32);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 1, was: " + gpuSupport, gpuSupport == 1);

		// against 1,8,16,32
		set.add(8);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 1, was: " + gpuSupport, gpuSupport == 1);

		// against 1,2,8,16,32
		set.add(2);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 1, was: " + gpuSupport, gpuSupport == 1);

		// against 1,2,8,16,32,48
		set.add(48);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 0, was: " + gpuSupport, gpuSupport == 0);

		// against 2
		set.clear();
		set.add(2);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 2, was: " + gpuSupport, gpuSupport == 2);

		// against 2,8
		set.add(8);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 2, was: " + gpuSupport, gpuSupport == 2);

		// against 2,4,8
		set.add(4);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 1, was: " + gpuSupport, gpuSupport == 1);

		// against 48
		set.clear();
		set.add(48);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 1, was: " + gpuSupport, gpuSupport == 1);

		// against 1,48
		set.add(1);
		candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());
		gpuSupport = joclEngine.getSupport(candidateSet);
		Assert.assertTrue("Should be 0, was: " + gpuSupport, gpuSupport == 0);

		joclEngine.cleanupEngine();
	}

	@Test
	public void jocl_speedup_test() {

		// perform test
		String filename = "mushroom.dat";
		String pathToFile = "src/test/resources/data/" + filename;
		dataLoader.setInputFileName(pathToFile);
		System.out.println("Loading data set: " + filename);
		sw.start();
		data = dataLoader.loadData();
		sw.stop();

		System.out.println("Loading took [s]: " + sw.getElapsedTimeSecs());
		System.out.println("Found clusters: "
				+ data.getNumberOfAttributesClusters());
		System.out.println("Found transactions: "
				+ data.getNumberOfTransactions());
		int maxAtt = 10 % (data.getMaxAttAligned());
		Random r = new Random();
		// >>>
		int howMany = 100;
		System.out.println("Test will be searching support " + howMany
				+ " times.");
		// >>>
		List<Integer> set = new LinkedList<Integer>();

		for (int l = 0; l < 1; l++) {
			set.add(r.nextInt(maxAtt) + 1);
		}
		char[] candidateSet = BinaryUtils.generateCharArray(set,
				data.getMaxAttAligned());

		System.out.println("OpenCL Test");

		sw.start();
		try {
			joclEngine = new JOCLSetsEngine();
			joclEngine.initEngine(data);
			System.out.println("OpenCL will run on: "+joclEngine.getSelectedDeviceType());
		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sw.stop();
		System.out.println(sw.getElapsedTimeSecs()
				+ " <- Time to initialize and upload transactions Matrix.");

		int gpuSupport = 0;
		sw.start();
		for (int i = 0; i < howMany; i++) {
			gpuSupport = joclEngine.getSupport(candidateSet);
		}
		sw.stop();
		System.out.println(sw.getElapsedTimeSecs()
				+ " <- OpenCL time to find support.");
		System.out.println(gpuSupport + " <- OpenCL supporting transactions.");

		joclEngine.cleanupEngine();

		System.out.println("CPU ST Test");
		int numberOfAttClusters = data.getNumberOfAttributesClusters();
		int cpuSupport = 0;
		sw.start();
		for (int k = 0; k < howMany; k++) {

			cpuSupport = 0;
			char[] transactionsMap = data.getTransactionsCharMap();
			for (int i = 0; i < data.getNumberOfTransactions(); i++) {
				char flag = 0;
				for (int j = 0; j < numberOfAttClusters; j++) {
					flag = (char) (flag | (((candidateSet[j] & transactionsMap[i
							* numberOfAttClusters + j]) ^ candidateSet[j])));
				}
				if (flag == 0) {
					cpuSupport++;
				}
			}
		}
		sw.stop();
		System.out.println(sw.getElapsedTimeSecs() + " <- CPU running time.");
		System.out.println(cpuSupport
				+ " <- CPU found supporting transactions.");

		Assert.assertTrue(cpuSupport == gpuSupport);
	}
}
