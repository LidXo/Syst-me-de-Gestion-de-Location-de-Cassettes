package gui;

import util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * LoginFrame - Écran d'authentification
 *
 * Page de connexion pour accéder à l'application Club Vidéo.
 * Authentification simple : admin/admin
 * Design moderne et épuré.
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.0
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private Color primaryColor = new Color(41, 128, 185);
    private Color hoverColor = new Color(52, 152, 219);

    public LoginFrame() {
        setTitle("Club Vidéo - Authentification");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Retire la barre de titre standard

        createLoginPanel();
        setVisible(true);
    }

    /**
     * Crée le panel d'authentification
     */
    private void createLoginPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Panel titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel title = new JLabel("CLUB VIDÉO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Panel formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Identifiant");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setForeground(Color.GRAY);
        
        txtUsername = new JTextField(15);
        txtUsername.setText("admin");
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblPassword = new JLabel("Mot de passe");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(Color.GRAY);
        
        txtPassword = new JPasswordField(15);
        txtPassword.setText("admin");
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(txtUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(txtPassword, gbc);

        // Panel boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        btnLogin = createStyledButton("CONNEXION", primaryColor);
        btnLogin.addActionListener(e -> login());

        btnCancel = createStyledButton("QUITTER", new Color(149, 165, 166));
        btnCancel.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(btnCancel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}