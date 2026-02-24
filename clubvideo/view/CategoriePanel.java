package clubvideo.view;

import clubvideo.dao.CategorieDAO;
import clubvideo.model.Categorie;
import clubvideo.util.UIStylesDark;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class CategoriePanel extends JPanel {

    private final CategorieDAO dao = new CategorieDAO();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Libellé"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField txtSearch = UIStylesDark.styledField(20);
    
    // Cache
    private List<Categorie> allCategories = new ArrayList<>();

    public CategoriePanel() {
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

        JLabel ttl = new JLabel("Gestion des Catégories");
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

        bar.add(ttl,   BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        // ── Table ──
        UIStylesDark.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        
        JScrollPane scroll = UIStylesDark.styledScroll(table);
        scroll.setBorder(null);

        card.add(bar,    BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // ── Events ──
        btnAdd.addActionListener(e -> openForm(null));
        
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editSelected();
            }
        });

        // ── Context menu ──
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

        refresh();
    }

    public void refresh() {
        try {
            allCategories = dao.findAll();
            filterTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        String q = txtSearch.getText().toLowerCase().trim();
        model.setRowCount(0);
        
        for (Categorie c : allCategories) {
            if (q.isEmpty() || c.getLibelleCat().toLowerCase().contains(q)) {
                model.addRow(new Object[]{c.getId(), c.getLibelleCat()});
            }
        }
    }

    private void openForm(Categorie existing) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing == null ? "Nouvelle catégorie" : "Modifier la catégorie", true);
        d.getContentPane().setBackground(UIStylesDark.BG2);
        d.setSize(400, 240);
        d.setLocationRelativeTo(this);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(UIStylesDark.BG2);
        body.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; 
        gc.insets = new Insets(6, 0, 6, 0);
        gc.weightx = 1.0;

        JTextField txt = UIStylesDark.styledField(20);
        if (existing != null) txt.setText(existing.getLibelleCat());
        
        gc.gridy = 0;
        body.add(UIStylesDark.sectionLabel("Libellé *"), gc);
        gc.gridy = 1;
        body.add(txt, gc);

        JLabel err = new JLabel(" ");
        err.setForeground(UIStylesDark.RED);
        err.setFont(UIStylesDark.FONT_SMALL);
        gc.gridy = 2;
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
            String lib = txt.getText().trim();
            if (lib.isEmpty()) { 
                err.setText("Le libellé est requis."); 
                return; 
            }
            try {
                if (existing == null) { 
                    dao.save(new Categorie(0, lib)); 
                } else { 
                    existing.setLibelleCat(lib); 
                    dao.update(existing); 
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

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { 
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne."); 
            return; 
        }
        int id = (int) model.getValueAt(row, 0);
        Categorie target = allCategories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
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
            "Voulez-vous vraiment supprimer cette catégorie ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try { 
                dao.delete(id); 
                refresh(); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Impossible de supprimer (peut-être utilisée) : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
