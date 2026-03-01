package clubvideo.view;

import clubvideo.dao.CategorieDAO;
import clubvideo.model.Categorie;
import clubvideo.util.UIStyles;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CategoriePanel extends JPanel implements Refreshable {

    private final CategorieDAO dao = new CategorieDAO();
    private DefaultTableModel tableModel;

    public CategoriePanel() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BG_MAIN);
        buildUI();
    }

    private void buildUI() {
        JPanel titleBar = makeTitleBar("Catégories de Cassettes");
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIStyles.FONT_BODY);
        tabs.setBackground(UIStyles.BG_CARD);
        tabs.setForeground(UIStyles.TEXT_MEDIUM);

        tabs.addTab("Liste", buildViewTab());
        tabs.addTab("Ajouter", buildAddTab());
        tabs.addTab("Modifier", buildEditTab());
        tabs.addTab("Supprimer", buildDeleteTab());

        add(titleBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // ── Afficher ──────────────────────────────────────────────────────────────
    private JPanel buildViewTab() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIStyles.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        tableModel = new DefaultTableModel(
                new String[] { "Code Catégorie", "Libellé" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        UIStyles.styleTable(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER));

        JButton btnR = UIStyles.makeButton("🔄 Actualiser", UIStyles.PRIMARY);
        btnR.setPreferredSize(new Dimension(130, 32));
        btnR.addActionListener(e -> refresh());
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tb.setBackground(UIStyles.BG_MAIN);
        tb.add(btnR);

        p.add(tb, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    // ── Ajouter ───────────────────────────────────────────────────────────────
    private JComponent buildAddTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JTextField fCode = UIStyles.makeField();
        JTextField fLib = UIStyles.makeField();

        addFormRow(form, "Code Catégorie * (ex: ACT)", fCode, 0);
        addFormRow(form, "Libellé *", fLib, 1);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.SUCCESS);
        addFormBtn(form, btnSave, 2);

        btnSave.addActionListener(e -> {
            try {
                dao.insert(new Categorie(fCode.getText().trim(), fLib.getText().trim()));
                ok("Catégorie [" + fCode.getText().trim() + "] enregistrée.");
                refresh();
                fCode.setText("");
                fLib.setText("");
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Modifier ──────────────────────────────────────────────────────────────
    private JComponent buildEditTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JTextField fSearch = UIStyles.makeField();
        fSearch.setPreferredSize(new Dimension(150, 32));
        JButton btnSearch = UIStyles.makeButton("Rechercher", UIStyles.INFO);
        btnSearch.setPreferredSize(new Dimension(120, 32));
        JPanel sb = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        sb.setBackground(UIStyles.BG_CARD);
        sb.add(UIStyles.makeLabel("Code catégorie : "));
        sb.add(fSearch);
        sb.add(btnSearch);
        GridBagConstraints gs = new GridBagConstraints();
        gs.gridx = 0;
        gs.gridy = 0;
        gs.gridwidth = 2;
        gs.anchor = GridBagConstraints.WEST;
        gs.insets = new Insets(0, 0, 18, 0);
        form.add(sb, gs);

        JTextField fCode = UIStyles.makeField();
        fCode.setEditable(false);
        fCode.setBackground(UIStyles.BG_MAIN);
        JTextField fLib = UIStyles.makeField();

        addFormRow(form, "Code Catégorie", fCode, 1);
        addFormRow(form, "Libellé", fLib, 2);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.WARNING);
        addFormBtn(form, btnSave, 3);

        btnSearch.addActionListener(e -> {
            try {
                Categorie c = dao.findById(fSearch.getText().trim().toUpperCase());
                if (c == null) {
                    error("Catégorie introuvable.");
                    return;
                }
                fCode.setText(c.getCategorie());
                fLib.setText(c.getLibelleCategorie());
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        btnSave.addActionListener(e -> {
            try {
                dao.update(new Categorie(fCode.getText().trim(), fLib.getText().trim()));
                ok("Catégorie mise à jour.");
                refresh();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Supprimer ─────────────────────────────────────────────────────────────
    private JComponent buildDeleteTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel warn = new JLabel("<html>⚠️ <b>Attention :</b> Impossible de supprimer une catégorie<br>"
                + "référencée par une ou plusieurs cassettes (contrainte FK).</html>");
        warn.setFont(UIStyles.FONT_BODY);
        warn.setForeground(UIStyles.DANGER);
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.gridwidth = 2;
        gw.anchor = GridBagConstraints.WEST;
        gw.insets = new Insets(0, 0, 20, 0);
        form.add(warn, gw);

        JTextField fCode = UIStyles.makeField();
        addFormRow(form, "Code Catégorie *", fCode, 1);

        JButton btnDel = UIStyles.makeButton("🗑 Supprimer", UIStyles.DANGER);
        addFormBtn(form, btnDel, 2);

        btnDel.addActionListener(e -> {
            String val = fCode.getText().trim();
            if (val.isEmpty()) {
                error("Saisir un code catégorie.");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                    "Supprimer la catégorie [" + val + "] ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    dao.delete(val);
                    ok("Catégorie supprimée.");
                    fCode.setText("");
                    refresh();
                } catch (Exception ex) {
                    error(ex.getMessage());
                }
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void addFormRow(JPanel p, String lbl, JComponent field, int row) {
        GridBagConstraints gl = new GridBagConstraints();
        gl.gridx = 0;
        gl.gridy = row;
        gl.anchor = GridBagConstraints.WEST;
        gl.insets = new Insets(6, 0, 6, 14);
        p.add(UIStyles.makeLabel(lbl), gl);
        GridBagConstraints gf = new GridBagConstraints();
        gf.gridx = 1;
        gf.gridy = row;
        gf.fill = GridBagConstraints.HORIZONTAL;
        gf.weightx = 1;
        gf.insets = new Insets(6, 0, 6, 0);
        p.add(field, gf);
    }

    private void addFormBtn(JPanel p, JButton btn, int row) {
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = 1;
        gb.gridy = row;
        gb.anchor = GridBagConstraints.WEST;
        gb.insets = new Insets(16, 0, 0, 0);
        p.add(btn, gb);
    }

    private JPanel makeTitleBar(String text) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIStyles.FONT_TITLE);
        lbl.setForeground(UIStyles.TEXT_DARK);
        bar.add(lbl, BorderLayout.WEST);
        return bar;
    }

    private void ok(String m) {
        JOptionPane.showMessageDialog(this, "✅ " + m, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, "❌ " + m, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void refresh() {
        if (tableModel == null)
            return;
        try {
            tableModel.setRowCount(0);
            for (Categorie c : dao.findAll())
                tableModel.addRow(new Object[] { c.getCategorie(), c.getLibelleCategorie() });
        } catch (Exception ex) {
            tableModel.setRowCount(0);
            System.err.println("Erreur de rafraîchissement CategoriePanel: " + ex.getMessage());
        }
    }
}
