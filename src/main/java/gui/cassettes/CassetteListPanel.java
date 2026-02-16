package gui.cassettes;

import dao.CassetteDAO;
import dao.TitreDAO;
import models.Cassette;
import models.Titre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CassetteListPanel - Affichage et gestion de la liste des cassettes
 *
 * Affiche toutes les cassettes disponibles en un tableau avec :
 * - ID Cassette
 * - Date d'achat
 * - Prix
 * - Titre du film
 *
 * Opérations CRUD disponibles :
 * - Ajouter une cassette
 * - Modifier une cassette
 * - Supprimer une cassette
 * - Rechercher une cassette
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class CassetteListPanel extends JPanel {

    private JTable tableCassettes;
    private DefaultTableModel modelTable;
    private CassetteDAO cassetteDAO;
    private TitreDAO titreDAO;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRechercheAvancee, btnActualiser;
    private CassetteSearchPanel searchPanel;

    public CassetteListPanel() {
        cassetteDAO = new CassetteDAO();
        titreDAO = new TitreDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        createSearchPanel();
        createTable();
        createButtonPanel();
        loadData();
    }

    /**
     * Crée le panel de recherche
     */
    private void createSearchPanel() {
        searchPanel = new CassetteSearchPanel(this::loadDataFiltered);
        add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * Crée le tableau des cassettes
     */
    private void createTable() {
        String[] colonnes = {"ID", "Date d'achat", "Prix (€)", "Titre"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCassettes = new JTable(modelTable);
        tableCassettes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCassettes.setRowHeight(25);
        tableCassettes.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableCassettes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableCassettes.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableCassettes.getColumnModel().getColumn(3).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(tableCassettes);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Crée le panel de boutons
     */
    private void createButtonPanel() {
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelBoutons.setBackground(new Color(240, 240, 240));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnAjouter = new JButton("➕ Ajouter");
        btnAjouter.addActionListener(e -> ajouterCassette());
        panelBoutons.add(btnAjouter);

        btnModifier = new JButton("✏️ Modifier");
        btnModifier.addActionListener(e -> modifierCassette());
        panelBoutons.add(btnModifier);

        btnSupprimer = new JButton("🗑️ Supprimer");
        btnSupprimer.addActionListener(e -> supprimerCassette());
        panelBoutons.add(btnSupprimer);

        panelBoutons.add(Box.createHorizontalStrut(20));

        btnRechercheAvancee = new JButton("🔍 Recherche avancée");
        btnRechercheAvancee.addActionListener(e -> afficherRecherche());
        panelBoutons.add(btnRechercheAvancee);

        btnActualiser = new JButton("🔄 Actualiser");
        btnActualiser.addActionListener(e -> loadData());
        panelBoutons.add(btnActualiser);

        add(panelBoutons, BorderLayout.SOUTH);
    }

    /**
     * Charge toutes les cassettes
     */
    public void loadData() {
        try {
            modelTable.setRowCount(0);
            List<Cassette> cassettes = cassetteDAO.readAll();

            for (Cassette cassette : cassettes) {
                Titre titre = titreDAO.read(cassette.getIdTitre());
                String nomTitre = (titre != null) ? titre.getNomTitre() : "N/A";

                Object[] row = {
                        cassette.getNumCassette(),
                        cassette.getDateAchat(),
                        String.format("%.2f €", cassette.getPrix()),
                        nomTitre
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
     * Charge les cassettes filtrées par titre
     */
    public void loadDataFiltered(String titre) {
        try {
            modelTable.setRowCount(0);
            List<Cassette> cassettes = cassetteDAO.readAll();

            for (Cassette cassette : cassettes) {
                Titre titreObj = titreDAO.read(cassette.getIdTitre());
                String nomTitre = (titreObj != null) ? titreObj.getNomTitre() : "N/A";

                // Filtrer par titre si la recherche n'est pas vide
                if (titre.isEmpty() || nomTitre.toLowerCase().contains(titre.toLowerCase())) {
                    Object[] row = {
                            cassette.getNumCassette(),
                            cassette.getDateAchat(),
                            String.format("%.2f €", cassette.getPrix()),
                            nomTitre
                    };
                    modelTable.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du filtrage : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Affiche/cache le panel de recherche avancée
     */
    private void afficherRecherche() {
        searchPanel.setVisible(!searchPanel.isVisible());
    }

    /**
     * Ajoute une nouvelle cassette
     */
    private void ajouterCassette() {
        CassetteFormPanel formPanel = new CassetteFormPanel(null);
        int result = JOptionPane.showConfirmDialog(this,
                formPanel,
                "Ajouter une cassette",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Cassette cassette = formPanel.getCassette();
            if (cassette != null) {
                try {
                    if (cassetteDAO.create(cassette)) {
                        JOptionPane.showMessageDialog(this,
                                "✓ Cassette ajoutée avec succès",
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Erreur : " + e.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Modifie la cassette sélectionnée
     */
    private void modifierCassette() {
        int selectedRow = tableCassettes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une cassette à modifier",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = (int) modelTable.getValueAt(selectedRow, 0);
            Cassette cassette = cassetteDAO.read(id);

            CassetteFormPanel formPanel = new CassetteFormPanel(cassette);
            int result = JOptionPane.showConfirmDialog(this,
                    formPanel,
                    "Modifier la cassette",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                Cassette updated = formPanel.getCassette();
                if (updated != null && cassetteDAO.update(updated)) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Cassette modifiée avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadData();
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
     * Supprime la cassette sélectionnée
     */
    private void supprimerCassette() {
        int selectedRow = tableCassettes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une cassette à supprimer",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette cassette ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (cassetteDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Cassette supprimée avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Impossible de supprimer (cassette en location)",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur : " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}