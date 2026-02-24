package clubvideo.view;

import clubvideo.util.UIStylesDark;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private static final String PAGE_ACCUEIL    = "accueil";
    private static final String PAGE_CASSETTES  = "cassettes";
    private static final String PAGE_ABONNES    = "abonnes";
    private static final String PAGE_LOCATIONS  = "locations";
    private static final String PAGE_CATEGORIES = "categories";
    private static final String PAGE_CONTACT    = "contact";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);

    private final AccueilPanel   accueilPanel   = new AccueilPanel();
    private final CassettePanel  cassettePanel  = new CassettePanel();
    private final AbonneePanel   abonneePanel   = new AbonneePanel();
    private final LocationPanel  locationPanel  = new LocationPanel();
    private final CategoriePanel categoriePanel = new CategoriePanel();
    private final ContactPanel   contactPanel   = new ContactPanel();

    private String currentPage = PAGE_ACCUEIL;
    private final java.util.Map<String, JButton> navButtons = new java.util.LinkedHashMap<>();

    public MainFrame() {
        setTitle("Vidéo Club — Système de Gestion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStylesDark.BG);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(),    BorderLayout.CENTER);

        navigate(PAGE_ACCUEIL);
    }

    // ── SIDEBAR ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UIStylesDark.BG2); // White
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIStylesDark.BORDER));
        sidebar.setPreferredSize(new Dimension(260, 0));

        // Logo
        JPanel logo = new JPanel(new BorderLayout(0, 4));
        logo.setBackground(UIStylesDark.BG2);
        logo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIStylesDark.BORDER),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        JLabel title = new JLabel("🎬  VIDÉO CLUB");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(UIStylesDark.ACCENT);
        
        JLabel sub   = new JLabel("Gestion de location de cassettes vidéo.");
        sub.setFont(UIStylesDark.FONT_SMALL);
        sub.setForeground(UIStylesDark.MUTED);
        
        logo.add(title, BorderLayout.CENTER);
        logo.add(sub,   BorderLayout.SOUTH);

        // Nav
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(UIStylesDark.BG2);
        nav.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        nav.add(sectionLabel("NAVIGATION"));
        nav.add(navButton("🏠", "Tableau de bord", PAGE_ACCUEIL));

        nav.add(Box.createVerticalStrut(8));
        nav.add(sectionLabel("GESTION"));
        nav.add(navButton("📼", "Cassettes",    PAGE_CASSETTES));
        nav.add(navButton("👤", "Abonnés",      PAGE_ABONNES));
        nav.add(navButton("🔄", "Locations",    PAGE_LOCATIONS));

        nav.add(Box.createVerticalStrut(8));
        nav.add(sectionLabel("CATALOGUES"));
        nav.add(navButton("🗂️", "Catégories",  PAGE_CATEGORIES));

        nav.add(Box.createVerticalStrut(8));
        nav.add(sectionLabel("AUTRE"));
        nav.add(navButton("✉️", "Contact",      PAGE_CONTACT));
        
        nav.add(Box.createVerticalGlue());

        // Bottom: user info + logout
        JPanel bottom = new JPanel(new BorderLayout(0, 12));
        bottom.setBackground(UIStylesDark.BG2);
        bottom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UIStylesDark.BORDER),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)));

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        userInfo.setBackground(UIStylesDark.BG2);
        
        JLabel avatar = new JLabel("A");
        avatar.setFont(new Font("SansSerif", Font.BOLD, 14));
        avatar.setForeground(Color.WHITE);
        avatar.setBackground(UIStylesDark.ACCENT);
        avatar.setOpaque(true);
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        // Circle effect hack (border radius not easy in Swing without custom paint)
        avatar.setBorder(BorderFactory.createEmptyBorder()); 
        
        JPanel userText = new JPanel(new GridLayout(2, 1));
        userText.setBackground(UIStylesDark.BG2);
        JLabel uname = new JLabel("Administrateur");
        uname.setFont(new Font("SansSerif", Font.BOLD, 13));
        uname.setForeground(UIStylesDark.TEXT);
        JLabel urole = new JLabel("Connecté");
        urole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        urole.setForeground(UIStylesDark.GREEN);
        
        userText.add(uname);
        userText.add(urole);
        
        userInfo.add(avatar); 
        userInfo.add(userText);

        JButton logout = new JButton("Déconnexion");
        logout.setBackground(UIStylesDark.BG2);
        logout.setForeground(UIStylesDark.RED);
        logout.setFont(UIStylesDark.FONT_SMALL);
        logout.setFocusPainted(false);
        logout.setBorder(BorderFactory.createLineBorder(new Color(252, 165, 165), 1));
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.setOpaque(true);
        logout.setPreferredSize(new Dimension(0, 32));
        
        logout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Se déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });

        bottom.add(userInfo, BorderLayout.NORTH);
        bottom.add(logout,   BorderLayout.SOUTH);

        JScrollPane navScroll = new JScrollPane(nav);
        navScroll.setBorder(null);
        navScroll.setBackground(UIStylesDark.BG2);
        navScroll.getViewport().setBackground(UIStylesDark.BG2);
        navScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        sidebar.add(logo,     BorderLayout.NORTH);
        sidebar.add(navScroll, BorderLayout.CENTER);
        sidebar.add(bottom,   BorderLayout.SOUTH);
        return sidebar;
    }

    private JComponent sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(UIStylesDark.MUTED);
        l.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton navButton(String icon, String label, String pageId) {
        JButton b = new JButton(icon + "   " + label);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setForeground(UIStylesDark.MUTED);
        b.setBackground(UIStylesDark.BG2);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        // Padding inside button
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        b.addActionListener(e -> navigate(pageId));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                if (!pageId.equals(currentPage)) { 
                    b.setBackground(UIStylesDark.BG3); 
                } 
            }
            public void mouseExited(MouseEvent e)  { 
                if (!pageId.equals(currentPage)) { 
                    b.setBackground(UIStylesDark.BG2); 
                } 
            }
        });
        navButtons.put(pageId, b);
        return b;
    }

    // ── MAIN CONTENT ──────────────────────────────────────────────────────────
    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStylesDark.BG);

        // Topbar
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(UIStylesDark.BG2);
        topbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIStylesDark.BORDER),
            BorderFactory.createEmptyBorder(0, 32, 0, 32)));
        topbar.setPreferredSize(new Dimension(0, 64));

        JLabel pageTitle = new JLabel("Tableau de bord");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        pageTitle.setForeground(UIStylesDark.TEXT);
        topbar.add(pageTitle, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEEE dd MMMM yyyy", java.util.Locale.FRENCH).format(new java.util.Date()));
        dateLabel.setFont(UIStylesDark.FONT_BODY);
        dateLabel.setForeground(UIStylesDark.MUTED);
        topbar.add(dateLabel, BorderLayout.EAST);

        this.cardPanel.putClientProperty("pageTitleLabel", pageTitle);

        // Cards
        cardPanel.setBackground(UIStylesDark.BG);
        cardPanel.add(accueilPanel,   PAGE_ACCUEIL);
        cardPanel.add(cassettePanel,  PAGE_CASSETTES);
        cardPanel.add(abonneePanel,   PAGE_ABONNES);
        cardPanel.add(locationPanel,  PAGE_LOCATIONS);
        cardPanel.add(categoriePanel, PAGE_CATEGORIES);
        cardPanel.add(contactPanel,   PAGE_CONTACT);

        main.add(topbar,    BorderLayout.NORTH);
        main.add(cardPanel, BorderLayout.CENTER);
        return main;
    }

    // ── NAVIGATION ────────────────────────────────────────────────────────────
    private final java.util.Map<String, String> pageTitles = new java.util.HashMap<String, String>() {{
        put(PAGE_ACCUEIL,    "Tableau de bord");
        put(PAGE_CASSETTES,  "Gestion des Cassettes");
        put(PAGE_ABONNES,    "Gestion des Abonnés");
        put(PAGE_LOCATIONS,  "Gestion des Locations");
        put(PAGE_CATEGORIES, "Gestion des Catégories");
        put(PAGE_CONTACT,    "Contact");
    }};

    private void navigate(String pageId) {
        currentPage = pageId;
        cardLayout.show(cardPanel, pageId);

        // Update nav button styles
        navButtons.forEach((id, btn) -> {
            if (id.equals(pageId)) {
                // Active state: Light Indigo background + Accent text + Left border
                btn.setBackground(new Color(49, 46, 129)); // Indigo 900 (Darker for dark theme)
                btn.setForeground(UIStylesDark.ACCENT);
                btn.setFont(new Font("SansSerif", Font.BOLD, 14));
                // Left border indicator
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, UIStylesDark.ACCENT),
                    BorderFactory.createEmptyBorder(10, 12, 10, 16)));
            } else {
                // Inactive state
                btn.setBackground(UIStylesDark.BG2);
                btn.setForeground(UIStylesDark.MUTED);
                btn.setFont(new Font("SansSerif", Font.BOLD, 14));
                btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            }
        });

        // Update page title
        JLabel lbl = (JLabel) cardPanel.getClientProperty("pageTitleLabel");
        if (lbl != null) lbl.setText(pageTitles.getOrDefault(pageId, ""));

        // Refresh the relevant panel
        switch (pageId) {
            case PAGE_ACCUEIL:    accueilPanel.refresh();   break;
            case PAGE_CASSETTES:  cassettePanel.refresh();  break;
            case PAGE_ABONNES:    abonneePanel.refresh();   break;
            case PAGE_LOCATIONS:  locationPanel.refresh();  break;
            case PAGE_CATEGORIES: categoriePanel.refresh(); break;
        }
    }
}
