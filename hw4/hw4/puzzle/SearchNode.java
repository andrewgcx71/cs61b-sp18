package hw4.puzzle;

public class SearchNode implements Comparable<SearchNode> {

    private WorldState c;
    private SearchNode previousNode;
    private int move;
    private int priority;

    public WorldState ws() {
        return c;
    }

    public SearchNode previousNode() {
        return previousNode;
    }

    public int getMove() {
        return move;
    }

    /** Constructor for SearchNode. */
    public SearchNode(WorldState c, SearchNode previousNode, int move) {
        this.c = c;
        this.previousNode = previousNode;
        this.move = move;
        this.priority = move + c.estimatedDistanceToGoal();
    }

    /** return 1 if current object
     * is bigger than the object in the passing argument.
     * ------------------------------------------------
     * return -1 if current object
     * is less than the object in the passing argument.
     * -----------------------------------------------
     * return 0 if both objects are equal.
     */
    @Override
    public int compareTo(SearchNode n) {
        if (this.priority > n.priority) {
            return 1;
        } else if (this.priority < n.priority) {
            return -1;
        } else {
            return 0;
        }
    }
}
