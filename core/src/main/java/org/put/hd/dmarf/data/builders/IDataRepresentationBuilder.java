package org.put.hd.dmarf.data.builders;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

/**
 * Implementing building means that this thing knows how to produce
 * {@link DataRepresentationBase} implementation relaying only on this methods.
 * 
 * @author Piotr
 * 
 */
public interface IDataRepresentationBuilder {
	/**
	 * Invoke this method when you want builder to produce representation
	 * containing one more item.
	 * 
	 * @param itemIdentifier
	 *            - single item to be pushed to the last transaction.
	 */
	public void addItemInTransaction(Integer itemIdentifier);

	/**
	 * Invoke this method when you want builder to produce representation
	 * containing one more transaction.
	 * 
	 * @param transactionIdentifier
	 *            - move to the next transaction.
	 */
	public void addTransaction(Integer transactionIdentifier);

	/**
	 * Final get statement for getting the data out of this class.
	 * 
	 * @return returns the data representation used from building.
	 */
	public DataRepresentationBase getDataRepresentation();

	/**
	 * Merges the data representation created by the implementation of the
	 * {@link IDataRepresentationBuilder} with the passed
	 * {@link InjectableDataRepresentation} which is used for merging pattern.
	 * 
	 * @param injectableDataRepresentation
	 *            : the data representation that data will be merged into
	 * @return the newly merged data representation.
	 */
	public InjectableDataRepresentation getMergedDataRepresentation(
			InjectableDataRepresentation injectableDataRepresentation);
}
