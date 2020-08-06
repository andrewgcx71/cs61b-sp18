package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    /** number of items in the list so far.*/
    protected int fillCount;
    /** the capacity of list.*/
    protected int capacity;
    /** return size of the buffer.
     * capacity is the max size of the boundedQueue*/
    @Override
    public int capacity() {
        return capacity;
    }

    /** return number of items currently in the buffer*/
    @Override
    public int fillCount() {
        return fillCount;
    }




}
