package org.put.hd.dmarf.stopwatches;

/*
 Copyright (c) 2005, Corey Goldberg

 StopWatch.java is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 */
public class StopWatch implements IStopWatch {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;

	/* (non-Javadoc)
	 * @see org.put.hd.dmarf.stopwatches.IStopWatch#start()
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
		this.running = true;
	}

	/* (non-Javadoc)
	 * @see org.put.hd.dmarf.stopwatches.IStopWatch#stop()
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	// Elapsed time in milliseconds
	public long getElapsedTime() {
		long elapsed;
		if (running) {
			elapsed = (System.currentTimeMillis() - startTime);
		} else {
			elapsed = (stopTime - startTime);
		}
		return elapsed;
	}

	/* (non-Javadoc)
	 * @see org.put.hd.dmarf.stopwatches.IStopWatch#getElapsedTimeSecs()
	 */
	public double getElapsedTimeSecs() {
		double elapsed;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000.0);
		} else {
			elapsed = ((stopTime - startTime) / 1000.0);
		}
		return elapsed;
	}
}
