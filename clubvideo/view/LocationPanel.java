package clubvideo.view;

import clubvideo.dao.*;
import clubvideo.model.*;
import clubvideo.util.UIStylesDark;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LocationPanel extends JPanel {

    private final LocationDAO locDao  = new LocationDAO();
    private final CassetteDAO casDao  = new CassetteDAO();
    private final AbonneeDAO  aboDao  = new AbonneeDAO();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"Cassette", "Abonné", "Date Limite", "Retour Prévu", "Statut"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // stocke id_cassette et id_abonne de chaque ligne affichée
    // Note: Utiliser une classe dédiée ou un Map serait plus propre, mais on garde la structure simple
    private final List<Location> currentLocations = new ArrayList<>();
    
    private boolean showEnCours = true;
    private JToggleButton btnEnCours;
    private JToggleButton btnHistorique;

    public LocationPanel() {
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

        JLabel ttl = new JLabel("Gestion des Locations");
        ttl.setFont(UIStylesDark.FONT_H2); 
        ttl.setForeground(UIStylesDark.TEXT);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setBackground(UIStylesDark.BG2);

        btnEnCours    = new JToggleButton("En cours", true);
        btnHistorique = new JToggleButton("Historique", false);
        
        styleToggle(btnEnCours, true);
        styleToggle(btnHistorique, false);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(btnEnCours); 
        bg.add(btnHistorique);

        JButton btnNouvelle = UIStylesDark.accentButton("+ Nouvelle location");
        JButton btnRetour   = UIStylesDark.greenButton("↩ Retour");

        right.add(btnEnCours); 
        right.add(btnHistorique);
        right.add(Box.createHorizontalStrut(16));
        right.add(btnRetour); 
        right.add(btnNouvelle);

        bar.add(ttl, BorderLayout.WEST); 
        bar.add(right, BorderLayout.EAST);

        UIStylesDark.styleTable(table);
        table.getColumnModel().getColumn(4).setMaxWidth(120);

        JScrollPane scroll = UIStylesDark.styledScroll(table);
        scroll.setBorder(null);

        card.add(bar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // ── Events ──
        btnEnCours.addActionListener(e -> { 
            showEnCours = true;  
            updateToggles();
            refresh(); 
        });
        
        btnHistorique.addActionListener(e -> { 
            showEnCours = false; 
            updateToggles();
            refresh(); 
        });
        
        btnNouvelle.addActionListener(e -> openNouvelleLocation());
        btnRetour.addActionListener(e   -> retourSelected());

        refresh();
    }
    
    private void updateToggles() {
        styleToggle(btnEnCours, showEnCours);
        styleToggle(btnHistorique, !showEnCours);
    }

    public void refresh() {
        model.setRowCount(0);
        currentLocations.clear();
        try {
            List<Location> all;
            if (showEnCours) {
                all = locDao.findEnCours();
            } else {
                // Historique : tout ce qui a été retourné (disponibilite = true)
                // Note: La logique métier semble être: dispo=false -> loué, dispo=true -> retourné
                // Mais findAll retourne tout. On filtre.
                all = locDao.findAll();
                // On ne garde que ceux qui sont retournés pour l'historique
                all.removeIf(l -> !l.isDisponibilite());
            }
            
            for (Location l : all) {
                // Récupération des noms si non présents dans l'objet Location
                // (Dépend de l'implémentation DAO, ici on suppose que c'est déjà joint ou on fait un fetch lazy)
                // Si getTitreCassette est null, on devrait le chercher, mais pour l'instant on affiche "—"
                
                String status = l.isDisponibilite() ? "Terminé" : "En cours";
                
                model.addRow(new Object[]{
                    l.getTitreCassette() != null ? l.getTitreCassette() : "Cassette #" + l.getIdCassette(),
                    l.getNomAbonne() != null ? l.getNomAbonne() : "Abonné #" + l.getIdAbonne(),
                    l.getDateLimite() != null ? sdf.format(l.getDateLimite()) : "—",
                    l.getDateRetourPrevue() != null ? sdf.format(l.getDateRetourPrevue()) : "—",
                    status
                });
                currentLocations.add(l);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openNouvelleLocation() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nouvelle location", true);
        d.getContentPane().setBackground(UIStylesDark.BG2);
        d.setSize(480, 420);
        d.setLocationRelativeTo(this);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(UIStylesDark.BG2);
        body.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; 
        gc.insets = new Insets(6, 0, 6, 0); 
        gc.weightx = 1.0;

        JComboBox<Object> cbCas = UIStylesDark.styledCombo();
        JComboBox<Object> cbAbo = UIStylesDark.styledCombo();
        JTextField fLimite  = UIStylesDark.styledField(20);
        JTextField fRetour  = UIStylesDark.styledField(20);

        // Dates par défaut
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DAY_OF_MONTH, 7); // +7 jours
        String nextWeek = sdf.format(cal.getTime());
        fLimite.setText(nextWeek);
        fRetour.setText(nextWeek);

        // Chargement des listes
        List<Cassette> cassettesDispo = new ArrayList<>();
        List<Abonnee> abonnes = new ArrayList<>();
        
        try {
            cassettesDispo = casDao.findDisponibles();
            abonnes = aboDao.findAll();
            
            cbCas.addItem("-- Choisir une cassette --");
            for (Cassette c : cassettesDispo) {
                cbCas.addItem(c.getTitreCas() + " (" + c.getId() + ")");
            }
            
            cbAbo.addItem("-- Choisir un abonné --");
            for (Abonnee a : abonnes) {
                // On pourrait afficher le nombre de locations en cours ici
                cbAbo.addItem(a.getNomAbo() + " (" + a.getId() + ")");
            }
        } catch (Exception ignored) {}

        // Variables finales pour usage dans lambda
        final List<Cassette> finalCassettes = cassettesDispo;
        final List<Abonnee> finalAbonnes = abonnes;

        addFormRow(body, gc, 0, "Cassette disponible *", cbCas);
        addFormRow(body, gc, 2, "Abonné *", cbAbo);
        
        // Dates sur une ligne
        JPanel rowDates = new JPanel(new GridLayout(1, 2, 16, 0));
        rowDates.setBackground(UIStylesDark.BG2);
        
        JPanel pLim = new JPanel(new BorderLayout(0, 6));
        pLim.setBackground(UIStylesDark.BG2);
        pLim.add(UIStylesDark.sectionLabel("Date limite"), BorderLayout.NORTH);
        pLim.add(fLimite, BorderLayout.CENTER);
        
        JPanel pRet = new JPanel(new BorderLayout(0, 6));
        pRet.setBackground(UIStylesDark.BG2);
        pRet.add(UIStylesDark.sectionLabel("Retour prévu"), BorderLayout.NORTH);
        pRet.add(fRetour, BorderLayout.CENTER);
        
        rowDates.add(pLim);
        rowDates.add(pRet);
        
        gc.gridy = 4;
        body.add(rowDates, gc);

        JLabel err = new JLabel(" ");
        err.setForeground(UIStylesDark.RED); 
        err.setFont(UIStylesDark.FONT_SMALL);
        gc.gridy = 6; 
        body.add(err, gc);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footer.setBackground(UIStylesDark.BG2);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIStylesDark.BORDER));
        
        JButton cancel = UIStylesDark.ghostButton("Annuler");
        JButton save   = UIStylesDark.accentButton("Valider la location");
        
        footer.add(cancel); 
        footer.add(save);

        cancel.addActionListener(e -> d.dispose());
        save.addActionListener(e -> {
            int ci = cbCas.getSelectedIndex();
            int ai = cbAbo.getSelectedIndex();
            
            if (ci <= 0 || ai <= 0) { 
                err.setText("Veuillez sélectionner une cassette et un abonné."); 
                return; 
            }
            
            try {
                Cassette cas = finalCassettes.get(ci - 1);
                Abonnee  abo = finalAbonnes.get(ai - 1);
                
                // Vérification limite 3 locations
                int nbLoc = locDao.countLocationsEnCours(abo.getId());
                if (nbLoc >= 3) {
                    err.setText("Cet abonné a atteint la limite de 3 locations !");
                    return;
                }
                
                Location l = new Location();
                l.setIdCassette(cas.getId());
                l.setIdAbonne(abo.getId());
                
                try { l.setDateLimite(sdf.parse(fLimite.getText())); } catch (Exception ignored) {}
                try { l.setDateRetourPrevue(sdf.parse(fRetour.getText())); } catch (Exception ignored) {}
                
                // Enregistrement
                // Note: La méthode enregistrerLocation doit gérer l'insertion
                // et mettre à jour le statut de la cassette si nécessaire (ou géré par trigger/procédure)
                locDao.enregistrerLocation(l);
                
                d.dispose(); 
                refresh();
            } catch (Exception ex) {
                err.setText("Erreur : " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        d.setLayout(new BorderLayout());
        d.add(body, BorderLayout.CENTER);
        d.add(footer, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void retourSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { 
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une location."); 
            return; 
        }
        
        Location loc = currentLocations.get(row);
        
        if (loc.isDisponibilite()) {
            JOptionPane.showMessageDialog(this, "Cette location est déjà terminée.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirmer le retour de la cassette :\n" + loc.getTitreCassette(), 
            "Retour Cassette", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try { 
                // On appelle la DAO pour marquer le retour
                // Note: La méthode enregistrerRetour prend idCassette et idAbonne
                // Idéalement elle devrait prendre l'ID de la location si dispo
                locDao.enregistrerRetour(loc.getIdCassette(), loc.getIdAbonne()); 
                refresh(); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du retour : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleToggle(JToggleButton b, boolean active) {
        if (active) { 
            b.setBackground(UIStylesDark.ACCENT); 
            b.setForeground(Color.WHITE); 
            b.setBorder(BorderFactory.createLineBorder(UIStylesDark.ACCENT, 1));
        } else { 
            b.setBackground(UIStylesDark.BG2);    
            b.setForeground(UIStylesDark.MUTED); 
            b.setBorder(BorderFactory.createLineBorder(UIStylesDark.BORDER, 1));
        }
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false); 
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(100, 32));
    }

    private void addFormRow(JPanel p, GridBagConstraints gc, int row, String lbl, JComponent f) {
        gc.gridy = row;
        p.add(UIStylesDark.sectionLabel(lbl), gc);
        gc.gridy = row + 1;
        p.add(f, gc);
    }
}
