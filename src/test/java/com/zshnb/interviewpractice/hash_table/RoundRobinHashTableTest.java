package com.zshnb.interviewpractice.hash_table;

import hash_table.RoundRobinHashTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoundRobinHashTableTest {
	private RoundRobinHashTable<MyInteger, Integer> hashTable;
	@BeforeEach
	public void setup() {
		hashTable = new RoundRobinHashTable<>();
	}

	@Test
	public void successful() {
		hashTable.put(new MyInteger(1), 1);
		hashTable.put(new MyInteger(1), 2);
		int value = hashTable.get(new MyInteger(1));
		Assertions.assertEquals(2, value);
		hashTable.put(new MyInteger(4), 3);
		value = hashTable.get(new MyInteger(4));
		Assertions.assertEquals(3, value);
	}

	private static class MyInteger {
		int value;

		public MyInteger(int value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			MyInteger myInteger = (MyInteger) o;
			return value == myInteger.value;
		}

		@Override
		public int hashCode() {
			return value % 3;
		}
	}
}
