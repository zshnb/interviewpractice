package hash_table;

import org.springframework.lang.Nullable;

public class LinearHashTable<K, V> implements HashTable<K, V> {
	private Node<K, V>[] table;

	public LinearHashTable() {
		table = (Node<K, V>[]) new Node[16];
	}

	@Override
	@Nullable
	public V get(K key) {
		int index = key.hashCode() % table.length;
		if (table[index].key.equals(key)) {
			return table[index].value;
		}
		while (++index < table.length) {
			if (table[index] != null && table[index].key.equals(key)) {
				return table[index].value;
			}
		}
		return null;
	}

	@Override
	public void put(K key, V val) {
		int index = key.hashCode() % table.length;
		Node<K, V> nodeInIndex = table[index];
		if (nodeInIndex != null) {
			if (nodeInIndex.key.equals(key)) {
				nodeInIndex.value = val;
				return;
			}
			while (nodeInIndex != null && index < table.length) {
				nodeInIndex = table[index];
				index += 1;
			}
		}
		table[index] = new Node<>(key, val);
	}

	private static class Node<K, V> {
		K key;
		V value;

		Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}
