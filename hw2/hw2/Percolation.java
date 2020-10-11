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

    // union find with virtual bottom site
    private WeightedQuickUnionUF uf1;

    // union find without virtual bottom site
    private WeightedQuickUnionUF uf2;

    //virtual top site
    private int virtualTopSite;

    //virtual bottom site
    private int virtualBottomSite;


    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        n = N;
        grid = new boolean[n][n];
        uf1 = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        virtualTopSite = n * n;
        virtualBottomSite = n * n + 1;
        for (int i = 0; i < n; i++) {
            uf1.union(virtualTopSite, getIndex(0, i));
            uf1.union(virtualBottomSite, getIndex(n - 1, i));
            uf2.union(virtualTopSite, getIndex(0, i));
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
                uf1.union(getIndex(row, col), getIndex(row - 1, col));
                uf2.union(getIndex(row, col), getIndex(row - 1, col));
            }
            if (row + 1 < n && isOpen(row + 1, col)) {
                uf1.union(getIndex(row, col), getIndex(row + 1, col));
                uf2.union(getIndex(row, col), getIndex(row + 1, col));
            }
            if (col - 1 >= 0 && isOpen(row, col - 1)) {
                uf1.union(getIndex(row, col), getIndex(row, col - 1));
                uf2.union(getIndex(row, col), getIndex(row, col - 1));
            }
            if (col + 1 < n && isOpen(row, col + 1)) {
                uf1.union(getIndex(row, col), getIndex(row, col + 1));
                uf2.union(getIndex(row, col), getIndex(row, col + 1));
            }
            if (uf1.connected(virtualTopSite, virtualBottomSite)) {
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
        return uf2.connected(virtualTopSite, getIndex(row, col)) && isOpen(row, col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return noos;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolated;
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }
}
