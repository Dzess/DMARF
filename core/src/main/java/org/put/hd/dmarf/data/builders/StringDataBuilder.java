package org.put.hd.dmarf.data.builders;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

/**
 * The {@link IDataRepresentationBuilder} which is responsible for creating the
 * string representation of data set. Both {@link Map} and {@link List}
 * representation of transactions & attributes.
 * 
 * @author Piotr
 * 
 */
public class StringDataBuilder implements IDataRepresentationBuilder {

	private LinkedHashMap<String, Integer> attributesString;
	private LinkedList<List<String>> transactionsString;
	private LinkedList<String> lastTransactionString;
	
	private int initialCapacity;
	private float loadFactor;

	public StringDataBuilder() {

		// hash map settings
		initialCapacity = 100;
		loadFactor = 0.75f;

		this.lastTransactionString = new LinkedList<String>();
		this.transactionsString = new LinkedList<List<String>>();
		this.attributesString = new LinkedHashMap<String, Integer>(initialCapacity,loadFactor);
	}

	public void addItemInTransaction(Integer item) {

		String itemIdentifier = item.toString().intern();
		// attributes
		if (!attributesString.containsKey(itemIdentifier)) {
			attributesString.put(itemIdentifier, 1);
		} else {
			Integer element = attributesString.get(itemIdentifier);
			element += 1;
			attributesString.put(itemIdentifier, element);
		}

		// transactions
		lastTransactionString.add(itemIdentifier);

	}

	public void addTransaction(Integer transactionIdentifier) {
		lastTransactionString = new LinkedList<String>();

		transactionsString.add(lastTransactionString);
	}

	public DataRepresentationBase getDataRepresentation() {
		InjectableDataRepresentation result = new InjectableDataRepresentation();
		injectValues(result);

		return result;
	}

	public InjectableDataRepresentation getMergedDataRepresentation(
			InjectableDataRepresentation injectableDataRepresentation) {
		injectValues(injectableDataRepresentation);

		return injectableDataRepresentation;
	}

	private void injectValues(InjectableDataRepresentation result) {
		result.setAttributes(attributesString);
		result.setTransactions(transactionsString);
	}

}
