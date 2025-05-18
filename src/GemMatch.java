
import javax.swing.*;
import java.awt.*;

/**
 * GemMatch Game
 * @author Mustafa Alismaeli
 *
 * This is the main file for running the game.
 */

public class GemMatch {

    private JFrame frame;
    private JButton startButton;
    private JLabel scoreLabel;
    private GamePanel gamePanel;
    private JLabel movesLabel;
    /**
     * Class to create the games UI
     */
    public GemMatch() {
        frame = new JFrame("Gem Match Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);
        frame.setLayout(new BorderLayout());

        // top UI
        JPanel topPanel = new JPanel();
        startButton = new JButton("Start Game");
        scoreLabel = new JLabel("Gems Matched: 0");
        movesLabel = new JLabel(" | Moves: 0");

        // adds the start button and the score to the top
        topPanel.add(startButton);
        topPanel.add(scoreLabel);
        topPanel.add(movesLabel);
        gamePanel = new GamePanel(scoreLabel, movesLabel);

        // handles clicking start game
        startButton.addActionListener(e -> gamePanel.startNewGame());


        // panel on top, game on buttom
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Main method: starts the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GemMatch());
    }
}

