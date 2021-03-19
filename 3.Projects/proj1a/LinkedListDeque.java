/** double end Queue.
 * @author Andrew Zhu.
 * */

public class LinkedListDeque<T> {

    /** sentinel: node provides convenience while building double end Queue.
     * previous reference points to last node in the list
     * and next reference points to the first node in the list,
     * and the data will be null.
     * if the deque is empty, prev and next point to itself.
     * */
    private Node sentinel;

    /** size of list.*/
    private int size;

    /** each Node instance is a part of LinkedListDeque (or double end Queue).*/
    private class Node {

        /**prev: reference points to previous Node instance.*/
        private Node prev;

        /**next: reference points to next Node instance.*/
        private Node next;

        /**data: data store in this particular Node instance. */
        private T data;

        /** Constructor with passing arguments. */
        Node(T d, Node p, Node n) {
            this.data = d;
            this.prev = p;
            this.next = n;
        }

        /** Constructor without passing arguments. */
        Node() {
        }
    }

    /** Create an empty linked list deque. */
    public LinkedListDeque() {
        this.sentinel = new Node();
        this.sentinel.next = sentinel;
        this.sentinel.prev = sentinel;
        this.sentinel.data = null;
        this.size = 0;
    }

    /** Adds an item of Type T to the front of the deque.*/
    public void addFirst(T d) {
        Node node = new Node(d, sentinel, null);
        if (isEmpty()) {
            sentinel.next = node;
            sentinel.prev = node;
            node.next = sentinel;
        }  else {
            Node temp = sentinel.next;
            node.next = temp;
            temp.prev = node;
            sentinel.next = node;
        }
        size += 1;

    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T d) {
        Node node = new Node(d, null, sentinel);
        if (isEmpty()) {
            sentinel.next = node;
            sentinel.prev = node;
            node.prev = sentinel;
        } else {
            Node temp = sentinel.prev;
            temp.next = node;
            node.prev = temp;
            sentinel.prev = node;
        }
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return sentinel.next.equals(sentinel);
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        String x = "";
        Node n = sentinel.next;
        while (!n.equals(sentinel)) {
            x += n.data.toString() + " ";
            n = n.next;
        }
        System.out.println(x);
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T res = sentinel.next.data;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return res;
    }

    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T res = sentinel.prev.data;
        Node last = sentinel.prev;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return res;
    }

    /**Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists,
     * returns null. Must not alter the deque!*/
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node n = sentinel;
        for (int i = 0; i <= index; i++) {
            n = n.next;
        }
        return n.data;
    }

    /**Same as get, but uses recursion.*/
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getT(index, sentinel.next);
    }

    /** Helper method for getRecursive. */
    private T getT(int index, Node n) {
        if (index == 0) {
            return n.data;
        } else {
            return getT(--index, n.next);
        }
    }
}

