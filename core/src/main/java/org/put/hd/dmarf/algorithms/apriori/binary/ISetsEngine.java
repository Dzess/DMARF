package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Set;
import java.util.SortedMap;

import javax.naming.directory.InvalidAttributesException;

import org.put.hd.dmarf.data.DataRepresentationBase;

public interface ISetsEngine {

	/**
	 * Gets the data sets corresponding with the binary representation
	 * 
	 * @param data
	 * @return
	 */
	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data, Integer support);

	public Set<BinaryItemSet> getCandidateSets(
			Set<BinaryItemSet> approvedCandidates, int i);

	/**
	 * Initializes the Engine.
	 * @throws InvalidAttributesException
	 */
	public void initEngine(DataRepresentationBase data) throws InvalidAttributesException;

	
	/**
	 * Cleanses the Engine's memory after run.
	 */
	public void cleanupEngine();
	
	/**
	 * Mines the support from the data.
	 * 
	 * @param data
	 * @param candidates
	 * @return
	 */
	public SortedMap<BinaryItemSet, Integer> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates,
			Integer support);

}