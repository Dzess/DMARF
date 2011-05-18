package org.put.hd.dmarf.unit.apriori.binary.itemset;

import org.junit.Assert;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;

public class BinaryItemSetEqualityTest {

	private char[] elements;
	
	public void setUpElements(int size){
		elements = new char[size];
	}
	
	@Test
	public void multiple_chunks(){
		setUpElements(3);
		elements[0] =  0;
		elements[1] =  (char) 65536;
		elements[2] =  128;
		
		// one chunk empty
		BinaryItemSet itemSet1 = new BinaryItemSet(elements);
		BinaryItemSet itemSet2 = new BinaryItemSet(elements);
		
		Assert.assertTrue(itemSet1.equals(itemSet2));
	}
	
	@Test
	public void one_chunk_empty(){
		
		setUpElements(1);
		elements[0] = 0;
		
		// one chunk empty
		BinaryItemSet itemSet1 = new BinaryItemSet(elements);
		BinaryItemSet itemSet2 = new BinaryItemSet(elements);
		
		Assert.assertTrue(itemSet1.equals(itemSet2));
		
	}
	
	@Test
	public void one_chunk(){
		
		// one chunk with the some values - corresponding
		// to the map of
		setUpElements(1);
		elements[0] =  255;
		
		// one chunk with element
		BinaryItemSet itemSet1 = new BinaryItemSet(elements);
		BinaryItemSet itemSet2 = new BinaryItemSet(elements);
				
		Assert.assertTrue(itemSet1.equals(itemSet2));
	}
}
