package org.put.hd.dmarf.data.builders;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

/**
 * Injects the data representation with integers version of representation,
 * build using the formatter provided methods.
 * 
 * @author Piotr
 * 
 */
public class IntegerDataBuilder implements IDataRepresentationBuilder {

	private LinkedList<Integer> lastTransaction;

	private Map<Integer, Integer> attributesCounter;
	private Map<Integer, List<Integer>> transactionsMap;
	private List<List<Integer>> transactionsList;

	private int initialCapacity;
	private float loadFactor;

	public IntegerDataBuilder() {

		// hash map settings
		initialCapacity = 100;
		loadFactor = 0.75f;

		this.lastTransaction = new LinkedList<Integer>();
		this.attributesCounter = new LinkedHashMap<Integer, Integer>(
				initialCapacity, loadFactor);
		this.transactionsMap = new LinkedHashMap<Integer, List<Integer>>(
				initialCapacity, loadFactor);
		this.transactionsList = new LinkedList<List<Integer>>();
	}

	public void addItemInTransaction(Integer itemIdentifier) {

		// attributes
		if (!attributesCounter.containsKey(itemIdentifier)) {
			attributesCounter.put(itemIdentifier, 1);
		} else {
			Integer element = attributesCounter.get(itemIdentifier);
			element += 1;
			attributesCounter.put(itemIdentifier, element);
		}

		// transactions
		lastTransaction.add(itemIdentifier);
	}

	public void addTransaction(Integer transactionIdentifier) {

		lastTransaction = new LinkedList<Integer>();

		transactionsMap.put(transactionIdentifier, lastTransaction);
		transactionsList.add(lastTransaction);
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

	private void injectValues(
			InjectableDataRepresentation injectableDataRepresentation) {
		injectableDataRepresentation.setAttributesCounter(attributesCounter);
		injectableDataRepresentation.setTransactionsList(transactionsList);
		injectableDataRepresentation.setTransactionsMap(transactionsMap);
	}

}
