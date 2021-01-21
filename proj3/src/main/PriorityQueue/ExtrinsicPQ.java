// lab 10

public interface ExtrinsicPQ<T> {
    void insert(T var1, double var2);

    T peek();

    //boolean contain(T t);

    T removeMin();

    void changePriority(T var1, double var2);

    int size();
}