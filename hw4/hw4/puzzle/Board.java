package hw4.puzzle;


import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.In;

public class Board implements WorldState {

    private int[][] tiles;


    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j.
     */
    public Board(int[][] tiles) {
        this.tiles = clone(tiles);
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank).
     */
    public int tileAt(int i, int j) {
        if (i < 0 || j < 0 || i >= tiles.length || j >= tiles.length) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    /**
     * Returns the board size N.
     */
    public int size() {
        return tiles.length;
    }

    /**
     * Returns the neighbors of the current board.
     * Use Professor Josh Hub's solution.
     * Reference: http://joshh.ug/neighbors.html
     */
    @Override
    public Iterable<WorldState> neighbors() {
        List<WorldState> res = new ArrayList<>();
        int row = 0;
        int col = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        //left neighbor
        if (col != 0) {
            int[][] neighbor = clone(this.tiles);
            neighbor[row][col] = neighbor[row][col - 1];
            neighbor[row][col - 1] = 0;
            res.add(new Board(neighbor));
        }
        //right neighbor
        if (col != size() - 1) {
            int[][] neighbor = clone(this.tiles);
            neighbor[row][col] = neighbor[row][col + 1];
            neighbor[row][col + 1] = 0;
            res.add(new Board(neighbor));
        }

        //top neighbor
        if (row != 0) {
            int[][] neighbor = clone(this.tiles);
            neighbor[row][col] = neighbor[row - 1][col];
            neighbor[row - 1][col] = 0;
            res.add(new Board(neighbor));
        }

        //bottom neighbor
        if (row != size() - 1) {
            int[][] neighbor = clone(this.tiles);
            neighbor[row][col] = neighbor[row + 1][col];
            neighbor[row + 1][col] = 0;
            res.add(new Board(neighbor));
        }
        return res;
    }

    //helper method for neighbors(), return a copy of the passing argument.*/
    private int[][] clone(int[][] t) {
        int size = t.length;
        int[][] T = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                T[i][j] = t[i][j];
            }
        }
        return T;
    }

    /**
     * Hamming estimate described below.
     */
    public int hamming() {
        int n = 1;
        int res = 0;
        int size = tiles.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (tiles[i][j] != n) {
                    res++;
                }
                n++;
            }
        }
        return res;
    }

    /**
     * Manhattan estimate described below.
     */
    public int manhattan() {
        int res = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int actual = i * size() + (j + 1);
                if (tiles[i][j] != 0 && actual != tiles[i][j]) {
                    int value = tiles[i][j];
                    //compute the actual position of value
                    int row = (int) (value / (size() + 0.1)); // round down
                    int col = value - row * size() - 1;
                    res += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return res;
    }

    /**
     * Estimated distance to goal.
     */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same
     * position as y's. why we are not required to override hashCode method?
     */
    @Override
    public boolean equals(Object y) {
        int size = tiles.length;
        Board Y = (Board) y;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.tiles[i][j] != Y.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        System.out.println(initial.manhattan());
    }
}