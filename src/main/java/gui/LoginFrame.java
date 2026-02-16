package gui;

import util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;

/**
 * LoginFrame - Écran d'authentification
 *
 * Page de connexion pour accéder à l'application Club Vidéo.
 * Authentification simple : admin/admin
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;

    public LoginFrame() {
        setTitle("Club Vidéo - Authentification");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        createLoginPanel();
        setVisible(true);
    }

    /**
     * Crée le panel d'authentification
     */
    private void createLoginPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Panel titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel title = new JLabel("Club Vidéo - Connexion");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Panel formulaire
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel lblUsername = new JLabel("Identifiant :");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUsername = new JTextField();
        txtUsername.setText("admin");
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblPassword = new JLabel("Mot de passe :");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword = new JPasswordField();
        txtPassword.setText("admin");
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));

        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);

        // Panel boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        btnLogin = new JButton("Connexion");
        btnLogin.setPreferredSize(new Dimension(130, 40));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> login());

        btnCancel = new JButton("Quitter");
        btnCancel.setPreferredSize(new Dimension(130, 40));
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(btnCancel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Valide les identifiants et lance MainFrame
     */
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer votre identifiant et mot de passe",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authentification simple
        if ("admin".equals(username) && "admin".equals(password)) {
            try {
                // Tester la connexion à la base
                DatabaseConnection.getInstance().testConnection();

                JOptionPane.showMessageDialog(this,
                        "✓ Connexion réussie !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainFrame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "✗ Erreur de connexion à la base de données :\n" + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "✗ Identifiant ou mot de passe incorrect",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }

    /**
     * Point d'entrée de l'application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}