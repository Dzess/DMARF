package org.put.hd.dmarf.data.builders;

import java.util.List;

import org.put.hd.dmarf.algorithms.IAlgorithm;
import org.put.hd.dmarf.data.DataRepresentationBase;
import org.put.hd.dmarf.data.InjectableDataRepresentation;

/**
 * Gets all the algorithm and checks what kind of
 * {@link IDataRepresentationBuilder} is needed be algorithm. Then passes such
 * created injected representation base to algorithm.
 * 
 * @author Piotr
 * 
 */
public class AlgorithmBasedBuilderFactory implements IDataRepresentationBuilder {

	private List<IDataRepresentationBuilder> representations;

	public AlgorithmBasedBuilderFactory(IAlgorithm algorithm) {

		if (algorithm == null)
			throw new NullPointerException(
					"Null is not applicable value for the IAlgorithm");

		representations = algorithm.getRequiredBuilders();

		if (representations == null || representations.size() == 0) {
			throw new RuntimeException("Algorithm returned no values");
		}

	}

	public void addItemInTransaction(Integer itemIdentifier) {

		for (IDataRepresentationBuilder builder : this.representations) {
			builder.addItemInTransaction(itemIdentifier);
		}
	}

	public void addTransaction(Integer transactionIdentifier) {
		for (IDataRepresentationBuilder builder : this.representations) {
			builder.addTransaction(transactionIdentifier);
		}
	}

	public DataRepresentationBase getDataRepresentation() {
		InjectableDataRepresentation repr = new InjectableDataRepresentation();

		for (IDataRepresentationBuilder builder : this.representations) {
			repr = builder.getMergedDataRepresentation(repr);
		}

		return repr;

	}

	public InjectableDataRepresentation getMergedDataRepresentation(
			InjectableDataRepresentation injectableDataRepresentation) {
		// This class performs no merging at all
		throw new RuntimeException("This class supports no merge operations");
	}

}
