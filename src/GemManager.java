import java.util.*;

/**
 * GemManager Class
 *
 * @author Mustafa Alismaeli
 * CS251 GemManager Part 1
 * Manager used to handle the gem game
 * Creates a new board, rows, columns, the gemtypes, and a random seed
 */
public class GemManager {
    private final Gem[][] board;
    private final int rows;
    private final int cols;
    private final char[] gemTypes;
    private final Random random;

    /**
     * Constructor for Gem Manager
     *
     * @param rows     Rows in a given game
     * @param cols     Columns in a given game
     * @param gemTypes The "gems" or symbols that can be used apart of a game
     *                 (Given in an array)
     * @param seed     Random number used to generate the randomness of the board
     *                 (to be replicated aka a seed)
     */
    public GemManager(int rows, int cols, char[] gemTypes, long seed) {
        this.rows = rows;
        this.cols = cols;
        this.gemTypes = gemTypes;
        this.random = new Random(seed);
        this.board = new Gem[rows][cols];
        fillBoardWithoutMatches();
    }


    private void fillBoardWithoutMatches() {
        // keep refilling until there are no matches
        do {
            fillBoard();
        } while (!findMatches().isEmpty());
    }

    private void fillBoard() {
        // basic filling of the board
        // goes through every slot and puts a random gem
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Gem(randomGem());
            }
        }
    }

    private char randomGem() {
        // Generates a random gemtype/symbol
        return gemTypes[random.nextInt(gemTypes.length)];
    }


    public boolean swap(int r1, int c1, int r2, int c2) {
        // swaps the gem types of row1col1 with row2col2.
        if (!isAdjacent(r1, c1, r2, c2)) return false;

        // using temp variable to store the swap
        Gem temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
        return true;
    }


    // Method used to check the cells of row1col1 are adjacent to row2col2
    private boolean isAdjacent(int r1, int c1, int r2, int c2) {
        return (Math.abs(r1 - r2) + Math.abs(c1 - c2)) == 1;
    }

    // Storing matched rows and columns into a string hash set (row, column)
    public Set<String> findMatches() {
        Set<String> matches = new HashSet<>();

        // Horizontal
        for (int r = 0; r < rows; r++) {
            int count = 1;
            for (int c = 1; c < cols; c++) {
                // checking if the spot next to matches
                if (board[r][c] != null && board[r][c - 1] != null &&
                        board[r][c].getType() == board[r][c - 1].getType()) {
                    count++;
                    if (c == cols - 1 && count >= 3) {
                        for (int k = 0; k < count; k++) {
                            matches.add(r + "," + (c - k));
                        }
                    }
                } else {
                    // if reach 3 then the past spots are added
                    if (count >= 3) {
                        for (int k = 1; k <= count; k++) {
                            matches.add(r + "," + (c - k));
                        }
                    }
                    count = 1;
                }
            }
        }

        // Vertical (same as Horizontal code)
        for (int c = 0; c < cols; c++) {
            int count = 1;
            // goes through every row and checks for matches going down
            for (int r = 1; r < rows; r++) {
                if (board[r][c] != null && board[r - 1][c] != null &&
                        board[r][c].getType() == board[r - 1][c].getType()) {
                    count++;
                    if (r == rows - 1 && count >= 3) {
                        for (int k = 0; k < count; k++) {
                            matches.add((r - k) + "," + c);
                        }
                    }
                } else {
                    // if reach 3 then the past spots are added
                    if (count >= 3) {
                        for (int k = 1; k <= count; k++) {
                            matches.add((r - k) + "," + c);
                        }
                    }
                    count = 1;
                }
            }
        }
        // returns all the matches (set)
        return matches;
    }

    /**
     * Remove any matches and drop the gems to fill
     *
     * @return Amount of matches removed
     */
    public int removeMatches() {
        int totalRemoved = 0;
        // Loops untill there are no matches are found
        while (true) {
            Set<String> matches = findMatches();
            // if there are no matches then end:
            if (matches.isEmpty()) break;
            totalRemoved += matches.size();

            // goes through all the matches and makes them null/empty
            for (String pos : matches) {
                String[] parts = pos.split(",");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);
                board[r][c] = null;
            }

            // drop any "empty" spots and fill with whats on top
//            dropGems();
        }
        return totalRemoved;
    }

    public void dropGems() {
        for (int c = 0; c < cols; c++) {
            int writeRow = rows - 1;
            for (int r = rows - 1; r >= 0; r--) {
                if (board[r][c] != null) {
                    board[writeRow--][c] = board[r][c];
                }
            }
            while (writeRow >= 0) {
                // generate new gems for the rows dropped from the top rows
                board[writeRow--][c] = new Gem(randomGem());
            }
        }
    }

    public Gem getGem(int r, int c) {
        return board[r][c];
    }

    // Goes through the gameboard and adds the gems to a stringbuilder
    // Stringifies the board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Gem[] row : board) {
            for (Gem gem : row) {
                sb.append(gem).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
