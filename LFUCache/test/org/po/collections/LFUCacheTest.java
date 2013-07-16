package org.po.collections;

import junit.framework.Assert;

import org.junit.Test;

public class LFUCacheTest {
	
	@Test
	public void testSmallCapacity() {
		// LFU cache of capacity 1
		LFUCache<String> cache = new LFUCache<String>(1);
		
		Assert.assertNull(cache.get("1"));
		
		cache.cache("1", "One");
		
		// Assert.assertEquals("One", cache.get("1"));
		
		cache.cache("2", "Two");
		
		Assert.assertNull(cache.get("1"));
		
		Assert.assertEquals("Two", cache.get("2"));
	}
	
	@Test
	public void testLFUEviction() {
		// set up LFU cache with capicity 2
		LFUCache<String> cache = new LFUCache<String>(2);
		
		cache.cache("1", "One");
		cache.cache("2", "Two");
		
		cache.get("1");
		
		Assert.assertEquals("One", cache.get("1"));
		Assert.assertEquals("Two", cache.get("2"));
		
		// '1' has been used twice, '2' has been used once
		
		cache.cache("3", "Three");
		
		// verify '3' is in the cache, and that '2' is evicted
		Assert.assertEquals("One", cache.get("1"));
		Assert.assertNull(cache.get("2"));
		Assert.assertEquals("Three", cache.get("3"));
		
		cache.get("3");
		cache.get("3");
		cache.get("3");
		// '1' has been used three times, '3' has been used four
		
		cache.cache("4", "Four");
		 
		// verify '4' is in the cache, and that '1' is evicted
		Assert.assertNull(cache.get("1"));
		Assert.assertEquals("Three", cache.get("3"));
		Assert.assertEquals("Four", cache.get("4"));
	}

}
