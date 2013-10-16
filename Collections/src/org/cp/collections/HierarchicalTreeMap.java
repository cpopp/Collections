package org.cp.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class HierarchicalTreeMap<V> implements HierarchicalMap<V> {
	private final HierarchicalTreeNode root = new HierarchicalTreeNode("");

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V get(Object key) {
		String[] topicParts = ((String)key).split("\\.");
		
		List<HierarchicalTreeNode> currentLevel = new ArrayList<HierarchicalTreeNode>();
		List<HierarchicalTreeNode> nextLevel = new ArrayList<HierarchicalTreeNode>();
		
		currentLevel.add(root);
		
		for(int i = 0; i < topicParts.length; i++) {
			for(HierarchicalTreeNode currentNode : currentLevel) {
				if(currentNode.hasChild(topicParts[i])) {
					nextLevel.add(currentNode.getChild(topicParts[i]));
				}
				if(currentNode.hasChild("*")) {
					nextLevel.add(currentNode.getChild("*"));
				}
			}
			currentLevel = nextLevel;
			nextLevel = new ArrayList<HierarchicalTreeNode>();
			
			if(currentLevel.size() == 0) {
				return null;
			}
		}
		
		Map<String, V> nodesWithValueByPath = new HashMap<String, V>();
		
		// get most specific node with a value and return its value
		for(HierarchicalTreeNode curNode : currentLevel) {
			if(curNode.hasValue()) {
				nodesWithValueByPath.put(curNode.getPath(), curNode.getValue());
			}
		}
		
		String bestPath = getMostSpecificKey(nodesWithValueByPath.keySet());
		
		return nodesWithValueByPath.get(bestPath);
	}

	protected static String getMostSpecificKey(Set<String> keys) {
		Map<Long, String> keysBySpecificity = new TreeMap<Long, String>();
		for(String key : keys) {
			String[] keyParts = key.split("\\.");
			long wildcardNumber = 0;
			for(int i = keyParts.length-1; i >= 0; i--) {
				wildcardNumber <<= 1;
				if(keyParts[i].equals("*")) {
					wildcardNumber |= 1;
				}
			}
			keysBySpecificity.put(wildcardNumber, key);
		}
		
		return keysBySpecificity.values().iterator().next();
	}
	
	@Override
	public V put(String key, V value) {
		HierarchicalTreeNode curNode = root;
		
		String[] keyParts = key.split("\\.");
		
		String path = "";
		boolean afterFirst = false;
		for(String keyPart : keyParts) {
			if(afterFirst) {
				path += ".";
			} else {
				afterFirst = true;
			}
			path += keyPart;
			if(curNode.hasChild(keyPart)) {
				curNode = curNode.getChild(keyPart);
			} else {
				curNode = curNode.addChild(path, keyPart);
			}
		}
		
		return curNode.setValue(value);
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<String, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	

	private class HierarchicalTreeNode {
		private final Map<String, HierarchicalTreeNode> children = new HashMap<String, HierarchicalTreeNode>();
		private final String path;
		
		private boolean hasValue = false;
		private V value = null;
		
		public HierarchicalTreeNode(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return path;
		}
		
		public boolean hasChild(String name) {
			return children.containsKey(name);
		}
		
		public HierarchicalTreeNode getChild(String name) {
			return children.get(name);
		}
		
		public HierarchicalTreeNode addChild(String path, String name) {
			HierarchicalTreeNode newNode = new HierarchicalTreeNode(path);
			children.put(name, newNode);
			return newNode;
		}
		
		public Map<String, HierarchicalTreeNode> getChildren() {
			return children;
		}
		
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			hasValue = true;
			return oldValue;
		}
		
		public boolean hasValue() {
			return hasValue;
		}
		
		public V getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "HierarchicalTreeNode [path=" + path + "]";
		}
	}
}
