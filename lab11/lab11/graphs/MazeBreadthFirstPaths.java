package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    public Maze maze;
    */
    private int sourceX;
    private int sourceY;
    private int targetX;
    private int targetY;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        // calling the constructor from parent class of MazeBreadthFirstPaths.
        super(m);
        // Add more variables here!
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> queue = new ArrayDeque<>();
        int source = maze.xyTo1D(sourceX, sourceY);
        int target = maze.xyTo1D(targetX, targetY);
        if (source == target) {
            return;
        }
        distTo[source] = 0;
        queue.add(source);
        marked[source] = true;
        announce();
        while (!queue.isEmpty()) {
            int node = queue.remove();
            for (int temp : maze.adj(node)) {
                if (!marked[temp]) { // if temp hasn't been visited yet, add to queue.
                    queue.add(temp);
                    edgeTo[temp] = node;
                    marked[temp] = true;
                    distTo[temp] = distTo[node] + 1;
                    announce();
                    if (temp == target) { // if we found the target, simply return or break the while loop.
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}

