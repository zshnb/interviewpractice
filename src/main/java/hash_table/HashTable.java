package hash_table;

public interface HashTable<K, V> {
	V get(K key);

	void put(K key, V val);
}
