package org.put.hd.dmarf.unit.apriori.binary.setsEngine;

import java.io.Reader;
import java.io.StringReader;

import org.put.hd.dmarf.algorithms.apriori.binary.BinarySetsEngine;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.BasicDataBuilder;
import org.put.hd.dmarf.data.formatters.SimpleDataFormatter;

public class BinarySetsEngineTestBase {

	protected BinarySetsEngine engine;

	public BinarySetsEngineTestBase() {
		super();
	}

	protected DataRepresentationBase getDataFromString(String dataString) {
		Reader stringReader = new StringReader(dataString);
		SimpleDataFormatter formatter = new SimpleDataFormatter(
				new BasicDataBuilder());
        return formatter.getFormattedData(stringReader);
	}

}