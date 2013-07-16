package org.po.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class NumericSortedMapTest {

	@Test
	public void testBasics() {
		NumericSortedMap<String> myMap = new NumericSortedMap<String>();
		myMap.put(1L, "One");
		myMap.put(2L, "Two");
		myMap.put(3L, "Three");
		
		Assert.assertEquals("One", myMap.get(1L));
		Assert.assertEquals("Two", myMap.get(2L));
		Assert.assertEquals("Three", myMap.get(3L));
		
		List<Long> list = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L));
		for(Long key : myMap.keySet()) {
			Assert.assertEquals(list.remove(0), key);
		}
	}

	@Test
	public void testZero() {
		NumericSortedMap<String> myMap = new NumericSortedMap<String>();
		myMap.put(0L, "Zero");
		myMap.put(1L, "One");
		myMap.put(2L, "Two");
		myMap.put(3L, "Three");
		
		Assert.assertEquals("Zero", myMap.get(0L));
		Assert.assertEquals("One", myMap.get(1L));
		Assert.assertEquals("Two", myMap.get(2L));
		Assert.assertEquals("Three", myMap.get(3L));
		
		List<Long> list = new ArrayList<Long>(Arrays.asList(0L, 1L, 2L, 3L));
		for(Long key : myMap.keySet()) {
			Assert.assertEquals(list.remove(0), key);
		}
	}
	
	@Test
	public void testRemoval() {
		NumericSortedMap<String> myMap = new NumericSortedMap<String>();
		myMap.put(0L, "Zero");
		myMap.put(1L, "One");
		myMap.put(2L, "Two");
		myMap.put(3L, "Three");
		
		Assert.assertEquals("Zero", myMap.get(0L));
		Assert.assertEquals("One", myMap.get(1L));
		Assert.assertEquals("Two", myMap.get(2L));
		Assert.assertEquals("Three", myMap.get(3L));
		
		Assert.assertEquals("Zero", myMap.remove(0L));
		Assert.assertEquals("Two", myMap.remove(2L));
		
		Assert.assertNull(myMap.get(0L));
		Assert.assertNull(myMap.get(2L));
		
		List<Long> list = new ArrayList<Long>(Arrays.asList(1L, 3L));
		for(Long key : myMap.keySet()) {
			Assert.assertEquals(list.remove(0), key);
		}
	}
	
	@Test
	public void testInsertionAfterRemoval() {
		NumericSortedMap<String> myMap = new NumericSortedMap<String>();
		myMap.put(0L, "Zero");
		myMap.put(1L, "One");
		myMap.put(2L, "Two");
		myMap.put(3L, "Three");

		Assert.assertEquals("One", myMap.remove(1L));
		Assert.assertEquals("Two", myMap.remove(2L));
		
		myMap.put(4L, "FOUR");
		myMap.put(5L, "FIVE");
		
		Assert.assertEquals("FOUR", myMap.remove(4L));
		
		myMap.put(6L, "SIX");
		myMap.put(7L, "SEVEN");
		
		List<Long> list = new ArrayList<Long>(Arrays.asList(0L, 3L, 5L, 6L, 7L));
		for(Long key : myMap.keySet()) {
			Assert.assertEquals(list.remove(0), key);
		}
	}
}
