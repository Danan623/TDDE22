package lab1;

/**
 * This class represents a symbol table, or hash table, with a very simple hash
 * function. Observe that you are not supposed to change hash function. Ensure
 * that you use linear probing as your method of collision handling.
 *
 * @author Magnus Nielsen, Tommy Färnqvist ...
 */
public class SymbolTable {
	private static final int INIT_CAPACITY = 7;

	/* Number of key-value pairs in the symbol table */
	private int size;
	/* Size of linear probing table */
	private int maxSize;
	/* The keys */
	private String[] keys;
	/* The values */
	private Character[] vals;

	private int indexKey;

	private boolean isFull = false;

	private int count;

	/**
	 * Create an empty hash table - use 7 as default size
	 */
	public SymbolTable() {
		this(INIT_CAPACITY);
	}

	/**
	 * Create linear probing hash table of given capacity
	 */
	public SymbolTable(int capacity) {
		size = 0;
		maxSize = capacity;
		keys = new String[maxSize];
		vals = new Character[maxSize];
	}

	/**
	 * Return the number of key-value pairs in the symbol table
	 */
	public int size() {
		return size;
	}

	/**
	 * Is the symbol table empty?
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Does a key-value pair with the given key exist in the symbol table?
	 */
	public boolean contains(String key) {
		return get(key) != null;
	}

	/**
	 * Hash function for keys - returns value between 0 and maxSize-1
	 */
	public int hash(String key) {
		int i;
		int v = 0;

		for (i = 0; i < key.length(); i++) {
			v += key.charAt(i);
		}
		return v % maxSize;
	}

	/**
	 * Insert the key-value pair into the symbol table.
	 */
	public void put(String key, Character val) {
		/* Convert to a unique indexing key */

		int counter = 0;
		/* Check if empty or full */
		if (key == null) {
			return;
		}
		if (val == null) {
			delete(key);
			return;
		}
		indexKey = hash(key);

		while (keys[count] != null && !isFull) {
			count++;

			if (count == maxSize - 1) { // only if array is full
				isFull = true;
			}
		}

		if (!isFull || keys[indexKey] != key) {
			/* Check if empty position and if deleted position */
			while (keys[indexKey] != null && counter < maxSize) {
				if (keys[indexKey].contains(key)) {
					keys[indexKey] = key;
					vals[indexKey] = val; // update new val
					break;
				}
				indexKey++;
				counter++;
				/* Overbound? */
				if (indexKey == maxSize) {
					indexKey = indexKey % maxSize;
				}
			}
			/* if empty postition exists and array not full */
			if ((keys[indexKey] != key && !isFull) || keys[indexKey] == null) { // || isEMpty
				keys[indexKey] = key;
				vals[indexKey] = val;
				size += 1;
			}
		}
		return;
	}

	/**
	 * Return the value associated with the given key, null if no such value.
	 */
	public Character get(String key) {
		if (key != null) {
			indexKey = hash(key);
			for (int i = 0; i < maxSize; i++) {

				if (keys[(indexKey + i) % maxSize] != null && keys[(indexKey + i) % maxSize].equals(key)) {
					return vals[(indexKey + i) % maxSize];
				}
			}
		}
		return null;
	}
	/** 
	 * @return key-index in array. If key == null -> return -1. 
	 * 
	 * */
	private int getPos(String key) {

		if (key != null) {
			indexKey = hash(key);

			for (int i = 0; i < maxSize; i++) {

				if (keys[(indexKey + i) % maxSize] != null && keys[(indexKey + i) % maxSize].contains(key)) {

					return (indexKey + i) % maxSize; 
				}
			}
		}
		return -1;
	}

	/**
	 * Delete the key (and associated value) from the symbol table.
	 */

	public void delete(String key) { 
		if (key == null) {
			return;
		}
		/* create hashkey */
		int indexKey = hash(key);
		/* check if position matches hashKey = null */
		if (keys[indexKey] == null) {
			return;
		}

		int count = 1;
		String tmpS;
		char tmpC;
		int keyPos = getPos(key); 
		if (keyPos == -1) {
			return;
		}
		vals[keyPos] = null; // delete
		keys[keyPos] = null;
		size--;

		// iterates through the array starting at index after deleted value. 
		//	until we encounter null again.
		for (int i = (keyPos + 1) % maxSize; count < maxSize; i = (i + 1) % maxSize) { 
			count++;

			if (keys[i] == null && vals[i] == null) {
				return;
			}
			tmpS = keys[i]; // save
			tmpC = vals[i];

			keys[i] = null; // delete
			vals[i] = null;
			size--;
			put(tmpS, tmpC); // put back
		}
	}

	/**
	 * Print the contents of the symbol table.
	 */
	public void dump() {
		String str = "";

		for (int i = 0; i < maxSize; i++) {
			str = str + i + ". " + vals[i];
			if (keys[i] != null) {
				str = str + " " + keys[i] + " (";
				str = str + hash(keys[i]) + ")";
			} else {
				str = str + " -";
			}
			System.out.println(str);
			str = "";
		}
	}
}
