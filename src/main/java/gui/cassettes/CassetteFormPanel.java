package gui.cassettes;

import dao.TitreDAO;
import models.Cassette;
import models.Titre;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * CassetteFormPanel - Formulaire pour ajouter/modifier une cassette
 *
 * Panel formulaire réutilisable pour :
 * - Ajouter une nouvelle cassette
 * - Modifier une cassette existante
 *
 * Champs :
 * - Date d'achat (JTextField)
 * - Prix (JSpinner)
 * - Titre (JComboBox)
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.0
 */
public class CassetteFormPanel extends JPanel {

    private JTextField txtDateAchat;
    private JSpinner spinnerPrix;
    private JComboBox<String> comboTitre;
    private List<Titre> titres;
    private TitreDAO titreDAO;
    private Cassette cassette;

    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = Color.WHITE;

    public CassetteFormPanel(Cassette cassette) {
        this.cassette = cassette;
        this.titreDAO = new TitreDAO();

        setLayout(new GridBagLayout());
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(450, 200));

        createComponents();
        if (cassette != null) {
            populateFields();
        }
    }

    /**
     * Crée les composants du formulaire
     */
    private void createComponents() {
        try {
            titres = titreDAO.readAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors du chargement des titres : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            titres = java.util.Collections.emptyList();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Date d'achat
        JLabel lblDate = new JLabel("Date d'achat (YYYY-MM-DD) :");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDateAchat = new JTextField(15);
        txtDateAchat.setText(LocalDate.now().toString());
        txtDateAchat.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Prix
        JLabel lblPrix = new JLabel("Prix (€) :");
        lblPrix.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinnerPrix = new JSpinner(new SpinnerNumberModel(15.99, 0.01, 999.99, 0.01));
        spinnerPrix.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Titre
        JLabel lblTitre = new JLabel("Titre du film :");
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboTitre = new JComboBox<>();
        comboTitre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Titre t : titres) {
            comboTitre.addItem(t.getNomTitre() + " (" + t.getDuree() + " min)");
        }

        gbc.gridx = 0; gbc.gridy = 0;
        add(lblDate, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        add(txtDateAchat, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblPrix, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        add(spinnerPrix, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(lblTitre, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        add(comboTitre, gbc);
    }

    /**
     * Remplit les champs avec les données de la cassette
     */
    private void populateFields() {
        if (cassette != null) {
            txtDateAchat.setText(cassette.getDateAchat().toString());
            spinnerPrix.setValue(cassette.getPrix());

            // Trouver et sélectionner le titre
            for (int i = 0; i < titres.size(); i++) {
                if (titres.get(i).getIdTitre() == cassette.getIdTitre()) {
                    comboTitre.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Récupère la cassette avec les données du formulaire
     * Retourne null si validation échoue
     */
    public Cassette getCassette() {
        try {
            // Validation
            String dateStr = txtDateAchat.getText().trim();
            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer une date d'achat",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            LocalDate date = LocalDate.parse(dateStr);
            double prix = (double) spinnerPrix.getValue();
            int titreIndex = comboTitre.getSelectedIndex();

            if (titreIndex < 0 || titres.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un titre",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            Titre selectedTitre = titres.get(titreIndex);

            if (cassette == null) {
                // Nouvelle cassette
                return new Cassette(0, date, prix, selectedTitre.getIdTitre());
            } else {
                // Modification
                cassette.setDateAchat(date);
                cassette.setPrix(prix);
                cassette.setIdTitre(selectedTitre.getIdTitre());
                return cassette;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}