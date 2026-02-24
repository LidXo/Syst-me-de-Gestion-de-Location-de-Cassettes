package clubvideo.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Utilitaire de style centralisé (Thème Modern Dark).
 */
public class UIStylesDark {

    // Palette "Modern Dark"
    public static final Color BG        = new Color(17, 24, 39);    // Gray 900
    public static final Color BG2       = new Color(31, 41, 55);    // Gray 800
    public static final Color BG3       = new Color(55, 65, 81);    // Gray 700

    public static final Color ACCENT    = new Color(129, 140, 248); // Indigo 400
    public static final Color ACCENT2   = new Color(99, 102, 241);  // Indigo 500

    public static final Color RED       = new Color(248, 113, 113); // Red 400
    public static final Color GREEN     = new Color(52, 211, 153);  // Emerald 400
    public static final Color BLUE      = new Color(96, 165, 250);  // Blue 400

    public static final Color TEXT      = new Color(243, 244, 246); // Gray 100
    public static final Color MUTED     = new Color(156, 163, 175); // Gray 400
    public static final Color BORDER    = new Color(75, 85, 99);    // Gray 600

    public static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,   24);
    public static final Font FONT_H2     = new Font("SansSerif", Font.BOLD,   18);
    public static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN,  14);
    public static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN,  12);
    public static final Font FONT_MONO   = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font FONT_BIG    = new Font("SansSerif", Font.BOLD,   32);

    /** Configure le Look & Feel global */
    public static void setup() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("Panel.background",            BG);
        UIManager.put("Frame.background",            BG);

        UIManager.put("Label.foreground",            TEXT);
        UIManager.put("Label.font",                  FONT_BODY);

        UIManager.put("TextField.background",        BG2);
        UIManager.put("TextField.foreground",        TEXT);
        UIManager.put("TextField.caretForeground",   ACCENT);
        UIManager.put("TextField.selectionBackground", new Color(55, 48, 163));
        UIManager.put("TextField.selectionForeground", TEXT);
        UIManager.put("TextField.border",            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        UIManager.put("PasswordField.background",    BG2);
        UIManager.put("PasswordField.foreground",    TEXT);
        UIManager.put("PasswordField.caretForeground", ACCENT);
        UIManager.put("PasswordField.border",        BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        UIManager.put("ComboBox.background",         BG2);
        UIManager.put("ComboBox.foreground",         TEXT);
        UIManager.put("ComboBox.selectionBackground",ACCENT);
        UIManager.put("ComboBox.selectionForeground",BG2);

        UIManager.put("Table.background",            BG2);
        UIManager.put("Table.foreground",            TEXT);
        UIManager.put("Table.gridColor",             BORDER);
        UIManager.put("Table.selectionBackground",   new Color(49, 46, 129));
        UIManager.put("Table.selectionForeground",   ACCENT);
        UIManager.put("Table.font",                  FONT_BODY);

        UIManager.put("TableHeader.background",      BG3);
        UIManager.put("TableHeader.foreground",      MUTED);
        UIManager.put("TableHeader.font",            new Font("SansSerif", Font.BOLD, 12));

        UIManager.put("ScrollPane.background",       BG);
        UIManager.put("ScrollBar.background",        BG);
        UIManager.put("ScrollBar.thumb",             new Color(107, 114, 128));
        UIManager.put("ScrollBar.track",             BG);

        UIManager.put("OptionPane.background",       BG2);
        UIManager.put("OptionPane.messageForeground",TEXT);

        UIManager.put("Button.background",           BG2);
        UIManager.put("Button.foreground",           TEXT);
        UIManager.put("Button.font",                 FONT_BODY);
    }

    public static JButton accentButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT);
        b.setForeground(BG2);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return b;
    }

    public static JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BG2);
        b.setForeground(TEXT);
        b.setFont(FONT_BODY);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(153, 27, 27));
        b.setForeground(RED);
        b.setFont(FONT_BODY);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(127, 29, 29), 1));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JButton greenButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN);
        b.setForeground(BG2);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JButton blueButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BLUE);
        b.setForeground(BG2);
        b.setFont(FONT_BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(BG2);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    public static JPasswordField styledPassword(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setBackground(BG2);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    public static JComboBox<Object> styledCombo() {
        JComboBox<Object> cb = new JComboBox<>();
        cb.setBackground(BG2);
        cb.setForeground(TEXT);
        cb.setFont(FONT_BODY);
        return cb;
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(MUTED);
        return l;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(FONT_BODY);
        table.setBackground(BG2);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(49, 46, 129));
        table.setSelectionForeground(ACCENT);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG3);
        header.setForeground(MUTED);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setBackground(BG2);
        cr.setForeground(TEXT);
        table.setDefaultRenderer(Object.class, cr);
    }

    public static JScrollPane styledScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(BG);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        sp.getViewport().setBackground(BG2);
        return sp;
    }

    public static JPanel cardPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG2);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        return p;
    }

    public static JPanel headerBar(String title) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        bar.setBackground(BG2);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_H2);
        lbl.setForeground(TEXT);
        bar.add(lbl);
        return bar;
    }
}
