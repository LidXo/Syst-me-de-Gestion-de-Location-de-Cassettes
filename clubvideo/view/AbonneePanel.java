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

public class AbonneePanel extends JPanel {

    private final AbonneeDAO      dao      = new AbonneeDAO();
    private final CarteAbbonneDAO carteDao = new CarteAbbonneDAO();
    private final LocationDAO     locDao   = new LocationDAO();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Nom", "Adresse", "Date Abonnement", "Locations"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField txtSearch = UIStylesDark.styledField(20);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    // Cache
    private List<Abonnee> allAbonnes = new ArrayList<>();

    public AbonneePanel() {
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

        JLabel ttl = new JLabel("Gestion des Abonnés");
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
        
        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setMaxWidth(60);  // ID
        cm.getColumn(3).setMaxWidth(140); // Date
        cm.getColumn(4).setMaxWidth(120); // Locations

        JScrollPane scroll = UIStylesDark.styledScroll(table);
        scroll.setBorder(null);

        card.add(bar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // Context menu
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(UIStylesDark.BG2);
        popup.setBorder(BorderFactory.createLineBorder(UIStylesDark.BORDER, 1));
        
        JMenuItem mEdit  = new JMenuItem("Modifier");
        JMenuItem mCarte = new JMenuItem("Voir carte");
        JMenuItem mDel   = new JMenuItem("Supprimer");
        
        mEdit.setBackground(UIStylesDark.BG2);  
        mEdit.setForeground(UIStylesDark.TEXT);
        mEdit.setFont(UIStylesDark.FONT_BODY);
        
        mCarte.setBackground(UIStylesDark.BG2); 
        mCarte.setForeground(UIStylesDark.ACCENT2);
        mCarte.setFont(UIStylesDark.FONT_BODY);
        
        mDel.setBackground(UIStylesDark.BG2);   
        mDel.setForeground(UIStylesDark.RED);
        mDel.setFont(UIStylesDark.FONT_BODY);
        
        popup.add(mEdit); 
        popup.add(mCarte); 
        popup.addSeparator(); 
        popup.add(mDel);
        
        table.setComponentPopupMenu(popup);
        mEdit.addActionListener(e  -> editSelected());
        mCarte.addActionListener(e -> carteSelected());
        mDel.addActionListener(e   -> deleteSelected());

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
            allAbonnes = dao.findAll();
            filterTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        String q = txtSearch.getText().toLowerCase().trim();
        model.setRowCount(0);
        
        for (Abonnee a : allAbonnes) {
            if (q.isEmpty() || a.getNomAbo().toLowerCase().contains(q)) {
                int n = 0;
                try { 
                    n = locDao.countLocationsEnCours(a.getId()); 
                } catch(Exception ignored){}
                
                String status = n + "/3";
                if (n >= 3) status += " (Max)";
                
                model.addRow(new Object[]{
                    a.getId(), 
                    a.getNomAbo(),
                    a.getAdresseAbo() != null ? a.getAdresseAbo() : "—",
                    a.getDateAbo() != null ? sdf.format(a.getDateAbo()) : "—",
                    status
                });
            }
        }
    }

    private void openForm(Abonnee existing) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            existing == null ? "Nouvel abonné" : "Modifier l'abonné", true);
        d.getContentPane().setBackground(UIStylesDark.BG2);
        d.setSize(460, 380);
        d.setLocationRelativeTo(this);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(UIStylesDark.BG2);
        body.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; 
        gc.insets = new Insets(6, 0, 6, 0);
        gc.weightx = 1.0;

        JTextField fNom  = UIStylesDark.styledField(20);
        JTextField fAdr  = UIStylesDark.styledField(20);
        JTextField fDate = UIStylesDark.styledField(20);

        if (existing != null) {
            fNom.setText(existing.getNomAbo());
            fAdr.setText(existing.getAdresseAbo() != null ? existing.getAdresseAbo() : "");
            fDate.setText(existing.getDateAbo() != null ? sdf.format(existing.getDateAbo()) : "");
        } else {
            fDate.setText(sdf.format(new Date()));
        }

        addFormRow(body, gc, 0, "Nom complet *", fNom);
        addFormRow(body, gc, 2, "Adresse", fAdr);
        addFormRow(body, gc, 4, "Date abonnement (jj/mm/aaaa)", fDate);

        JLabel err = new JLabel(" ");
        err.setForeground(UIStylesDark.RED); 
        err.setFont(UIStylesDark.FONT_SMALL);
        gc.gridy = 6; 
        body.add(err, gc);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footer.setBackground(UIStylesDark.BG2);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIStylesDark.BORDER));
        
        JButton cancel = UIStylesDark.ghostButton("Annuler");
        JButton save   = UIStylesDark.accentButton("Enregistrer");
        
        footer.add(cancel); 
        footer.add(save);

        cancel.addActionListener(e -> d.dispose());
        save.addActionListener(e -> {
            if (fNom.getText().trim().isEmpty()) { 
                err.setText("Le nom est requis."); 
                return; 
            }
            try {
                Abonnee a = existing != null ? existing : new Abonnee();
                a.setNomAbo(fNom.getText().trim());
                a.setAdresseAbo(fAdr.getText().trim());
                try { 
                    a.setDateAbo(fDate.getText().isEmpty() ? null : sdf.parse(fDate.getText())); 
                } catch (Exception ignored) {}
                
                if (existing == null) {
                    Abonnee newAbonnee = dao.save(a);
                    carteDao.save(new CarteAbbonne(0, newAbonnee.getNomAbo(), newAbonnee.getAdresseAbo(), newAbonnee.getId()));
                } else {
                    dao.update(a);
                    CarteAbbonne carte = carteDao.findByAbonne(a.getId());
                    if (carte != null) {
                        carte.setNomAbo(a.getNomAbo()); 
                        carte.setAdresseAbo(a.getAdresseAbo());
                        carteDao.update(carte);
                    }
                }
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

    private void carteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { 
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonné."); 
            return; 
        }
        int id = (int) model.getValueAt(row, 0);
        String nom = (String) model.getValueAt(row, 1);
        String adr = (String) model.getValueAt(row, 2);
        
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Carte d'Abonné", true);
        d.setSize(420, 280);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(UIStylesDark.BG);

        JPanel carte = new JPanel(new BorderLayout(0, 16));
        carte.setBackground(new Color(255, 255, 255));
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStylesDark.BORDER, 1),
            BorderFactory.createEmptyBorder(24, 32, 24, 32)));

        JLabel club = new JLabel("VIDÉO CLUB — CARTE MEMBRE");
        club.setFont(new Font("SansSerif", Font.BOLD, 12));
        club.setForeground(UIStylesDark.ACCENT);

        JLabel lNom = new JLabel(nom.toUpperCase());
        lNom.setFont(new Font("SansSerif", Font.BOLD, 22));
        lNom.setForeground(UIStylesDark.TEXT);

        JLabel lAdr = new JLabel(adr);
        lAdr.setFont(UIStylesDark.FONT_BODY);
        lAdr.setForeground(UIStylesDark.MUTED);

        JLabel lId = new JLabel("N° " + String.format("%06d", id));
        lId.setFont(new Font("Monospaced", Font.BOLD, 14));
        lId.setForeground(UIStylesDark.ACCENT2);

        carte.add(club, BorderLayout.NORTH);
        
        JPanel mid = new JPanel(new GridLayout(3, 1, 0, 8));
        mid.setBackground(Color.WHITE);
        mid.add(lNom); 
        mid.add(lAdr); 
        mid.add(lId);
        
        carte.add(mid, BorderLayout.CENTER);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(UIStylesDark.BG);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.add(carte, BorderLayout.CENTER);

        JButton close = UIStylesDark.ghostButton("Fermer");
        close.addActionListener(e -> d.dispose());
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(UIStylesDark.BG);
        footer.add(close);

        d.setLayout(new BorderLayout());
        d.add(container, BorderLayout.CENTER);
        d.add(footer, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { 
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne."); 
            return; 
        }
        int id = (int) model.getValueAt(row, 0);
        Abonnee target = allAbonnes.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
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
            "Supprimer cet abonné ?\nAttention : Sa carte et ses locations seront aussi supprimées.", 
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
