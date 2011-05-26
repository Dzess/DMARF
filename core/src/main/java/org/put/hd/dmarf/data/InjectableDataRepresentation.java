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

	/**
	 * Immutable constructor used for the creating the data with all 
	 * fields already filled. 
	 */
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
	
	/**
	 * Default constructor used for creating the empty representation 
	 * with nulls on every field. Except the ones set with setters.
	 */
	public InjectableDataRepresentation(){
		
	}

	//
	// Setters part of the code
	//
	public void setAttributesCounter(Map<Integer, Integer> attributesCounter) {
		AttributesCounter = attributesCounter;
	}

	public void setTransactionsMap(Map<Integer, List<Integer>> transactionsMap) {
		TransactionsMap = transactionsMap;
	}

	public void setTransactionsList(List<List<Integer>> transactionsList) {
		TransactionsList = transactionsList;
	}

	public void setTransactions(List<List<String>> transactions) {
		Transactions = transactions;
	}

	public void setAttributes(Map<String, Integer> attributes) {
		Attributes = attributes;
	}

	public void setTransactionsCharMap(char[] transactionsCharMap) {
		TransactionsCharMap = transactionsCharMap;
	}

	public void setMaxAttIndex(int maxAttIndex) {
		MaxAttIndex = maxAttIndex;
	}

	public void setMaxAttAligned(int maxAttAligned) {
		MaxAttAligned = maxAttAligned;
	}

	public void setNumberOfAttributesClusters(int numberOfAttributesClusters) {
		NumberOfAttributesClusters = numberOfAttributesClusters;
	}

	public void setNumberOfTransactions(int numberOfTransactions) {
		NumberOfTransactions = numberOfTransactions;
	}

}
