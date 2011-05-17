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
	public double getElapsedTimeOverall();
	/**
	 * Gets the time of the algorithm operation concerning frequent sets (?) generation. 
	 * @return time of algorithm operations in seconds.
	 */
	public double getElapsedTimeGeneration();
	
	/**
	 * Starts the algorithm using the passed data.
	 * @param data. Data representation
	 * @param minSupport. Minimal support.
	 * @param minCredibility. Minimal credibility.
	 */
	public void start(DataRepresentationBase data, double minSupport, double minCredibility);
	
	/**
	 * Gets the result for the this algorithm
	 * @return the list of {@link Rule} inferred from data.
	 */
	public List<Rule> getRules();
}
