package lab11.graphs;
import java.util.Queue;
import java.util.LinkedList;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo; // edgeTo is a very confused method name, it basically takes current node and return previous node.
    public boolean[] marked;
    */

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        bfs(0);
    }

    // Helper methods go here
    public void bfs(int source) {
        Queue<Integer> queue = new LinkedList<>();
        distTo[source] = 0;
        marked[source] = true;
        announce();
        queue.add(source);
        while (!queue.isEmpty()) {
            int current = queue.remove();
            for (int neighbor: maze.adj(current)) {
                if (marked[neighbor] && neighbor != edgeTo[current]) {
                    return;
                }
                if (!marked[neighbor]) {
                    distTo[neighbor] = distTo[current] + 1;
                    marked[neighbor] = true;
                    edgeTo[neighbor] = current;
                    queue.add(neighbor);
                    announce();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        Maze maze = new Maze("lab11/graphs/maze.txt");
//        MazeCycles mazecycles = new MazeCycles(maze);
//        mazecycles.solve();
//    }
}

