package gui.locations;

import dao.AbonneDAO;
import dao.LocationDAO;
import dao.TitreDAO;
import dao.CassetteDAO;
import models.Abonne;
import models.Location;
import models.Titre;
import models.Cassette;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RetourPanel - Panel de gestion des retours de cassettes
 *
 * Permet de visualiser et enregistrer les retours de cassettes.
 * Les retours suppriment l'entrée dans la table LOCATION.
 *
 * Fonctionnalités :
 * - Affichage des locations en cours (cassettes à retourner)
 * - Enregistrement du retour d'une cassette
 * - Historique des cassettes retournées
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class RetourPanel extends JPanel {

    private JTable tableLocationEnCours;
    private DefaultTableModel modelTable;
    private LocationDAO locationDAO;
    private AbonneDAO abonneDAO;
    private CassetteDAO cassetteDAO;
    private TitreDAO titreDAO;
    private JButton btnRetourner, btnActualiser;
    private JLabel lblNombreLocations;

    public RetourPanel() {
        locationDAO = new LocationDAO();
        abonneDAO = new AbonneDAO();
        cassetteDAO = new CassetteDAO();
        titreDAO = new TitreDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        createInfoPanel();
        createTable();
        createButtonPanel();
        loadData();
    }

    /**
     * Crée le panel d'information
     */
    private void createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(230, 240, 250));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblInfo = new JLabel("Locations en cours à retourner :");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 13));

        lblNombreLocations = new JLabel("0 cassettes");
        lblNombreLocations.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombreLocations.setForeground(new Color(41, 128, 185));

        infoPanel.add(lblInfo);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(lblNombreLocations);

        add(infoPanel, BorderLayout.NORTH);
    }

    /**
     * Crée le tableau des locations en cours
     */
    private void createTable() {
        String[] colonnes = {"ID Abonné", "Nom", "ID Cassette", "Titre", "Depuis le"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableLocationEnCours = new JTable(modelTable);
        tableLocationEnCours.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLocationEnCours.setRowHeight(25);
        tableLocationEnCours.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableLocationEnCours.getColumnModel().getColumn(1).setPreferredWidth(130);
        tableLocationEnCours.getColumnModel().getColumn(2).setPreferredWidth(70);
        tableLocationEnCours.getColumnModel().getColumn(3).setPreferredWidth(200);
        tableLocationEnCours.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tableLocationEnCours);
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

        btnRetourner = new JButton("✓ Confirmer le retour");
        btnRetourner.setFont(new Font("Arial", Font.BOLD, 12));
        btnRetourner.setBackground(new Color(52, 152, 219));
        btnRetourner.setForeground(Color.WHITE);
        btnRetourner.setFocusPainted(false);
        btnRetourner.addActionListener(e -> retournerCassette());
        panelBoutons.add(btnRetourner);

        panelBoutons.add(Box.createHorizontalStrut(20));

        btnActualiser = new JButton("🔄 Actualiser");
        btnActualiser.addActionListener(e -> loadData());
        panelBoutons.add(btnActualiser);

        add(panelBoutons, BorderLayout.SOUTH);
    }

    /**
     * Charge les locations en cours
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

            // Mettre à jour le label
            int count = modelTable.getRowCount();
            lblNombreLocations.setText(count + " cassette" + (count > 1 ? "s" : ""));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retourne la cassette sélectionnée
     */
    private void retournerCassette() {
        int selectedRow = tableLocationEnCours.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une location à retourner",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numAbonne = (int) modelTable.getValueAt(selectedRow, 0);
            String nomAbonne = (String) modelTable.getValueAt(selectedRow, 1);
            int numCassette = (int) modelTable.getValueAt(selectedRow, 2);
            String titre = (String) modelTable.getValueAt(selectedRow, 3);

            // Confirmation du retour
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirmer le retour :\n\n" +
                            "Abonné : " + nomAbonne + "\n" +
                            "Cassette : " + titre,
                    "Confirmation du retour",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (locationDAO.delete(numAbonne, numCassette)) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Retour enregistré avec succès !\n\n" +
                                    nomAbonne + " a retourné : " + titre,
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erreur lors de l'enregistrement du retour",
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
}