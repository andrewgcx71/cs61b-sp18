// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import synthesizer.AbstractBoundedQueue;

import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[])new Object[capacity];
    }


    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
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
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
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
            } return res;
        } else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        if (!this.isEmpty()) {
            return rb[first];
        } else {
            throw new RuntimeException("Ring Buffer Underflow");
        }
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
    /** Implement enhenced for loop.*/
    @Override
    public Iterator<T> iterator() {
        return new EnhancedForLoop();
    }

    /** A inner class that implements iterator interface.*/
    private class EnhancedForLoop implements Iterator<T> {
        /** keep track of how many item has been iterated.*/
        private int counter = 0;
        /** keep track the rb array by its index position*/
        private int index = 0;
        /** hasNext(). */
        @Override
        public boolean hasNext() {
            return counter < fillCount;
        }

        /** next(). */
        @Override
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
