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

public class RegisterPanel extends JPanel {
    private final MainFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    public RegisterPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("CREATE PROFILE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setForeground(new Color(0, 255, 180));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 12, 12));
        form.setBackground(Color.BLACK);
        form.setBorder(BorderFactory.createEmptyBorder(50, 240, 50, 240));

        JLabel userLabel = createLabel("3-LETTER NAME:");
        JLabel passLabel = createLabel("PASSWORD:");
        JLabel confirmLabel = createLabel("CONFIRM:");

        usernameField = createTextField();
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 18));

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Monospaced", Font.BOLD, 18));

        JButton registerButton = createArcadeButton("CREATE");
        JButton backButton = createArcadeButton("BACK");

        form.add(userLabel);
        form.add(usernameField);
        form.add(passLabel);
        form.add(passwordField);
        form.add(confirmLabel);
        form.add(confirmPasswordField);
        form.add(registerButton);
        form.add(backButton);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> frame.showScreen(MainFrame.LOGIN));
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

    private void register() {
        String username = usernameField.getText().trim().toUpperCase();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!username.matches("[A-Z]{3}")) {
            JOptionPane.showMessageDialog(this, "Profile name must be exactly 3 letters.");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        boolean registered = frame.getUserStore().registerUser(username, password);
        if (!registered) {
            JOptionPane.showMessageDialog(this, "Registration failed. Name may already exist.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Profile created.");
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        frame.showScreen(MainFrame.LOGIN);
    }
}