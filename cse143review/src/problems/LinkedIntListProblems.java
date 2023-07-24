package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        ListNode temp = list.front;
        list.front = list.front.next.next;
        list.front.next = temp.next;
        list.front.next.next = temp;
        temp.next = null;
    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        if (list.front != null && list.front.next != null) {
            ListNode frontNode = list.front;
            list.front = list.front.next;
            frontNode.next = null;

            ListNode temp = list.front;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = frontNode;
        }
    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        LinkedIntList result;
        ListNode iter;
        ListNode builder;
        if (a.front != null) {
            result = new LinkedIntList(a.front.data);
            builder = result.front;
            iter = a.front.next;
            while (iter != null) {
                builder.next = new ListNode(iter.data);
                iter = iter.next;
                builder = builder.next;
            }
            iter = b.front;
        } else if (b.front != null) {
            result = new LinkedIntList(b.front.data);
            builder = result.front;
            iter = b.front.next;
        } else {
            return new LinkedIntList();
        }
        while (iter != null) {
            builder.next = new ListNode(iter.data);
            iter = iter.next;
            builder = builder.next;
        }
        return result;
    }
}
