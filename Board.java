import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] board;
    private int n;

    public Board(int[][] tiles) {
        n = tiles[0].length;
        board = new int[n][n];
        for (int i = 0; i < tiles[0].length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String s = "";
        s += ("" + n + "\n");
        for (int[] i : board) {
            for (int j : i) {
                s += j;
                s += " ";
            }
            s += "\n";
        }

        return s;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        int spot = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != spot && board[i][j] != 0) {
                    count++;
                }
                spot++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        int spot = 1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int tileValue = board[i][j];
                if (tileValue != spot && tileValue != 0) {
                    int goalRow = (tileValue - 1) / n;
                    int goalColumn = ((tileValue - 1) % n);
                    int deltaRow = Math.abs(i - goalRow);
                    int deltaColumn = Math.abs(j - goalColumn);
                    count += (deltaRow + deltaColumn);
                }
                spot++;
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board copy = (Board) y;
        return this.n == copy.n && Arrays.deepEquals(this.board, copy.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        int position = findBlank();

        int i = position / n;
        int j = position % n;


        if (i > 0) {
            neighbors.enqueue(new Board(swap(i, j, i - 1, j)));
        }
        if (i < n - 1) {
            neighbors.enqueue(new Board(swap(i, j, i + 1, j)));
        }
        if (j > 0) {
            neighbors.enqueue(new Board(swap(i, j, i, j - 1)));
        }
        if (j < n - 1) {
            neighbors.enqueue(new Board(swap(i, j, i, j + 1)));
        }
        return neighbors;
    }

    private int findBlank() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    return j + i * n;
                }
            }
        }
        return -1;
    }

    private int[][] swap(int i, int j, int k, int l) {
        int[][] ret = copy(board);
        int temp = ret[i][j];
        ret[i][j] = ret[k][l];
        ret[k][l] = temp;
        return ret;
    }

    private int[][] copy(int[][] cop) {
        int[][] ret = new int[cop.length][cop.length];
        for (int i = 0; i < cop.length; i++) {
            for (int j = 0; j < cop.length; j++) {
                ret[i][j] = cop[i][j];
            }
        }
        return ret;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin;
        if (board[0][0] != 0 && board[0][1] != 0) {
            twin = new Board(swap(0, 0, 0, 1));
        }
        else {
            twin = new Board(swap(1, 0, 1, 1));
        }
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        initial.toString();
    }

}
