
public class MyHashMap<Key, Value> {

    // Entry class to store key-value pairs
    private static class Entry<Key, Value> {
        Key key;
        Value value;

        Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }


    private static final int DEFAULT_CAPACITY = 23; // Initial capacity of the hashmap
    private static final float LOAD_FACTOR = 1; // Load factor threshold for resizing

    private MyLinkedList<Entry<Key, Value>>[] table; // Hash table storing linked lists of entries
    private int size;  // Current number of key-value pairs in the hashmap

    // Constructor
    public MyHashMap() {
        table = new MyLinkedList[DEFAULT_CAPACITY];
        size = 0;
    }


    // Hash function to calculate the index for a given key
    private int hashIndex(Key key) {
        if (key == null) {
            return 0; // Null keys are hashed to index 0
        }

        String keyString = key.toString(); // Convert key to string for hashing
        int k = 0;

        // Calculate hash value by iterating over the string's characters
        for (int i = 0; i < keyString.length(); i++) {
            k = 17 * k + keyString.charAt(i); // Multiply by a prime number for uniqueness
            k = Math.abs(k); // Ensure the hash value is non-negative
        }

        // Return the index within the bounds of the table size
        return k % table.length;
    }


    // Add a key-value pair to the hashmap
    public void addtoHash(Key key, Value value) {
        int index = hashIndex(key);

        // If the bucket at the calculated index is empty, initialize it
        if (table[index] == null) {
            table[index] = new MyLinkedList<>();
        }

        // Check if the key already exists in the bucket and update its value
        for (int i = 0; i < table[index].size(); i++) {
            Entry<Key, Value> entry = table[index].get(i);
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // Add a new entry to the bucket if the key does not already exist
        table[index].add(new Entry<>(key, value));
        size++;

        // Check the load factor and resize the table if necessary
        if ((float) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }


    // returns the value from the given key.
    public Value getValueFromKey(Key key) {
        int index = hashIndex(key);

        // If the bucket at the calculated index is empty, return null
        if (table[index] == null) {
            return null;
        }

        // Search for the key in the bucket and return its associated value
        for (int i = 0; i < table[index].size(); i++) {
            Entry<Key, Value> entry = table[index].get(i);
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        // if the key couldn't found return null
        return null;
    }

    // Check if the hash map contains a given key
    public boolean containsKey(Key key) {
        int index = hashIndex(key);

        // If the bucket at the calculated index is empty, return false
        if (table[index] == null) {
            return false;
        }

        // Search for the key in the bucket
        for (int i = 0; i < table[index].size(); i++) {
            Entry<Key, Value> entry = table[index].get(i);
            if (entry.key.equals(key)) {
                return true; // Return true if the key is found.
            }
        }

        return false; // Return false if the key is not found.
    }

    // Resize the hash table when the load factor exceeds the threshold.
    private void resize() {
        MyLinkedList<Entry<Key, Value>>[] oldTable = table; // storing the old table.
        table = new MyLinkedList[oldTable.length * 2]; // Double the size of the table
        size = 0; // reset the size.

        // Pass all key-value pairs to the new table from the old table.
        for (MyLinkedList<Entry<Key, Value>> bucket : oldTable) {
            if (bucket != null) {
                for (int i = 0; i < bucket.size(); i++) {
                    Entry<Key, Value> entry = bucket.get(i);
                    addtoHash(entry.key, entry.value);
                }
            }
        }
    }
}