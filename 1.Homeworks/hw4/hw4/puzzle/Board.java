package hw4.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board implements WorldState {

    private int[][] tiles;

    private boolean hashCodeCalled = false;

    private int hashcode;

    private Random r = new Random(4354);

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

    //helper method for neighbors(), return a copy of tiles.*/
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
        int value = 1;
        int res = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != value) {
                    res++;
                }
                value++;
            }
        }
        return res;
    }

    /** Manhattan estimate described below. */
    public int manhattan() {
        int res = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                //goal: goal value in this tile[i][j]
                int goal = i * size() + (j + 1);
                if (tiles[i][j] != 0 && goal != tiles[i][j]) {
                    int value = tiles[i][j];
                    //compute the actual position of value
                    int row = (value - 1) / size();
                    int col = (value - 1) % size();
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
        if (y == null) {
            return false;
        }
        if (!this.getClass().equals(y.getClass())) {
            return false;
        }
        if (size() != ((Board) y).tiles.length) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (this.tiles[i][j] != ((Board) y).tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Override it for the sake of Auto Grader */
    @Override
    public int hashCode() {
        if (!hashCodeCalled) {
            for (int i = 0; i < size(); i++) {
                for (int j = 0; j < size(); j++) {
                    hashcode += tiles[i][j] * r.nextInt(size() * size());
                }
            }
            hashCodeCalled = true;
        }
        return hashcode;
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
}
