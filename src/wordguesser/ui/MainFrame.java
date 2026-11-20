package wordguesser.ui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wordguesser.model.User;
import wordguesser.service.UserStore;
import wordguesser.service.WordService;

public class MainFrame extends JFrame {
    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String MENU = "MENU";
    public static final String STATS = "STATS";
    public static final String SINGLE = "SINGLE";
    public static final String PVP = "PVP";
    public static final String LEADERBOARD = "LEADERBOARD";

    private final CardLayout cardLayout;
    private final JPanel container;

    private final UserStore userStore;
    private final WordService wordService;

    private User currentUser;

    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final MainMenuPanel menuPanel;
    private final StatsPanel statsPanel;
    private final SinglePlayerPanel singlePlayerPanel;
    private final PvpPanel pvpPanel;
    private final LeaderboardPanel leaderboardPanel;

    public MainFrame() {
        setTitle("Word Guesser");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userStore = new UserStore();
        wordService = new WordService();

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        menuPanel = new MainMenuPanel(this);
        statsPanel = new StatsPanel(this);
        singlePlayerPanel = new SinglePlayerPanel(this);
        pvpPanel = new PvpPanel(this);
        leaderboardPanel = new LeaderboardPanel(this);

        container.add(loginPanel, LOGIN);
        container.add(registerPanel, REGISTER);
        container.add(menuPanel, MENU);
        container.add(statsPanel, STATS);
        container.add(singlePlayerPanel, SINGLE);
        container.add(pvpPanel, PVP);
        container.add(leaderboardPanel, LEADERBOARD);

        add(container);
        showScreen(LOGIN);
    }

    public void showScreen(String name) {
        if (MENU.equals(name)) {
            menuPanel.refresh();
        } else if (STATS.equals(name)) {
            statsPanel.refresh();
        } else if (SINGLE.equals(name)) {
            singlePlayerPanel.prepareNewSession();
        } else if (PVP.equals(name)) {
            pvpPanel.prepareNewSession();
        } else if (LEADERBOARD.equals(name)) {
            leaderboardPanel.refresh();
        }
        cardLayout.show(container, name);
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public WordService getWordService() {
        return wordService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void logout() {
        currentUser = null;
        showScreen(LOGIN);
    }
}