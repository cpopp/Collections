package org.cp.collections;

import junit.framework.Assert;

import org.junit.Test;

public class HierarchicalMapTest {

	private HierarchicalMap<Integer> getNewMap() {
		return new HierarchicalTreeMap<Integer>();
	}
	
	@Test
	public void testPutAndGet() {
		HierarchicalMap<Integer> map = getNewMap();
		map.put("US", 1);
		map.put("US.MN", 2);
		map.put("US.MN.Winona", 3);
		
		Assert.assertEquals(1, map.get("US").intValue());
		Assert.assertEquals(2, map.get("US.MN").intValue());
		Assert.assertEquals(3, map.get("US.MN.Winona").intValue());
	}
	
	@Test
	public void testGetWithBadFieldscNonmatchingKeys() {
		HierarchicalMap<Integer> map = getNewMap();
		map.put("US", 1);
		map.put("US.MN", 2);
		map.put("US.MN.Winona", 3);
		
		Assert.assertEquals(1, map.get("US").intValue());
		Assert.assertNull(map.get("US.WI"));
		
		Assert.assertEquals(2, map.get("US.MN").intValue());
		Assert.assertNull(map.get("US.MN.Rochester"));
		
		Assert.assertEquals(3, map.get("US.MN.Winona").intValue());
		Assert.assertNull(map.get("US.MN.Winona.Somewhere"));
		Assert.assertNull(map.get("US.MN.Winona.Somewhere.EvenFurther"));
	}
	
	@Test
	public void testPutAndGetSimpleEndingWildcards() {
		HierarchicalMap<Integer> map = getNewMap();
		map.put("US.*", 1);
		map.put("US.MN.*", 2);
		map.put("US.MN.Winona.*", 3);
		
		Assert.assertEquals(1, map.get("US.MN").intValue());
		Assert.assertEquals(2, map.get("US.MN.Winona").intValue());
		Assert.assertEquals(3, map.get("US.MN.Winona.Somewhere").intValue());
	}
	
	@Test
	public void testPutAndGetSimpleStartingWildcards() {
		HierarchicalMap<Integer> map = getNewMap();
		map.put("*.MN", 2);
		map.put("US.*.Winona", 3);
		map.put("*.*.Rochester", 4);
		
		Assert.assertEquals(2, map.get("US.MN").intValue());
		Assert.assertEquals(3, map.get("US.MN.Winona").intValue());
		Assert.assertEquals(4, map.get("US.MN.Rochester").intValue());
	}
	
	@Test
	public void testPutAndGetMixedWildcards() {
		HierarchicalMap<Integer> map = getNewMap();
		map.put("Country.State.City", 1);
		map.put("*.*.*", 2);
		map.put("Country.*.*", 3);
		map.put("*.*.City", 4);

		Assert.assertEquals(1, map.get("Country.State.City").intValue());
		Assert.assertEquals(2, map.get("Does.Not.Match").intValue());
		Assert.assertEquals(3, map.get("Country.State.OtherCity").intValue());
		Assert.assertEquals(3, map.get("Country.OtherState.OtherCity").intValue());
		Assert.assertEquals(4, map.get("OtherCountry.OtherState.City").intValue());
		Assert.assertEquals(4, map.get("Country.Blah.City").intValue());
	}
	
	@Test
	public void testPutAndGetCompleteWildcards() {
		HierarchicalMap<Integer> map = getNewMap();
		int counter = 0;
		
		for(String s : HierarchicalHashMap.getWildcards(new String[]{"A", "B", "C"})) {
			map.put(s, counter++);
		}
		
		
		counter = 0;
		for(String s : HierarchicalHashMap.getWildcards(new String[]{"A", "B", "C"})) {
			Assert.assertEquals(counter++, map.get(s).intValue());
		}
	}
}
