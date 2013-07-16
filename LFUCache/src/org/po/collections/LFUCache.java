package org.po.collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LFUCache<E> {
	private final int maxCapacity;
	
	private Map<String, E> cache = new HashMap<String, E>();
	private Map<String, Long> usageCount = new HashMap<String, Long>();
	
	private NumericSortedMap<Set<String>> keysByUsageCount = new NumericSortedMap<Set<String>>();
	
	public LFUCache(int capacity) {
		this.maxCapacity = capacity;
	}

	public synchronized E get(String key) {
		Long usageCount = this.usageCount.get(key);
		
		if(usageCount == null) {
			return null;
		}
		
		// update the usage for this key
		this.usageCount.put(key, usageCount+1);
		
		// put the key in the new usage set
		Set<String> keysWithSameUsage = keysByUsageCount.get(usageCount+1);
		if(keysWithSameUsage == null) {
			keysWithSameUsage = new HashSet<String>();
			keysByUsageCount.put(usageCount+1, keysWithSameUsage);
		}
		keysWithSameUsage.add(key);
		
		// remove the key (and set if nothing else remains) from the
		// previous usage set
		Set<String> keysWithOldUsage = keysByUsageCount.get(usageCount);
		keysWithOldUsage.remove(key);
		if(keysWithOldUsage.size() == 0) {
			keysByUsageCount.remove(usageCount);
		}
		
		return cache.get(key);
	}
	
	public synchronized void cache(String key, E value) {
		if(cache.containsKey(key)) {
			cache.put(key, value);
		} else {
			if(cache.size() == maxCapacity) {
				evictLFU();
			}
			
			putAfterCapacityVerified(key, value);
		}
	}
	
	private void putAfterCapacityVerified(String key, E value) {
		// put it in the map with cached values, and set its initial
		// usage frequency to 0
		cache.put(key, value);
		usageCount.put(key, 0L);
		
		// get (or create) the set of keys with no usage, and
		// then add the new key
		Set<String> keysWithNoUsage = keysByUsageCount.get(0L);
		if(keysWithNoUsage == null) {
			keysWithNoUsage = new HashSet<String>();
			keysByUsageCount.put(0L, keysWithNoUsage);
		}
		
		keysWithNoUsage.add(key);
	}
	
	public synchronized void evict(String key) {
		Long usageCount = this.usageCount.remove(key);
		
		if(usageCount == null) {
			return;
		}
		
		cache.remove(key);
		
		Set<String> keysWithSameUsage = keysByUsageCount.get(usageCount);
		keysWithSameUsage.remove(key);
		
		if(keysWithSameUsage.size() == 0) {
			keysByUsageCount.remove(usageCount);
		}
	}
	
	private void evictLFU() {
		// keyset of the usage count is sorted, so this will give us
		// the long representing the least frequency of usage a set exists for
		Long firstKey = keysByUsageCount.keySet().iterator().next();
		
		Set<String> keys = keysByUsageCount.get(firstKey);
		
		// remove one of these keys, they all have equal usage
		evict(keys.iterator().next());
	}
}
