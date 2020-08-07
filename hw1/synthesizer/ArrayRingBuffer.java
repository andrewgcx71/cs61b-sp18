package synthesizer;
import java.util.Iterator;
/**ArrayRingBuff, Arraylist without resizing.
 * @authur
 * */
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {

    /** Index for the next dequeue or peek. */
    private int first;

    /** Index for the next enqueue. */
    private int last;

    /** Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[]) new Object[capacity];
    }


    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isEmpty()) {
            rb[first] = x;
            fillCount++;
        } else if (!isFull()) {
            if (last + 1 == capacity) {
                last = 0;
                rb[last] = x;
            } else {
                rb[++last] = x;
            }
            fillCount++;
        } else {
            throw new RuntimeException("Ring buffer overflow");
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (!isEmpty()) {
            T res = rb[first];
            rb[first] = null;
            fillCount--;
            if (fillCount == 0) {
                first = 0;
                last = 0;
            } else {
                if (first + 1 == capacity) {
                    first = 0;
                } else {
                    first++;
                }
            }
            return res;
        } else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (!this.isEmpty()) {
            return rb[first];
        } else {
            throw new RuntimeException("Ring Buffer Underflow");
        }
    }

    @Override
    /** Implement enhenced for loop.*/
    public Iterator<T> iterator() {
        return new EnhancedForLoop();
    }

    /** A inner class that implements iterator interface.*/
    private class EnhancedForLoop implements Iterator<T> {

        /** keep track of how many item has been iterated.*/
        private int counter = 0;

        /** keep track the rb array by its index position.*/
        private int index = 0;

        @Override
        /** hasNext(). */
        public boolean hasNext() {
            return counter < fillCount;
        }

        @Override
        /** next(). */
        public T next() {
            counter++;
            int next = index + 1;
            if (next == capacity) {
                index = 0;
                return rb[next - 1];
            } else {
                return rb[index++];
            }
        }

    }
}
