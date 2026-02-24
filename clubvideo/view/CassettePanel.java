package clubvideo.view;

import clubvideo.dao.*;
import clubvideo.model.*;
import clubvideo.util.UIStylesDark;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class CassettePanel extends JPanel {

    private final CassetteDAO  dao    = new CassetteDAO();
    private final CategorieDAO catDao = new CategorieDAO();
    private final LocationDAO  locDao = new LocationDAO();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Titre", "Auteur", "Catégorie", "Durée", "Prix", "Date Achat", "Dispo"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField txtSearch = UIStylesDark.styledField(20);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    // Cache pour le filtrage
    private List<Cassette> allCassettes = new ArrayList<>();
    private List<Integer> louesIds = new ArrayList<>();

    public CassettePanel() {
        setLayout(new BorderLayout());
        setBackground(UIStylesDark.BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIStylesDark.BG2);
        card.setBorder(BorderFactory.createLineBorder(UIStylesDark.BORDER, 1));

        // ── Toolbar ──
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(UIStylesDark.BG2);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIStylesDark.BORDER),
            BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        JLabel ttl = new JLabel("Gestion des Cassettes");
        ttl.setFont(UIStylesDark.FONT_H2); 
        ttl.setForeground(UIStylesDark.TEXT);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setBackground(UIStylesDark.BG2);
        
        txtSearch.setPreferredSize(new Dimension(240, 36));
        txtSearch.putClientProperty("JTextField.placeholderText", "Rechercher...");
        
        JButton btnAdd = UIStylesDark.accentButton("+ Ajouter");
        btnAdd.setPreferredSize(new Dimension(120, 36));
        
        right.add(txtSearch); 
        right.add(btnAdd);

        bar.add(ttl, BorderLayout.WEST); 
        bar.add(right, BorderLayout.EAST);

        UIStylesDark.styleTable(table);
        
        // Configuration des colonnes
        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setMaxWidth(60);  // ID
        cm.getColumn(4).setMaxWidth(100); // Durée
        cm.getColumn(5).setMaxWidth(120); // Prix
        cm.getColumn(6).setMaxWidth(120); // Date
        cm.getColumn(7).setMaxWidth(80);  // Dispo

        JScrollPane scroll = UIStylesDark.styledScroll(table);
        scroll.setBorder(null);

        card.add(bar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // Context menu
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(UIStylesDark.BG2);
        popup.setBorder(BorderFactory.createLineBorder(UIStylesDark.BORDER, 1));
        
        JMenuItem edit = new JMenuItem("Modifier");
        edit.setBackground(UIStylesDark.BG2); 
        edit.setForeground(UIStylesDark.TEXT);
        edit.setFont(UIStylesDark.FONT_BODY);
        
        JMenuItem del  = new JMenuItem("Supprimer");
        del.setBackground(UIStylesDark.BG2);  
        del.setForeground(UIStylesDark.RED);
        del.setFont(UIStylesDark.FONT_BODY);
        
        popup.add(edit); 
        popup.add(del);
        
        table.setComponentPopupMenu(popup);
        edit.addActionListener(e -> editSelected());
        del.addActionListener(e  -> deleteSelected());

        btnAdd.addActionListener(e -> openForm(null));
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editSelected();
            }
        });
        
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        refresh();
    }

    public void refresh() {
        try {
            allCassettes = dao.findAll();
            List<Location> locs = locDao.findEnCours();
            louesIds.clear();
            for(Location l : locs) louesIds.add(l.getIdCassette());
            
            filterTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        String q = txtSearch.getText().toLowerCase().trim();
        model.setRowCount(0);
        
        for (Cassette c : allCassettes) {
            boolean match = q.isEmpty() || 
                           c.getTitreCas().toLowerCase().contains(q) ||
                           (c.getAuteurCas() != null && c.getAuteurCas().toLowerCase().contains(q));
            
            if (match) {
                String cat = (c.getLibelleCategorie() != null) ? c.getLibelleCategorie() : "—";
                String prix = String.format("%,.0f F", c.getPrixCas());
                String date = (c.getDateAchat() != null) ? sdf.format(c.getDateAchat()) : "—";
                String dispo = louesIds.contains(c.getId()) ? "NON" : "OUI";
                
                model.addRow(new Object[]{
                    c.getId(), 
                    c.getTitreCas(), 
                    c.getAuteurCas(),
                    cat,
                    c.getDureeCas() + " min", 
                    prix,
                    date,
                    dispo
                });
            }
        }
    }

    private void openForm(Cassette existing) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            existing == null ? "Nouvelle cassette" : "Modifier la cassette", true);
        d.getContentPane().setBackground(UIStylesDark.BG2);
        d.setSize(500, 520);
        d.setLocationRelativeTo(this);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(UIStylesDark.BG2);
        body.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; 
        gc.insets = new Insets(6, 0, 6, 0);
        gc.weightx = 1.0;

        JTextField fTitre  = UIStylesDark.styledField(20);
        JTextField fAuteur = UIStylesDark.styledField(20);
        JTextField fPrix   = UIStylesDark.styledField(12);
        JTextField fDuree  = UIStylesDark.styledField(12);
        JTextField fDate   = UIStylesDark.styledField(12);
        JComboBox<Object> cbCat = UIStylesDark.styledCombo();

        // Remplissage combo
        try {
            cbCat.addItem("-- Aucune --");
            for (Categorie c : catDao.findAll()) cbCat.addItem(c);
        } catch (Exception ignored) {}

        if (existing != null) {
            fTitre.setText(existing.getTitreCas());
            fAuteur.setText(existing.getAuteurCas() != null ? existing.getAuteurCas() : "");
            fPrix.setText(String.valueOf((int)existing.getPrixCas()));
            fDuree.setText(String.valueOf(existing.getDureeCas()));
            fDate.setText(existing.getDateAchat() != null ? sdf.format(existing.getDateAchat()) : "");
            
            // Sélection catégorie
            for (int i = 1; i < cbCat.getItemCount(); i++) {
                Categorie item = (Categorie) cbCat.getItemAt(i);
                if (item.getId() == existing.getIdCategorie()) {
                    cbCat.setSelectedIndex(i); 
                    break;
                }
            }
        } else {
            fDate.setText(sdf.format(new Date()));
        }

        // Construction formulaire
        addFormRow(body, gc, 0, "Titre *", fTitre);
        addFormRow(body, gc, 2, "Auteur", fAuteur);
        
        // Ligne double (Prix / Durée)
        JPanel row3 = new JPanel(new GridLayout(1, 2, 16, 0));
        row3.setBackground(UIStylesDark.BG2);
        
        JPanel pPrix = new JPanel(new BorderLayout(0, 6));
        pPrix.setBackground(UIStylesDark.BG2);
        pPrix.add(UIStylesDark.sectionLabel("Prix (FCFA)"), BorderLayout.NORTH);
        pPrix.add(fPrix, BorderLayout.CENTER);
        
        JPanel pDuree = new JPanel(new BorderLayout(0, 6));
        pDuree.setBackground(UIStylesDark.BG2);
        pDuree.add(UIStylesDark.sectionLabel("Durée (min)"), BorderLayout.NORTH);
        pDuree.add(fDuree, BorderLayout.CENTER);
        
        row3.add(pPrix);
        row3.add(pDuree);
        
        gc.gridy = 4;
        body.add(row3, gc);
        
        addFormRow(body, gc, 5, "Date achat (jj/mm/aaaa)", fDate);
        
        gc.gridy = 7;
        body.add(UIStylesDark.sectionLabel("Catégorie"), gc);
        gc.gridy = 8;
        body.add(cbCat, gc);

        JLabel err = new JLabel(" ");
        err.setForeground(UIStylesDark.RED); 
        err.setFont(UIStylesDark.FONT_SMALL);
        gc.gridy = 9;
        body.add(err, gc);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footer.setBackground(UIStylesDark.BG2);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIStylesDark.BORDER));
        
        JButton cancel = UIStylesDark.ghostButton("Annuler");
        JButton save   = UIStylesDark.accentButton("Enregistrer");
        
        footer.add(cancel); 
        footer.add(save);

        cancel.addActionListener(e -> d.dispose());
        save.addActionListener(e -> {
            if (fTitre.getText().trim().isEmpty()) { 
                err.setText("Le titre est requis."); 
                return; 
            }
            try {
                Cassette c = existing != null ? existing : new Cassette();
                c.setTitreCas(fTitre.getText().trim());
                c.setAuteurCas(fAuteur.getText().trim());
                
                String sPrix = fPrix.getText().replace(",",".");
                c.setPrixCas(sPrix.isEmpty() ? 0 : Double.parseDouble(sPrix));
                
                String sDuree = fDuree.getText().trim();
                c.setDureeCas(sDuree.isEmpty() ? 0 : Integer.parseInt(sDuree));
                
                try { 
                    c.setDateAchat(fDate.getText().isEmpty() ? null : sdf.parse(fDate.getText())); 
                } catch (Exception ignored) {}
                
                if (cbCat.getSelectedIndex() > 0) {
                    c.setIdCategorie(((Categorie) cbCat.getSelectedItem()).getId());
                } else {
                    c.setIdCategorie(0);
                }
                
                if (existing == null) dao.save(c); 
                else dao.update(c);
                
                d.dispose(); 
                refresh();
            } catch (Exception ex) { 
                err.setText("Erreur : " + ex.getMessage()); 
            }
        });

        d.setLayout(new BorderLayout());
        d.add(body, BorderLayout.CENTER);
        d.add(footer, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void addFormRow(JPanel p, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridy = row;
        p.add(UIStylesDark.sectionLabel(label), gc);
        gc.gridy = row + 1;
        p.add(field, gc);
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { 
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne."); 
            return; 
        }
        int id = (int) model.getValueAt(row, 0);
        // On cherche dans la liste en cache pour éviter un appel DB inutile
        Cassette target = allCassettes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (target != null) openForm(target);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { 
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne."); 
            return; 
        }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment supprimer cette cassette ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try { 
                dao.delete(id); 
                refresh(); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Impossible de supprimer : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
