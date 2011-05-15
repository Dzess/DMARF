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
 * <i>
 * Not all the get like methods must have values or be implemented in a proper way. This depends on
 * the representation under this class.
 * </i>
 * 
 * @author Piotr
 */
public abstract class DataRepresentationBase {

	/**
	 * Maps the attributes of the data with the cardinality of this attributes.
	 * In the DataMining is is equal to Support(attribute)
	 */
	protected Map<Integer, Integer> AttributesCounter;

	/**
	 * Holds the each transaction mapped by id with list of products
	 * (attributes)
	 */
	protected Map<Integer, List<Integer>> TransactionsMap;

	/**
	 * Simple list of all transactions.
	 */
	protected List<List<Integer>> TransactionsList;

	/**
	 * The simple string like transaction representation
	 */
	protected List<List<String>> Transactions;

	/**
	 * The simple string like attribute representation
	 */
	protected Map<String, Integer> Attributes;

	public Map<String, Integer> getAttributes() {
		return Attributes;
	}
	
	public List<List<String>> getTransactions(){
		return Transactions;
	}

	public Map<Integer, Integer> getAttributesCounter() {
		return AttributesCounter;
	}

	public Map<Integer, List<Integer>> getTransactionsMap() {
		return TransactionsMap;
	}

	public List<List<Integer>> getTransactionsList() {
		return TransactionsList;
	}
}
