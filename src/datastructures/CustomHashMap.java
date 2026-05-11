/**
 * Author: Ammar Kapadia
 * Description: Generic CustomHashMap mapping a String key to any value type V.
 *              Separate-chaining collision handling using a linked list per bucket.
 *              Generalised in Sprint 2 (was String -> Vehicle in Sprint 1) so other
 *              modules can reuse it (StatsManager uses CustomHashMap<Integer> for
 *              the hour counter; CustomGraph could use CustomHashMap<EdgeLinkedList>;
 *              DijkstraSolver could use CustomHashMap<Integer> for distance maps).
 */
package datastructures;

public class CustomHashMap<V> {

    private static class HashNode<V> {
        String key;
        V value;
        HashNode<V> next;

        HashNode(String key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode<V>[] table;
    private int tableSize;
    private int size;  // number of entries currently in the map

    @SuppressWarnings("unchecked")
    public CustomHashMap(int tableSize) {
        this.tableSize = tableSize;
        this.table = (HashNode<V>[]) new HashNode[tableSize];
        this.size = 0;
    }

    /**
     * Standard hash function mapping the string to a valid array index.
     * Uses Math.abs to prevent negative indices.
     */
    private int hashFunction(String key) {
        return Math.abs(key.hashCode() % tableSize);
    }

    /**
     * Inserts or updates a value. If the key already exists, the value is
     * overwritten and size is unchanged.
     */
    public void put(String key, V value) {
        if (key == null) return;
        int index = hashFunction(key);
        HashNode<V> current = table[index];

        // Update path: key already in chain
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Insert path: prepend new node to the chain
        HashNode<V> newNode = new HashNode<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    /**
     * Retrieves a value in O(1) average time.
     */
    public V get(String key) {
        if (key == null) return null;
        int index = hashFunction(key);
        HashNode<V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Removes and returns the value for the given key, or null if absent.
     */
    public V remove(String key) {
        if (key == null) return null;
        int index = hashFunction(key);
        HashNode<V> current = table[index];
        HashNode<V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    /** Number of entries currently in the map. O(1). */
    public int size() {
        return size;
    }

    /** Returns every key currently in the map. Used for iteration (e.g. peak-hour search). */
    public String[] keys() {
        String[] result = new String[size];
        int idx = 0;
        for (int i = 0; i < tableSize; i++) {
            HashNode<V> current = table[i];
            while (current != null) {
                result[idx++] = current.key;
                current = current.next;
            }
        }
        return result;
    }
}