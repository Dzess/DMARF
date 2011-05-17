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
public class BinarySetsEngine implements ISetsEngine {

	/* (non-Javadoc)
	 * @see org.put.hd.dmarf.algorithms.apriori.binary.ISetsEngine#getSingleCandidateSets(org.put.hd.dmarf.data.DataRepresentationBase)
	 */
	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(DataRepresentationBase data) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.put.hd.dmarf.algorithms.apriori.binary.ISetsEngine#getCandidateSets(java.util.SortedMap, int)
	 */
	public Set<BinaryItemSet> getCandidateSets(
			SortedMap<BinaryItemSet, Integer> frequentSupportMap, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.put.hd.dmarf.algorithms.apriori.binary.ISetsEngine#verifyCandidatesInData(org.put.hd.dmarf.data.DataRepresentationBase, java.util.Set)
	 */
	public Set<BinaryItemSet> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates) {
		// TODO Auto-generated method stub
		return null;
	}

}
