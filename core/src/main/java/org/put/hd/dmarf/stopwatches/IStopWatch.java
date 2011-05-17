package org.put.hd.dmarf.stopwatches;

public interface IStopWatch {

	/**
	 * Starts the time counting.
	 */
	public void start();
	
	/**
	 * Stops the time counting.
	 */
	public void stop();

	/**
	 * Returns the elapsed time in seconds.
	 * @return Elapsed time since start in seconds.
	 */
	public double getElapsedTimeSecs();

}