import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * CS251 Gem Match GamePanel
 * @author Mustafa Alismaeli
 * The handler for the game panel and drawing the gems
 */

/**
 * TODO:
 * - Add game over when reach certain score - done:
 *   - Implement option to continue game or start new game
 * - Animate drops - done
 * - Rename score -> moves - done
 * - Score for how many gems removed - done
 * - Match 4: get a gem bomb -> removes all of that color that row/col
 * - Match 5: get gem book -> remove all of that color on the board
 * - Hint -> Use Gomoku computer code to find best move
 */
// Implementing mouse listener to
class GamePanel extends JPanel implements MouseListener {
    private static final int CELL_SIZE = 64;
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final char[] GEM_TYPES = {'+', '*', '.', '-', '#'};

    private GemManager gemManager;
    private JLabel scoreLabel;
    private JLabel movesLabel;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int score = 0;
    private int moves = 0;
    private Image backgroundImage;
    private Timer delayTimer;

    // Using a hashmap to map out the images to the symbol
    private Map<Character, BufferedImage> gemImages = new HashMap<>();

    // Creates the basic panel empty with the score and button
    public GamePanel(JLabel scoreLabel, JLabel movesLabel) {
        this.movesLabel = movesLabel;
        this.scoreLabel = scoreLabel;

        setPreferredSize(
                new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE)
        );
        setBackground(Color.WHITE);
        addMouseListener(this);
        loadImages();
    }

    public void startNewGame() {
        // creates the gem game backend
        gemManager = new GemManager(
                ROWS, COLS, GEM_TYPES, System.currentTimeMillis());
        // seed: random based off current time, so new games are not the same

        score = 0;
        moves = 0;
        selectedRow = -1;
        selectedCol = -1;
        updateScore();
        repaint();
    }


    // Loading the images needed for game
    private void loadImages() {
        try {
            /**
             * Images weren't loading so found a post online saying I needed
             * to use ImageIO to load
             */
            gemImages.put('+', ImageIO.read(
                    getClass().getResource("/images/ruby.png")));
            gemImages.put('*', ImageIO.read(
                    getClass().getResource("/images/sapphire.png")));
            gemImages.put('.', ImageIO.read(
                    getClass().getResource("/images/red-gem.png")));
            gemImages.put('-', ImageIO.read(
                    getClass().getResource("/images/emerald.png")));
            gemImages.put('#', ImageIO.read(
                    getClass().getResource("/images/blue-gem.png")));

            // background image
            backgroundImage = ImageIO.read(
                    getClass().getResource("/images/background.png"));

        } catch (Exception e) {
            System.err.println("Failed to load gem images: " + e.getMessage());
        }
    }

    // updates the score label
    private void updateScore() {
        movesLabel.setText(" | Moves: " + moves);
        scoreLabel.setText("Gems Matched: " + score);
    }

    // Main paint component for drawing the game board
    protected void paintComponent(Graphics g) {
        // clear the board
        super.paintComponent(g);

        // require a backend
        if (gemManager == null) return;

        // cell size (to be scaled)
        int cellWidth = getWidth() / COLS;
        int cellHeight = getHeight() / ROWS;


        // create the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(),
                    this);
        } else {
            g.setColor(Color.WHITE); // fallback
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // goes through every cell and draws the image based on that symbol
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                // symbol -> image of the gem
                Gem gem = gemManager.getGem(r, c);
                // if the gem is valid and NOT null
                if (gem != null) {
                    Image img = gemImages.get(gem.getType());
                    g.drawImage(
                            img,
                            c * cellWidth,
                            r * cellHeight,
                            cellWidth,
                            cellHeight,
                            this);
                    /**
                     * if the spot is null: which is the case after matches are
                     * removed, then crop the background so it appears blank
                     * for null pieces
                     */
                } else {
                    // getting the size of the background
                    int bgWidth = backgroundImage.getWidth(this);
                    int bgHeight = backgroundImage.getHeight(this);

                    // gets the corners of the cropped backgrund image
                    /**
                     * Since my ui is scalaable, I have to get the part of
                     * the background image that I need based on that scale
                     */
                    int scaledX  = c * bgWidth  / COLS;
                    int scaledY  = r * bgHeight / ROWS;
                    int scaledX2 = (c + 1) * bgWidth  / COLS;
                    int scaledY2 = (r + 1) * bgHeight / ROWS;

                    g.drawImage(
                            backgroundImage,
                            c * cellWidth,
                            r * cellHeight,
                            (c + 1) * cellWidth,
                            (r + 1) * cellHeight,
                            scaledX, scaledY, scaledX2, scaledY2,
                            this
                    );
                }


            }
        }

        // Indicate what is selected
        if (selectedRow != -1 && selectedCol != -1) {
            // outline cell with yellow
            g.setColor(Color.YELLOW);
            g.drawRect(selectedCol * cellWidth, selectedRow * cellHeight,
                    cellWidth, cellHeight);
            g.drawRect(selectedCol * cellWidth + 1,
                    selectedRow * cellHeight + 1,
                    cellWidth - 2, cellHeight - 2);
        }
    }


    // Mouse click handler
    public void mouseClicked(MouseEvent e) {
        // calculate the width and height of cells (scalable)
        int cellWidth = getWidth() / COLS;
        int cellHeight = getHeight() / ROWS;

        // divide the click location by width to find what cell
        int c = e.getX() / cellWidth;
        int r = e.getY() / cellHeight;


        // ensure within bounds and there is a game going
        if (r >= ROWS || c >= COLS || gemManager == null) {
            return;
        }

        // if there is nothing selected then select gem
        if (selectedRow == -1 && selectedCol == -1) {
            selectedRow = r;
            selectedCol = c;
        // if selecting the same gem -> unselect
        } else if (selectedRow == r && selectedCol == c) {
            selectedRow = -1;
            selectedCol = -1;
            /**
             * checking if the selected cell is next to previously selected cell
             * using absolute value to indicate distance:
             * If there is a possible 3 in a row (handled by gemmamagnger) then
             * removematches and drop
              */
        } else if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
            moves += 1;
            updateScore();
            // if there are 20+ moves, end the game
            if (moves >= 20) {
                JOptionPane.showMessageDialog(
                        this,
                        "Game Over! You reached 20 moves." +
                                "\nGems Match: " + score,
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            // if swamp is valid
            if (gemManager.swap(selectedRow, selectedCol, r, c)) {
                // add score of 1 for every match
                int removed = gemManager.removeMatches();
                if (removed > 0) {
                    score += removed;
                    updateScore();
                    repaint();
                    delayTimer = new Timer(1000, ev -> {
                        gemManager.dropGems();
                        repaint();
                        /**
                         * I kept having the bug where new gems spawned didn't
                         * have matches removed so I had to repeat the timer
                         * until it doesn't find any matches, similar to the
                         * GemManagerTest
                         */
                        int newRemovals = gemManager.removeMatches();
                        if (newRemovals > 0) {
                            repaint();
                            delayTimer.restart();
                        } else {
                            delayTimer.stop();
                        }
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                }
            }
            selectedRow = -1;
            selectedCol = -1;
        // if not next to then the new selection is the new start
        } else {
            selectedRow = r;
            selectedCol = c;
        }
        // repaint after every move - checking against backend board
        repaint();
    }

    // added by intellij
    // since actionlistener implemented, need to handle other events
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
