package gui.cassettes;

import dao.CassetteDAO;
import dao.TitreDAO;
import models.Cassette;
import models.Titre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

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
 * @version 2.1
 */
public class CassetteListPanel extends JPanel {

    private JTable tableCassettes;
    private DefaultTableModel modelTable;
    private CassetteDAO cassetteDAO;
    private TitreDAO titreDAO;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private CassetteSearchPanel searchPanel;

    private Color primaryColor = new Color(41, 128, 185);
    private Color hoverColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color panelBackgroundColor = Color.WHITE;

    public CassetteListPanel() {
        cassetteDAO = new CassetteDAO();
        titreDAO = new TitreDAO();
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        createTitlePanel();
        createSearchPanel();
        createTable();
        createButtonPanel();
        loadData();
    }

    private void createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JLabel title = new JLabel("Gestion des Cassettes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Crée le panel de recherche
     */
    private void createSearchPanel() {
        searchPanel = new CassetteSearchPanel(this::loadDataFiltered);
        searchPanel.setBackground(panelBackgroundColor);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        add(searchPanel, BorderLayout.NORTH); // This will be added after the title panel
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
        tableCassettes.setRowHeight(30);
        tableCassettes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableCassettes.setBackground(panelBackgroundColor);
        tableCassettes.setFillsViewportHeight(true);
        tableCassettes.setGridColor(new Color(230, 230, 230));

        // Header styling
        JTableHeader header = tableCassettes.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230)); // Light gray background
        header.setForeground(Color.BLACK); // Black text
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        tableCassettes.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableCassettes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableCassettes.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableCassettes.getColumnModel().getColumn(3).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(tableCassettes);
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

        btnAjouter = createStyledButton("➕ Ajouter", primaryColor);
        btnAjouter.addActionListener(e -> ajouterCassette());
        panelBoutons.add(btnAjouter);

        btnModifier = createStyledButton("✏️ Modifier", primaryColor);
        btnModifier.addActionListener(e -> modifierCassette());
        panelBoutons.add(btnModifier);

        btnSupprimer = createStyledButton("🗑️ Supprimer", new Color(231, 76, 60)); // Red color for delete
        btnSupprimer.addActionListener(e -> supprimerCassette());
        panelBoutons.add(btnSupprimer);

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
        button.setPreferredSize(new Dimension(120, 35));
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
     * Charge toutes les cassettes
     */
    public void loadData() {
        loadDataFiltered("");
    }

    /**
     * Charge les cassettes filtrées par titre
     */
    public void loadDataFiltered(String titreRecherche) {
        try {
            modelTable.setRowCount(0);
            List<Cassette> cassettes = cassetteDAO.readAll();
            List<Object[]> rows = new ArrayList<>();

            for (Cassette cassette : cassettes) {
                Titre titreObj = titreDAO.read(cassette.getIdTitre());
                String nomTitre = (titreObj != null) ? titreObj.getNomTitre() : "N/A";

                // Filtrer par titre si la recherche n'est pas vide
                if (titreRecherche == null || titreRecherche.isEmpty() ||
                    nomTitre.toLowerCase().contains(titreRecherche.toLowerCase())) {

                    Object[] row = {
                            cassette.getNumCassette(),
                            cassette.getDateAchat(),
                            String.format("%.2f €", cassette.getPrix()),
                            nomTitre
                    };
                    rows.add(row);
                }
            }

            // Ajouter toutes les lignes au modèle
            for (Object[] row : rows) {
                modelTable.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
            try {
                Cassette cassette = formPanel.getCassette();
                if (cassette != null) {
                    if (cassetteDAO.create(cassette)) {
                        JOptionPane.showMessageDialog(this,
                                "✓ Cassette ajoutée avec succès",
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

            if (cassette == null) {
                JOptionPane.showMessageDialog(this,
                        "Erreur : Cassette introuvable",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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
                            "Impossible de supprimer (cassette en location ou erreur)",
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