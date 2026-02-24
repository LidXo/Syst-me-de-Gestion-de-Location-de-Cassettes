package clubvideo.view;

import clubvideo.util.UIStylesDark;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JLabel lblError;

    public LoginFrame() {
        setTitle("Vidéo Club — Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStylesDark.BG);
        setContentPane(root);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStylesDark.BG2);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(4, 0, 0, 0, UIStylesDark.ACCENT),
            BorderFactory.createEmptyBorder(48, 48, 48, 48)));
        card.setPreferredSize(new Dimension(400, 480));
        card.setMaximumSize(new Dimension(400, 480));

        // Logo
        JLabel logo = new JLabel("🎬 VIDÉO CLUB", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.BOLD, 32));
        logo.setForeground(UIStylesDark.ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Système de Gestion — HIT-T", SwingConstants.CENTER);
        subtitle.setFont(UIStylesDark.FONT_SMALL);
        subtitle.setForeground(UIStylesDark.MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Champs
        JLabel lblU = UIStylesDark.sectionLabel("IDENTIFIANT");
        txtUser = UIStylesDark.styledField(20);
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel lblP = UIStylesDark.sectionLabel("MOT DE PASSE");
        txtPass = UIStylesDark.styledPassword(20);
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        lblError = new JLabel(" ");
        lblError.setFont(UIStylesDark.FONT_SMALL);
        lblError.setForeground(UIStylesDark.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogin = UIStylesDark.accentButton("SE CONNECTER");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Démo : admin / 1234", SwingConstants.CENTER);
        hint.setFont(UIStylesDark.FONT_SMALL);
        hint.setForeground(UIStylesDark.MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        ActionListener loginAction = e -> doLogin();
        btnLogin.addActionListener(loginAction);
        txtUser.addActionListener(loginAction);
        txtPass.addActionListener(loginAction);

        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(40));
        card.add(lblU);
        card.add(Box.createVerticalStrut(8));
        card.add(txtUser);
        card.add(Box.createVerticalStrut(20));
        card.add(lblP);
        card.add(Box.createVerticalStrut(8));
        card.add(txtPass);
        card.add(Box.createVerticalStrut(12));
        card.add(lblError);
        card.add(Box.createVerticalStrut(12));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(24));
        card.add(hint);

        root.add(card);
    }

    private void doLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if ("admin".equals(user) && "1234".equals(pass)) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            lblError.setText("Identifiants incorrects !");
            txtPass.setText("");
        }
    }
}
