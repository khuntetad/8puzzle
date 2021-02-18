import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode goal;
    private Stack<Board> soln;
    private boolean isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previousNode;
        private int priority;

        public SearchNode(Board board, SearchNode previous, int priority) {
            if (board == null)
                throw new IllegalArgumentException();

            this.board = board;
            this.previousNode = previous;
            this.priority = priority;
        }

        public int compareTo(SearchNode that) {
            int prioritythis = this.priority + this.board.manhattan();
            int prioritythat = that.priority + that.board.manhattan();

            if (prioritythis > prioritythat) {
                return 1;
            }

            else if (prioritythis < prioritythat) {
                return -1;
            }

            else {
                return 0;
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        isSolvable = false;
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqSwap = new MinPQ<SearchNode>();

        soln = new Stack<Board>();

        SearchNode node = new SearchNode(initial, null, 0);
        SearchNode twinNode = new SearchNode(initial.twin(), null, 0);

        pq.insert(node);
        pqSwap.insert(twinNode);

        while (!isSolvable) {
            SearchNode minNode = pq.delMin();
            SearchNode minNodeTwin = pqSwap.delMin();

            if (minNode.board.isGoal()) {
                isSolvable = true;
                goal = minNode;
            }

            if (minNodeTwin.board.isGoal()) {
                break;
            }

            for (Board b : minNode.board.neighbors()) {
                if (minNode.previousNode == null || !b.equals(minNode.previousNode.board))
                    pq.insert(new SearchNode(b, minNode, minNode.priority + 1));
            }

            for (Board b : minNodeTwin.board.neighbors())
                if (minNodeTwin.previousNode == null || !b.equals(minNodeTwin.previousNode.board))
                    pqSwap.insert(new SearchNode(b, minNode, minNodeTwin.priority + 1));

            minNode = pq.min();
            // soln.add(current.board);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }

        return goal.priority;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        SearchNode node = goal;

        while (node != null) {
            soln.push(node.board);
            node = node.previousNode;
        }

        return soln;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
