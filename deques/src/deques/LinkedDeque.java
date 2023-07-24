package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    // initializes an empty deque
    public LinkedDeque() {
        size = 0;
        front = new Node<>(null);
        back = new Node<>(null, front, null);
        front.next = back;
    }

    // adds item to the front of the deque
    public void addFirst(T item) {
        size += 1;
        Node<T> newItem = new Node<>(item, front, front.next);
        front.next.prev = newItem;
        front.next = newItem;
    }

    // adds item to the end of the deque
    public void addLast(T item) {
        size += 1;
        Node<T> newItem = new Node<>(item, back.prev, back);
        back.prev.next = newItem;
        back.prev = newItem;
    }

    // removes element at the front of the deque,
    // returns value stored in the removed element.
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> item = front.next;
        front.next = item.next;
        item.next.prev = front;
        item.prev = null;
        item.next = null;
        return item.value;
    }

    // removes element at the end of the deque
    // returns value stored in the removed element
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> item = back.prev;
        back.prev = item.prev;
        item.prev.next = back;
        item.prev = null;
        item.next = null;
        return item.value;
    }

    // returns the value at given index
    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> curr;
        if (index <= size / 2) {
            curr = front.next;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        } else {
            curr = back.prev;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
        }
        return curr.value;
    }

    // returns current size of the deque
    public int size() {
        return size;
    }
}
