package org.put.hd.dmarf.data.builders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

import weka.core.Attribute;
import weka.core.FastVector;

/**
 * The possible default implementation of {@link IDataRepresentationBuilder}
 * class. Uses {@link LinkedHashMap} and {@link LinkedList} collections from
 * {@link java.util}
 * 
 * @author Piotr
 * 
 */
public class BasicDataBuilder implements IDataRepresentationBuilder {

	private DataRepresentationBase data;

	private int initialCapacity;
	private float loadFactor;

	private Map<Integer, Integer> attributesCounter;
	private Map<Integer, List<Integer>> transactionsMap;
	private List<List<Integer>> transactionsList;
	private Byte[][] transactionsByteMap;

	private LinkedList<Integer> lastTransaction;

	private LinkedList<String> lastTransactionString;
	private LinkedList<List<String>> transactionsString;
	private Map<String, Integer> attributesString;

	// data set parameters
	private int maxAttIndex;
	private int maxAttAligned;
	private int numberOfAttributesClusters;
	private int numberOfTransactions;

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
			attributesString.put(itemIdentifier.toString(), 1);
		} else {
			Integer element = attributesCounter.get(itemIdentifier);
			element += 1;
			attributesCounter.put(itemIdentifier, element);
			attributesString.put(itemIdentifier.toString(), element);
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

		generateDataSetParameters();

		generateTransactionsByteMap();

		// produce the data representation
		data = new InjectableDataRepresentation(attributesCounter,
				transactionsList, transactionsMap, transactionsString,
				attributesString, transactionsByteMap, maxAttIndex,
				maxAttAligned, numberOfAttributesClusters, numberOfTransactions);
		return data;
	}

	/**
	 * Generates greatest attribute index, it's corresponding aligned index,
	 * number of attributes byte clusters, number of transactions in whole data
	 * set.
	 */
	private void generateDataSetParameters() {

		// finding the biggest attribute number
		// pretty damn odd way
		int[] attsVector = new int[attributesCounter.keySet().size()];
		Iterator<Integer> it = attributesCounter.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			attsVector[i] = it.next().intValue();
			i++;
		}

		// elements in the vector must be sorted !
		Arrays.sort(attsVector);

		if (attsVector.length == 0) {
			throw new RuntimeException("No attributes found in data set.");
		}

		maxAttIndex = attsVector[attsVector.length - 1];
		maxAttAligned = 8 * (int) (Math.ceil(maxAttIndex / 8.0));
		numberOfAttributesClusters = (int) Math.ceil(maxAttIndex / 8.0);
		numberOfTransactions = transactionsList.size();

	}

	private void generateTransactionsByteMap() {

		if (numberOfAttributesClusters == 0 || numberOfTransactions == 0)
			throw new RuntimeException("Cannot work on empty transactions set.");

		transactionsByteMap = new Byte[numberOfTransactions][numberOfAttributesClusters];

		// here comes the TransactionsByteMap population magic
		int transIdx = 0;
		for (List<Integer> transaction : transactionsList) {
			// does it have to be sorted?
			Collections.sort(transaction);
			transactionsByteMap[transIdx] = generateByteArray(transaction);
			transIdx++;
		}
	}

	/**
	 * Transforms a list of attributes into ByteBitMap representation.
	 * 
	 * @param transaction
	 *            Sorted list of attributes represented by integers.
	 * @return Byte[] array with full length.
	 */
	public Byte[] generateByteArray(List<Integer> transaction) {

		int[] transactionBitArray = new int[maxAttAligned];
		Byte[] transactionByteArray = new Byte[numberOfAttributesClusters];
		// add elements to instance from the transaction on the proper place
		for (int j = 0; j < transactionBitArray.length; j++) {

			// populating with bits
			if (transaction.contains(j + 1)) {
				transactionBitArray[j] = 1;
			} else {
				transactionBitArray[j] = 0;
			}

			// populating ByteMap
			if ((j + 1) % 8 == 0) { // we've got a cluster to save!
				int clusterValue = 0;
				for (int k = 0; k < 8; k++) {
					clusterValue += Math.pow(2, k)
							* transactionBitArray[j - 7 + k];
				}
				transactionByteArray[(j + 1) / 8 - 1] = (byte) clusterValue;
			}
		}
		return transactionByteArray;
	}
}
