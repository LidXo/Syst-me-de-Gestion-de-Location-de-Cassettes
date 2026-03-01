package clubvideo.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * UIStyles — Thème PREMIUM LIGHT.
 * Match les références visuelles du dashboard et de la carte membre.
 */
public class UIStyles {

    // ── Palette PREMIUM LIGHT ────────────────────────────────────────────────
    public static final Color PRIMARY = new Color(0x4F46E5); // Indigo Pur
    public static final Color BG_MAIN = new Color(0xF1F5F9); // Slate 100 (Dashboard BG)
    public static final Color BG_SIDEBAR = Color.WHITE;
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_NAV_ACTIVE = new Color(0xEEF2FF); // Indigo 50

    public static final Color TEXT_DARK = new Color(0x0F172A); // Slate 900
    public static final Color TEXT_MEDIUM = new Color(0x64748B); // Slate 500
    public static final Color TEXT_LIGHT = new Color(0x94A3B8); // Slate 400

    public static final Color BORDER = new Color(0xE2E8F0); // Slate 200
    public static final Color SUCCESS = new Color(0x10B981); // Emerald
    public static final Color WARNING = new Color(0xF59E0B); // Amber
    public static final Color DANGER = new Color(0xEF4444); // Red
    public static final Color INFO = new Color(0x0EA5E9); // Sky
    public static final Color ACCENT = new Color(0x8B5CF6); // Violet

    // ── Polices ───────────────────────────────────────────────────────────────
    public static final Font FONT_LOGO = new Font("Segoe UI Bold", Font.PLAIN, 22);
    public static final Font FONT_TITLE = new Font("Segoe UI Bold", Font.PLAIN, 28);
    public static final Font FONT_HEADER = new Font("Segoe UI Semibold", Font.PLAIN, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI Bold", Font.BOLD, 14);
    public static final Font FONT_NAV = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI Bold", Font.PLAIN, 11);

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TabbedPane.selected", Color.WHITE);
            UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
            UIManager.put("TabbedPane.borderHighlightColor", BORDER);
            UIManager.put("TabbedPane.background", BG_MAIN);
        } catch (Exception ignored) {
        }
    }

    // ── Factories ─────────────────────────────────────────────────────────────
    public static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Texte noir/foncé pour lisibilité maximale sur bouton clair
        btn.setForeground(Color.BLACK);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    public static JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_DARK);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    public static JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_MEDIUM);
        return l;
    }

    public static JPanel makeCard(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)));
        if (title != null && !title.isEmpty()) {
            JLabel lbl = new JLabel(title);
            lbl.setFont(FONT_HEADER);
            lbl.setForeground(TEXT_DARK);
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
            p.add(lbl, BorderLayout.NORTH);
        }
        return p;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(BG_NAV_ACTIVE);
        table.setSelectionForeground(PRIMARY);
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT_DARK);

        table.getTableHeader().setFont(FONT_SMALL);
        table.getTableHeader().setBackground(new Color(0xF8FAFC));
        table.getTableHeader().setForeground(TEXT_DARK);
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.LEFT);
    }

    /** Créer une carte statistique stylisée avec bordure du bas colorée */
    public static JPanel makePremiumStatCard(String title, String subtext, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(BG_CARD);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 0, 1, BORDER),
                        BorderFactory.createMatteBorder(0, 0, 5, 0, accentColor)),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        valueLabel.setFont(new Font("Segoe UI Bold", Font.PLAIN, 42));
        valueLabel.setForeground(TEXT_DARK);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(BG_CARD);

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(FONT_SMALL);
        lblTitle.setForeground(TEXT_LIGHT);

        JLabel lblSub = new JLabel(subtext);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(accentColor);

        info.add(lblTitle);
        info.add(Box.createVerticalStrut(2));
        info.add(lblSub);

        card.add(valueLabel, BorderLayout.NORTH);
        card.add(info, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Envelopper un panel dans un JScrollPane stylisé pour éviter les troncatures
     */
    public static JScrollPane makeScrollable(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getViewport().setBackground(BG_MAIN);
        sp.setBackground(BG_MAIN);
        return sp;
    }
}
