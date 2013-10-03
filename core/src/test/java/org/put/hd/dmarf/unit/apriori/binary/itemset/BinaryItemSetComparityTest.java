package org.put.hd.dmarf.unit.apriori.binary.itemset;

import org.junit.Assert;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.binary.BinaryItemSet;

public class BinaryItemSetComparityTest {

	private char[] elements1;
	private char[] elements2;

	public void setUpElements(int size) {
		elements1 = new char[size];
		elements2 = new char[size];
	}

	@Test
	public void vector_with_more_ones_on_the_left_wins_one_chunk() {

		setUpElements(3);
		elements1[0] =  0;
		elements2[0] =  255;

		// one chunk empty
		BinaryItemSet itemSet1 = new BinaryItemSet(elements1);
		BinaryItemSet itemSet2 = new BinaryItemSet(elements2);

		// the left item set with
		// 11111111 < 00000000 is for sure winner
		Assert.assertTrue(itemSet1.compareTo(itemSet2) > 0);
	}

	@Test
	public void vector_with_more_ones_left_wins_many_chunks() {
		setUpElements(3);
		elements1[0] =  137;
		elements2[0] =  137;

		elements1[1] =  124;
		elements2[1] =  124;

		elements1[2] =  32768;
		elements2[2] =  32769;

		System.out.println(Integer.toBinaryString(elements1[2]));
		System.out.println(Integer.toBinaryString(elements2[2]));

		// one chunk empty
		BinaryItemSet itemSet1 = new BinaryItemSet(elements1);
		BinaryItemSet itemSet2 = new BinaryItemSet(elements2);

		// the left item set with
		// 1111001 < 1111000 is for sure winner (is fist - smaller one)
		Assert.assertTrue(itemSet1.compareTo(itemSet2) > 0);
	}
	
	@Test
	public void equality_in_data_sets_using_compare(){
		setUpElements(3);
		elements1[0] =  137;
		elements2[0] =  137;

		elements1[1] =  124;
		elements2[1] =  124;

		elements1[2] =  32768;
		elements2[2] =  32768;

		System.out.println(Integer.toBinaryString(elements1[2]));
		System.out.println(Integer.toBinaryString(elements2[2]));

		// one chunk empty
		BinaryItemSet itemSet1 = new BinaryItemSet(elements1);
		BinaryItemSet itemSet2 = new BinaryItemSet(elements2);

		// the left item set with
		// 1111001 < 1111000 is for sure winner (is fist - smaller one)
		Assert.assertTrue(itemSet1.compareTo(itemSet2) == 0);
	}
}
