package org.put.hd.dmarf.data.builders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

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
	private char[] transactionsCharMap;

	private LinkedList<Integer> lastTransaction;

	private LinkedList<String> lastTransactionString;
	private LinkedList<List<String>> transactionsString;
	private Map<String, Integer> attributesString;

	// data set parameters
	private int maxAttIndex;
	private int alignedMaxAttIndex;
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

		transactionsCharMap = generateTransactionsCharMap(transactionsList,
				numberOfAttributesClusters);

		// produce the data representation
		data = new InjectableDataRepresentation(attributesCounter,
				transactionsList, transactionsMap, transactionsString,
				attributesString, transactionsCharMap, maxAttIndex,
				alignedMaxAttIndex, numberOfAttributesClusters,
				numberOfTransactions);
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

		// we need to align attributes to clusters
		// numberOfAttributesClusters = (int) Math.ceil(maxAttIndex / 16.0);

		// and align clusters to nice gpu access
		numberOfAttributesClusters = (int) Math.pow(2, Math.ceil(Math.log(Math
				.ceil(maxAttIndex / 16.0)) / Math.log(2)));

		// numberOfAttributesClusters = (int) Math.ceil(maxAttIndex / 128.0) *
		// 8;

		alignedMaxAttIndex = 16 * numberOfAttributesClusters;

		numberOfTransactions = transactionsList.size();

	}

	/**
	 * Generates the whole CharMap for all transactions
	 * 
	 * @param transactions
	 *            List<List<Integer>> of all transactions. These must not be
	 *            empty.
	 * @param numberOfAttributesClusters
	 *            acquired from DataRepresentationBase. This must be >0
	 * @return the charMap :)
	 */
	public static char[] generateTransactionsCharMap(
			List<List<Integer>> transactionsLists,
			int numberOfAttributesClusters) {

		if (numberOfAttributesClusters == 0 || transactionsLists.size() == 0)
			throw new RuntimeException("Cannot work on empty transactions set.");

		char[] charMap = new char[transactionsLists.size()
				* numberOfAttributesClusters];

		// here comes the TransactionsByteMap population magic
		int transIdx = 0;
		for (List<Integer> transaction : transactionsLists) {
			System.arraycopy(
					generateCharArray(transaction,
							numberOfAttributesClusters * 16), 0, charMap,
					transIdx * numberOfAttributesClusters,
					numberOfAttributesClusters);
			transIdx++;
		}
		return charMap;
	}

	/**
	 * Transforms a list of attributes into CharBitMap representation.
	 * 
	 * @param transaction
	 *            Sorted list of attributes represented by integers.
	 * @param maxAttAligned
	 *            Greatest attribute number aligned to char representation.
	 * @return Char[] array with full length.
	 */
	public static char[] generateCharArray(List<Integer> transaction,
			int maxAttAligned) {

		char[] transactionCharArray = new char[maxAttAligned / 16];

		for (int att : transaction) {
			transactionCharArray[(att - 1) / 16] |= (1 << (((att % 16)) == 0 ? 0
					: 16 - (att % 16)));
		}
		return transactionCharArray;
	}

	public InjectableDataRepresentation getMergedDataRepresentation(
			InjectableDataRepresentation injectableDataRepresentation) {
		// This class supports no merge operation
		throw new RuntimeException("This class supports no merge operation");
	}
}
