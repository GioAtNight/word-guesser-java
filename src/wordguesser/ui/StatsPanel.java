package wordguesser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import wordguesser.model.User;

public class StatsPanel extends JPanel {
    private final MainFrame frame;
    private final JTextArea statsArea;

    public StatsPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("PLAYER STATS", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(new Color(0, 255, 180));
        title.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));

        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.BOLD, 18));
        statsArea.setBackground(new Color(10, 10, 10));
        statsArea.setForeground(Color.WHITE);
        statsArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 2));
        scrollPane.getViewport().setBackground(new Color(10, 10, 10));

        JButton backButton = new JButton("BACK");
        backButton.setFont(new Font("Monospaced", Font.BOLD, 18));
        backButton.setBackground(new Color(20, 20, 20));
        backButton.setForeground(new Color(0, 255, 180));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 3));
        backButton.addActionListener(e -> frame.showScreen(MainFrame.MENU));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.add(backButton);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        User user = frame.getCurrentUser();
        if (user == null) {
            statsArea.setText("NO PLAYER LOGGED IN.");
            return;
        }

        int totalWins = user.getWins() + user.getPvpWins();
        int totalLosses = user.getLosses() + user.getPvpLosses();

        StringBuilder sb = new StringBuilder();
        sb.append("PROFILE: ").append(user.getUsername()).append("\n\n");
        sb.append("SINGLE PLAYER\n");
        sb.append("-------------------------\n");
        sb.append("GAMES PLAYED : ").append(user.getGamesPlayed()).append("\n");
        sb.append("WINS         : ").append(user.getWins()).append("\n");
        sb.append("LOSSES       : ").append(user.getLosses()).append("\n\n");

        sb.append("PLAYER VS PLAYER\n");
        sb.append("-------------------------\n");
        sb.append("PVP WINS     : ").append(user.getPvpWins()).append("\n");
        sb.append("PVP LOSSES   : ").append(user.getPvpLosses()).append("\n\n");

        sb.append("TOTAL RECORD\n");
        sb.append("-------------------------\n");
        sb.append("TOTAL WINS   : ").append(totalWins).append("\n");
        sb.append("TOTAL LOSSES : ").append(totalLosses).append("\n");

        statsArea.setText(sb.toString());
    }
}