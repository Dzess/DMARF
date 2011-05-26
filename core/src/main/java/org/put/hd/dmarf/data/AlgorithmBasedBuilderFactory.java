package org.put.hd.dmarf.data;

import java.util.LinkedList;
import java.util.List;

import org.put.hd.dmarf.data.builders.IDataRepresentationBuilder;

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

	public AlgorithmBasedBuilderFactory() {
		representations = new LinkedList<IDataRepresentationBuilder>();
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
