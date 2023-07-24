package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    private int size = 0;
    // array
    // size

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }


    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */



    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        K currKey;
        for (int i = 0; i < size; i++) {
            currKey = entries[i].getKey();
            if (currKey == null && key == null) {
                return entries[i].getValue();
            } else if (currKey != null && currKey.equals(key)) {
                return entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        V oldVal = this.remove(key);
        if (size == entries.length) {
            resize();
        }
        entries[size] = new SimpleEntry<>(key, value);
        size++;
        return oldVal;
    } // tentatively complete


    // tentative resizing function
    private void resize() {
        SimpleEntry<K, V>[] newArray =
            createArrayOfEntries(entries.length * 2);
        for (int i = 0; i < size; i++) {
            newArray[i] = entries[i];
        }
        entries = newArray;
    }

    @Override
    public V remove(Object key) {
        // in theory the following `if statement` should work for both cases:
        // if ((key == null && entries[i].getKey() == null)
        //      || (key != null && entries[i].getKey().equals(key)))
        K currKey;
        for (int i = 0; i < size; i++) {
            currKey = entries[i].getKey();
            if (currKey == null && key == null) {
                V oldVal = entries[i].getValue();
                entries[i] = entries[size - 1];
                size--;
                return oldVal;
            } else if (currKey != null && currKey.equals(key)) {
                V oldVal = entries[i].getValue();
                entries[i] = entries[size - 1];
                size--;
                return oldVal;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        K currKey;
        for (int i = 0; i < size; i++) {
            currKey = entries[i].getKey();
            if (currKey == null && key == null) {
                return true;
            } else if (currKey != null && currKey.equals(key)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries, size);
    }

    /*
    Doing so will give you a better string representation for assertion errors the debugger.
    @Override
    public String toString() {
        return super.toString();
    } */

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        private int position;
        private final int size;
        // You may add more fields and constructor parameters

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int mapSize) {
            this.entries = entries;
            this.size = mapSize;
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            position++;
            return entries[position - 1];
        }
    }
}
