package org.put.hd.dmarf.algorithms;

import java.util.List;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Describes what an algorithm should do.
 * @author Piotr
 *
 */
public interface IAlgorithm {
	/**
	 * Gets the time of the all algorithm operations without  the I/O operations.
	 * @return time of algorithm operations in seconds.
	 */
	public long getElapsedTimeOverall();
	/**
	 * Gets the time of the algorithm operation concerning frequent sets (?) generation. 
	 * @return time of algorithm operations in seconds.
	 */
	public long getElapsedTimeGeneration();
	
	/**
	 * Starts the algorithm using the passed data.
	 * @param data
	 */
	public void start(DataRepresentationBase data);
	
	/**
	 * Gets the result for the this algorithm
	 * @return the list of {@link Rule} inferred from data.
	 */
	public List<Rule> getRules();
}
