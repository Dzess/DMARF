package org.put.hd.dmarf.algorithms;

import java.util.List;

import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.stopwatches.IStopWatch;
import org.put.hd.dmarf.stopwatches.StopWatch;

/**
 * Fancy class for easier algorithm development. Using pattern template method
 * pattern. If you thing you can implement {@link IAlgorithm} without it you are
 * welcome. This class uses the {@link StopWatch} class for time measurement.
 * 
 * @author Piotr
 * 
 */
public abstract class AlgorithmBase implements IAlgorithm {

	private IStopWatch overallStopWatch = new StopWatch();
	protected IStopWatch generationStopWatch = new StopWatch();
	private IStopWatch initializationStopWatch = new StopWatch();

	public double getElapsedTimeOverall() {
		return overallStopWatch.getElapsedTimeSecs();
	}

	public double getElapsedTimeGeneration() {
		return generationStopWatch.getElapsedTimeSecs();
	}

	public double getElapsedTimeInitialization() {
		return initializationStopWatch.getElapsedTimeSecs();
	}

	public void start(DataRepresentationBase data, double minSupport,
			double minCredibility) {

		overallStopWatch.start();

		// Initialize relevant memory.
		initializationStopWatch.start();
		initMemory(data);
		initializationStopWatch.stop();

		// Frequent set generation step
		generationStopWatch.start();
		startSetGeneration(data, minSupport, minCredibility);
		generationStopWatch.stop();

		// Rules generation step
		startRuleGeneration(data, minSupport, minCredibility);

		// Cleanup relevant memory.
		cleanupMemory();

		overallStopWatch.stop();
	}

	protected abstract void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility);

	protected abstract void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility);

	protected abstract void initMemory(DataRepresentationBase data);

	protected abstract void cleanupMemory();

	public abstract List<Rule> getRules();
}
