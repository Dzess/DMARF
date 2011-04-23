package org.put.hd.dmarf.data;

import java.util.List;
import java.util.Map;

/**
 * Class represents the data read from input sources. The format of data is
 * table containing transactions (rows) and attributes (columns). Not all
 * attributes must have value, thus table will have some nulls.
 * 
 * This is only the interface for the data representation using other interfaces
 * like {@link Map} or {@link List}.
 * 
 * @author Piotr Jessa
 */
public abstract class DataRepresentationBase {

	/**
	 * Maps the attributes of the data with the cardinality of this attributes.
	 * In the DataMining is is equal to Support(attribute)
	 */
	private Map<Integer, Integer> AttributesCounter;

	/**
	 * Holds the each transaction mapped by id with list of products
	 * (attributes)
	 */
	private Map<Integer, List<Integer>> TransactionsMap;

	/**
	 * Simple list of all transactions.
	 */
	private List<List<Integer>> TransactionsList;

	public Map<Integer, Integer> getAttributesCounter() {
		return AttributesCounter;
	}

	public Map<Integer, List<Integer>> getTransactions() {
		return TransactionsMap;
	}

	public List<List<Integer>> getTransactionsList() {
		return TransactionsList;
	}
}
