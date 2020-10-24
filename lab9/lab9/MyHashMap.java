package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author az
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

        public static void main(String[] args) {
        Map61B<Integer, String> map = new MyHashMap<>();
        map.put(3,"a");
        map.put(9,"a");
        map.put(5,"c");
        map.put(4,"b");
        map.remove(5);
        for(int n: map) {
            System.out.println(n);
            System.out.println(map.get(n));
        }

    }

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private Set<K> set = new TreeSet<>();
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if(size == 0) {
            return null;
        }
        int i = hash(key);
        return buckets[i].get(key);
        //throw new UnsupportedOperationException();
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if(loadFactor() > MAX_LF) {
            resizing();
        }
        if(!containsKey(key)) {
            set.add(key);
            size++;
        }
        int i = hash(key);
        buckets[i].put(key, value);
        //throw new UnsupportedOperationException();
    }

    private void resizing() {
        int nob = buckets.length * 2; // number of buckets
        ArrayMap<K, V>[] newBuckets = new ArrayMap[nob];
        for (int i = 0; i < nob; i++) {
            newBuckets[i] = new ArrayMap<>();
        }
        for (K k : set) {
            int newHash = Math.floorMod(k.hashCode(), nob);
            newBuckets[newHash] = buckets[hash(k)];
        }
        buckets = newBuckets;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
        //throw new UnsupportedOperationException();
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return set;
        //throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if(!containsKey(key)) {
            return null;
        }
        set.remove(key);
        size--;
        return buckets[hash(key)].remove(key);
        //throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if(!get(key).equals(value)) {
            return null;
        }
        return remove(key);
        //throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return set.iterator();
        //throw new UnsupportedOperationException();
    }
}
