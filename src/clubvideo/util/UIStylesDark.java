package clubvideo.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * UIStylesDark — Thème SOMBRE (Dark Mode).
 * Même API que UIStyles. Passer de l'un à l'autre suffit
 * à basculer le thème de toute l'application.
 */
public class UIStylesDark {

    // ── Palette sombre ────────────────────────────────────────────────────────
    public static final Color PRIMARY       = new Color(0x4A90D9);
    public static final Color PRIMARY_DARK  = new Color(0x121212);
    public static final Color PRIMARY_LIGHT = new Color(0x5BA3E8);
    public static final Color ACCENT        = new Color(0xE8873A);
    public static final Color SUCCESS       = new Color(0x2ECC71);
    public static final Color DANGER        = new Color(0xE74C3C);
    public static final Color WARNING       = new Color(0xF39C12);
    public static final Color INFO          = new Color(0x3498DB);

    public static final Color BG_MAIN       = new Color(0x1E1E2E);
    public static final Color BG_CARD       = new Color(0x2A2A3E);
    public static final Color BG_SIDEBAR    = new Color(0x121212);
    public static final Color BG_HEADER     = new Color(0x1A1A2E);

    public static final Color TEXT_DARK     = new Color(0xE0E0E0);
    public static final Color TEXT_MEDIUM   = new Color(0xA0A0B0);
    public static final Color TEXT_LIGHT    = new Color(0x606070);
    public static final Color TEXT_WHITE    = Color.WHITE;

    public static final Color BORDER        = new Color(0x3A3A5C);
    public static final Color TABLE_HEADER  = new Color(0x2C2C4A);
    public static final Color TABLE_ROW_ALT = new Color(0x252535);
    public static final Color TABLE_SEL     = new Color(0x3A4A7A);

    // ── Polices (identiques au thème clair) ───────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Consolas",  Font.PLAIN, 12);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LOGO    = new Font("Segoe UI", Font.BOLD,  17);

    // ── Factories ─────────────────────────────────────────────────────────────
    public static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_HEADER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        return btn;
    }

    public static JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY);
        f.setBackground(new Color(0x1A1A2E));
        f.setForeground(TEXT_DARK);
        f.setCaretColor(TEXT_DARK);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        f.setPreferredSize(new Dimension(220, 32));
        return f;
    }

    public static JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_DARK);
        return l;
    }

    public static JPanel makeCard(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        if (title != null && !title.isEmpty()) {
            JLabel lbl = new JLabel(title);
            lbl.setFont(FONT_HEADER);
            lbl.setForeground(PRIMARY);
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            p.add(lbl, BorderLayout.NORTH);
        }
        return p;
    }

    public static void styleTable(JTable t) {
        t.setFont(FONT_BODY);
        t.setRowHeight(28);
        t.setGridColor(BORDER);
        t.setShowVerticalLines(false);
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_DARK);
        t.setSelectionBackground(TABLE_SEL);
        t.setSelectionForeground(TEXT_WHITE);
        t.setFillsViewportHeight(true);

        t.getTableHeader().setFont(FONT_HEADER);
        t.getTableHeader().setBackground(TABLE_HEADER);
        t.getTableHeader().setForeground(TEXT_WHITE);
        t.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        Color altRow = TABLE_ROW_ALT;
        Color normRow = BG_CARD;
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? normRow : altRow);
                    c.setForeground(TEXT_DARK);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
    }

    /** Applique un UIManager minimal pour que les panneaux prennent le fond sombre. */
    public static void applyDarkUIManager() {
        UIManager.put("Panel.background",          BG_MAIN);
        UIManager.put("OptionPane.background",     BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_DARK);
        UIManager.put("Button.background",         BG_CARD);
        UIManager.put("Button.foreground",         TEXT_DARK);
        UIManager.put("TextField.background",      new Color(0x1A1A2E));
        UIManager.put("TextField.foreground",      TEXT_DARK);
        UIManager.put("TextArea.background",       new Color(0x1A1A2E));
        UIManager.put("TextArea.foreground",       TEXT_DARK);
        UIManager.put("ScrollPane.background",     BG_CARD);
        UIManager.put("TabbedPane.background",     BG_CARD);
        UIManager.put("TabbedPane.foreground",     TEXT_DARK);
        UIManager.put("Label.foreground",          TEXT_DARK);
        UIManager.put("ComboBox.background",       new Color(0x1A1A2E));
        UIManager.put("ComboBox.foreground",       TEXT_DARK);
    }
}
