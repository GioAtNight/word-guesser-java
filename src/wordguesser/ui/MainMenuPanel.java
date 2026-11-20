package wordguesser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import wordguesser.model.User;

public class MainMenuPanel extends JPanel {
    private final MainFrame frame;
    private final JLabel welcomeLabel;

    public MainMenuPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("WORD GUESSER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 255, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));

        welcomeLabel = new JLabel("", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.BLACK);
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(welcomeLabel, BorderLayout.SOUTH);

        JPanel buttons = new JPanel(new GridLayout(5, 1, 15, 15));
        buttons.setBackground(Color.BLACK);
        buttons.setBorder(BorderFactory.createEmptyBorder(60, 260, 60, 260));

        JButton singleButton = createArcadeButton("SINGLE PLAYER");
        JButton pvpButton = createArcadeButton("PLAYER VS PLAYER");
        JButton statsButton = createArcadeButton("VIEW STATS");
        JButton leaderboardButton = createArcadeButton("LEADERBOARD");
        JButton logoutButton = createArcadeButton("LOGOUT");

        buttons.add(singleButton);
        buttons.add(pvpButton);
        buttons.add(statsButton);
        buttons.add(leaderboardButton);
        buttons.add(logoutButton);

        add(northPanel, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);

        singleButton.addActionListener(e -> frame.showScreen(MainFrame.SINGLE));
        pvpButton.addActionListener(e -> frame.showScreen(MainFrame.PVP));
        statsButton.addActionListener(e -> frame.showScreen(MainFrame.STATS));
        leaderboardButton.addActionListener(e -> frame.showScreen(MainFrame.LEADERBOARD));
        logoutButton.addActionListener(e -> frame.logout());
    }

    private JButton createArcadeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setBackground(new Color(20, 20, 20));
        button.setForeground(new Color(0, 255, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 3));
        return button;
    }

    public void refresh() {
        User user = frame.getCurrentUser();
        welcomeLabel.setText("PLAYER: " + (user != null ? user.getUsername() : "---"));
    }
}