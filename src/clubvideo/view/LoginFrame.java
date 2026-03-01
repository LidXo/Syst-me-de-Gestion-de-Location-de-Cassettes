package clubvideo.view;

import clubvideo.dao.TestConnection;
import clubvideo.util.UIStyles;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private JTextField tfUser;
    private JPasswordField pfPass;
    private JLabel lblError;
    private JLabel lblConnStatus;
    private JLabel lblCopyright;

    public LoginFrame() {
        super("VideoClub — Connexion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
        checkDbStatus();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyles.BG_MAIN);

        // ── Top Banner ────────────────────────────────────────────────────────
        JPanel banner = new JPanel(new GridBagLayout());
        banner.setBackground(UIStyles.PRIMARY);
        banner.setPreferredSize(new Dimension(480, 180));

        JPanel bannerContent = new JPanel();
        bannerContent.setLayout(new BoxLayout(bannerContent, BoxLayout.Y_AXIS));
        bannerContent.setOpaque(false);

        JLabel ico = new JLabel("📼", SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        ico.setForeground(Color.WHITE);
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("VIDÉO CLUB");
        appName.setFont(new Font("Segoe UI Bold", Font.PLAIN, 28));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Espace de Gestion de Location");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(0xC7D2FE)); // Indigo clair
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        bannerContent.add(ico);
        bannerContent.add(Box.createVerticalStrut(10));
        bannerContent.add(appName);
        bannerContent.add(subtitle);
        banner.add(bannerContent);

        // ── Form Area ─────────────────────────────────────────────────────────
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        formWrapper.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setPreferredSize(new Dimension(380, 340));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER, 1),
                BorderFactory.createEmptyBorder(30, 36, 40, 36)));

        JLabel lTitle = new JLabel("Connexion");
        lTitle.setFont(new Font("Segoe UI Bold", Font.PLAIN, 22));
        lTitle.setForeground(UIStyles.TEXT_DARK);
        lTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lUser = UIStyles.makeLabel("Identifiant");
        lUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfUser = UIStyles.makeField();
        tfUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfUser.setText("admin");

        JLabel lPass = UIStyles.makeLabel("Mot de passe");
        lPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        pfPass = new JPasswordField("admin123");
        pfPass.setFont(UIStyles.FONT_BODY);
        pfPass.setForeground(UIStyles.TEXT_DARK);
        pfPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        pfPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pfPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblError = new JLabel(" ");
        lblError.setFont(UIStyles.FONT_SMALL);
        lblError.setForeground(UIStyles.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnLogin = UIStyles.makeButton("Entrer dans l'espace", UIStyles.PRIMARY);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> login());

        form.add(lTitle);
        form.add(Box.createVerticalStrut(24));
        form.add(lUser);
        form.add(Box.createVerticalStrut(6));
        form.add(tfUser);
        form.add(Box.createVerticalStrut(16));
        form.add(lPass);
        form.add(Box.createVerticalStrut(6));
        form.add(pfPass);
        form.add(lblError);
        form.add(Box.createVerticalStrut(8));
        form.add(btnLogin);

        formWrapper.add(form);

        // ── Status Bar / Footer ──────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIStyles.BG_MAIN);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyles.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)));

        lblConnStatus = new JLabel("⏳ Connexion...");
        lblConnStatus.setFont(UIStyles.FONT_SMALL);
        lblConnStatus.setForeground(UIStyles.TEXT_LIGHT);

        lblCopyright = new JLabel("Version 2.1 — © 2024 VideoClub Pro");
        lblCopyright.setFont(UIStyles.FONT_SMALL);
        lblCopyright.setForeground(UIStyles.TEXT_LIGHT);

        footer.add(lblConnStatus, BorderLayout.WEST);
        footer.add(lblCopyright, BorderLayout.EAST);

        root.add(banner, BorderLayout.NORTH);
        root.add(formWrapper, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void checkDbStatus() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return TestConnection.isConnected();
            }

            @Override
            protected void done() {
                try {
                    boolean ok = get();
                    if (ok) {
                        lblConnStatus.setText("● Connexion OK");
                        lblConnStatus.setForeground(UIStyles.SUCCESS);
                    } else {
                        lblConnStatus.setText("● Connexion ÉCHEC");
                        lblConnStatus.setForeground(UIStyles.DANGER);
                    }
                } catch (Exception ignored) {
                }
            }
        };
        worker.execute();
    }

    private void login() {
        String user = tfUser.getText().trim();
        String pass = new String(pfPass.getPassword());
        if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass)) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            lblError.setText("⚠ Identifiant ou mot de passe incorrect.");
            pfPass.setText("");
            tfUser.requestFocus();
        }
    }
}
