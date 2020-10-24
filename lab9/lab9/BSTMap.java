package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author az
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {

//    public static void main(String[] args) {
//        BSTMap<Integer, String> map = new BSTMap<>();
//        map.put(3,"a");
//        map.put(9,"a");
//        map.put(5,"a");
//        map.put(4,"a");
//        map.put(2,"a");
//        map.put(7,"a");
//        map.put(8,"a");
//        map.put(14,"a");
//        map.put(11,"a");
//        map.put(12,"a");
//        map.put(6,"a");
//        map.remove(9);
//        for(int n: map) {
//            System.out.println(n);
//        }
//
//    }
    public class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    public Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private Set<K> set = new HashSet<>();

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if(p == null) {
            return null;
        }
        int cmp = p.key.compareTo(key);
        if(cmp > 0) {
            return getHelper(key, p.left);
        } else if(cmp < 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
        //throw new UnsupportedOperationException();
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
        //throw new UnsupportedOperationException();
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     * When you add a node to tree recurrsively, you have to return newly added node back to
     * its parent so its parent can point to it.
     */
    private Node putHelper(K key, V value, Node p) {
        if(p == null) {
            p = new Node(key, value);
            size++;
            set.add(key);
            return p;
        }
        if (p.key.compareTo(key) < 0) {
            p.right = putHelper(key, value, p.right);
        } else if(p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.value = value;
        }
        return p;
        //throw new UnsupportedOperationException();
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if(root == null) {
            size++;
            set.add(key);
            root = new Node(key, value);
        }
        putHelper(key, value, root);
        //throw new UnsupportedOperationException();
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
        //throw new UnsupportedOperationException();
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return set;
        //throw new UnsupportedOperationException();
    }


    /** Get the left most leaf of current node. if node doesn't have a leave, return null immediately.
     */


    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if(!containsKey(key)) {
            return null;
        }
        Node parent = Parent(key, root);
        Node abandonedSon = (isLeftChild(parent, key)) ? parent.left : parent.right;
        Node newSon = getSmallestNode(abandonedSon);
        if(parent.equals(abandonedSon) && newSon != null) {
            root = newSon;
            newSon.left = abandonedSon.left;
            newSon.right = abandonedSon.right;
        }
        if(isLeftChild(parent, key) && newSon != null) {
            parent.left = newSon;
            newSon.left = abandonedSon.left;
            newSon.right = abandonedSon.right;
        }
        if(isRightChild(parent, key) && newSon != null) {
            parent.right = newSon;
            newSon.left = abandonedSon.left;
            newSon.right = abandonedSon.right;
        }
        if(isLeftChild(parent, key) && newSon == null) {
            parent.left = abandonedSon.left;
        }
        if(isRightChild(parent, key) && newSon == null) {
            parent.right = abandonedSon.left;
        }
        set.remove(key);
        size--;
        return abandonedSon.value;

        //throw new UnsupportedOperationException();
    }


    // return true if is left child, otherwise false.
    private boolean isLeftChild(Node parent, K key) {
        if(parent.left == null) {
            return false;
        }
        return parent.left.key.equals(key);
    }

    // return true if is right child, otherwise false.
    private boolean isRightChild(Node parent, K key) {
        if(parent.right == null) {
            return false;
        }
        return parent.right.key.equals(key);
    }

    // Return the parent of key, return null if no match.
    private Node helperParent(K key, Node p, Node parent) {
        if(p == null) {
            return null;
        }
        int cmp = p.key.compareTo(key);
        if(cmp > 0) {
            return helperParent(key,p.left, p);
        } else if(cmp < 0) {
            return helperParent(key, p.right, p);
        } else {
            return parent;
        }
        //throw new UnsupportedOperationException();
    }

    // Return the parent of key, return null if no match.
    private Node Parent(K key, Node p) {
        return helperParent(key, p, p);
        //throw new UnsupportedOperationException();
    }

    // Return the current node's smallest right child, return null if no right children.
    private Node getSmallestNode(Node node){
        if(node.right == null) {
            return null;
        }
        if(node.right.left == null) {
            Node temp = node.right;
            node.right = node.right.right;
            return temp;
        }
        return helperGetSmallestNode(node.right);
    }

    // Return the current node's smallest child,. This method assume the initial node.left is not null.
    private Node helperGetSmallestNode(Node node){
        if(node.left.left == null) {
            Node temp = node.left;
            node.left = node.left.right;
            return temp;
        }
        return helperGetSmallestNode(node.left);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if(!get(key).equals(value)) {
            return null;
        } else{
            return remove(key);
        }
        //throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return set.iterator();
        //throw new UnsupportedOperationException();
    }

}
