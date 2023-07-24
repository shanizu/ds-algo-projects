package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;
    private double resizingLoadFactorThreshold;

    private int chainCount;
    private int chainInitialCapacity;

    private int size;
    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
        this.chainInitialCapacity = chainInitialCapacity;
        this.chainCount = initialChainCount;
        this.size = 0;
        this.chains = createArrayOfChains(chainCount);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int chainIndex = getChainFor(key, chainCount);
        if (this.chains[chainIndex] == null) {
            return null;
        }
        return this.chains[chainIndex].get(key);
    }

    @Override
    public V put(K key, V value) {
        V oldVal = this.remove(key); // remove key
        if (needsResize()) {
            resize();
        }
        int chainIndex = getChainFor(key, chainCount);
        if (chains[chainIndex] == null) {
            chains[chainIndex] = createChain(chainInitialCapacity);
        }
        this.chains[chainIndex].put(key, value);
        size++;
        return oldVal;
    }

    // this method determines whether the array of chains needs to be
    // resized; returns the result as boolean (based on loadFactorThreshold)
    private boolean needsResize() {
        return ((size * 1.0) / chainCount >= resizingLoadFactorThreshold);
    }

    // this method resizes the map such that the array of chain is double in size;
    // previously stored entries are rehashed and replaced into the newly sized map,
    // and the chainCount is updated to represent the new size.
    private void resize() {
        AbstractIterableMap<K, V>[] newMap = createArrayOfChains(2 * chainCount);
        for (int i = 0; i < chainCount; i++) {
            if (this.chains[i] != null) {
                for (Entry<K, V> curr : chains[i]) {
                    K key = curr.getKey();
                    int newIndex = getChainFor(key, newMap.length);
                    if (newMap[newIndex] == null) {
                        newMap[newIndex] = createChain(chainInitialCapacity);
                    }
                    newMap[newIndex].put(key, curr.getValue());
                }
            }
        }
        this.chains = newMap;
        this.chainCount = newMap.length;
    }


    // given the object `key` and size of the array of chains as an int,
    // this method returns the determined position/index for the object
    // as an int.
    private int getChainFor(Object key, int arraySize) {
        int code;
        if (key == null) {
            code = 0;
        } else {
            code = key.hashCode();
        }
        if (code < 0) {
            code *= -1;
        }
        return code % arraySize;
    }

    @Override
    public V remove(Object key) {
        if (this.containsKey(key)) {
            size--;
            int index = getChainFor(key, chainCount);
            V value = chains[index].remove(key);
            // if chain is empty, make chain slot null
            if (chains[index].size() == 0) {
                chains[index] = null;
            }
            return value;
        }
        return null;
    }

    @Override
    public void clear() {
        this.chains = createArrayOfChains(chainCount);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int chainIndex = getChainFor(key, chainCount);
        if (chains[chainIndex] == null) {
            return false;
        }
        return chains[chainIndex].containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains, this.size);
    }

    // Doing so will give you a better string representation for assertion errors the debugger.
    @Override
    public String toString() {
        return super.toString();
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private Iterator<Map.Entry<K, V>> currIterator;
        private int chainPosition;
        private int size;
        private int count;


        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains, int numEntries) {
            this.chains = chains;
            this.size = numEntries;
            this.chainPosition = -1;
            this.count = 0;
            if (hasNext()) {
                findNextChain();
                currIterator = chains[chainPosition].iterator();
            }
            // System.out.println("New ChainedHashMap iterator has been initialized for " + this.chains);
        }

        private void findNextChain() {
            chainPosition++;
            while (chainPosition < chains.length && chains[chainPosition] == null) {
                chainPosition++;
                // System.out.println("CHAIN POSITION WAS INCREMENTED, NEW INDEX = " + chainPosition);
            }
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            System.out.println("<next() has been called>");
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (!currIterator.hasNext()) {
                // System.out.println("  <Curent chain iter hasNext() returned FALSE>");
                findNextChain();
                currIterator = chains[chainPosition].iterator();
                // System.out.println("  New iterator has been initialized for index #" + chainPosition);
            }
            count++;
            Map.Entry<K, V> result = currIterator.next();
            // System.out.println("    Iterator: entry #" + (count - 1) + " yields " + result);
            return result;
        }
    }
}
