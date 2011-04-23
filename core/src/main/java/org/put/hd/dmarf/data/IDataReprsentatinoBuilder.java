package org.put.hd.dmarf.data;

/**
 * Implementing building means that this thing knows how to produce
 * {@link DataRepresentationBase} implementation relaying only on this methods.
 * @author Piotr
 *
 */
public interface IDataReprsentatinoBuilder {
	/**
	 * Invoke this method when you want builder to produce representation containing 
	 * one more item.
	 * @param itemIdentifier - single item to be pushed to the last transaction.
	 */
	public void addItemInTransaction(Integer itemIdentifier);
	
	/**
	 * Invoke this method when you want builder to produce representation containing one 
	 * more transaction.
	 * @param transactionIdentifier - move to the next transaction.
	 */
	public void addTransaction(Integer transactionIdentifier);
	
	/**
	 * Final get statement for getting the data out of this class.
	 * @return returns the data representation used from building.
	 */
	public DataRepresentationBase getDataRepresentation();
}
