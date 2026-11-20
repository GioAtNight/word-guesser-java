package wordguesser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import wordguesser.model.User;

public class LoginPanel extends JPanel {
    private final MainFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("WORD GUESSER LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setForeground(new Color(0, 255, 180));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 12, 12));
        form.setBackground(Color.BLACK);
        form.setBorder(BorderFactory.createEmptyBorder(60, 260, 60, 260));

        JLabel userLabel = createLabel("3-LETTER NAME:");
        JLabel passLabel = createLabel("PASSWORD:");

        usernameField = createTextField();
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 18));

        JButton loginButton = createArcadeButton("LOGIN");
        JButton registerButton = createArcadeButton("REGISTER");

        form.add(userLabel);
        form.add(usernameField);
        form.add(passLabel);
        form.add(passwordField);
        form.add(loginButton);
        form.add(registerButton);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> frame.showScreen(MainFrame.REGISTER));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Monospaced", Font.BOLD, 18));
        return field;
    }

    private JButton createArcadeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 18));
        button.setBackground(new Color(20, 20, 20));
        button.setForeground(new Color(0, 255, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 180), 3));
        return button;
    }

    private void login() {
        String username = usernameField.getText().trim().toUpperCase();
        String password = new String(passwordField.getPassword());

        User user = frame.getUserStore().authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid profile name or password.");
            return;
        }

        frame.setCurrentUser(user);
        usernameField.setText("");
        passwordField.setText("");
        frame.showScreen(MainFrame.MENU);
    }
}