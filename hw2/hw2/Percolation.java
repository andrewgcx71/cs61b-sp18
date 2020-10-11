package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // n represents n x n grid
    private int n;

    // empty grid
    private boolean[][] grid;

    // number of open sites
    private int noos;

    // indicate weather the system percolate
    private boolean isPercolated;

    // union find
    private WeightedQuickUnionUF uf;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        n = N;
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 0; i < n; i++) {
            uf.union(n * n, getIndex(0, i));
        }
    }
    //takes row and col, return the index number of site
    private int getIndex(int row, int col) {
        return row * n + col;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            noos++;
            if (row - 1 >= 0 && isOpen(row - 1, col)) {
                uf.union(getIndex(row, col), getIndex(row - 1, col));
            }
            if (row + 1 < n && isOpen(row + 1, col)) {
                uf.union(getIndex(row, col), getIndex(row + 1, col));
            }
            if (col - 1 >= 0 && isOpen(row, col - 1)) {
                uf.union(getIndex(row, col), getIndex(row, col - 1));
            }
            if (col + 1 < n && isOpen(row, col + 1)) {
                uf.union(getIndex(row, col), getIndex(row, col + 1));
            }
            if (!isPercolated && row == n - 1 && isFull(row, col)) {
                isPercolated = true;
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return uf.connected(n * n, getIndex(row, col)) && isOpen(row, col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return noos;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolated;
    }
    //public static void main(String[] args)   // use for unit testing (not required)
}
