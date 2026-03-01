package clubvideo.view;

import clubvideo.dao.AbonneDAO;
import clubvideo.dao.LocationDAO;
import clubvideo.model.Abonne;
import clubvideo.model.Location;
import clubvideo.util.UIStyles;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class LocationPanel extends JPanel implements Refreshable {

    private final LocationDAO daoLoc = new LocationDAO();
    private final AbonneDAO daoAbo = new AbonneDAO();
    private DefaultTableModel tableModel;

    public LocationPanel() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BG_MAIN);
        buildUI();
    }

    private void buildUI() {
        JPanel titleBar = makeTitleBar("Gestion des Locations");
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIStyles.FONT_BODY);
        tabs.setBackground(UIStyles.BG_CARD);
        tabs.setForeground(UIStyles.TEXT_MEDIUM);

        tabs.addTab("En cours", buildViewTab());
        tabs.addTab("Nouvelle location", buildNewLocationTab());
        tabs.addTab("Retour cassette", buildRetourTab());
        tabs.addTab("Supprimer", buildDeleteTab());

        add(titleBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // ── Onglet : Locations en cours ──────────────────────────────────────────
    private JPanel buildViewTab() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIStyles.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        tableModel = new DefaultTableModel(
                new String[] { "N° Abonné", "Nom Abonné", "N° Cassette", "Titre", "Date Location" }, 0) {
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

    // ── Onglet : Nouvelle location ────────────────────────────────────────────
    private JComponent buildNewLocationTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel info = new JLabel("<html>"
                + "<b>Règles :</b><br>"
                + "• Un abonné ne peut avoir plus de <b>3 cassettes</b> en location simultanément.<br>"
                + "• Si le couple (abonné, cassette) existe déjà, seule la date est mise à jour (UPSERT)."
                + "</html>");
        info.setFont(UIStyles.FONT_BODY);
        info.setForeground(UIStyles.INFO);
        GridBagConstraints gi = new GridBagConstraints();
        gi.gridx = 0;
        gi.gridy = 0;
        gi.gridwidth = 2;
        gi.anchor = GridBagConstraints.WEST;
        gi.insets = new Insets(0, 0, 20, 0);
        form.add(info, gi);

        JTextField fAbo = UIStyles.makeField();
        JTextField fNom = UIStyles.makeField();
        fNom.setEditable(false);
        fNom.setBackground(UIStyles.BG_MAIN);
        JTextField fCass = UIStyles.makeField();
        JTextField fDate = UIStyles.makeField();
        fDate.setText(LocalDate.now().toString());

        JButton btnVerif = UIStyles.makeButton("Vérifier", UIStyles.INFO);
        btnVerif.setPreferredSize(new Dimension(110, 32));

        String[] labels = { "N° Abonné *", "Nom Abonné", "N° Cassette *", "Date Location *" };
        JComponent[] fields = { fAbo, fNom, fCass, fDate };
        for (int i = 0; i < labels.length; i++) {
            addFormRow(form, labels[i], fields[i], i + 1);
        }
        // Bouton Vérifier en face de N° Abonné
        GridBagConstraints gv = new GridBagConstraints();
        gv.gridx = 2;
        gv.gridy = 1;
        gv.insets = new Insets(6, 8, 6, 0);
        form.add(btnVerif, gv);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.SUCCESS);
        addFormBtn(form, btnSave, 5);

        btnVerif.addActionListener(e -> {
            try {
                Abonne a = daoAbo.findById(Integer.parseInt(fAbo.getText().trim()));
                if (a == null) {
                    error("Abonné introuvable.");
                    return;
                }
                fNom.setText(a.getNomAbonne());
                if (a.getNombreLocation() >= 3)
                    fNom.setText(a.getNomAbonne() + "  ⛔ QUOTA ATTEINT");
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        btnSave.addActionListener(e -> {
            try {
                int noAbo = Integer.parseInt(fAbo.getText().trim());
                int noCass = Integer.parseInt(fCass.getText().trim());
                LocalDate date = LocalDate.parse(fDate.getText().trim());

                Abonne a = daoAbo.findById(noAbo);
                if (a == null) {
                    error("Abonné N°" + noAbo + " introuvable.");
                    return;
                }
                if (a.getNombreLocation() >= 3) {
                    error("Quota atteint : " + a.getNomAbonne() + " a déjà 3 cassettes louées.");
                    return;
                }

                daoLoc.insertOrUpdate(new Location(noAbo, noCass, date));
                a.setNombreLocation(a.getNombreLocation() + 1);
                daoAbo.update(a);

                ok("Location enregistrée.");
                refresh();
                fAbo.setText("");
                fNom.setText("");
                fCass.setText("");
                fDate.setText(LocalDate.now().toString());
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Onglet : Retour cassette ──────────────────────────────────────────────
    private JComponent buildRetourTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel info = new JLabel("<html>Enregistrez le retour d'une cassette :<br>"
                + "la location sera supprimée et le compteur de l'abonné décrémenté.</html>");
        info.setFont(UIStyles.FONT_BODY);
        info.setForeground(UIStyles.ACCENT);
        GridBagConstraints gi = new GridBagConstraints();
        gi.gridx = 0;
        gi.gridy = 0;
        gi.gridwidth = 3;
        gi.anchor = GridBagConstraints.WEST;
        gi.insets = new Insets(0, 0, 20, 0);
        form.add(info, gi);

        JTextField fAbo = UIStyles.makeField();
        JTextField fNom = UIStyles.makeField();
        fNom.setEditable(false);
        fNom.setBackground(UIStyles.BG_MAIN);
        JTextField fCass = UIStyles.makeField();

        JButton btnVerif = UIStyles.makeButton("Vérifier", UIStyles.INFO);
        btnVerif.setPreferredSize(new Dimension(110, 32));
        JButton btnRetour = UIStyles.makeButton("✅ Valider Retour", UIStyles.ACCENT);
        btnRetour.setEnabled(false);

        addFormRow(form, "N° Abonné *", fAbo, 1);
        addFormRow(form, "Nom Abonné", fNom, 2);
        addFormRow(form, "N° Cassette *", fCass, 3);

        GridBagConstraints gv = new GridBagConstraints();
        gv.gridx = 2;
        gv.gridy = 1;
        gv.insets = new Insets(6, 8, 6, 0);
        form.add(btnVerif, gv);
        addFormBtn(form, btnRetour, 4);

        btnVerif.addActionListener(e -> {
            try {
                Abonne a = daoAbo.findById(Integer.parseInt(fAbo.getText().trim()));
                if (a == null) {
                    error("Abonné introuvable.");
                    return;
                }
                fNom.setText(a.getNomAbonne());
                btnRetour.setEnabled(true);
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        btnRetour.addActionListener(e -> {
            try {
                int noAbo = Integer.parseInt(fAbo.getText().trim());
                int noCass = Integer.parseInt(fCass.getText().trim());
                int conf = JOptionPane.showConfirmDialog(this,
                        "Confirmer le retour de la cassette N°" + noCass +
                                " par l'abonné N°" + noAbo + " ?",
                        "Confirmation retour", JOptionPane.YES_NO_OPTION);
                if (conf != JOptionPane.YES_OPTION)
                    return;

                daoLoc.delete(noAbo, noCass);
                Abonne a = daoAbo.findById(noAbo);
                if (a != null && a.getNombreLocation() > 0) {
                    a.setNombreLocation(a.getNombreLocation() - 1);
                    daoAbo.update(a);
                }
                ok("Retour enregistré. Cassette N°" + noCass + " libérée.");
                refresh();
                fAbo.setText("");
                fNom.setText("");
                fCass.setText("");
                btnRetour.setEnabled(false);
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Onglet : Supprimer ────────────────────────────────────────────────────
    private JComponent buildDeleteTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel warn = new JLabel("<html>⚠️ <b>Suppression directe</b> d'une location<br>"
                + "par son couple identifiant (N° abonné + N° cassette).</html>");
        warn.setFont(UIStyles.FONT_BODY);
        warn.setForeground(UIStyles.DANGER);
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.gridwidth = 2;
        gw.anchor = GridBagConstraints.WEST;
        gw.insets = new Insets(0, 0, 20, 0);
        form.add(warn, gw);

        JTextField fAbo = UIStyles.makeField();
        JTextField fCass = UIStyles.makeField();
        addFormRow(form, "N° Abonné *", fAbo, 1);
        addFormRow(form, "N° Cassette *", fCass, 2);

        JButton btnDel = UIStyles.makeButton("🗑 Supprimer", UIStyles.DANGER);
        addFormBtn(form, btnDel, 3);

        btnDel.addActionListener(e -> {
            try {
                int noAbo = Integer.parseInt(fAbo.getText().trim());
                int noCass = Integer.parseInt(fCass.getText().trim());
                int ok = JOptionPane.showConfirmDialog(this,
                        "Supprimer la location (abonné=" + noAbo + ", cassette=" + noCass + ") ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ok == JOptionPane.YES_OPTION) {
                    daoLoc.delete(noAbo, noCass);
                    ok("Location supprimée.");
                    fAbo.setText("");
                    fCass.setText("");
                    refresh();
                }
            } catch (Exception ex) {
                error(ex.getMessage());
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
            for (Object[] row : daoLoc.findAllDetailed())
                tableModel.addRow(row);
        } catch (Exception ex) {
            tableModel.setRowCount(0);
            System.err.println("Erreur de rafraîchissement LocationPanel: " + ex.getMessage());
        }
    }
}
