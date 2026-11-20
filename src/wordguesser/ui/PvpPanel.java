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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import wordguesser.model.LetterState;
import wordguesser.model.User;
import wordguesser.service.GameEngine;

public class PvpPanel extends JPanel {
    private final MainFrame frame;

    private JComboBox<Integer> lengthBox;
    private JTextField player2Field;
    private JPasswordField player2PasswordField;
    private JButton startButton;
    private JButton submitButton;
    private JButton backButton;
    private JTextField guessField;
    private JLabel statusLabel;

    private JPanel leftBoardPanel;
    private JPanel rightBoardPanel;

    private JLabel[][] p1Cells;
    private JLabel[][] p2Cells;

    private User player1;
    private User player2;
    private String player1Word;
    private String player2Word;
    private int wordLength;
    private final int maxAttempts = 6;
    private int p1Attempt;
    private int p2Attempt;
    private boolean player1Solved = false;
    private boolean player2Solved = false;
    private boolean player1Turn = true;
    private boolean gameActive = false;

    private JLabel leftBoardTitle;
    private JLabel rightBoardTitle;

    private JLabel bannerLabel;
    private Timer bannerTimer;
    private boolean bannerVisible = true;

    public PvpPanel(MainFrame frame) {
        this.frame = frame;
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("PLAYER VS PLAYER", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(new Color(0, 255, 180));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        bannerLabel = new JLabel(" ", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        bannerLabel.setForeground(Color.YELLOW);
        bannerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bannerLabel.setPreferredSize(new java.awt.Dimension(1000, 35));
        bannerLabel.setMinimumSize(new java.awt.Dimension(1000, 35));

        JPanel config = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        config.setBackground(Color.BLACK);

        JLabel lengthLabel = createInfoLabel("WORD LENGTH:");
        JLabel p2UserLabel = createInfoLabel("PLAYER 2:");
        JLabel p2PassLabel = createInfoLabel("PASSWORD:");

        lengthBox = new JComboBox<>();
        lengthBox.setFont(new Font("Monospaced", Font.BOLD, 16));

        player2Field = new JTextField(8);
        player2Field.setFont(new Font("Monospaced", Font.BOLD, 16));

        player2PasswordField = new JPasswordField(8);
        player2PasswordField.setFont(new Font("Monospaced", Font.BOLD, 16));

        startButton = createArcadeButton("START PVP");

        config.add(lengthLabel);
        config.add(lengthBox);
        config.add(p2UserLabel);
        config.add(player2Field);
        config.add(p2PassLabel);
        config.add(player2PasswordField);
        config.add(startButton);

        statusLabel = new JLabel("PLAYER 1 IS THE LOGGED-IN USER. ENTER PLAYER 2 CREDENTIALS.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(bannerLabel, BorderLayout.NORTH);
        infoPanel.add(statusLabel, BorderLayout.SOUTH);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.BLACK);
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(config, BorderLayout.CENTER);
        northPanel.add(infoPanel, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 15, 15));
        center.setBackground(Color.BLACK);
        center.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        leftBoardPanel = new JPanel();
        rightBoardPanel = new JPanel();

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setBackground(Color.BLACK);
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setBackground(Color.BLACK);

        leftBoardTitle = new JLabel("P1", SwingConstants.CENTER);
        leftBoardTitle.setFont(new Font("Monospaced", Font.BOLD, 22));
        leftBoardTitle.setForeground(new Color(255, 215, 0));
        leftBoardTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        rightBoardTitle = new JLabel("P2", SwingConstants.CENTER);
        rightBoardTitle.setFont(new Font("Monospaced", Font.BOLD, 22));
        rightBoardTitle.setForeground(new Color(255, 215, 0));
        rightBoardTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        leftWrapper.add(leftBoardTitle, BorderLayout.NORTH);
        leftWrapper.add(leftBoardPanel, BorderLayout.CENTER);

        rightWrapper.add(rightBoardTitle, BorderLayout.NORTH);
        rightWrapper.add(rightBoardPanel, BorderLayout.CENTER);

        center.add(leftWrapper);
        center.add(rightWrapper);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setBackground(Color.BLACK);

        JLabel guessLabel = createInfoLabel("CURRENT GUESS:");
        guessField = new JTextField(15);
        guessField.setFont(new Font("Monospaced", Font.BOLD, 18));

        submitButton = createArcadeButton("SUBMIT");
        backButton = createArcadeButton("BACK");

        bottom.add(guessLabel);
        bottom.add(guessField);
        bottom.add(submitButton);
        bottom.add(backButton);

        add(northPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startPvpGame());
        submitButton.addActionListener(e -> submitGuess());
        backButton.addActionListener(e -> frame.showScreen(MainFrame.MENU));

        guessField.setEnabled(false);
        submitButton.setEnabled(false);
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
        bannerVisible = true;

        if (bannerTimer != null && bannerTimer.isRunning()) {
        bannerTimer.stop();
    }

        bannerTimer = new Timer(400, e -> {
        bannerVisible = !bannerVisible;
        bannerLabel.setForeground(bannerVisible ? color : new Color(30, 30, 30));
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

    private void updateActivePlayerHighlight() {
        Color activeBorder = new Color(255, 215, 0);
        Color inactiveBorder = new Color(0, 255, 180);

        if (player1Turn) {
            leftBoardPanel.setBorder(BorderFactory.createLineBorder(activeBorder, 4));
            rightBoardPanel.setBorder(BorderFactory.createLineBorder(inactiveBorder, 2));
        } else {
            leftBoardPanel.setBorder(BorderFactory.createLineBorder(inactiveBorder, 2));
            rightBoardPanel.setBorder(BorderFactory.createLineBorder(activeBorder, 4));
        }
    }

    public void prepareNewSession() {
        lengthBox.removeAllItems();
        for (Integer len : frame.getWordService().getAvailableLengths()) {
            lengthBox.addItem(len);
        }

        player2Field.setText("");
        player2PasswordField.setText("");
        guessField.setText("");
        statusLabel.setText("PLAYER 1 IS THE LOGGED-IN USER. ENTER PLAYER 2 CREDENTIALS.");
        leftBoardTitle.setText("P1");
        rightBoardTitle.setText("P2");

        leftBoardPanel.removeAll();
        rightBoardPanel.removeAll();
        leftBoardPanel.revalidate();
        rightBoardPanel.revalidate();
        leftBoardPanel.repaint();
        rightBoardPanel.repaint();

        gameActive = false;
        player1Solved = false;
        player2Solved = false;
        guessField.setEnabled(false);
        submitButton.setEnabled(false);

        stopBanner();
        bannerLabel.setText(" ");
        leftBoardPanel.setBorder(null);
        rightBoardPanel.setBorder(null);
    }

    private void startPvpGame() {
        player1 = frame.getCurrentUser();
        if (player1 == null) {
            JOptionPane.showMessageDialog(this, "No logged-in player found.");
            return;
        }

        String p2Username = player2Field.getText().trim().toUpperCase();
        String p2Password = new String(player2PasswordField.getPassword());

        player2 = frame.getUserStore().authenticate(p2Username, p2Password);
        if (player2 == null) {
            JOptionPane.showMessageDialog(this, "Player 2 login failed.");
            return;
        }

        if (player1.getUsername().equalsIgnoreCase(player2.getUsername())) {
            JOptionPane.showMessageDialog(this, "Player 2 must be different from Player 1.");
            return;
        }

        Integer selectedLength = (Integer) lengthBox.getSelectedItem();
        if (selectedLength == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid word length.");
            return;
        }

        wordLength = selectedLength;
        player1Word = frame.getWordService().getRandomWord(wordLength);
        player2Word = frame.getWordService().getRandomWord(wordLength);

        if (player1Word == null || player2Word == null) {
            JOptionPane.showMessageDialog(this, "No words found for that length.");
            return;
        }

        p1Attempt = 0;
        p2Attempt = 0;
        player1Solved = false;
        player2Solved = false;
        player1Turn = true;
        gameActive = true;

        leftBoardTitle.setText(player1.getUsername());
        rightBoardTitle.setText(player2.getUsername());

        buildBoards(maxAttempts, wordLength);
        guessField.setEnabled(true);
        submitButton.setEnabled(true);
        guessField.setText("");
        statusLabel.setText("GAME STARTED. " + player1.getUsername() + " GOES FIRST.");
        stopBanner();
        bannerLabel.setText(" ");
        updateActivePlayerHighlight();
    }

    private void buildBoards(int rows, int cols) {
        leftBoardPanel.removeAll();
        rightBoardPanel.removeAll();

        leftBoardPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        rightBoardPanel.setLayout(new GridLayout(rows, cols, 5, 5));

        leftBoardPanel.setBackground(Color.BLACK);
        rightBoardPanel.setBackground(Color.BLACK);

        p1Cells = new JLabel[rows][cols];
        p2Cells = new JLabel[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel left = createCell();
                JLabel right = createCell();
                p1Cells[r][c] = left;
                p2Cells[r][c] = right;
                leftBoardPanel.add(left);
                rightBoardPanel.add(right);
            }
        }

        leftBoardPanel.revalidate();
        rightBoardPanel.revalidate();
        leftBoardPanel.repaint();
        rightBoardPanel.repaint();
    }

    private JLabel createCell() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(40, 40, 40));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 2));
        label.setFont(new Font("Monospaced", Font.BOLD, 20));
        return label;
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

        if (player1Turn) {
            if (p1Attempt < maxAttempts && !player1Solved) {
                processGuessForPlayer(guess, p1Cells, p1Attempt, player1Word);
                p1Attempt++;

                if (guess.equals(player1Word)) {
                    player1Solved = true;
                    finishPvp(player1, player2, player1Word);
                    return;
                }
            }

            player1Turn = false;
            if (p2Attempt >= maxAttempts || player2Solved) {
                player1Turn = true;
            }
        } else {
            if (p2Attempt < maxAttempts && !player2Solved) {
                processGuessForPlayer(guess, p2Cells, p2Attempt, player2Word);
                p2Attempt++;

                if (guess.equals(player2Word)) {
                    player2Solved = true;
                    finishPvp(player2, player1, player2Word);
                    return;
                }
            }

            player1Turn = true;
            if (p1Attempt >= maxAttempts || player1Solved) {
                player1Turn = false;
            }
        }

        if ((p1Attempt >= maxAttempts || player1Solved) && (p2Attempt >= maxAttempts || player2Solved)) {
            finishDraw();
            return;
        }

        statusLabel.setText("NOW PLAYING: " + (player1Turn ? player1.getUsername() : player2.getUsername()));
        guessField.setText("");
        updateActivePlayerHighlight();
    }

    private void processGuessForPlayer(String guess, JLabel[][] board, int attemptRow, String targetWord) {
        if (attemptRow >= maxAttempts) {
            return;
        }

        List<LetterState> result = GameEngine.evaluateGuess(guess, targetWord);

        for (int i = 0; i < wordLength; i++) {
            board[attemptRow][i].setText(String.valueOf(Character.toUpperCase(guess.charAt(i))));
            switch (result.get(i)) {
                case CORRECT:
                    board[attemptRow][i].setBackground(new Color(83, 141, 78));
                    board[attemptRow][i].setForeground(Color.WHITE);
                    break;
                case PRESENT:
                    board[attemptRow][i].setBackground(new Color(181, 159, 59));
                    board[attemptRow][i].setForeground(Color.WHITE);
                    break;
                case ABSENT:
                    board[attemptRow][i].setBackground(new Color(120, 124, 126));
                    board[attemptRow][i].setForeground(Color.WHITE);
                    break;
            }
        }
    }

    private void finishPvp(User winner, User loser, String solvedWord) {
        gameActive = false;
        guessField.setEnabled(false);
        submitButton.setEnabled(false);

        winner.incrementPvpWins();
        loser.incrementPvpLosses();

        frame.getUserStore().updateUser(winner);
        frame.getUserStore().updateUser(loser);

        if (frame.getCurrentUser() != null && frame.getCurrentUser().getUsername().equalsIgnoreCase(winner.getUsername())) {
        frame.setCurrentUser(winner);
    }   else if (frame.getCurrentUser() != null && frame.getCurrentUser().getUsername().equalsIgnoreCase(loser.getUsername())) {
        frame.setCurrentUser(loser);
    }

        String loserWord = winner.getUsername().equalsIgnoreCase(player1.getUsername())
        ? player2Word.toUpperCase()
        : player1Word.toUpperCase();

        stopBanner();
        bannerLabel.setText("WINNER: " + winner.getUsername());
        bannerLabel.setForeground(new Color(0, 255, 120));

        statusLabel.setText(
        "WINNER: " + winner.getUsername()
        + " | SOLVED: " + solvedWord.toUpperCase()
        + " | " + loser.getUsername() + ": " + loserWord
    );

        leftBoardPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 120), 4));
        rightBoardPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 120), 4));

    JOptionPane.showMessageDialog(
        this,
        "Winner: " + winner.getUsername()
        + "\nSolved word: " + solvedWord.toUpperCase()
        + "\n" + loser.getUsername() + "'s word: " + loserWord
    );
}

    private void finishDraw() {
        gameActive = false;
        guessField.setEnabled(false);
        submitButton.setEnabled(false);

        showBanner("DRAW GAME", new Color(255, 200, 0));
        statusLabel.setText(
        "DRAW. " + player1.getUsername() + ": " + player1Word.toUpperCase()
        + " | " + player2.getUsername() + ": " + player2Word.toUpperCase()
    );

        leftBoardPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 4));
        rightBoardPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 4));

    JOptionPane.showMessageDialog(
        this,
        "Draw.\nNobody solved their word."
        + "\n\n" + player1.getUsername() + "'s word: " + player1Word.toUpperCase()
        + "\n" + player2.getUsername() + "'s word: " + player2Word.toUpperCase()
       );
    }
}