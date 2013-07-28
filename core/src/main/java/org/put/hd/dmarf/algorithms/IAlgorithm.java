package org.put.hd.dmarf.algorithms;

import java.util.List;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;

/**
 * Describes what an algorithm should do.
 * 
 * @author Piotr
 * 
 */
public interface IAlgorithm {

	/**
	 * Gets the required builders from the algorithm. Each algorithm can use
	 * different builders to get what is really needed for it.
	 * 
	 * @return The list of {@link IDataRepresentationBuilder} instances that
	 *         will be used to build data for this specified algorithm.
	 */
	public List<IDataRepresentationBuilder> getRequiredBuilders();

	/**
	 * Gets the time of the all algorithm operations without the I/O operations.
	 * 
	 * @return time of algorithm operations in seconds.
	 */
	public double getElapsedTimeOverall();

	/**
	 * Gets the time of the algorithm operation concerning frequent sets (?)
	 * generation.
	 * 
	 * @return time of algorithm operations in seconds.
	 */
	public double getElapsedTimeGeneration();

	/**
	 * Gets the time of loading data into relevant memory.
	 * 
	 * @return time of algorithm operations in seconds.
	 */
	public double getElapsedTimeInitialization();

	/**
	 * Starts the algorithm using the passed data.
	 * 
	 * @param data
	 *            : Data representation
	 * @param minSupport
	 *            : Minimal support.
	 * @param minConfidence
	 *            : Minimal confidence.
	 */
	public void start(DataRepresentationBase data, double minSupport,
			double minConfidence);

	/**
	 * Gets the result for the this algorithm
	 * 
	 * @return the list of {@link Rule} inferred from data.
	 */
	public List<Rule> getRules();
}
