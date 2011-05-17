package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * Encapsulates all operations for the generating, growing the
 * {@link BinaryItemSet} in the apriori algorithm.
 * 
 * @author Piotr
 * 
 */
public class BinarySetsEngine implements ISetsEngine {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.put.hd.dmarf.algorithms.apriori.binary.ISetsEngine#getSingleCandidateSets
	 * (org.put.hd.dmarf.data.DataRepresentationBase)
	 */
	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data, Integer supportThreshold) {
		
		
		// MT: get the each attribute independently
		SortedMap<BinaryItemSet, Integer> frequentSets = new TreeMap<BinaryItemSet, Integer>();
		
		for (Map.Entry<Integer, Integer> entry : data.getAttributesCounter()
				.entrySet()) {

			Integer value = entry.getValue();
			Integer key = entry.getKey();

			// add elements only with proper support
			if (value >= supportThreshold) {
				
				// smart ass generation of vectors like
				// 00000100000, 0000000000001000,10000000,0000001 or something like that
				char[] vector = getVectorForKey(key);
				
				// just make the item set
				BinaryItemSet itemSet = new BinaryItemSet(vector);
				frequentSets.put(itemSet, value);
			}
		}
		return frequentSets;
	}

	private char[] getVectorForKey(Integer key) {
		// TODO use generate byte array here to get the proper vector for key. 
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.put.hd.dmarf.algorithms.apriori.binary.ISetsEngine#getCandidateSets
	 * (java.util.SortedMap, int)
	 */
	public Set<BinaryItemSet> getCandidateSets(
			SortedMap<BinaryItemSet, Integer> frequentSupportMap, int i) {
		// TODO Auto-generated method stub
		// NA RAZIE MO¯EMY NAPISAÆ GENEROWANIE NIE BITOWE ALE OD RAZU ZACZA£BYM
		// OD BITOWEGO
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.put.hd.dmarf.algorithms.apriori.binary.ISetsEngine#verifyCandidatesInData
	 * (org.put.hd.dmarf.data.DataRepresentationBase, java.util.Set)
	 */
	public Set<BinaryItemSet> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates) {
		// TODO Auto-generated method stub
		// TUTAJ SPRAWDZANIE MINIG SUPPORT KTORY MA WYLECIEC NA GPU
		return null;
	}

}
