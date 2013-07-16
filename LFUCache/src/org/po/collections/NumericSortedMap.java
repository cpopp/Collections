package org.po.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class NumericSortedMap<V> implements SortedMap<Long, V>{
	private final Map<Long, DoublyLinkedNode<V>> map;
	private final DoublyLinkedNode<V> head;
	
	public NumericSortedMap() {
		this.map = new HashMap<Long, DoublyLinkedNode<V>>();
		this.head = new DoublyLinkedNode<V>(0L, null);
	}
	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		if(key instanceof Long) {
			if(0L == ((Long)key)) {
				return head.getValue();
			}
		}
		
		DoublyLinkedNode<V> node = map.get(key);
		return node == null ? null : map.get(key).getValue();
	}

	@Override
	public V put(Long key, V value) {
		if(value == null) {
			throw new IllegalArgumentException("Null keys not supported");
		}
		
		if(key < 0) {
			throw new IllegalArgumentException("Negative keys are not allowed");
		}
		
		if(key == 0) {
			return head.getAndSetValue(value);
		} 
		
		DoublyLinkedNode<V> exactNode = map.get(key.longValue());
		if(exactNode != null) {
			return exactNode.getAndSetValue(value);
		}
		
		DoublyLinkedNode<V> prevNode = null;
		if(key == 1) {
			prevNode = head;
		} else {
			prevNode = map.get(key.longValue()-1);
		}
		
		if(prevNode != null) {
			DoublyLinkedNode<V> newNode = new DoublyLinkedNode<V>(key, value);
			newNode.setPrev(prevNode);
			
			if(prevNode.getNext() != null) {
				newNode.setNext(prevNode.getNext());
				prevNode.getNext().setPrev(newNode);
			}
			
			prevNode.setNext(newNode);
			
			map.put(key, newNode);
			
			return null;
		}
		
		DoublyLinkedNode<V> nextNode = map.get(key.longValue()+1);
		if(nextNode != null) {
			DoublyLinkedNode<V> newNode = new DoublyLinkedNode<V>(key, value);
			
			newNode.setNext(nextNode);
			newNode.setPrev(nextNode.getPrev());
			
			nextNode.getPrev().setNext(newNode);
			nextNode.setPrev(newNode);
			
			map.put(key, newNode);
		}
		
		throw new IllegalArgumentException("Adjacent key not found.  Key must be 0, 1, or be adjacent to a value already in the map");
	}

	@Override
	public V remove(Object key) {
		if(!(key instanceof Long)) {
			return null;
		}
		
		Long keyValue = (Long)key;
		if(keyValue == 0) {
			return head.getAndSetValue(null);
		}
		
		DoublyLinkedNode<V> thisNode = map.remove(keyValue);
		if(thisNode == null) {
			return null;
		}
		
		thisNode.getPrev().setNext(thisNode.getNext());
		if(thisNode.getNext() != null) {
			thisNode.getNext().setPrev(thisNode.getPrev());
		}
		
		return thisNode.getValue();
	}

	@Override
	public void putAll(Map<? extends Long, ? extends V> m) {
		for(Entry<? extends Long, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		map.clear();
		head.setValue(null);
		head.setNext(null);
	}

	@Override
	public Comparator<? super Long> comparator() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public SortedMap<Long, V> subMap(Long fromKey, Long toKey) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public SortedMap<Long, V> headMap(Long toKey) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public SortedMap<Long, V> tailMap(Long fromKey) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Long firstKey() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Long lastKey() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Set<Long> keySet() {
		return new KeySet();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Set<java.util.Map.Entry<Long, V>> entrySet() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	private class KeySet extends AbstractSet<Long> {

		@Override
		public Iterator<Long> iterator() {
			return new MyIterator();
		}

		@Override
		public int size() {
			return NumericSortedMap.this.size();
		}
		
		@Override
		public boolean contains(Object o) {
			return NumericSortedMap.this.containsKey(o);
		}
		
		@Override
		public boolean remove(Object o) {
			return NumericSortedMap.this.remove(o) != null;
		}
		
		@Override
		public void clear() {
			NumericSortedMap.this.clear();
		}
	}
	
	private class MyIterator implements Iterator<Long> {
		private DoublyLinkedNode<V> current = head;
		
		public MyIterator() {
			if(current.getValue() == null && current.getNext() != null) {
				current = current.getNext();
			}
		}
		
		@Override
		public boolean hasNext() {
			return current.getValue() != null;
		}

		@Override
		public Long next() {
			if(!hasNext()) {
				return null;
			}
			
			Long value = current.getKey();
			current = current.getNext();
			return value;
		}

		@Override
		public void remove() {
			NumericSortedMap.this.remove(current.getKey());
		}
	}
}
