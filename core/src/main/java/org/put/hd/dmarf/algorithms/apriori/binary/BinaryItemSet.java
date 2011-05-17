package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Arrays;

/**
 * Binary representation of the item set in java code. 
 * Low data representation format with fast bitwise operations. 
 * @author Piotr
 *
 */
public class BinaryItemSet implements Comparable<BinaryItemSet> {

	private char[] attributeVector;
	
	public BinaryItemSet( char[] elements){

		// perform shallow copy
		this.attributeVector = new char[elements.length];
		for (int i = 0; i < elements.length; i++) {
			this.attributeVector[i] = elements[i];
		}
		
	}

	/**
	 * Gets the vector describing the attributes in binary format, using 
	 * 8 bit chunks of data.
	 * @return
	 */
	public char[] getAttributeVector() {
		return attributeVector;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(attributeVector);
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
		BinaryItemSet other = (BinaryItemSet) obj;
		if (!Arrays.equals(attributeVector, other.attributeVector))
			return false;
		return true;
	}

	public int compareTo(BinaryItemSet o) {
		
		for (int i = 0; i < this.attributeVector.length; i++) {
			
			if( this.attributeVector[i] < o.attributeVector[i])
				return 1;
			
			if(this.attributeVector[i] > o.attributeVector[i])
				return -1;
			
			// if equal iterate further
		}
		
		// actually this code should never be achieved
		return 0;
	}
}
