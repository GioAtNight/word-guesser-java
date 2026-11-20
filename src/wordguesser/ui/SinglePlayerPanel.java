package wordguesser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import wordguesser.model.LetterState;
import wordguesser.model.User;
import wordguesser.service.GameEngine;

public class SinglePlayerPanel extends JPanel {
    private final MainFrame frame;

    private JComboBox<Integer> lengthBox;
    private JButton startButton;
    private JTextField guessField;
    private JButton guessButton;
    private JButton backButton;
    private JLabel infoLabel;
    private JPanel boardPanel;

    private JLabel[][] cells;

    private String targetWord;
    private int wordLength;
    private final int maxAttempts = 6;
    private int currentAttempt = 0;
    private boolean gameActive = false;

    private JLabel bannerLabel;
    private Timer bannerTimer;
    private boolean bannerVisible = true;

    public SinglePlayerPanel(MainFrame frame) {
        this.frame = frame;
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("SINGLE PLAYER", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(new Color(0, 255, 180));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        bannerLabel = new JLabel(" ", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        bannerLabel.setForeground(Color.YELLOW);
        bannerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        topPanel.setBackground(Color.BLACK);

        JLabel lengthLabel = createInfoLabel("WORD LENGTH:");
        lengthBox = new JComboBox<>();
        lengthBox.setFont(new Font("Monospaced", Font.BOLD, 16));

        startButton = createArcadeButton("START GAME");

        topPanel.add(lengthLabel);
        topPanel.add(lengthBox);
        topPanel.add(startButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.BLACK);
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(bannerLabel, BorderLayout.CENTER);
        northPanel.add(topPanel, BorderLayout.SOUTH);

        boardPanel = new JPanel();
        boardPanel.setBackground(Color.BLACK);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottomPanel.setBackground(Color.BLACK);

        JLabel guessLabel = createInfoLabel("GUESS:");
        guessField = new JTextField(12);
        guessField.setFont(new Font("Monospaced", Font.BOLD, 18));

        guessButton = createArcadeButton("SUBMIT");
        backButton = createArcadeButton("BACK");

        bottomPanel.add(guessLabel);
        bottomPanel.add(guessField);
        bottomPanel.add(guessButton);
        bottomPanel.add(backButton);

        infoLabel = new JLabel("SELECT A WORD LENGTH AND START.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.BLACK);
        southPanel.add(infoLabel, BorderLayout.NORTH);
        southPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startGame());
        guessButton.addActionListener(e -> submitGuess());
        backButton.addActionListener(e -> frame.showScreen(MainFrame.MENU));

        guessField.setEnabled(false);
        guessButton.setEnabled(false);
    }

    private JButton createArcadeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setBackground(new Color(20, 20, 20));
        button.setForeground(new Color(0, 255, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 3));
        return button;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void showBanner(String message, Color color) {
        bannerLabel.setText(message);
        bannerLabel.setForeground(color);
        bannerLabel.setVisible(true);

        if (bannerTimer != null && bannerTimer.isRunning()) {
        bannerTimer.stop();
    }
}
    
    private void showFlashingBanner(String message, Color color) {
        bannerLabel.setText(message);
        bannerLabel.setForeground(color);
        bannerLabel.setVisible(true);
        bannerVisible = true;

        if (bannerTimer != null && bannerTimer.isRunning()) {
        bannerTimer.stop();
    }

        bannerTimer = new Timer(400, e -> {
        bannerVisible = !bannerVisible;
        bannerLabel.setForeground(bannerVisible ? color : Color.BLACK);
        }
    );
        bannerTimer.start();
}

    private void stopBanner() {
        if (bannerTimer != null) {
        bannerTimer.stop();
    }
        bannerLabel.setVisible(true);
        bannerLabel.setForeground(Color.YELLOW);
}

    public void prepareNewSession() {
        lengthBox.removeAllItems();
        for (Integer len : frame.getWordService().getAvailableLengths()) {
            lengthBox.addItem(len);
        }
        clearBoard();
        gameActive = false;
        targetWord = null;
        currentAttempt = 0;
        infoLabel.setText("SELECT A WORD LENGTH AND START.");
        guessField.setText("");
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        stopBanner();
        bannerLabel.setText(" ");
    }

    private void startGame() {
        Integer selectedLength = (Integer) lengthBox.getSelectedItem();
        if (selectedLength == null) {
            JOptionPane.showMessageDialog(this, "No valid word lengths found.");
            return;
        }

        wordLength = selectedLength;
        targetWord = frame.getWordService().getRandomWord(wordLength);

        if (targetWord == null) {
            JOptionPane.showMessageDialog(this, "No words found for that length.");
            return;
        }

        currentAttempt = 0;
        gameActive = true;
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        infoLabel.setText("GAME STARTED. YOU HAVE " + maxAttempts + " ATTEMPTS.");
        stopBanner();
        bannerLabel.setText(" ");
        buildBoard(maxAttempts, wordLength);
    }

    private void buildBoard(int rows, int cols) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(rows, cols, 6, 6));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 140, 20, 140));
        boardPanel.setBackground(Color.BLACK);

        cells = new JLabel[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(new Color(40, 40, 40));
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 2));
                label.setFont(new Font("Monospaced", Font.BOLD, 22));
                cells[r][c] = label;
                boardPanel.add(label);
            }
        }

        revalidate();
        repaint();
    }

    private void clearBoard() {
        boardPanel.removeAll();
        revalidate();
        repaint();
    }

    private void submitGuess() {
        if (!gameActive) return;

        String guess = guessField.getText().trim().toLowerCase();

        if (!guess.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "Only letters are allowed.");
            return;
        }

        if (guess.length() != wordLength) {
            JOptionPane.showMessageDialog(this, "Guess must be " + wordLength + " letters.");
            return;
        }

        if (!frame.getWordService().isValidWord(guess, wordLength)) {
            JOptionPane.showMessageDialog(this, "That word is not in the word list.");
            return;
        }

        List<LetterState> result = GameEngine.evaluateGuess(guess, targetWord);

        for (int i = 0; i < wordLength; i++) {
            cells[currentAttempt][i].setText(String.valueOf(Character.toUpperCase(guess.charAt(i))));
            switch (result.get(i)) {
                case CORRECT:
                    cells[currentAttempt][i].setBackground(new Color(83, 141, 78));
                    cells[currentAttempt][i].setForeground(Color.WHITE);
                    break;
                case PRESENT:
                    cells[currentAttempt][i].setBackground(new Color(181, 159, 59));
                    cells[currentAttempt][i].setForeground(Color.WHITE);
                    break;
                case ABSENT:
                    cells[currentAttempt][i].setBackground(new Color(120, 124, 126));
                    cells[currentAttempt][i].setForeground(Color.WHITE);
                    break;
            }
        }

        currentAttempt++;
        guessField.setText("");

        if (guess.equals(targetWord)) {
            endGame(true, "YOU WON! WORD: " + targetWord.toUpperCase());
        } else if (currentAttempt >= maxAttempts) {
            endGame(false, "YOU LOST. WORD: " + targetWord.toUpperCase());
        } else {
            infoLabel.setText("ATTEMPTS LEFT: " + (maxAttempts - currentAttempt));
        }
    }

    private void endGame(boolean won, String message) {
        gameActive = false;
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        infoLabel.setText(message);

        User user = frame.getCurrentUser();
        user.incrementGamesPlayed();

        if (won) {
        user.incrementWins();
        showFlashingBanner("YOU WIN!", new Color(0, 255, 120));
    } else {
        user.incrementLosses();
        showBanner("GAME OVER", new Color(255, 80, 80));
    }

        frame.getUserStore().updateUser(user);

        JOptionPane.showMessageDialog(this, message);
    }
}