package gui.abonnes;

import dao.AbonneDAO;
import models.Abonne;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * AbonneListPanel - Affichage et gestion de la liste des abonnés
 *
 * Affiche tous les abonnés (clients) du club vidéo en un tableau avec :
 * - ID Abonné
 * - Nom
 * - Adresse
 * - Date d'abonnement
 * - Date d'entrée
 *
 * Opérations CRUD disponibles :
 * - Ajouter un abonné
 * - Modifier un abonné
 * - Supprimer un abonné
 * - Rechercher un abonné
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.1
 */
public class AbonneListPanel extends JPanel {

    private JTable tableAbonnes;
    private DefaultTableModel modelTable;
    private AbonneDAO abonneDAO;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private AbonneSearchPanel searchPanel;

    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color panelBackgroundColor = Color.WHITE;

    public AbonneListPanel() {
        abonneDAO = new AbonneDAO();
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
        JLabel title = new JLabel("Gestion des Abonnés");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Crée le panel de recherche
     */
    private void createSearchPanel() {
        searchPanel = new AbonneSearchPanel(this::loadDataFiltered);
        searchPanel.setBackground(panelBackgroundColor);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * Crée le tableau des abonnés
     */
    private void createTable() {
        String[] colonnes = {"ID", "Nom", "Adresse", "Date abonnement", "Date entrée"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableAbonnes = new JTable(modelTable);
        tableAbonnes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableAbonnes.setRowHeight(30);
        tableAbonnes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableAbonnes.setBackground(panelBackgroundColor);
        tableAbonnes.setFillsViewportHeight(true);
        tableAbonnes.setGridColor(new Color(230, 230, 230));

        // Header styling
        JTableHeader header = tableAbonnes.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230)); // Light gray background
        header.setForeground(Color.BLACK); // Black text
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        tableAbonnes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableAbonnes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableAbonnes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableAbonnes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableAbonnes.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tableAbonnes);
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
        btnAjouter.addActionListener(e -> ajouterAbonne());
        panelBoutons.add(btnAjouter);

        btnModifier = createStyledButton("✏️ Modifier", primaryColor);
        btnModifier.addActionListener(e -> modifierAbonne());
        panelBoutons.add(btnModifier);

        btnSupprimer = createStyledButton("🗑️ Supprimer", new Color(231, 76, 60));
        btnSupprimer.addActionListener(e -> supprimerAbonne());
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
     * Charge tous les abonnés
     */
    public void loadData() {
        try {
            modelTable.setRowCount(0);
            List<Abonne> abonnes = abonneDAO.readAll();

            for (Abonne abonne : abonnes) {
                Object[] row = {
                        abonne.getNumAbonne(),
                        abonne.getNom(),
                        abonne.getAdresse(),
                        abonne.getDateAbonnement(),
                        abonne.getDateEntree()
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
     * Charge les abonnés filtrés par nom
     */
    public void loadDataFiltered(String nom) {
        try {
            modelTable.setRowCount(0);
            List<Abonne> abonnes = abonneDAO.readAll();

            for (Abonne abonne : abonnes) {
                if (nom.isEmpty() || abonne.getNom().toLowerCase().contains(nom.toLowerCase())) {
                    Object[] row = {
                            abonne.getNumAbonne(),
                            abonne.getNom(),
                            abonne.getAdresse(),
                            abonne.getDateAbonnement(),
                            abonne.getDateEntree()
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
     * Ajoute un nouvel abonné
     */
    private void ajouterAbonne() {
        AbonneFormPanel formPanel = new AbonneFormPanel(null);
        int result = JOptionPane.showConfirmDialog(this,
                formPanel,
                "Ajouter un abonné",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Abonne abonne = formPanel.getAbonne();
            if (abonne != null) {
                try {
                    if (abonneDAO.create(abonne)) {
                        JOptionPane.showMessageDialog(this,
                                "✓ Abonné ajouté avec succès",
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
     * Modifie l'abonné sélectionné
     */
    private void modifierAbonne() {
        int selectedRow = tableAbonnes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un abonné à modifier",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = (int) modelTable.getValueAt(selectedRow, 0);
            Abonne abonne = abonneDAO.read(id);

            AbonneFormPanel formPanel = new AbonneFormPanel(abonne);
            int result = JOptionPane.showConfirmDialog(this,
                    formPanel,
                    "Modifier l'abonné",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                Abonne updated = formPanel.getAbonne();
                if (updated != null && abonneDAO.update(updated)) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Abonné modifié avec succès",
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
     * Supprime l'abonné sélectionné
     */
    private void supprimerAbonne() {
        int selectedRow = tableAbonnes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un abonné à supprimer",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelTable.getValueAt(selectedRow, 0);
        String nom = (String) modelTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer " + nom + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (abonneDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Abonné supprimé avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Impossible de supprimer (abonné possède des locations)",
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