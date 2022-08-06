package hash_table;

import org.springframework.lang.Nullable;

public class RoundRobinHashTable<K, V> implements HashTable<K, V> {
	private Node<K, V>[] table;

	public RoundRobinHashTable() {
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
			int jumpCount = 0;
			while (++index < table.length) {
				nodeInIndex = table[index];
				if (nodeInIndex == null) {
					table[index] = new Node<>(key, val);
					return;
				}
				if (nodeInIndex.jumpCount >= jumpCount) {
					jumpCount += 1;
				} else {
					for (int i = table.length - 1; i > index; i--) {
						table[i] = table[i - 1];
					}
					table[index] = new Node<>(key, val, jumpCount);
				}
			}
		}
		table[index] = new Node<>(key, val);
	}

	private static class Node<K, V> {
		K key;
		V value;
		int jumpCount;

		Node(K key, V value) {
			this.key = key;
			this.value = value;
			jumpCount = 0;
		}

		Node(K key, V value, int jumpCount) {
			this.key = key;
			this.value = value;
			this.jumpCount = jumpCount;
		}
	}
}
