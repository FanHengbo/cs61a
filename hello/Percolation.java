/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

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
                isOpen[i][j] = false;
            }
        }
        // The last two elements are top row root and bottom row root respectively
        sites = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < n; ++i) {
            // Connect the top row of sites to the top root
            sites.union(N * N, i + 2);
            // Connect the bottom row of sites to the bottom root
            sites.union(N * N + 1, (N - 1) * N + 2 + i);
        }
    }

    private int index(int row, int col) {
        if (row > N || col > N) {
            return -1;
        }
        return (row - 1) * N + col - 1;
    }

    // opens the site (row, col) if it is not open already
    // TODO: Buggy!
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        boolean upper = false;
        boolean left = false;
        boolean right = false;
        isOpen[row - 1][col - 1] = true;
        if (row > 1) {
            upper = isOpen(row - 1, col) && sites.find(index(row - 1, col)) == sites.find(N * N);
        }
        if (col == 1) {
            right = isOpen(row, col + 1) && sites.find(index(row, col + 1)) == sites.find(N * N);
        }
        else if (col == N) {
            left = isOpen(row, col - 1) && sites.find(index(row, col - 1)) == sites.find(N * N);
        }
        else {
            right = isOpen(row, col + 1) && sites.find(index(row, col + 1)) == sites.find(N * N);
            left = isOpen(row, col - 1) && sites.find(index(row, col - 1)) == sites.find(N * N);
        }
        if (upper && left && right) {
            sites.union(index(row, col), N * N);
        }
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

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        for (int i = 0; i < 3; ++i) {
            System.out.println(p.isOpen(i + 1, 1));
        }
    }
}
