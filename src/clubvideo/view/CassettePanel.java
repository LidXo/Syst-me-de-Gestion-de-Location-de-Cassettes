package clubvideo.view;

import clubvideo.dao.CassetteDAO;
import clubvideo.dao.LocationDAO;
import clubvideo.model.Cassette;
import clubvideo.util.UIStyles;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CassettePanel extends JPanel implements Refreshable {

    private final CassetteDAO dao = new CassetteDAO();
    private final LocationDAO daoLoc = new LocationDAO();
    private DefaultTableModel tableModel;

    public CassettePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BG_MAIN);
        buildUI();
    }

    private void buildUI() {
        // ── Titre de section ──────────────────────────────────────────────────
        JPanel titleBar = makeTitleBar("Gestion du Catalogue");
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIStyles.FONT_BODY);
        tabs.setBackground(UIStyles.BG_CARD);
        tabs.setForeground(UIStyles.TEXT_MEDIUM);

        tabs.addTab("Catalogue", buildViewTab());
        tabs.addTab("Ajouter", buildAddTab());
        tabs.addTab("Modifier", buildEditTab());
        tabs.addTab("Supprimer", buildDeleteTab());

        add(titleBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // ── Onglet Afficher ───────────────────────────────────────────────────────
    private JPanel buildViewTab() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIStyles.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        tableModel = new DefaultTableModel(
                new String[] { "N° Cassette", "Date Achat", "Titre", "Auteur", "Durée (min)", "Prix (€)", "Catégorie" },
                0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UIStyles.styleTable(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER));

        JTextField tfSearch = UIStyles.makeField();
        tfSearch.setPreferredSize(new Dimension(240, 32));
        JButton btnSearch = UIStyles.makeButton("🔍 Rechercher", UIStyles.INFO);
        btnSearch.setPreferredSize(new Dimension(140, 32));
        JButton btnRefresh = UIStyles.makeButton("🔄 Actualiser", UIStyles.PRIMARY);
        btnRefresh.setPreferredSize(new Dimension(130, 32));

        btnSearch.addActionListener(e -> {
            try {
                tableModel.setRowCount(0);
                for (Cassette c : dao.findByTitre(tfSearch.getText().trim()))
                    addRow(c);
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });
        btnRefresh.addActionListener(e -> refresh());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBackground(UIStyles.BG_MAIN);
        toolbar.add(new JLabel("Rechercher par titre :"));
        toolbar.add(tfSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnRefresh);

        p.add(toolbar, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private void addRow(Cassette c) {
        tableModel.addRow(new Object[] {
                c.getNoCassette(), c.getDateAchat(), c.getTitre(),
                c.getAuteur(), c.getDuree(), c.getPrix(), c.getCategorie()
        });
    }

    // ── Onglet Ajouter ────────────────────────────────────────────────────────
    private JComponent buildAddTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JTextField fNo = UIStyles.makeField();
        JTextField fDate = UIStyles.makeField();
        fDate.setText(LocalDate.now().toString());
        JTextField fTitre = UIStyles.makeField();
        JTextField fAut = UIStyles.makeField();
        JTextField fDur = UIStyles.makeField();
        JTextField fPrix = UIStyles.makeField();
        JTextField fCat = UIStyles.makeField();

        String[] labels = { "N° Cassette *", "Date Achat *", "Titre *", "Auteur *", "Durée (min) *", "Prix (€) *",
                "Code Catégorie *" };
        JTextField[] fields = { fNo, fDate, fTitre, fAut, fDur, fPrix, fCat };
        for (int i = 0; i < labels.length; i++)
            addFormRow(form, labels[i], fields[i], i);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.SUCCESS);
        addFormBtn(form, btnSave, labels.length);

        btnSave.addActionListener(e -> {
            try {
                dao.insert(new Cassette(
                        Integer.parseInt(fNo.getText().trim()),
                        LocalDate.parse(fDate.getText().trim()),
                        fTitre.getText().trim(), fAut.getText().trim(),
                        Integer.parseInt(fDur.getText().trim()),
                        new BigDecimal(fPrix.getText().trim()),
                        fCat.getText().trim()));
                ok("Cassette enregistrée avec succès.");
                for (JTextField f : fields)
                    f.setText("");
                fDate.setText(LocalDate.now().toString());
                refresh();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Onglet Modifier ───────────────────────────────────────────────────────
    private JComponent buildEditTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JTextField fSearch = UIStyles.makeField();
        fSearch.setPreferredSize(new Dimension(180, 32));
        JButton btnSearch = UIStyles.makeButton("Rechercher", UIStyles.INFO);
        btnSearch.setPreferredSize(new Dimension(120, 32));
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        searchBar.setBackground(UIStyles.BG_CARD);
        searchBar.add(UIStyles.makeLabel("N° Cassette : "));
        searchBar.add(fSearch);
        searchBar.add(btnSearch);
        GridBagConstraints gs = new GridBagConstraints();
        gs.gridx = 0;
        gs.gridy = 0;
        gs.gridwidth = 2;
        gs.anchor = GridBagConstraints.WEST;
        gs.insets = new Insets(0, 0, 18, 0);
        form.add(searchBar, gs);

        JTextField fId = UIStyles.makeField();
        fId.setEditable(false);
        fId.setBackground(UIStyles.BG_MAIN);
        JTextField fDate = UIStyles.makeField();
        JTextField fTitre = UIStyles.makeField();
        JTextField fAut = UIStyles.makeField();
        JTextField fDur = UIStyles.makeField();
        JTextField fPrix = UIStyles.makeField();
        JTextField fCat = UIStyles.makeField();

        String[] labels = { "N° Cassette", "Date Achat", "Titre", "Auteur", "Durée (min)", "Prix (€)",
                "Code Catégorie" };
        JTextField[] fields = { fId, fDate, fTitre, fAut, fDur, fPrix, fCat };
        for (int i = 0; i < labels.length; i++)
            addFormRow(form, labels[i], fields[i], i + 1);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.WARNING);
        addFormBtn(form, btnSave, labels.length + 1);

        btnSearch.addActionListener(e -> {
            try {
                Cassette c = dao.findById(Integer.parseInt(fSearch.getText().trim()));
                if (c == null) {
                    error("Cassette introuvable.");
                    return;
                }
                fId.setText(String.valueOf(c.getNoCassette()));
                fDate.setText(c.getDateAchat().toString());
                fTitre.setText(c.getTitre());
                fAut.setText(c.getAuteur());
                fDur.setText(String.valueOf(c.getDuree()));
                fPrix.setText(c.getPrix().toPlainString());
                fCat.setText(c.getCategorie());
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        btnSave.addActionListener(e -> {
            try {
                dao.update(new Cassette(
                        Integer.parseInt(fId.getText().trim()),
                        LocalDate.parse(fDate.getText().trim()),
                        fTitre.getText().trim(), fAut.getText().trim(),
                        Integer.parseInt(fDur.getText().trim()),
                        new BigDecimal(fPrix.getText().trim()),
                        fCat.getText().trim()));
                ok("Cassette mise à jour.");
                refresh();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        outer.add(form);
        return UIStyles.makeScrollable(outer);
    }

    // ── Onglet Supprimer ──────────────────────────────────────────────────────
    private JComponent buildDeleteTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel warn = new JLabel("<html>⚠️ <b>Attention :</b> La suppression est <b>irréversible</b>.<br>"
                + "La cassette ne doit avoir aucune location active.</html>");
        warn.setFont(UIStyles.FONT_BODY);
        warn.setForeground(UIStyles.DANGER);
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.gridwidth = 2;
        gw.anchor = GridBagConstraints.WEST;
        gw.insets = new Insets(0, 0, 20, 0);
        form.add(warn, gw);

        JTextField fId = UIStyles.makeField();
        addFormRow(form, "N° Cassette *", fId, 1);

        JButton btnDel = UIStyles.makeButton("🗑 Supprimer", UIStyles.DANGER);
        addFormBtn(form, btnDel, 2);

        btnDel.addActionListener(e -> {
            String val = fId.getText().trim();
            if (val.isEmpty()) {
                error("Veuillez saisir un N° cassette.");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                    "Confirmer la suppression de la cassette N° " + val + " ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    int id = Integer.parseInt(val);

                    // Vérification des dépendances (Locations)
                    if (daoLoc.countByCassette(id) > 0) {
                        error("Impossible de supprimer cette cassette car elle est actuellement louée.");
                        return;
                    }

                    dao.delete(id);
                    ok("Cassette supprimée avec succès.");
                    fId.setText("");
                    refresh();
                } catch (NumberFormatException nfe) {
                    error("Le N° cassette doit être un nombre valide.");
                } catch (Exception ex) {
                    error("Erreur lors de la suppression : " + ex.getMessage());
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
            for (Cassette c : dao.findAll())
                addRow(c);
        } catch (Exception ex) {
            tableModel.setRowCount(0); // Clear table on error
            System.err.println("Erreur de rafraîchissement CassettePanel: " + ex.getMessage());
        }
    }
}
