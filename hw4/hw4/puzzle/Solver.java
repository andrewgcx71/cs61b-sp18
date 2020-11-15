package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.List;
import java.util.ArrayList;


public class Solver {
    /**
     * minimum number of moves to solve the puzzle starting at initial worldState.
     */
    private int m = 0;

    /**
     * A sequence of WorldState from the initial WorldState to the solution.
     */
    private List<WorldState> lst = new ArrayList<>();

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        SearchNode n = new SearchNode(initial, null, 0);
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(n);
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            if (node.ws().isGoal()) {
                m = node.getMove();
                while (node != null) {
                    //we add each ws to the beginning of the list
                    // because it's currently in a reversed order.
                    lst.add(0, node.ws());
                    node = node.previousNode();
                }
                break;
            } else if (node.previousNode() == null) {
                for (WorldState neighbor: node.ws().neighbors()) {
                    pq.insert(new SearchNode(neighbor, node, node.getMove() + 1));
                }
            } else {
                for (WorldState neighbor: node.ws().neighbors()) {
                    if (!neighbor.equals(node.previousNode().ws())) {
                        pq.insert(new SearchNode(neighbor, node, node.getMove() + 1));
                    }
                }
            }
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting at the initial worldState.
     * (How many steps get to goal?)
     */
    public int moves() {
        return m;
    }

    /**
     * Return a sequence of WorldState from the initial WorldState to the solution.
     */
    public Iterable<WorldState> solution() {
        return lst;
    }
}