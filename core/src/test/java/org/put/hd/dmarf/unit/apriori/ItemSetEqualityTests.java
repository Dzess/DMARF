package org.put.hd.dmarf.unit.apriori;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.nst.ItemSet;

public class ItemSetEqualityTests {

	@Test
	public void equal_item_set_are_equal(){
		
		ItemSet itemSet1 = new ItemSet("A");
		ItemSet itemSet2 = new ItemSet("B");
		ItemSet itemSet3 = new ItemSet("A");
		
		Assert.assertTrue(itemSet1.equals(itemSet3) == true);
		Assert.assertTrue(itemSet1.equals(itemSet2) == false);
	}
	
	@Test
	public void equal_item_set_with_multiple_collections_in_it(){
		
		SortedSet<String> elements1 = new TreeSet<String>();
		elements1.add("A");
		elements1.add("B");
		elements1.add("1");
		
		SortedSet<String> elements2 = new TreeSet<String>();
		elements2.add("1");
		elements2.add("A");
		elements2.add("B");
		
		ItemSet itemSet1 = new ItemSet(elements1);
		ItemSet itemSet2 = new ItemSet(elements2);
		
		Assert.assertTrue(itemSet1.equals(itemSet2));
	}
	
	@Test
	public void not_equal_when_collections_are_different(){

		SortedSet<String> elements1 = new TreeSet<String>();
		elements1.add("A");
		elements1.add("B");
		
		SortedSet<String> elements2 = new TreeSet<String>();
		elements2.add("1");
		elements2.add("A");
		elements2.add("B");
		
		ItemSet itemSet1 = new ItemSet(elements1);
		ItemSet itemSet2 = new ItemSet(elements2);
		
		Assert.assertFalse(itemSet1.equals(itemSet2));
	}
	
}
