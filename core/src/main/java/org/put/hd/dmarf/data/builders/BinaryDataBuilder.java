package org.put.hd.dmarf.data.builders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;
import org.put.hd.dmarf.data.utils.BinaryUtils;

/**
 * Builds the binary data representation. This building of binary representation
 * could be a bit faster. It also requires the integer representation done
 * inside.
 * 
 * @author Piotr
 * 
 */
public class BinaryDataBuilder implements IDataRepresentationBuilder {

	private IntegerDataBuilder integerDataBuilder;

	private int maxAttIndex;
	private int bitAlignment;

	private int alignedMaxAttIndex;
	private int numberOfAttributesClusters;
	private int numberOfTransactions;

	private Map<Integer, Integer> attributesCounter;

	private List<List<Integer>> transactionsList;

	/***
	 * Creates Data builder with desired attributes clusters alignment.
	 * 
	 * Creating this class will also create {@link IntegerDataBuilder} which is
	 * required inside.
	 * 
	 * @param bitAlignment
	 *            Suggested: - 0 for clusters alignment to nearest power of 2. -
	 *            1 cluster for CPU computations. - x*4, x>=1, for GPU; best
	 *            memory access x*128.
	 */
	public BinaryDataBuilder(int bitAlignemt) {

		this.integerDataBuilder = new IntegerDataBuilder();
		this.bitAlignment = bitAlignemt;
	}

	/***
	 * Default constructor with simple attributes clusters alignment for CPU.
	 * Creating this class will also create {@link IntegerDataBuilder} which is
	 * required inside.
	 */
	public BinaryDataBuilder() {
		this(1);
	}

	public void addItemInTransaction(Integer itemIdentifier) {
		// pass the values to the integer engine
		this.integerDataBuilder.addItemInTransaction(itemIdentifier);
	}

	public void addTransaction(Integer transactionIdentifier) {
		// pass the values to the integer engine
		this.integerDataBuilder.addTransaction(transactionIdentifier);
	}

	public DataRepresentationBase getDataRepresentation() {
		return injectValues(new InjectableDataRepresentation());
	}

	public InjectableDataRepresentation getMergedDataRepresentation(
			InjectableDataRepresentation data) {

		return injectValues(data);
	}

	private InjectableDataRepresentation injectValues(
			InjectableDataRepresentation data) {

		// get the integer engine elements
		DataRepresentationBase integerRepresentation = this.integerDataBuilder
				.getDataRepresentation();

		this.attributesCounter = integerRepresentation.getAttributesCounter();
		this.transactionsList = integerRepresentation.getTransactionsList();

		// get the parameters from this data
		generateDataSetParameters();

		// get the binary representation
		char[] transactionsCharMap = BinaryUtils.generateTransactionsCharMap(
				this.transactionsList, numberOfAttributesClusters);

		// inject into data
		data.setMaxAttAligned(this.alignedMaxAttIndex);
		data.setMaxAttIndex(this.maxAttIndex);
		data.setNumberOfAttributesClusters(numberOfAttributesClusters);
		data.setNumberOfTransactions(numberOfTransactions);
		data.setTransactionsCharMap(transactionsCharMap);

		// injecting the data from the integer set
		data.setAttributesCounter(attributesCounter);
		data.setTransactionsList(transactionsList);

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

		this.maxAttIndex = attsVector[attsVector.length - 1];

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

		this.alignedMaxAttIndex = 16 * numberOfAttributesClusters;

		this.numberOfTransactions = transactionsList.size();

	}

}
