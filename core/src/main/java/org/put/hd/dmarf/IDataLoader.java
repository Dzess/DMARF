package org.put.hd.dmarf;

/**
 * Describes the behavior of data loading class.
 * @author Piotr Jessa
 */
public interface IDataLoader {

	/**
	 * Describes the behavior of single implementation data loader class.
	 * @param name
	 */
	public void setInputFileName(String name);

	public void loadData();

}
