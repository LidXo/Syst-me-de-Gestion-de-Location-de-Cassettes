package clubvideo.view;

import clubvideo.dao.*;
import clubvideo.util.UIStylesDark;
import javax.swing.*;
import java.awt.*;

public class AccueilPanel extends JPanel {

    public AccueilPanel() {
        setLayout(new BorderLayout(0, 24));
        setBackground(UIStylesDark.BG);
        setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        refresh();
    }

    public void refresh() {
        removeAll();

        // ─── HERO ─────────────────────────────────────────────────
        JPanel hero = new JPanel(new BorderLayout());
        hero.setBackground(UIStylesDark.BG2);
        hero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStylesDark.BORDER, 1),
            BorderFactory.createEmptyBorder(32, 40, 32, 40)));

        JLabel title = new JLabel("Bienvenue au Vidéo Club");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(UIStylesDark.ACCENT);

        JLabel sub = new JLabel("<html><div style='width:600px;color:#6b7280;font-size:14px;line-height:1.6'>"
            + "Système de gestion complet pour votre club de location de cassettes vidéo.<br>"
            + "Gérez le catalogue, les abonnés, les locations et les retours en toute simplicité.</div></html>");
        sub.setForeground(UIStylesDark.MUTED);

        hero.add(title, BorderLayout.NORTH);
        hero.add(Box.createVerticalStrut(12), BorderLayout.CENTER);
        hero.add(sub, BorderLayout.SOUTH);

        // ─── STATS ────────────────────────────────────────────────
        int nCass = 0, nAbo = 0, nLoc = 0, nCat = 0;
        try {
            nCass = new CassetteDAO().findAll().size();
            nAbo  = new AbonneeDAO().findAll().size();
            nLoc  = new LocationDAO().findEnCours().size();
            nCat  = new CategorieDAO().findAll().size();
        } catch (Exception e) {
            // si pas de BDD : valeurs à 0
        }

        JPanel stats = new JPanel(new GridLayout(1, 4, 24, 0));
        stats.setBackground(UIStylesDark.BG);
        
        stats.add(createStatCard("📼", "CASSETTES",     String.valueOf(nCass), "dans le catalogue"));
        stats.add(createStatCard("👤", "ABONNÉS",       String.valueOf(nAbo),  "membres actifs"));
        stats.add(createStatCard("🔄", "LOCATIONS",     String.valueOf(nLoc),  "en cours"));
        stats.add(createStatCard("🗂️", "CATÉGORIES",    String.valueOf(nCat),  "genres disponibles"));

        // ─── FEATURES ─────────────────────────────────────────────
        // On remplace les features par un panneau vide ou autre chose si besoin
        // Pour l'instant on garde juste Hero + Stats pour un look épuré
        
        JPanel container = new JPanel(new BorderLayout(0, 32));
        container.setBackground(UIStylesDark.BG);
        container.add(hero, BorderLayout.NORTH);
        container.add(stats, BorderLayout.CENTER);

        add(container, BorderLayout.NORTH);

        revalidate();
        repaint();
    }

    private JPanel createStatCard(String icon, String label, String value, String sub) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIStylesDark.BG2);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStylesDark.BORDER, 1),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        // Header: Icon + Label
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setBackground(UIStylesDark.BG2);
        
        JLabel lblIcon = new JLabel(icon + " ");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        JLabel lblText = new JLabel(label);
        lblText.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblText.setForeground(UIStylesDark.MUTED);
        
        header.add(lblIcon);
        header.add(lblText);

        // Value
        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 42));
        val.setForeground(UIStylesDark.TEXT);

        // Subtext
        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(UIStylesDark.FONT_SMALL);
        subLbl.setForeground(UIStylesDark.MUTED);

        p.add(header, BorderLayout.NORTH);
        p.add(val,    BorderLayout.CENTER);
        p.add(subLbl, BorderLayout.SOUTH);

        return p;
    }
}
