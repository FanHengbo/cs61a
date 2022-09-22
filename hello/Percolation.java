/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private WeightedQuickUnionUF sites;
    private int N;
    private Boolean[][] isOpen;
    private int numOfOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        numOfOpen = 0;
        N = n;
        // Initialize all the sites to be blocked
        isOpen = new Boolean[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                isOpen[i][j] = Boolean.FALSE;
            }
        }
        // The last two elements are top row root and bottom row root respectively
        sites = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < n; ++i) {
            // Connect the top row of sites to the top root
            sites.union(N * N, i);
            // Connect the bottom row of sites to the bottom root
            sites.union(N * N + 1, (N - 1) * N + i);
        }
    }

    private int index(int row, int col) {
        if (row > N || col > N) {
            return -1;
        }
        return (row - 1) * N + col - 1;
    }

    private void connectNeighborhood(int row, int col) {
        if (row != 1) {
            if (isOpen(row - 1, col)) {
                sites.union(index(row, col), index(row - 1, col));
            }
        }
        if (row != N) {
            if (isOpen(row + 1, col)) {
                sites.union(index(row, col), index(row + 1, col));
            }
        }
        if (col != 1) {
            if (isOpen(row, col - 1)) {
                sites.union(index(row, col), index(row, col - 1));
            }
        }
        if (col != N) {
            if (isOpen(row, col + 1)) {
                sites.union(index(row, col), index(row, col + 1));
            }
        }
    }

    // opens the site (row, col) if it is not open already
    //
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        isOpen[row - 1][col - 1] = Boolean.TRUE;
        connectNeighborhood(row, col);
        numOfOpen++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > N || col > N) {
            throw new IllegalArgumentException("Index out of range!");
        }
        return isOpen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && sites.find(index(row, col)) == sites.find(N * N);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return sites.find(N * N + 1) == sites.find(N * N);
    }

    private void printGraph() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (isOpen[i][j]) {
                    System.out.print("o");
                }
                else {
                    System.out.print("#");
                }
            }
            System.out.println();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        int randomRow = StdRandom.uniformInt(1, 6);
        int randomCol = StdRandom.uniformInt(1, 6);
        while (!p.percolates()) {
            while (p.isOpen(randomRow, randomCol)) {
                randomRow = StdRandom.uniformInt(1, 6);
                randomCol = StdRandom.uniformInt(1, 6);
            }
            p.open(randomRow, randomCol);
        }
        p.printGraph();
        System.out.println(p.percolates());
        System.out.println(p.numberOfOpenSites());
    }
}
