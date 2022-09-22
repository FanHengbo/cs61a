/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] threshold;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        int randomRow = StdRandom.uniformInt(1, n + 1);
        int randomCol = StdRandom.uniformInt(1, n + 1);
        this.trials = trials;
        Percolation per;
        threshold = new double[trials];

        for (int i = 0; i < trials; ++i) {
            per = new Percolation(n);
            while (!per.percolates()) {
                while (per.isOpen(randomRow, randomCol)) {
                    randomRow = StdRandom.uniformInt(1, n + 1);
                    randomCol = StdRandom.uniformInt(1, n + 1);
                }
                per.open(randomRow, randomCol);
            }
            threshold[i] = (double) per.numberOfOpenSites() / (double) (n * n);
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = this.mean();
        double std = this.stddev();

        return (mean - 1.96 * std / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = this.mean();
        double std = this.stddev();

        return (mean + 1.96 * std / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        PercolationStats test = new PercolationStats(200, 100);
        System.out.println("mean = " + test.mean());
        System.out.println("stddev = " + test.stddev());
        System.out.println(
                "95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi()
                        + "]");

    }
}
