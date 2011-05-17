package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Set;
import java.util.SortedMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

public interface ISetsEngine {

	/**
	 * Gets the data sets corresponding with the binary representation
	 * @param data
	 * @return
	 */
	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data);

	public Set<BinaryItemSet> getCandidateSets(
			SortedMap<BinaryItemSet, Integer> frequentSupportMap, int i);

	/**
	 * Mines the support from the data.
	 * @param data
	 * @param candidates
	 * @return
	 */
	public Set<BinaryItemSet> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates);

}