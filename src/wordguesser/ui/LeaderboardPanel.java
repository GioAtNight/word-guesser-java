package wordguesser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import wordguesser.model.User;

public class LeaderboardPanel extends JPanel {
    private final MainFrame frame;

    private final JPanel leaderboardListPanel;
    private final JLabel firstPlaceLabel;
    private final JLabel secondPlaceLabel;
    private final JLabel thirdPlaceLabel;

    public LeaderboardPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setForeground(new Color(0, 255, 180));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JPanel podiumPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        podiumPanel.setBackground(Color.BLACK);
        podiumPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        secondPlaceLabel = createPodiumLabel("2ND\n---", new Color(192, 192, 192));
        firstPlaceLabel = createPodiumLabel("1ST\n---", new Color(255, 215, 0));
        thirdPlaceLabel = createPodiumLabel("3RD\n---", new Color(205, 127, 50));

        podiumPanel.add(secondPlaceLabel);
        podiumPanel.add(firstPlaceLabel);
        podiumPanel.add(thirdPlaceLabel);

        leaderboardListPanel = new JPanel();
        leaderboardListPanel.setLayout(new BoxLayout(leaderboardListPanel, BoxLayout.Y_AXIS));
        leaderboardListPanel.setBackground(Color.BLACK);
        leaderboardListPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        JScrollPane scrollPane = new JScrollPane(leaderboardListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 2));
        scrollPane.getViewport().setBackground(Color.BLACK);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        backButton.addActionListener(e -> frame.showScreen(MainFrame.MENU));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.add(backButton);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.BLACK);
        wrapperPanel.add(podiumPanel, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        wrapperPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(title, BorderLayout.NORTH);
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private JLabel createPodiumLabel(String text, Color color) {
        JLabel label = new JLabel("<html><center>" + text.replace("\n", "<br>") + "</center></html>", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(20, 20, 20));
        label.setForeground(color);
        label.setFont(new Font("Monospaced", Font.BOLD, 22));
        label.setBorder(BorderFactory.createLineBorder(color, 3));
        return label;
    }

    public void refresh() {
        List<User> users = frame.getUserStore().loadUsers();

        users.sort(
            Comparator.comparingInt((User u) -> u.getWins() + u.getPvpWins())
                      .reversed()
                      .thenComparing(User::getUsername)
        );

        updatePodium(users);
        updateLeaderboardList(users);
    }

    private void updatePodium(List<User> users) {
        firstPlaceLabel.setText(formatPodiumText("1ST", users, 0));
        secondPlaceLabel.setText(formatPodiumText("2ND", users, 1));
        thirdPlaceLabel.setText(formatPodiumText("3RD", users, 2));
    }

    private String formatPodiumText(String rank, List<User> users, int index) {
        if (index >= users.size()) {
            return "<html><center>" + rank + "<br>---</center></html>";
        }

        User user = users.get(index);
        int totalWins = user.getWins() + user.getPvpWins();

        return "<html><center>" + rank + "<br>" + user.getUsername() + "<br>" + totalWins + " WINS</center></html>";
    }

    private void updateLeaderboardList(List<User> users) {
        leaderboardListPanel.removeAll();

        JLabel header = new JLabel(
            String.format("%-6s %-8s %-10s %-10s %-10s", "RANK", "NAME", "SP_WINS", "PVP_WINS", "TOTAL"),
            SwingConstants.LEFT
        );
        header.setFont(new Font("Monospaced", Font.BOLD, 16));
        header.setForeground(new Color(0, 255, 180));
        header.setAlignmentX(LEFT_ALIGNMENT);
        leaderboardListPanel.add(header);
        leaderboardListPanel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int totalWins = user.getWins() + user.getPvpWins();

            JLabel row = new JLabel(
                String.format("%-6d %-8s %-10d %-10d %-10d",
                    i + 1,
                    user.getUsername(),
                    user.getWins(),
                    user.getPvpWins(),
                    totalWins
                )
            );

            row.setFont(new Font("Monospaced", Font.BOLD, 15));
            row.setOpaque(true);
            row.setAlignmentX(LEFT_ALIGNMENT);
            row.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            if (i == 0) {
                row.setBackground(new Color(60, 45, 0));
                row.setForeground(new Color(255, 215, 0));
            } else if (i == 1) {
                row.setBackground(new Color(50, 50, 50));
                row.setForeground(new Color(220, 220, 220));
            } else if (i == 2) {
                row.setBackground(new Color(70, 40, 20));
                row.setForeground(new Color(205, 127, 50));
            } else {
                row.setBackground(new Color(15, 15, 15));
                row.setForeground(Color.WHITE);
            }

            leaderboardListPanel.add(row);
            leaderboardListPanel.add(Box.createVerticalStrut(5));
        }

        leaderboardListPanel.revalidate();
        leaderboardListPanel.repaint();
    }
}