package org.put.hd.dmarf.data;

import java.util.List;
import java.util.Map;

import org.omg.CORBA.NVList;

/**
 * Injectable representation of {@link DataRepresentationBase}. Simplest
 * possible ever.
 * 
 * @author Piotr
 * 
 */
public class InjectableDataRepresentation extends DataRepresentationBase {

	public InjectableDataRepresentation(Map<Integer, Integer> attributeCounter,
			List<List<Integer>> transactionList,
			Map<Integer, List<Integer>> transactionMap,
			List<List<String>> transactions, Map<String, Integer> attributes,
			char[] transactionsCharMap, int maxAttIndex, int maxAttAligned,
			int numberOfAttributesClusters, int numberOfTransactions) {
		this.AttributesCounter = attributeCounter;
		this.TransactionsList = transactionList;
		this.TransactionsMap = transactionMap;

		this.Transactions = transactions;
		this.Attributes = attributes;
		this.TransactionsCharMap = transactionsCharMap;

		this.MaxAttIndex = maxAttIndex;
		this.MaxAttAligned = maxAttAligned;
		this.NumberOfAttributesClusters = numberOfAttributesClusters;
		this.NumberOfTransactions = numberOfTransactions;
	}

}
