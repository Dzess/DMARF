package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Set;
import java.util.SortedMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Encapsulates all operations for the generating, growing the 
 * {@link BinaryItemSet} in the apriori algorithm.
 * @author Piotr
 *
 */
public class BinarySetsEngine {

	/**
	 * Gets the data sets corresponding with the binary representation
	 * @param data
	 * @return
	 */
	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(DataRepresentationBase data) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<BinaryItemSet> getCandidateSets(
			SortedMap<BinaryItemSet, Integer> frequentSupportMap, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Mines the support from the data.
	 * @param data
	 * @param candidates
	 * @return
	 */
	public Set<BinaryItemSet> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates) {
		// TODO Auto-generated method stub
		return null;
	}

}
