package org.cp.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HierarchicalHashMap<V> extends HashMap<String, V> implements HierarchicalMap<V> {
	/**
	 * Returns an entry from the map using a hierarhical key.  For example, if
	 * the key US.MN.Winona is retrieved...we will check for entries of the key in
	 * the following order:
	 * Country.State.City
	 * *.State.City
	 * Country.*.City
	 * *.*.City
	 * Country.State.*
	 * *.State.*
	 * Country.*.*
	 * *.*.*
	 */
	@Override
	public V get(Object key) {
		String hierarhicalKey = (String)key;
		String[] keyParts = hierarhicalKey.split("\\.");
		
		List<String> keys = getWildcards(keyParts);
		
		V value = null;
		
		for(String curKey : keys) {
			value = super.get(curKey);
			if(value != null) {
				return value;
			}
		}
		
		return value;
	}
	
	@Override
	public V put(String key, V value) {
		return super.put(key, value);
	}
	
	protected static String buildRetrievalKey(String[] levels) {
		StringBuilder sb = new StringBuilder();
		boolean afterFirst = false;
		for(String level : levels) {
			if(afterFirst) {
				sb.append(".");
			} else {
				afterFirst=true;
			}
			sb.append(level);
		}
		return sb.toString();
	}
	
	protected static List<String> getWildcards(String[] keyParts) {
		List<String> wildcards = new ArrayList<String>();
		
		long wildcardMask = 0;
		
		int combinations = (int)Math.pow(2, keyParts.length);
		
		List<String[]> toStrip = new ArrayList<String[]>();
		
		for(int combination = 0; combination < combinations; combination++) {
			String[] newKeyParts = Arrays.copyOf(keyParts, keyParts.length);
			long curBitMask = wildcardMask;
			for(int i = 0; i < keyParts.length; i++) {
				if((curBitMask & 1) == 1) {
					newKeyParts[i] = "*";
				}
				curBitMask >>>= 1;
			}
			toStrip.add(newKeyParts);
			wildcards.add(buildRetrievalKey(newKeyParts));
			wildcardMask++;
		}
		
		return wildcards;
	}
}
