import java.util.ArrayList;

public class HashTable<K,V> {
	private TableEntrys<K, V>[] hashTable; 
	private int numberOfEntries;
	private int locationsUsed; 
	private static final int DEFAULT_SIZE = 2500; 
	private static final double MAX_LOAD_FACTOR = 0.5;
	private int loadF = 0;
	private static int hashF = 0;
	private int collisionH = 0;
	private int countingCollisions = 0;
	public HashTable() {
		this(DEFAULT_SIZE); 
	}
	
	@SuppressWarnings("unchecked")
	public HashTable(int tableSize) {
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntrys[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}                                      //HashFunction: SSF or PAF //collisionHandling LP or DH
	public HashTable(double loadFactor, String hashFunction, String collisionHandling) {
		int primeSize = getNextPrime(DEFAULT_SIZE);
		hashTable = new TableEntrys[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
		if(loadFactor == 0.5) {
			loadF = 1;
		}
		else if(loadFactor == 0.8){
			loadF = 0;
		}
		if(hashFunction == "SSF") {
			hashF = 1;
		}
		else if(hashFunction == "PAF") {
			hashF = 0;
		}
		if(collisionHandling == "LP") {
			collisionH = 1;
		}
		else if(collisionHandling == "DH") {
			collisionH = 0;
		}
	}
	
	
	private boolean isPrime(int num) {
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}
		
	public TableEntrys<K,V> getTableEntry(int index){
		return hashTable[index];
	}
	private int getNextPrime(int num) {
		if (num <= 1)
            return 2;
		else if(isPrime(num))
			return num;
        boolean found = false;   
        while (!found)
        {
            num++;     
            if (isPrime(num))
                found = true;
        }     
        return num;
	}
	
	public int findIndex(K key) {
		int index = 0;
		if(hashF == 1) {
			index = SimpleSummationFunction(key);
		}
		else if(hashF == 0) {
			index = PolynomialAccumulationFunction(key);
		}
		index = probe(index, key);
		return index;
	}
		
	public TableEntrys<K,V> add(K key, V value) {
		TableEntrys<K,V> oldValue;
		if (isHashTableTooFull())
			rehash();
		int index = 0;
		if(hashF == 1) {
			index = SimpleSummationFunction(key);
		}
		else if(hashF == 0) {
			index = PolynomialAccumulationFunction(key);
		}
		
		index = probe(index, key);
		if(hashTable[index] == null) {
			hashTable[index] = new TableEntrys<K,V>(key,value);
			numberOfEntries++;
			locationsUsed++;
			oldValue = null;
		}
		else {
			oldValue = hashTable[index];
			hashTable[index] = new TableEntrys<K,V>(key,value);
		}
		return oldValue;
	}
	
	public int probe(int index, K key) {
		boolean found = false;		
		int x;
		if (collisionH==1) {//LP
			x = 1;
		}
		else //DH
			x = 7 - index % 7;
		while(!found && (hashTable[index] != null)) {			
			if(key.equals(hashTable[index].getKey()))
				found = true;
			else {
				countingCollisions++;
				index = (index + x) % hashTable.length;
				if(hashTable[index] != null) {
					if(key.equals(hashTable[index].getKey())) {
						found = true;
					}
				}
			}
								
		}
		return index;
	}
	public int getnumberOfCollisions() {
		return countingCollisions;
	}
	
	private int SimpleSummationFunction(K key) {
		int sum = 0;
		for(int i = 0; i < key.toString().length(); i++) {
			sum += (int)(key.toString().charAt(i));
		}
		int index = sum % hashTable.length;
		if(index < 0)
			index += hashTable.length;
		return index;
	}
	
	private int PolynomialAccumulationFunction(K key)
	{
		int sum = 0;
		for(int i = 0; i < key.toString().length();i++) {
			sum += ((int)key.toString().charAt(i)) * Math.pow(31, key.toString().length()-i);
		}
		int index = sum % hashTable.length;
		if(index < 0)
			index += hashTable.length;
		return index;
	}
	
	public boolean isHashTableTooFull() {
		double load_factor = (double)locationsUsed / (double)getSize();
		
		if(loadF == 1) {
			if (load_factor >= 0.5)
				return true;
		}
		else if(loadF == 0) {
			if (load_factor >= 0.8)
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void rehash() {
		TableEntrys<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntrys[newSize]; 
		numberOfEntries = 0; 
		locationsUsed = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if (oldTable[index] != null)
				add(oldTable[index].getKey(), oldTable[index].getValue());
		}
	}
	
	public int getSize() {
		return hashTable.length;
	}
	
	public boolean isEmpty() {
		return numberOfEntries == 0;
	}
	
}

