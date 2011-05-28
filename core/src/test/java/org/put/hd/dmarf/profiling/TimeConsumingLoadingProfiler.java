package org.put.hd.dmarf.profiling;

import java.io.File;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;
import org.put.hd.dmarf.data.loaders.SimpleDataLoader;

/**
 * Tests used only for profiling purposes. Snatches of code to get the loading
 * done. Those are not JUnit test because we do not want them to be called from
 * maven like build systems.
 * 
 * @author Piotr
 * 
 */
public class TimeConsumingLoadingProfiler {

	private SimpleDataLoader dataloader;
	private SimpleDataFormatter formatter;
	private BasicDataBuilder builder;

	public void setUp() {

		// use all the normal classes
		builder = new BasicDataBuilder();
		formatter = new SimpleDataFormatter(builder);
		dataloader = new SimpleDataLoader(formatter);

	}

	public void time_consuming_loading() {
		String pathToFile = "resources" + File.separator + "data"
				+ File.separator + "pumsb_star.dat";
		dataloader.setInputFileName(pathToFile);
		DataRepresentationBase data = dataloader.loadData();

		// get nothing with this data
		int size = data.getTransactions().size();

		System.out.println("Data loaded:" + size);
	}

	public static void main(String[] args) {
		TimeConsumingLoadingProfiler prof = new TimeConsumingLoadingProfiler();

		prof.setUp();
		prof.time_consuming_loading();
	}
}
