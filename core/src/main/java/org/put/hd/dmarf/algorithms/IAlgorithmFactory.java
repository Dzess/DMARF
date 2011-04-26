package org.put.hd.dmarf.algorithms;


/**
 * Describes what a possible factory of algorithms should support.
 * @author Piotr
 *
 */
public interface IAlgorithmFactory {

	/**
	 * Returns the number of possible algorithms in this factory.
	 * @return the number of possible algorithms in this factory.
	 */
	public int getNumberOfAlgorithms();
	
	/**
	 * Gets the selected algorithm.
	 * @return
	 */
	public IAlgorithm getAlgorithm(int number);

}
