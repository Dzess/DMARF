package org.put.hd.dmarf.algorithms.apriori.binary;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Binary representation of the item set in java code. Low data representation
 * format with fast bitwise operations.
 * 
 * @author Piotr
 * 
 */
public class BinaryItemSet implements Comparable<BinaryItemSet> {

	private char[] attributeVector;
	private int numberOfAttributes;

	public BinaryItemSet(char[] elements) {

		// perform shallow copy
		this.attributeVector = new char[elements.length];
        System.arraycopy(elements, 0, this.attributeVector, 0, elements.length);

		// get the number of attributes from the attribute vector ?
		int sum = 0;
		for (char v : this.attributeVector) {
			sum += BinaryItemSet.bitCount(v);
		}
		this.numberOfAttributes = sum;
	}

	public BinaryItemSet(char[] elements, int numberOfAttributes) {

		// set the number of attributes
		this.numberOfAttributes = numberOfAttributes;

		// perform shallow copy
		this.attributeVector = new char[elements.length];
        System.arraycopy(elements, 0, this.attributeVector, 0, elements.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("\nItem Set\n");
		builder.append("Attribute vector: \n");
		for (int i = 0; i < this.attributeVector.length; i++) {
			char[] chunk = BinaryItemSet.getBinaryString(this.attributeVector[i]);
			builder.append("Chunk ").append(i).append(": ");
			builder.append(chunk);
			builder.append('\n');
		}

		return builder.toString();
	}

	/**
	 * Gets the vector describing the attributes in binary format, using 16 bit
	 * chunks of data.
	 * 
	 * @return vector of attributes in binary format
	 */
	public char[] getAttributeVector() {
		return attributeVector;
	}

	/**
	 * Get the number of '1' in the bitmap vector. Coded as the char[] table.
	 * 
	 * @return Number of attributes in set.
	 */
	public int getNumberOfAttributes() {
		return numberOfAttributes;
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
        return Arrays.equals(attributeVector, other.attributeVector);
    }

	public int compareTo(BinaryItemSet o) {

		for (int i = 0; i < this.attributeVector.length; i++) {

			if (this.attributeVector[i] < o.attributeVector[i])
				return 1;

			if (this.attributeVector[i] > o.attributeVector[i])
				return -1;

			// if equal iterate further
		}

		// actually this code should never be achieved
		return 0;
	}

	/**
	 * Gets from {@link BinaryItemSet} with n attributes. N
	 * {@link BinaryItemSet} objects each with <i>n -1</i> attributes.
	 * 
	 * @param inputSet
	 *            : the vector to be divided
	 * @param numberOfAttributes
	 *            : generation number
	 * @return Collection of sets with <i>n-1</i> attributes.
	 */
	public static Collection<BinaryItemSet> divideSet(char[] inputSet,
			int numberOfAttributes) {
		Set<BinaryItemSet> output = new LinkedHashSet<BinaryItemSet>();

		if (numberOfAttributes < 2)
			return output;

		int newGeneration = numberOfAttributes - 1;

		for (int i = 0; i < inputSet.length; i++) {
			// 1000 0000 0000 0000
			char mask = 32768;
			for (int j = 0; j < 16; j++) {
				// get the set with n-1 attributes
				char invertedMask = (char) ~mask;
				char outChunk = (char) (invertedMask & inputSet[i]);

				// if same then
				// 0000
				// 0010 case was
				if (outChunk != inputSet[i]) {
					char[] newElements = inputSet.clone();
					newElements[i] = outChunk;
					BinaryItemSet set = new BinaryItemSet(newElements,
							newGeneration);
					output.add(set);

				}
				// move to mask 0...1..00 to the end
				mask = (char) (mask >>> 1);
			}
		}

		return output;
	}

	/**
	 * Gets from {@link BinaryItemSet} with n attributes. N
	 * {@link BinaryItemSet} objects each with <i>n -1</i> attributes.
	 * 
	 * @param inputSet
	 *            : set to be divided.
	 * @return Collection of sets with <i>n-1</i> attributes.
	 */
	public static Collection<BinaryItemSet> divideSet(BinaryItemSet inputSet) {
		return divideSet(inputSet.getAttributeVector(),
				inputSet.getNumberOfAttributes());
	}

	/**
	 * Get the number of '1' in the binary representation number.
	 * 
	 * @param x
	 *            : number to be looked into.
	 * @return the number of '1' in the binary representation.
	 */
	public static int bitCount(char x) {
		// classic shift method
		int result = 0;
		for (int i = 0; i < 16; i++) {
			result += x & 1;
			x >>>= 1;
		}
		return result;
	}

	/**
	 * Gets the same as the Integer.toBinaryString, but does it with trailing
	 * zeros at the beginning.
	 * 
	 * @param number
	 *            : the value to be changed into binary representation.
	 * @return the char[] representation of the passed value.
	 */
	public static char[] getBinaryString(char number) {

		char value = number;

		char[] output = new char[16];

		// 'read from behind' the standard algorithm
		for (int i = 0; i < 16; i++) {
			if (value % 2 == 1) {
				output[15 - i] = '1';
			} else {
				output[15 - i] = '0';
			}
			value = (char) (value / 2);
		}

		return output;
	}
}
