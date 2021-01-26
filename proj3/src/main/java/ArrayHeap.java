// Grab from lab 10

public class ArrayHeap<T> implements ExtrinsicPQ<T> {
    private ArrayHeap<T>.Node[] contents = new ArrayHeap.Node[16];
    private int size;
    public static void main(String[] args) {
        ArrayHeap<String> pq = new ArrayHeap();
        pq.insert("a", 3.0D);
        pq.removeMin();

        for(int i = 1; i <= pq.size; ++i) {
            System.out.println(pq.contents[i]);
        }

    }

    public ArrayHeap() {
        this.contents[0] = null;
        this.size = 0;
    }

    private static int leftIndex(int i) {
        return i * 2;
    }

    private static int rightIndex(int i) {
        return i * 2 + 1;
    }

    private static int parentIndex(int i) {
        return i / 2;
    }

    private ArrayHeap<T>.Node getNode(int index) {
        return !this.inBounds(index) ? null : this.contents[index];
    }

    private boolean inBounds(int index) {
        return index <= this.size && index >= 1;
    }

    private void swap(int index1, int index2) {
        ArrayHeap<T>.Node node1 = this.getNode(index1);
        ArrayHeap<T>.Node node2 = this.getNode(index2);
        this.contents[index1] = node2;
        this.contents[index2] = node1;
    }

    private int min(int index1, int index2) {
        ArrayHeap<T>.Node node1 = this.getNode(index1);
        ArrayHeap<T>.Node node2 = this.getNode(index2);
        if (node1 == null) {
            return index2;
        } else if (node2 == null) {
            return index1;
        } else {
            return node1.myPriority < node2.myPriority ? index1 : index2;
        }
    }

    private void swim(int index) {
        this.validateSinkSwimArg(index);

        while(true) {
            int parent = parentIndex(index);
            if (parent == 0 || this.contents[parent].myPriority <= this.contents[index].myPriority) {
                return;
            }

            this.swap(parent, index);
            index = parent;
        }
    }

    private void sink(int index) {
        this.validateSinkSwimArg(index);

        while(true) {
            int left = leftIndex(index);
            int right = rightIndex(index);
            int smallest = left;
            if (this.inBounds(right) && this.contents[left].myPriority > this.contents[right].myPriority) {
                smallest = right;
            }

            if (!this.inBounds(left) || this.contents[index].myPriority < this.contents[smallest].myPriority) {
                return;
            }

            this.swap(smallest, index);
            index = smallest;
        }
    }


    public void insert(T item, double priority) {
        if (this.size + 1 == this.contents.length) {
            this.resize(this.contents.length * 2);
        }

        ++this.size;
        this.contents[this.size] = new ArrayHeap.Node(item, priority);
        this.swim(this.size);
    }

    public T peek() {
        return this.size == 0 ? null : this.contents[1].myItem;
    }

    public T removeMin() {
        if (this.size == 0) {
            return null;
        } else {
            this.swap(1, this.size);
            T res = this.contents[this.size].myItem;
            this.contents[this.size] = null;
            --this.size;
            if (this.size >= 1) {
                this.sink(1);
            }

            return res;
        }
    }

    public int size() {
        return this.size;
    }

    public void changePriority(T item, double priority) {
        if (this.size >= 1 && this.contents[1].myItem.equals(item)) {
            if (priority > this.contents[1].myPriority) {
                this.contents[1].myPriority = priority;
                this.sink(1);
            }

        } else {
            for(int i = 2; i <= this.size; ++i) {
                if (this.contents[i].myItem.equals(item)) {
                    this.contents[i].myPriority = priority;
                    ArrayHeap<T>.Node parent = this.contents[parentIndex(i)];
                    if (priority > parent.myPriority) {
                        this.sink(i);
                    } else {
                        this.swim(i);
                    }
                    break;
                }
            }

        }
    }

    public String toString() {
        return this.toStringHelper(1, "");
    }

    private String toStringHelper(int index, String soFar) {
        if (this.getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = rightIndex(index);
            toReturn = toReturn + this.toStringHelper(rightChild, "        " + soFar);
            if (this.getNode(rightChild) != null) {
                toReturn = toReturn + soFar + "    /";
            }

            toReturn = toReturn + "\n" + soFar + this.getNode(index) + "\n";
            int leftChild = leftIndex(index);
            if (this.getNode(leftChild) != null) {
                toReturn = toReturn + soFar + "    \\";
            }

            toReturn = toReturn + this.toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    private void validateSinkSwimArg(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("Cannot sink or swim nodes with index 0 or less");
        } else if (index > this.size) {
            throw new IllegalArgumentException("Cannot sink or swim nodes with index greater than current size.");
        } else if (this.contents[index] == null) {
            throw new IllegalArgumentException("Cannot sink or swim a null node.");
        }
    }

    private void resize(int capacity) {
        ArrayHeap<T>.Node[] temp = new ArrayHeap.Node[capacity];

        for(int i = 1; i < this.contents.length; ++i) {
            temp[i] = this.contents[i];
        }

        this.contents = temp;
    }

    private class Node {
        private T myItem;
        private double myPriority;

        private Node(T item, double priority) {
            this.myItem = item;
            this.myPriority = priority;
        }

        public T item() {
            return this.myItem;
        }

        public double priority() {
            return this.myPriority;
        }

        public String toString() {
            return this.myItem.toString() + ", " + this.myPriority;
        }
    }
}