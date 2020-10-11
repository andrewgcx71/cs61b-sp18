package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    // n represents n x n grid
    private int n;

    // t represents the number of experiment
    private int t;

    private PercolationFactory pf;

    //Array of fraction of open sites
    private double[] lst;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory PF) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        n = N;
        t = T;
        pf = PF;
        lst = new double[t];
        for (int i = 0; i < t; i++) {
            lst[i] = calculateFractionOfOpenSites();
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(lst);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(lst);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * Math.sqrt(stddev()) / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh()       {
        return mean() + 1.96 * Math.sqrt(stddev()) / Math.sqrt(t);
    }

    //return fraction of open sites as soon as system is percolated.
    private double calculateFractionOfOpenSites() {
        Percolation system = pf.make(n);
        while (!system.percolates()) {
            int row = StdRandom.uniform(0, n);
            int col = StdRandom.uniform(0, n);
            system.open(row, col);

        }
        return ((double) system.numberOfOpenSites()) / (n * n);
    }
    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(200, 2, pf);
        System.out.println(ps.confidenceHigh());
        System.out.println(ps.confidenceLow());
        System.out.println(ps.mean());
    }
}
