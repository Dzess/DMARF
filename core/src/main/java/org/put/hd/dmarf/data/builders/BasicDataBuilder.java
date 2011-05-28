package org.put.hd.dmarf.data.builders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;
import org.put.hd.dmarf.data.utils.BinaryUtils;

/**
 * The possible default implementation of {@link IDataRepresentationBuilder}
 * class. Uses {@link LinkedHashMap} and {@link LinkedList} collections from
 * {@link java.util}
 * 
 * @author Piotr
 * @deprecated This class is old an inefficient in memory. Use {@link AlgorithmBasedBuilderFactory} to determine
 * what kind of representations are needed depending on {@link IAlgorithm}.
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
	private int bitAlignment;
	private int maxAttIndex;
	private int alignedMaxAttIndex;
	private int numberOfAttributesClusters;
	private int numberOfTransactions;

	/***
	 * Default constructor with simple attributes clusters alignment for CPU
	 */
	public BasicDataBuilder() {
		this(1);
	}

	/***
	 * Creates Data builder with desired attributes clusters alignment.
	 * 
	 * @param bitAlignment
	 *            Suggested: - 0 for clusters alignment to nearest power of 2. -
	 *            1 cluster for CPU computations. - x*4, x>=1, for GPU; best
	 *            memory access x*128.
	 */
	public BasicDataBuilder(int bitAlignment) {
		this.bitAlignment = bitAlignment;

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

		transactionsCharMap = BinaryUtils.generateTransactionsCharMap(
				transactionsList, numberOfAttributesClusters);

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

		// we need to align attributes clusters
		if (bitAlignment == 0) {
			numberOfAttributesClusters = (int) Math.pow(
					2,
					Math.ceil(Math.log(Math.ceil(maxAttIndex / 16.0))
							/ Math.log(2)));
		} else {
			numberOfAttributesClusters = (int) Math.ceil(Math
					.ceil(maxAttIndex / 16.0) / (double) bitAlignment)
					* bitAlignment;
		}

		alignedMaxAttIndex = 16 * numberOfAttributesClusters;

		numberOfTransactions = transactionsList.size();

	}

	public InjectableDataRepresentation getMergedDataRepresentation(
			InjectableDataRepresentation injectableDataRepresentation) {
		// This class supports no merge operation
		throw new RuntimeException("This class supports no merge operation");
	}
}
