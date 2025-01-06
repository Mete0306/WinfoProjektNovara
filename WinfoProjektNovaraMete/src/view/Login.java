package view;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    // --------- Komponenten für Login ---------
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // --------- Konstruktor ---------
    public Login() {
        setTitle("HighSpeed Login");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#1E1E1E"));

        initializeComponents();

        setLocationRelativeTo(null); 
        setVisible(true);
    }

    // --------- Initialisierungsmethoden ---------
    private void initializeComponents() {
        JLabel highSpeedLabel = new JLabel("HighSpeed GmbH");
        highSpeedLabel.setForeground(Color.WHITE);
        highSpeedLabel.setFont(new Font("Arial", Font.BOLD, 24));
        highSpeedLabel.setBounds(20, 20, highSpeedLabel.getPreferredSize().width, highSpeedLabel.getPreferredSize().height);
        add(highSpeedLabel);

        JLabel madeByLabel = new JLabel("- Made by Novara -");
        madeByLabel.setForeground(Color.LIGHT_GRAY);
        madeByLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        int highSpeedLabelWidth = highSpeedLabel.getPreferredSize().width;
        int madeByLabelWidth = madeByLabel.getPreferredSize().width;
        int xPosition = highSpeedLabel.getX() + (highSpeedLabelWidth - madeByLabelWidth) / 2;
        madeByLabel.setBounds(xPosition, highSpeedLabel.getY() + highSpeedLabel.getHeight() + 5, madeByLabelWidth, madeByLabel.getPreferredSize().height);
        add(madeByLabel);

        JLabel loginLabel = new JLabel("Log in", SwingConstants.CENTER);
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 36));
        loginLabel.setBounds(350, 150, 300, 50);
        add(loginLabel);

        JLabel usernameLabel = new JLabel("Benutzername");
        usernameLabel.setForeground(Color.LIGHT_GRAY);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setBounds(350, 250, 300, 30);
        add(usernameLabel);

        usernameField = new JTextField("username@example.com");
        usernameField.setBounds(350, 290, 300, 50);
        usernameField.setBackground(Color.decode("#333333"));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.decode("#444444")));
        add(usernameField);

        JLabel passwordLabel = new JLabel("Passwort");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setBounds(350, 370, 300, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(350, 410, 300, 50);
        passwordField.setBackground(Color.decode("#333333"));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setEchoChar('*');
        passwordField.setBorder(BorderFactory.createLineBorder(Color.decode("#444444")));
        add(passwordField);

        loginButton = new JButton("Enter");
        loginButton.setBounds(350, 490, 300, 50);
        loginButton.setBackground(Color.decode("#333333"));
        loginButton.setForeground(Color.LIGHT_GRAY);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        add(loginButton);
    }

    // --------- Getter-Methoden für Main-Klasse ---------
    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
