package org.po.collections;

public class DoublyLinkedNode<V> {
	private final Long key;
	
	private DoublyLinkedNode<V> prev;
	private DoublyLinkedNode<V> next;
	private V value;

	public DoublyLinkedNode(Long key, V value) {
		this.key = key;
		this.value = value;
	}

	public DoublyLinkedNode<V> getPrev() {
		return prev;
	}

	public void setPrev(DoublyLinkedNode<V> prev) {
		this.prev = prev;
	}

	public DoublyLinkedNode<V> getNext() {
		return next;
	}

	public void setNext(DoublyLinkedNode<V> next) {
		this.next = next;
	}

	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
	
	public V getAndSetValue(V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}
	
	public Long getKey() {
		return key;
	}
}
