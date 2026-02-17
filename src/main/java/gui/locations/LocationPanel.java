package gui.locations;

import dao.AbonneDAO;
import dao.CassetteDAO;
import dao.LocationDAO;
import dao.TitreDAO;
import models.Abonne;
import models.Cassette;
import models.Location;
import models.Titre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

/**
 * LocationPanel - Gestion des locations de cassettes
 *
 * ⚠️ PANEL CRITIQUE - Implémente la RÈGLE MÉTIER :
 * Un abonné peut louer MAXIMUM 3 cassettes simultanément
 *
 * Fonctionnalités :
 * - Affichage des locations actuelles
 * - Enregistrement d'une nouvelle location (avec validation)
 * - Historique des locations
 *
 * Validations métier :
 * ✓ canAddLocation() : Vérifie limite de 3 cassettes
 * ✓ estLouee() : Vérifie que cassette n'est pas déjà louée
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.1
 */
public class LocationPanel extends JPanel {

    private JTable tableLocations;
    private DefaultTableModel modelTable;
    private LocationDAO locationDAO;
    private AbonneDAO abonneDAO;
    private CassetteDAO cassetteDAO;
    private TitreDAO titreDAO;
    private JButton btnAjouterLocation, btnRetourner, btnActualiser;

    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color panelBackgroundColor = Color.WHITE;

    public LocationPanel() {
        locationDAO = new LocationDAO();
        abonneDAO = new AbonneDAO();
        cassetteDAO = new CassetteDAO();
        titreDAO = new TitreDAO();
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        createTitlePanel();
        createTable();
        createButtonPanel();
        loadData();
    }

    private void createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JLabel title = new JLabel("Gestion des Locations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Crée le tableau des locations
     */
    private void createTable() {
        String[] colonnes = {"ID Abonné", "Nom Abonné", "ID Cassette", "Titre", "Date Location"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableLocations = new JTable(modelTable);
        tableLocations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLocations.setRowHeight(30);
        tableLocations.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableLocations.setBackground(panelBackgroundColor);
        tableLocations.setFillsViewportHeight(true);
        tableLocations.setGridColor(new Color(230, 230, 230));

        // Header styling
        JTableHeader header = tableLocations.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230)); // Light gray background
        header.setForeground(Color.BLACK); // Black text
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(tableLocations);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE); // Ensure viewport is white
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Crée le panel de boutons
     */
    private void createButtonPanel() {
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBoutons.setBackground(backgroundColor);
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnAjouterLocation = createStyledButton("➕ Louer une cassette", primaryColor);
        btnAjouterLocation.addActionListener(e -> ajouterLocation());
        panelBoutons.add(btnAjouterLocation);

        btnRetourner = createStyledButton("🔙 Retourner une cassette", new Color(231, 76, 60));
        btnRetourner.addActionListener(e -> retournerCassette());
        panelBoutons.add(btnRetourner);

        panelBoutons.add(Box.createHorizontalStrut(20));

        btnActualiser = createStyledButton("🔄 Actualiser", new Color(149, 165, 166));
        btnActualiser.addActionListener(e -> loadData());
        panelBoutons.add(btnActualiser);

        add(panelBoutons, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    /**
     * Charge toutes les locations
     */
    public void loadData() {
        try {
            modelTable.setRowCount(0);
            List<Location> locations = locationDAO.readAll();

            for (Location loc : locations) {
                Abonne abonne = abonneDAO.read(loc.getNumAbonne());
                Cassette cassette = cassetteDAO.read(loc.getNumCassette());
                Titre titre = (cassette != null) ? titreDAO.read(cassette.getIdTitre()) : null;

                String nomAbonne = (abonne != null) ? abonne.getNom() : "N/A";
                String nomTitre = (titre != null) ? titre.getNomTitre() : "N/A";

                Object[] row = {
                        loc.getNumAbonne(),
                        nomAbonne,
                        loc.getNumCassette(),
                        nomTitre,
                        loc.getDateLocation()
                };
                modelTable.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * AJOUTE UNE NOUVELLE LOCATION
     * ⚠️ VÉRIFICATIONS RÈGLE MÉTIER :
     * 1. Max 3 cassettes par abonné (canAddLocation)
     * 2. Cassette pas déjà louée (estLouee)
     */
    private void ajouterLocation() {
        try {
            List<Abonne> abonnes = abonneDAO.readAll();
            List<Cassette> cassettes = cassetteDAO.readAll();

            if (abonnes.isEmpty() || cassettes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucun abonné ou cassette disponible",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Dialog de saisie
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Enregistrer une location", true);
            dialog.setSize(450, 250);
            dialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.WHITE);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JComboBox<String> comboAbonne = new JComboBox<>();
            for (Abonne a : abonnes) {
                comboAbonne.addItem(a.getNom() + " (ID: " + a.getNumAbonne() + ")");
            }

            JComboBox<String> comboCassette = new JComboBox<>();
            for (Cassette c : cassettes) {
                Titre t = titreDAO.read(c.getIdTitre());
                String titre = (t != null) ? t.getNomTitre() : "N/A";
                comboCassette.addItem(titre + " (ID: " + c.getNumCassette() + ")");
            }

            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Abonné :"), gbc);
            gbc.gridx = 1; gbc.gridy = 0;
            panel.add(comboAbonne, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Cassette :"), gbc);
            gbc.gridx = 1; gbc.gridy = 1;
            panel.add(comboCassette, gbc);

            JButton btnOK = createStyledButton("Confirmer location", primaryColor);
            btnOK.addActionListener(e -> {
                try {
                    Abonne selectedAbonne = abonnes.get(comboAbonne.getSelectedIndex());
                    Cassette selectedCassette = cassettes.get(comboCassette.getSelectedIndex());

                    // ⚠️ VÉRIFICATION 1 : Max 3 cassettes par abonné
                    if (!abonneDAO.canAddLocation(selectedAbonne.getNumAbonne())) {
                        JOptionPane.showMessageDialog(dialog,
                                "❌ ERREUR MÉTIER :\n\n" +
                                        selectedAbonne.getNom() + " a déjà 3 cassettes en location.\n" +
                                        "Maximum de 3 cassettes par abonné atteint !\n\n" +
                                        "Veuillez retourner une cassette avant.",
                                "Limite atteinte",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // ⚠️ VÉRIFICATION 2 : Cassette pas déjà louée
                    if (locationDAO.estLouee(selectedCassette.getNumCassette())) {
                        JOptionPane.showMessageDialog(dialog,
                                "❌ Cette cassette est déjà en location à un autre abonné",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Tout est OK, créer la location
                    Location location = new Location(
                            selectedAbonne.getNumAbonne(),
                            selectedCassette.getNumCassette(),
                            LocalDate.now()
                    );

                    if (locationDAO.create(location)) {
                        JOptionPane.showMessageDialog(LocationPanel.this,
                                "✓ Location enregistrée pour " + selectedAbonne.getNom() + "\n" +
                                        "Cassette : " + getTitreFromCassette(selectedCassette),
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Erreur : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 0; gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(btnOK, gbc);

            dialog.add(panel);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * RETOURNE UNE CASSETTE
     * Supprime la location et rend la cassette disponible
     */
    private void retournerCassette() {
        int selectedRow = tableLocations.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une location à retourner",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numAbonne = (int) modelTable.getValueAt(selectedRow, 0);
            int numCassette = (int) modelTable.getValueAt(selectedRow, 2);

            Abonne abonne = abonneDAO.read(numAbonne);
            String nomAbonne = (abonne != null) ? abonne.getNom() : "Abonné " + numAbonne;

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirmer le retour de la cassette pour " + nomAbonne + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (locationDAO.delete(numAbonne, numCassette)) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Cassette retournée avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erreur lors du retour",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Récupère le titre associé à une cassette
     */
    private String getTitreFromCassette(Cassette cassette) {
        try {
            Titre titre = titreDAO.read(cassette.getIdTitre());
            return (titre != null) ? titre.getNomTitre() : "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }
}