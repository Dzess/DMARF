package org.put.hd.dmarf.data.builders;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

/**
 * The possible default implementation of {@link IDataReprsentatinoBuilder}
 * class. Uses {@link LinkedHashMap} and {@link LinkedList} collections from
 * {@link java.util}
 * 
 * @author Piotr
 * 
 */
public class BasicDataBuilder implements IDataReprsentatinoBuilder {

	private DataRepresentationBase data;

	private int initialCapacity;
	private float loadFactor;

	private Map<Integer, Integer> attributesCounter;
	private Map<Integer, List<Integer>> transactionsMap;
	private List<List<Integer>> transactionsList;

	private LinkedList<Integer> lastTransaction;
	
	private LinkedList<String> lastTransactionString;
	private LinkedList<List<String>> transactionsString;
	private Map<String,Integer> attributesString;

	public BasicDataBuilder() {

		// hash map settings
		initialCapacity = 100;
		loadFactor = 0.75f;

		attributesCounter = new LinkedHashMap<Integer, Integer>(
				initialCapacity, loadFactor);
		transactionsMap = new LinkedHashMap<Integer, List<Integer>>(
				initialCapacity, loadFactor);
		transactionsList = new LinkedList<List<Integer>>();
		
		// string version of representation (bigger bit ;)
		lastTransactionString = new LinkedList<String>();
		transactionsString = new LinkedList<List<String>>();
		attributesString = new LinkedHashMap<String, Integer>();
	}

	public void addItemInTransaction(Integer itemIdentifier) {

		updateAttributeCounter(itemIdentifier);
		updateTransactionCounters(itemIdentifier);
	}

	private void updateTransactionCounters(Integer itemIdentifier) {

		// update the element on the list
		lastTransaction.add(itemIdentifier);
		lastTransactionString.add(itemIdentifier.toString());
	}

	private void updateAttributeCounter(Integer itemIdentifier) {

		if (!attributesCounter.containsKey(itemIdentifier)) {
			attributesCounter.put(itemIdentifier, 1);
			attributesString.put(itemIdentifier.toString(),1);
		} else {
			Integer element = attributesCounter.get(itemIdentifier);
			element += 1;
			attributesCounter.put(itemIdentifier, element);
			attributesString.put(itemIdentifier.toString(),element);
		}
	}

	public void addTransaction(Integer transactionIdentifier) {

		// code responsible for creating new list within the maps
		// they point to the SAME list for memory efficiency
		lastTransaction = new LinkedList<Integer>();
		lastTransactionString = new LinkedList<String>();
		
		transactionsMap.put(transactionIdentifier, lastTransaction);
		
		
		transactionsList.add(lastTransaction);
		transactionsString.add(lastTransactionString);
	}

	public DataRepresentationBase getDataRepresentation() {

		// produce the data representation
		data = new InjectableDataRepresentation(attributesCounter,
				transactionsList, transactionsMap,transactionsString,attributesString);
		return data;
	}
}
