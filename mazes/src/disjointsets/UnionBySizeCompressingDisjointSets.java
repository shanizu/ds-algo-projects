package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private final HashMap<T, Integer> ids;
    private int size;
    private int numSets;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        ids = new HashMap<>();
        size = 0;
    }

    @Override
    public void makeSet(T item) {
        ids.put(item, size);
        pointers.add(-1);
        size++;
    }

    @Override
    public int findSet(T item) {
        Integer index = ids.get(item);
        Integer current = index;
        if (index == null) {
            throw new IllegalArgumentException(item + " is not in any set.");
        }
        while (pointers.get(index) >= 0) {
            index = pointers.get(index);
        } // index now contains to root index/location
        Integer next;
        while (pointers.get(current) >= 0) {
            next = pointers.get(current);
            pointers.set(current, index);
            current = next;
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        if (root1 == root2) {
            return false;
        }
        int size1 = pointers.get(root1);
        int size2 = pointers.get(root2);
        if (size2 >= size1) {
            pointers.set(root2, root1);  // sets root2 to point at root1
            pointers.set(root1, size1 + size2);
        } else {
            pointers.set(root1, root2);  // sets root1 to point at root2
            pointers.set(root2, size1 + size2);
        }
        return true;
    }

    private void compressPath1(T item, int newRoot, int oldRootSize) {
        Integer index = ids.get(item);
        Integer nextIndex = 0;
        while (pointers.get(index) >= 0) {
            nextIndex = pointers.get(index);
            pointers.set(index, newRoot);
            index = nextIndex;

        }
        pointers.set(index, newRoot);
        pointers.set(newRoot, pointers.get(newRoot) + oldRootSize);
    }
}
