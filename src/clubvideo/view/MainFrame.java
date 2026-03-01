package clubvideo.view;

import clubvideo.util.UIStyles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private JPanel contentArea;
    private JPanel sidebar;
    private CardLayout cardLayout;

    // Tous les panels
    private AccueilPanel accueilPanel;
    private CassettePanel cassettePanel;
    private AbonnePanel abonnePanel;
    private CategoriePanel categoriePanel;
    private LocationPanel locationPanel;
    private ContactPanel contactPanel;

    public MainFrame() {
        super("VideoClub Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 850);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        UIStyles.applyLookAndFeel();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 0));

        // ── Instanciation des panels ──────────────────────────────────────────
        accueilPanel = new AccueilPanel();
        cassettePanel = new CassettePanel();
        abonnePanel = new AbonnePanel();
        categoriePanel = new CategoriePanel();
        locationPanel = new LocationPanel();
        contactPanel = new ContactPanel();

        // ── Zone de contenu ───────────────────────────────────────────────────
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIStyles.BG_MAIN);
        contentArea.add(accueilPanel, "ACCUEIL");
        contentArea.add(cassettePanel, "CASSETTE");
        contentArea.add(abonnePanel, "ABONNEE");
        contentArea.add(categoriePanel, "CATEGORIE");
        contentArea.add(locationPanel, "LOCATION");
        contentArea.add(contactPanel, "CONTACT");

        // ── Sidebar ───────────────────────────────────────────────────────────
        sidebar = buildSidebar();

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);

        // Panel par défaut et refresh initial massif
        showPanel("ACCUEIL");
        refreshAll();
    }

    private void refreshAll() {
        accueilPanel.refresh();
        cassettePanel.refresh();
        abonnePanel.refresh();
        categoriePanel.refresh();
        locationPanel.refresh();
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(UIStyles.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIStyles.BORDER));

        // ── Top Area: Logo ────────────────────────────────────────────────────
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 2));
        top.setBackground(UIStyles.BG_SIDEBAR);
        top.setBorder(BorderFactory.createEmptyBorder(30, 24, 30, 24));

        JLabel logo = new JLabel("VIDÉO CLUB");
        logo.setFont(new Font("Segoe UI Bold", Font.PLAIN, 26));
        logo.setForeground(UIStyles.PRIMARY);

        JLabel subLogo = new JLabel("Gestion de location de Cassettes");
        subLogo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLogo.setForeground(UIStyles.TEXT_LIGHT);

        top.add(logo);
        top.add(subLogo);

        // ── Center Area: Navigation Sections ──────────────────────────────────
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(UIStyles.BG_SIDEBAR);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(0, 16, 20, 16));

        // Section NAVIGATION
        scrollContent.add(makeSectionLabel("NAVIGATION"));
        scrollContent.add(Box.createVerticalStrut(8));
        scrollContent.add(makeNavBtn("Tableau de bord", "ACCUEIL"));
        scrollContent.add(Box.createVerticalStrut(24));

        // Section GESTION
        scrollContent.add(makeSectionLabel("GESTION"));
        scrollContent.add(Box.createVerticalStrut(8));
        scrollContent.add(makeNavBtn("Cassettes", "CASSETTE"));
        scrollContent.add(Box.createVerticalStrut(4));
        scrollContent.add(makeNavBtn("Abonnés", "ABONNEE"));
        scrollContent.add(Box.createVerticalStrut(4));
        scrollContent.add(makeNavBtn("Locations", "LOCATION"));
        scrollContent.add(Box.createVerticalStrut(24));

        // Section CATALOGUES
        scrollContent.add(makeSectionLabel("CATALOGUES"));
        scrollContent.add(Box.createVerticalStrut(8));
        scrollContent.add(makeNavBtn("Catégories", "CATEGORIE"));
        scrollContent.add(Box.createVerticalStrut(4));
        scrollContent.add(makeNavBtn("Support", "CONTACT"));

        // ── Bottom Area: Profile & Logout ─────────────────────────────────────
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(UIStyles.BG_SIDEBAR);
        bottom.setBorder(BorderFactory.createEmptyBorder(20, 16, 24, 16));

        // Profile Card
        JPanel profile = new JPanel(new BorderLayout(15, 0));
        profile.setOpaque(true);
        profile.setBackground(UIStyles.BG_SIDEBAR);
        profile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        profile.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar (Lettre A dans un rond indigo)
        JLabel avatar = new JLabel("A", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Bold", Font.PLAIN, 18));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(UIStyles.PRIMARY);
        avatar.setPreferredSize(new Dimension(44, 44));
        // Note: On simule le rond par un rendu custom si besoin, ici on reste simple
        profile.add(avatar, BorderLayout.WEST);

        JPanel pInfo = new JPanel(new GridLayout(2, 1, 0, 0));
        pInfo.setBackground(UIStyles.BG_SIDEBAR);
        JLabel uName = new JLabel("Administrateur");
        uName.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        uName.setForeground(UIStyles.TEXT_DARK);
        JLabel uStatus = new JLabel("● Connecté");
        uStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        uStatus.setForeground(UIStyles.SUCCESS);
        pInfo.add(uName);
        pInfo.add(uStatus);
        profile.add(pInfo, BorderLayout.CENTER);

        bottom.add(profile);
        bottom.add(Box.createVerticalStrut(20));

        // Logout Button
        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.setFont(UIStyles.FONT_NAV);
        btnLogout.setForeground(UIStyles.DANGER);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xFEE2E2), 1), // Bordure légère rouge
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });

        bottom.add(btnLogout);

        sidebar.add(top, BorderLayout.NORTH);
        sidebar.add(scrollContent, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);
        return sidebar;
    }

    private JButton makeNavBtn(String label, String key) {
        JButton btn = new JButton(label);
        btn.setFont(UIStyles.FONT_NAV);
        btn.setForeground(UIStyles.TEXT_MEDIUM);
        btn.setBackground(UIStyles.BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        btn.addActionListener(e -> showPanel(key));
        btn.setActionCommand(key);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(UIStyles.BG_NAV_ACTIVE)) {
                    btn.setBackground(new Color(0xF8FAFC));
                    btn.setForeground(UIStyles.TEXT_DARK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(UIStyles.BG_NAV_ACTIVE)) {
                    btn.setBackground(UIStyles.BG_SIDEBAR);
                    btn.setForeground(UIStyles.TEXT_MEDIUM);
                }
            }
        });
        return btn;
    }

    private JLabel makeSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI Bold", Font.PLAIN, 11));
        l.setForeground(UIStyles.TEXT_LIGHT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        l.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        return l;
    }

    public void showPanel(String key) {
        cardLayout.show(contentArea, key);

        // Mettre à jour l'apparence des boutons de nav
        if (sidebar != null) {
            for (Component c : sidebar.getComponents()) {
                if (c instanceof JButton) {
                    JButton b = (JButton) c;
                    if (b.getActionCommand() != null && b.getActionCommand().equals(key)) {
                        b.setForeground(UIStyles.PRIMARY);
                        b.setBackground(Color.WHITE);
                        b.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
                    } else {
                        b.setForeground(UIStyles.TEXT_MEDIUM);
                        b.setBackground(UIStyles.BG_SIDEBAR);
                        b.setFont(UIStyles.FONT_NAV);
                    }
                }
            }
        }

        // Rafraîchir le panel affiché
        for (Component c : contentArea.getComponents()) {
            if (c.isVisible() && c instanceof Refreshable)
                ((Refreshable) c).refresh();
        }
    }
}
