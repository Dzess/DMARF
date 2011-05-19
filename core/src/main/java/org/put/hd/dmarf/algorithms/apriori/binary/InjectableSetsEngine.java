package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Set;
import java.util.SortedMap;

import org.put.hd.dmarf.data.DataRepresentationBase;

/**
 * This class uses the various {@see ISetsEngine} to compose the one
 * {@link ISetsEngine}.
 * 
 * @author Piotr
 * 
 */
public class InjectableSetsEngine implements ISetsEngine {

	private ISetsEngine candidateEngine;
	private ISetsEngine verifierEngine;

	/**
	 * Creates the instance of the class {@link InjectableSetsEngine}.
	 * 
	 * @param candidateEngine
	 *            : {@linkplain ISetsEngine} used for candidate generation
	 * @param verifierEngine
	 *            : {@linkplain ISetsEngine} used for candidate verification
	 */
	public InjectableSetsEngine(ISetsEngine candidateEngine,
			ISetsEngine verifierEngine) {
		super();
		this.candidateEngine = candidateEngine;
		this.verifierEngine = verifierEngine;
	}

	public SortedMap<BinaryItemSet, Integer> getSingleCandidateSets(
			DataRepresentationBase data, Integer support) {
		// pass to engine below
		return this.candidateEngine.getSingleCandidateSets(data, support);
	}

	public Set<BinaryItemSet> getCandidateSets(
			Set<BinaryItemSet> approvedCandidates, int i) {
		// pass to engine below
		return this.candidateEngine.getCandidateSets(approvedCandidates, i);
	}

	public SortedMap<BinaryItemSet, Integer> verifyCandidatesInData(
			DataRepresentationBase data, Set<BinaryItemSet> candidates,
			Integer support) {
		// pass to engine below
		return this.verifierEngine.verifyCandidatesInData(data, candidates,
				support);
	}

}
