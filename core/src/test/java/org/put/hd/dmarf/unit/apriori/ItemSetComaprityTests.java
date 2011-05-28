package org.put.hd.dmarf.unit.apriori;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.put.hd.dmarf.algorithms.apriori.nst.ItemSet;

public class ItemSetComaprityTests {

	@Test
	public void the_same_sized_item_sets_are_lexicographicly_comparable(){
		SortedSet<String> elements1 = new TreeSet<String>();
		elements1.add("1");

		SortedSet<String> elements2 = new TreeSet<String>();
		elements2.add("2");

		ItemSet itemSet1 = new ItemSet(elements1);
		ItemSet itemSet2 = new ItemSet(elements2);

		Assert.assertTrue( itemSet1.compareTo(itemSet2) < 0);
	}
	
	public void the_same_sized_sets_are_lexicographicly_comparable_bigger_sets(){
		SortedSet<String> elements1 = new TreeSet<String>();
		elements1.add("1");
		elements1.add("2");
		elements1.add("3"); // ops there is 3
		elements1.add("5");
		elements1.add("6");
		elements1.add("7");

		SortedSet<String> elements2 = new TreeSet<String>();
		elements2.add("1");
		elements2.add("2");
		elements2.add("4"); // ops there is 4
		elements2.add("5");
		elements2.add("6");
		elements2.add("7");

		ItemSet itemSet1 = new ItemSet(elements1);
		ItemSet itemSet2 = new ItemSet(elements2);

		// 1,2,3,5,6,7 < 1,2,4,5,6,7
		Assert.assertTrue( itemSet1.compareTo(itemSet2) < 0);
	}
	
	@Test
	public void smaller_collection_are_less_than_bigger_item_sets() {

		SortedSet<String> elements1 = new TreeSet<String>();
		elements1.add("A");
		elements1.add("B");

		SortedSet<String> elements2 = new TreeSet<String>();
		elements2.add("1");
		elements2.add("A");
		elements2.add("B");

		ItemSet itemSet1 = new ItemSet(elements1);
		ItemSet itemSet2 = new ItemSet(elements2);

		Assert.assertTrue( itemSet1.compareTo(itemSet2) < 0);
	}
	
	@Test
	public void bigger_collection_are_greater_than_smaller_item_sets(){
		SortedSet<String> elements1 = new TreeSet<String>();
		elements1.add("A");
		elements1.add("B");

		SortedSet<String> elements2 = new TreeSet<String>();
		elements2.add("1");
		elements2.add("A");
		elements2.add("B");

		ItemSet itemSet1 = new ItemSet(elements1);
		ItemSet itemSet2 = new ItemSet(elements2);

		Assert.assertTrue( itemSet2.compareTo(itemSet1) > 0);
	}
}
