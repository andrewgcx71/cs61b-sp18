/** Implement list use Array.
 * @author andrew */
public class ArrayDeque<T> {

    /** size of list.*/
    private int size;

    /** our Arrays. */
    private T[] items;

    /** index position of next first item in the Array. */
    private int nextFirst;

    /** index position of next last item in the Array. */
    private int nextLast;

    /** Creates an empty array deque. */
    public ArrayDeque() {
        this.size = 0;
        this.items = (T[]) new Object[8];
        this.nextFirst = 3;
        this.nextLast = 4;
        this.size = 0;
    }

    /** Resizing
     * either decrease the size of Array due to usage factor of the Array is lower than 25%
     * or increase the size of Array because it's full.
     * */
    private void resizing() {
        int currentSize = size;
        int newSize;
        if (currentSize == items.length) {
            newSize = currentSize * 2;
        } else {
            newSize = items.length / 2;
        }
        T[] temp = (T[]) new Object[newSize];
        int start = nextFirst + 1;
        int end = nextLast - 1;
        if (start == items.length) {
            start = 0;
        }
        if (end == -1) {
            end = items.length - 1;
        }

        int startTemp = newSize / 4;
        if (start <= end) {
            System.arraycopy(items, start, temp, startTemp, size);
        } else {
            System.arraycopy(items, start, temp, startTemp, (items.length - start));
            int a = startTemp + (items.length - start);
            int b = size - (items.length - start);
            System.arraycopy(items, 0, temp, a, b);
        }
        nextFirst = startTemp - 1;
        nextLast = nextFirst + size + 1;
        items = temp;
    }


    /** Adds an item of Type T to the front of the deque.*/
    public void addFirst(T d) {
        if (size == items.length) {
            resizing();
        }
        items[nextFirst] = d;
        nextFirst--;
        if (nextFirst == -1) {
            nextFirst = items.length - 1;
        }
        size++;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T d) {
        if (size == items.length) {
            resizing();
        }
        items[nextLast] = d;
        nextLast++;
        if (nextLast == items.length) {
            nextLast = 0;
        }
        size++;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        String x = "";
        int start = nextFirst + 1;
        int end = nextLast - 1;
        if (start == items.length) {
            start = 0;
        }
        if (end == -1) {
            end = items.length - 1;
        }
        if (start <= end) {
            for (int i = start; i <= end; i++) {
                x += items[i].toString() + " ";
            }
        } else {
            for (int i = start; i < items.length; i++) {
                x += items[i].toString() + " ";
            }
            for (int i = 0;  i < size - (items.length - start); i++) {
                x += items[i].toString() + " ";
            }
        }
        System.out.println(x);
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if ((size - 1) * 1.0 / items.length < 0.25 && items.length >= 16) {
            resizing();
        }
        nextFirst++;
        if (nextFirst == items.length) {
            nextFirst = 0;
        }
        size--;
        T res = items[nextFirst];
        items[nextFirst] = null;
        return res;
    }

    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if ((size - 1) * 1.0 / items.length < 0.25 && items.length >= 16) {
            resizing();
        }
        nextLast--;
        if (nextLast == -1) {
            nextLast = items.length - 1;
        }
        size--;
        T res = items[nextLast];
        items[nextLast] = null;
        return res;
    }

    /**Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists,
     * returns null. Must not alter the deque!*/
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int start = nextFirst + 1;
        int end = nextLast - 1;
        if (start == items.length) {
            start = 0;
        }
        if (end == -1) {
            end = items.length - 1;
        }
        if (start + index <= items.length - 1) {
            return items[start + index];
        } else {
            return items[start + index - (items.length - 1) - 1];
        }
    }
}
