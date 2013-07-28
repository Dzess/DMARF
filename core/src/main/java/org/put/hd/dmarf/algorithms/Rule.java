package org.put.hd.dmarf.algorithms;

import java.util.List;

/**
 * Represents the class for association rule. Immutable type.
 * 
 * @author Piotr
 * 
 */
public class Rule {

	private StringBuilder builder;

	@Override
	public String toString() {
		builder = new StringBuilder();

		// creating the first line
		// no: ( item_1 AND item_2 ...AND...item_n) => ( item_m )
		builder.append(id);
		builder.append(": ( ");

		List<Integer> itemsConditional = conditionalPart;
		appendItems(itemsConditional);

		builder.append(" ) => ( ");

		List<Integer> itemsExecutive = executivePart;
		appendItems(itemsExecutive);

		builder.append(" )");

		// print the support and confidence
		builder.append("  Confidence: ");
		builder.append(this.confidence);

		builder.append("  Support: ");
		builder.append(this.support);

		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conditionalPart == null) ? 0 : conditionalPart.hashCode());
		result = prime * result + confidence;
		result = prime * result
				+ ((executivePart == null) ? 0 : executivePart.hashCode());
		
		result = prime * result + support;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;

		if (conditionalPart == null) {
			if (other.conditionalPart != null)
				return false;
		}
		// custom set comparison
		else if (!smartEquals(conditionalPart, other.conditionalPart))
			return false;
		if (confidence != other.confidence)
			return false;
		if (executivePart == null) {
			if (other.executivePart != null)
				return false;
		}
		// custom set comparison
		else if (!smartEquals(executivePart, other.executivePart))
			return false;

        return support == other.support;
    }

	/**
	 * Compares two lists, but in the set meaning. Sequence is NOT important
	 * 
	 * @param list1 the first list
	 * @param list2 the second list
     *
	 * @return True if equal
	 */
	private boolean smartEquals(List<Integer> list1, List<Integer> list2) {

		if (list1.size() != list2.size())
			return false;

		for (Integer integer : list2) {

			if (!list1.contains(integer))
				return false;
		}

		return true;
	}

	private void appendItems(List<Integer> items) {
		for (int i = 0; i < items.size(); i++) {
			builder.append(items.get(i)).append(" ");
			if (i + 1 != items.size())
				builder.append(" AND ");
		}
	}

	private int id;

	public Rule(int id, List<Integer> conditionalPart,
			List<Integer> executivePart, int confidence, int support) {
		this.id = id;
		this.conditionalPart = conditionalPart;
		this.executivePart = executivePart;
		this.confidence = confidence;
		this.support = support;
	}

	private List<Integer> conditionalPart;
	private List<Integer> executivePart;
	private int confidence;
	private int support;

	public int getId() {

		return id;
	}

	public List<Integer> getConditionalPart() {
		return conditionalPart;
	}

	public List<Integer> getExecutivePart() {
		return executivePart;
	}

	public int getSupport() {
		return support;
	}

	public int getConfidence() {
		return confidence;
	}
}
