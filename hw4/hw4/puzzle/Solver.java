package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import java.util.*;
public class Solver {

    /** minimum number of moves to solve the puzzle starting at initial worldState. */
    private int m = 0;

    /** A sequence of WorldState from the initial WorldState to the solution. */
    private List<WorldState> lst = new ArrayList<>();

    /** for key-value pair, the value is a two elements array,
     * first element represent the number of moves for inintal worldstate to current worldstate,
     * the second element is estimated distance for from current worldstate
     * to goal plus the moves from initial worldstate to current worldstate  */
    private HashMap<WorldState, int[]> hm = new HashMap<>();

    /** store child and parent pair, where the key is child, and value is parent */
    private HashMap<WorldState, WorldState> map= new HashMap<>();


    /**Return 1 if the the first WorldState is bigger than second, return -1 if less than, otherwise return 0. */
    private class CompareWorldState implements Comparator<WorldState> {
        @Override
        public int compare(WorldState w1, WorldState w2) {
            if (hm.get(w1)[1] > hm.get(w2)[1]) {
                return 1;
            } else if (hm.get(w1)[1] < hm.get(w2)[1]) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /** Constructor which solves the puzzle, computing
     everything necessary for moves() and solution() to
     not have to solve the problem again. Solves the
     puzzle using the A* algorithm. Assumes a solution exists.*/
    public Solver(WorldState initial) {
        Solver.CompareWorldState cmp = new Solver.CompareWorldState();
        MinPQ<WorldState> pq = new MinPQ<>(cmp);
        int[] a = {0, initial.estimatedDistanceToGoal()};
        hm.put(initial,a);
        pq.insert(initial);
        while (!pq.isEmpty()) {
            WorldState current = pq.delMin();
            if (current.isGoal()) {
                m = hm.get(current)[0];
                lst.add(0, current); // always add to the beginning of the list.
                for (int i = m - 1; i >= 0; i--) {
                    lst.add(0, map.get(current));// always add to the beginning of the list.
                    current = map.get(current);
                }
                break;
            }
            for (WorldState n: current.neighbors()) {
                if (!hm.containsKey(n)) {
                    int[] array = new int[2];
                    array[0] = hm.get(current)[0] + 1;
                    array[1] = array[0] + n.estimatedDistanceToGoal();
                    hm.put(n, array);
                    map.put(n, current);
                    pq.insert(n);
                }
            }
        }
    }

    /** Returns the minimum number of moves to solve the puzzle starting at the initial worldState. (How many steps get to goal?)*/
    public int moves() {
        return m;
    }

    /** Return a sequence of WorldState from the initial WorldState to the solution. */
    public Iterable<WorldState> solution() {
        return lst;
    }
}

