package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Set;
import java.util.SortedMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * JOCLs implementation of the {@link ISetsEngine} mostly for data mining.
 * @author Piotr
 *
 */
public class JOCLSetsEngine implements ISetsEngine {

	public Set<BinaryItemSet> getCandidateSets(
			SortedMap<BinaryItemSet, Integer> frequentSupportMap, int i) {
		// TODO Auto-generated method stub
		// TEN KOD TE� MO�E TRAFI� NA JOCLA  - LATER
		return null;
	}

	public Set<BinaryItemSet> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates) {
		// TODO Auto-generated method stub
		
		// TUTAJ PISZESZ KOD PRZESZUKUJ�CY JOCLa
		
		return null;
	}

	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data, Integer support) {
		
		// TODO: write
		// TUTAJ MO�NA WRZUCI� ELEGANCKO WRZUCANIE DANYCH DO JOCLA

		
		return null;
	}

}
