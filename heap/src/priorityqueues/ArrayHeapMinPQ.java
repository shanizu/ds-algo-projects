package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;

    List<PriorityNode<T>> items;
    HashMap<T, Integer> indices;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        indices = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) { // all O(1)
        PriorityNode<T> temp = items.get(a);
        indices.put(temp.getItem(), b);
        indices.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    private void bubbleUp(int index) {
        if (parentOf(index) >= 0 &&
            items.get(parentOf(index)).getPriority() > items.get(index).getPriority()) {
            swap(index, parentOf(index));
            bubbleUp(parentOf(index));
        }
    }

    private void bubbleDown(int index) {
        if (childOf(index) == (items.size() - 1)) {  // only one child exists (reaches end of array)
            if (items.get(childOf(index)).getPriority() < items.get(index).getPriority()) {
                swap(index, childOf(index));
            }
        } else if (childOf(index) < items.size() - 1) { // item at index has 2 children
            int smallChild;
            if (items.get(childOf(index)).getPriority() <= items.get(childOf(index)+1).getPriority()) {
                smallChild = childOf(index);
            } else {
                smallChild = childOf(index) + 1;
            }
            if (items.get(index).getPriority() > items.get(smallChild).getPriority()) {
                swap(index, smallChild);
                bubbleDown(smallChild);
            }
        }
    }

    private int parentOf(int child) {
        return (child - 1) / 2;
    } // O(1)

    private int childOf(int parent) { //returns left child
        return parent * 2 + 1;
    } // O(1)

    @Override
    public void add(T item, double priority) {
        if (indices.containsKey(item)) { // O(1)
            throw new IllegalArgumentException("No duplicates items!");
        }
        PriorityNode<T> newItem = new PriorityNode<>(item, priority); // O(1)
        items.add(newItem); // O(1)
        indices.put(item, items.size() - 1); // O(1)
        bubbleUp(items.size() - 1);
    }

    @Override
    public boolean contains(T item) {
        return indices.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("bozo dis bit empty");
        }
        return items.get(0).getItem();
    }

    @Override
    public T removeMin() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("bozo dis bit empty");
        }
        if (items.size() == 1) {
            return items.remove(0).getItem();
        }
        T result = items.get(0).getItem();
        items.set(0, items.remove(items.size() - 1));
        indices.remove(result);
        indices.put(items.get(0).getItem(), 0);
        bubbleDown(0);
        return result;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!indices.containsKey(item)) {
            throw new NoSuchElementException("L + ratio");
        } else if (indices.size() == 1) {
            int index = 0;
        } else {
            int index = indices.get(item);
            items.get(index).setPriority(priority);
            bubbleDown(index);
            index = indices.get(item);
            bubbleUp(index);
        }
    }

    @Override
    public int size() {
        return items.size();
    }
}
