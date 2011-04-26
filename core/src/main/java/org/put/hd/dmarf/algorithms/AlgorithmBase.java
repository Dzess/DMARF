package org.put.hd.dmarf.algorithms;

import java.util.List;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Fancy class for easier algorithm development. Using pattern template method
 * pattern. If you thing you can implement {@link IAlgorithm} without it you are
 * welcome.
 * This class uses the {@link StopWatch} class for time measurement.
 * @author Piotr
 * 
 */
public abstract class AlgorithmBase implements IAlgorithm {

	private StopWatch overallStopWatch = new StopWatch();
	protected StopWatch generationStopWatch = new StopWatch();

	public long getElapsedTimeOverall() {
		return overallStopWatch.getElapsedTimeSecs();
	}

	public long getElapsedTimeGeneration() {
		return generationStopWatch.getElapsedTimeSecs();
	}

	public void start(DataRepresentationBase data, double minSupport,
			double minCredibility) {

		overallStopWatch.start();
		
		// Frequent set generation step
		generationStopWatch.start();
		startSetGeneration(data, minSupport, minCredibility);
		generationStopWatch.stop();
		
		// Rules generation step
		startRuleGeneration(data, minSupport, minCredibility);
		
		overallStopWatch.stop();
	}

	protected abstract void startRuleGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility);

	protected abstract void startSetGeneration(DataRepresentationBase data,
			double minSupport, double minCredibility);

	public abstract List<Rule> getRules();
}
