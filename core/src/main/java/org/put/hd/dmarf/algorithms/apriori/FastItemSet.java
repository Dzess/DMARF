package org.put.hd.dmarf.algorithms.apriori;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Faster version (suing integers for values). Has support for
 * {@link Comparable} interface which is must have feature for
 * {@link SortedList}
 * 
 * @author Piotr
 * 
 */
public class FastItemSet implements Comparable<FastItemSet> {

	private SortedSet<Integer> elements;

	public SortedSet<Integer> getElements() {
		return elements;
	}

	public FastItemSet() {
		elements = new TreeSet<Integer>();
	}

	public FastItemSet(FastItemSet set1, FastItemSet set2) {
		elements = new TreeSet<Integer>(set1.getElements());
		elements.addAll(set2.getElements());
	}

	public FastItemSet(Integer Integer) {
		elements = new TreeSet<Integer>();
		elements.add(Integer);
	}

	public FastItemSet(SortedSet<Integer> set) {
		elements = new TreeSet<Integer>(set);
	}

	public int compareTo(FastItemSet o) {
		if (o.equals(this))
			return 0;

		// code for checking the less
		if (o.elements.size() > this.elements.size())
			return -1;
		else if (o.elements.size() < this.elements.size())
			return 1;
		else {
			// the case when we have two sets with the same amount elements, and
			// we want
			// the lexigraphic sorting of the elements inside
			Iterator<Integer> it1 = this.elements.iterator();
			Iterator<Integer> it2 = o.elements.iterator();
			for (int i = 0; i < this.elements.size(); i++) {
				Integer value1 = it1.next();
				Integer value2 = it2.next();

				int compOut = value1.compareTo(value2);
				if (compOut > 0)
					return 1;
				if (compOut < 0)
					return -1;

				// if equal just go with the loop
			}
		}

		throw new AssertionError("This code should never been reach");

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return elements.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FastItemSet other = (FastItemSet) obj;

		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!collectionEquals(other.elements))
			return false;
		return true;
	}

	/**
	 * Helper method for looking of the two sets are equal. It ignores the
	 * sequence.
	 * 
	 * @return True if equal. Otherwise false.
	 */
	private boolean collectionEquals(SortedSet<Integer> o) {

		if (elements.size() != o.size())
			return false;

		for (Integer singleElement : elements) {
			if (!o.contains(singleElement))
				return false;
		}

		return true;
	}
}
