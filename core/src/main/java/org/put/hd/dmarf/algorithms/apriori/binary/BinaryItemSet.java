package org.put.hd.dmarf.algorithms.apriori.binary;

/**
 * Binary representation of the item set in java code. 
 * Low data representation format with fast bitwise operations. 
 * @author Piotr
 *
 */
public class BinaryItemSet {

	private byte[] attributeVector;
	
	public BinaryItemSet(int vectorSize){
		this.attributeVector = new byte[vectorSize];
	}

	/**
	 * Gets the vector describing the attributes in binary format, using 
	 * 8 bit chunks of data.
	 * @return
	 */
	public byte[] getAttributeVector() {
		return attributeVector;
	}
	
	// TODO: write equality
	
	// TODO: write comparer
}
