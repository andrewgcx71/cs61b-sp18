import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        return null;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> res = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            if (!q1.isEmpty() && !q2.isEmpty()) {
                if (q1.peek().compareTo(q2.peek()) < 0) {
                    res.enqueue(q1.dequeue());
                } else {
                    res.enqueue(q2.dequeue());
                }
            } else if (!q1.isEmpty()) {
                res.enqueue(q1.dequeue());
            } else {
                res.enqueue(q2.dequeue());
            }
        }
        return res;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items.size() == 1 || items.size() == 0) {
            return items;
        } else {
            int median = items.size() / 2;
            Queue<Item> q1 = new Queue<>();
            Queue<Item> q2 = new Queue<>();
            int counter = 0;
            for (Item item: items) {
                if (counter <= median - 1) {
                    q1.enqueue(item);
                    counter++;
                } else {
                    q2.enqueue(item);
                }
            }
            q1 = mergeSort(q1);
            q2 = mergeSort(q2);
            return mergeSortedQueues(q1, q2);
        }
    }

    //Test
    @Test
    public void testMergeSort() {
        Queue<Integer> original = new Queue<>();
        original.enqueue(3);
        original.enqueue(98);
        original.enqueue(50);
        original.enqueue(39);
        original.enqueue(85);
        System.out.println("Original: ");
        for (int temp: original) {
            System.out.println(temp + ", ");
        }
        Queue result = MergeSort.mergeSort(original);
        System.out.println("----------------------------");
        System.out.println("Original: ");
        for (int temp: original) {
            System.out.println( temp + ", ");
        }
        System.out.println("----------------------------");
        System.out.println("Result: ");
        for (Object temp: result) {
            System.out.println((Integer) temp + ", ");
        }
    }
}
