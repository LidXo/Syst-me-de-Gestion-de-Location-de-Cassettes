package clubvideo.view;

import clubvideo.dao.AbonneDAO;
import clubvideo.dao.CarteAbonneeDAO;
import clubvideo.dao.LocationDAO;
import clubvideo.model.Abonne;
import clubvideo.model.CarteAbonnee;
import clubvideo.util.UIStyles;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class AbonnePanel extends JPanel implements Refreshable {

    private final AbonneDAO daoAbo = new AbonneDAO();
    private final CarteAbonneeDAO daoCarte = new CarteAbonneeDAO();
    private final LocationDAO daoLoc = new LocationDAO();

    private DefaultTableModel tmAbonne;
    private DefaultTableModel tmCarte;
    private JTextField fSearchEdit; // Pour navigation depuis clic droit
    private JTabbedPane tabs;

    public AbonnePanel() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BG_MAIN);
        buildUI();
    }

    private void buildUI() {
        JPanel titleBar = makeTitleBar("Gestion des Abonnés");
        tabs = new JTabbedPane();
        tabs.setFont(UIStyles.FONT_BODY);
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(UIStyles.TEXT_MEDIUM);

        tabs.addTab("Abonnés", buildViewAbonne());
        tabs.addTab("Cartes", buildViewCarte());
        tabs.addTab("Ajouter", buildAddTab());
        tabs.addTab("Modifier", buildEditTab());
        tabs.addTab("Supprimer", buildDeleteTab());

        add(titleBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // ── Onglet Afficher Abonnés ───────────────────────────────────────────────
    private JPanel buildViewAbonne() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIStyles.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        tmAbonne = new DefaultTableModel(
                new String[] { "N° Abonné", "Nom", "Adresse", "Date Abonnement", "Date Entrée", "Nb Locations" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(tmAbonne);
        UIStyles.styleTable(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER));

        // ── Menu Clic Droit ───────────────────────────────────────────────────
        JPopupMenu popup = new JPopupMenu();
        JMenuItem itemCard = new JMenuItem("Voir la carte");
        JMenuItem itemEdit = new JMenuItem("Modifier ses infos");
        popup.add(itemCard);
        popup.add(itemEdit);
        table.setComponentPopupMenu(popup);

        itemCard.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    int id = (int) tmAbonne.getValueAt(row, 0);
                    Abonne a = daoAbo.findById(id);
                    if (a != null) {
                        new SubscriberCardDialog((Frame) SwingUtilities.getWindowAncestor(this), a).setVisible(true);
                    }
                } catch (Exception ex) {
                    error(ex.getMessage());
                }
            }
        });
        itemEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tmAbonne.getValueAt(row, 0);
                fSearchEdit.setText(String.valueOf(id));
                tabs.setSelectedIndex(3);
                // On pourrait aussi déclencher auto-recherche ici
            }
        });

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

    // ── Onglet Cartes Abonnés ─────────────────────────────────────────────────
    private JPanel buildViewCarte() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIStyles.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        tmCarte = new DefaultTableModel(
                new String[] { "N° Abonné", "Nom", "Adresse", "Date Abonnement", "Date Entrée" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(tmCarte);
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

    // ── Onglet Ajouter ────────────────────────────────────────────────────────
    private JComponent buildAddTab() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outer.setBackground(UIStyles.BG_MAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyles.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JTextField fNo = UIStyles.makeField();
        JTextField fNom = UIStyles.makeField();
        JTextField fAddr = UIStyles.makeField();
        JTextField fAbo = UIStyles.makeField();
        fAbo.setText(LocalDate.now().toString());
        JTextField fEnt = UIStyles.makeField();
        fEnt.setText(LocalDate.now().toString());
        JSpinner spNb = new JSpinner(new SpinnerNumberModel(0, 0, 3, 1));
        spNb.setFont(UIStyles.FONT_BODY);

        JLabel noteCard = new JLabel("ℹ️  La carte d'abonné sera créée automatiquement.");
        noteCard.setFont(UIStyles.FONT_SMALL);
        noteCard.setForeground(UIStyles.INFO);
        GridBagConstraints gn = new GridBagConstraints();
        gn.gridx = 0;
        gn.gridy = 0;
        gn.gridwidth = 2;
        gn.anchor = GridBagConstraints.WEST;
        gn.insets = new Insets(0, 0, 16, 0);
        form.add(noteCard, gn);

        String[] labels = { "N° Abonné *", "Nom *", "Adresse *", "Date Abonnement *", "Date Entrée *", "Nb Locations" };
        JComponent[] fields = { fNo, fNom, fAddr, fAbo, fEnt, spNb };
        for (int i = 0; i < labels.length; i++)
            addFormRow(form, labels[i], fields[i], i + 1);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.SUCCESS);
        addFormBtn(form, btnSave, labels.length + 1);

        btnSave.addActionListener(e -> {
            try {
                int no = Integer.parseInt(fNo.getText().trim());
                LocalDate dateAbo = LocalDate.parse(fAbo.getText().trim());
                LocalDate dateEnt = LocalDate.parse(fEnt.getText().trim());

                Abonne a = new Abonne(no, fNom.getText().trim(), fAddr.getText().trim(),
                        dateAbo, dateEnt, (Integer) spNb.getValue());
                daoAbo.insert(a);

                CarteAbonnee ca = new CarteAbonnee(no, fNom.getText().trim(),
                        fAddr.getText().trim(), dateAbo, dateEnt);
                daoCarte.insert(ca);

                ok("Abonné N°" + no + " enregistré + carte créée.");
                refresh();
                fNo.setText("");
                fNom.setText("");
                fAddr.setText("");
                fAbo.setText(LocalDate.now().toString());
                fEnt.setText(LocalDate.now().toString());
                spNb.setValue(0);
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

        fSearchEdit = UIStyles.makeField();
        fSearchEdit.setPreferredSize(new Dimension(160, 32));
        JButton btnSearch = UIStyles.makeButton("Rechercher", UIStyles.INFO);
        btnSearch.setPreferredSize(new Dimension(120, 32));
        JPanel sb = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        sb.setBackground(UIStyles.BG_CARD);
        sb.add(UIStyles.makeLabel("N° Abonné : "));
        sb.add(fSearchEdit);
        sb.add(btnSearch);
        GridBagConstraints gs = new GridBagConstraints();
        gs.gridx = 0;
        gs.gridy = 0;
        gs.gridwidth = 2;
        gs.anchor = GridBagConstraints.WEST;
        gs.insets = new Insets(0, 0, 18, 0);
        form.add(sb, gs);

        JTextField fId = UIStyles.makeField();
        fId.setEditable(false);
        fId.setBackground(UIStyles.BG_MAIN);
        JTextField fNom = UIStyles.makeField();
        JTextField fAddr = UIStyles.makeField();
        JTextField fAbo = UIStyles.makeField();
        JTextField fEnt = UIStyles.makeField();
        JSpinner spNb = new JSpinner(new SpinnerNumberModel(0, 0, 3, 1));
        spNb.setFont(UIStyles.FONT_BODY);

        String[] labels = { "N° Abonné", "Nom", "Adresse", "Date Abonnement", "Date Entrée", "Nb Locations" };
        JComponent[] fields = { fId, fNom, fAddr, fAbo, fEnt, spNb };
        for (int i = 0; i < labels.length; i++)
            addFormRow(form, labels[i], fields[i], i + 1);

        JButton btnSave = UIStyles.makeButton("✅ Enregistrer", UIStyles.WARNING);
        addFormBtn(form, btnSave, labels.length + 1);

        btnSearch.addActionListener(e -> {
            try {
                Abonne a = daoAbo.findById(Integer.parseInt(fSearchEdit.getText().trim()));
                if (a == null) {
                    error("Abonné introuvable.");
                    return;
                }
                fId.setText(String.valueOf(a.getNoAbonne()));
                fNom.setText(a.getNomAbonne());
                fAddr.setText(a.getAdresseAbonne());
                fAbo.setText(a.getDateAbonnement().toString());
                fEnt.setText(a.getDateEntree().toString());
                spNb.setValue(a.getNombreLocation());
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        btnSave.addActionListener(e -> {
            try {
                daoAbo.update(new Abonne(
                        Integer.parseInt(fId.getText().trim()),
                        fNom.getText().trim(), fAddr.getText().trim(),
                        LocalDate.parse(fAbo.getText().trim()),
                        LocalDate.parse(fEnt.getText().trim()),
                        (Integer) spNb.getValue()));
                ok("Abonné mis à jour.");
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

        JLabel warn = new JLabel("<html>⚠️ <b>Attention :</b> Supprime l'abonné ET sa carte.<br>"
                + "L'abonné ne doit avoir aucune location active.</html>");
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
        addFormRow(form, "N° Abonné *", fId, 1);

        JButton btnDel = UIStyles.makeButton("🗑 Supprimer", UIStyles.DANGER);
        addFormBtn(form, btnDel, 2);

        btnDel.addActionListener(e -> {
            String val = fId.getText().trim();
            if (val.isEmpty()) {
                error("Saisir un N° abonné.");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                    "Supprimer l'abonné N° " + val + " et sa carte ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    int id = Integer.parseInt(val);

                    // Vérification des dépendances (Locations)
                    if (daoLoc.countByAbonne(id) > 0) {
                        error("Impossible de supprimer cet abonné car il possède des locations en cours.");
                        return;
                    }

                    daoCarte.delete(id); // cascade FK
                    daoAbo.delete(id);
                    ok("Abonné et carte supprimés avec succès.");
                    fId.setText("");
                    refresh();
                } catch (NumberFormatException nfe) {
                    error("Le N° abonné doit être un nombre valide.");
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
        try {
            if (tmAbonne != null) {
                tmAbonne.setRowCount(0);
                for (Abonne a : daoAbo.findAll())
                    tmAbonne.addRow(new Object[] {
                            a.getNoAbonne(), a.getNomAbonne(), a.getAdresseAbonne(),
                            a.getDateAbonnement(), a.getDateEntree(), a.getNombreLocation()
                    });
            }
            if (tmCarte != null) {
                tmCarte.setRowCount(0);
                for (CarteAbonnee c : daoCarte.findAll())
                    tmCarte.addRow(new Object[] {
                            c.getNoAbonne(), c.getNomAbonne(), c.getAdresseAbonne(),
                            c.getDateAbonnement(), c.getDateEntree()
                    });
            }
        } catch (Exception ex) {
            if (tmAbonne != null)
                tmAbonne.setRowCount(0);
            if (tmCarte != null)
                tmCarte.setRowCount(0);
            System.err.println("Erreur de rafraîchissement AbonnePanel: " + ex.getMessage());
        }
    }
}
