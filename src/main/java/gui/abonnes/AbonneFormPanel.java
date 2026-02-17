package gui.abonnes;

import models.Abonne;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * AbonneFormPanel - Formulaire pour ajouter/modifier un abonné
 *
 * Panel formulaire réutilisable pour :
 * - Ajouter un nouvel abonné
 * - Modifier un abonné existant
 *
 * Champs :
 * - Nom (JTextField)
 * - Adresse (JTextArea)
 * - Date d'abonnement (JTextField)
 * - Date d'entrée (JTextField)
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.0
 */
public class AbonneFormPanel extends JPanel {

    private JTextField txtNom;
    private JTextArea txtAdresse;
    private JTextField txtDateAbonnement;
    private JTextField txtDateEntree;
    private Abonne abonne;

    private Color backgroundColor = Color.WHITE;

    public AbonneFormPanel(Abonne abonne) {
        this.abonne = abonne;
        setLayout(new GridBagLayout());
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(500, 350));

        createComponents();
        if (abonne != null) {
            populateFields();
        }
    }

    /**
     * Crée les composants du formulaire
     */
    private void createComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Nom
        JLabel lblNom = new JLabel("Nom :");
        lblNom.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNom = new JTextField(20);
        txtNom.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Adresse
        JLabel lblAdresse = new JLabel("Adresse :");
        lblAdresse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAdresse = new JTextArea(3, 20);
        txtAdresse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAdresse.setLineWrap(true);
        txtAdresse.setWrapStyleWord(true);
        txtAdresse.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollAdresse = new JScrollPane(txtAdresse);

        // Date d'abonnement
        JLabel lblDateAbo = new JLabel("Date d'abonnement (YYYY-MM-DD) :");
        lblDateAbo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDateAbonnement = new JTextField(15);
        txtDateAbonnement.setText(LocalDate.now().toString());
        txtDateAbonnement.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Date d'entrée
        JLabel lblDateEntree = new JLabel("Date d'entrée (YYYY-MM-DD) :");
        lblDateEntree.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDateEntree = new JTextField(15);
        txtDateEntree.setText(LocalDate.now().toString());
        txtDateEntree.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0; gbc.gridy = 0;
        add(lblNom, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        add(txtNom, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblAdresse, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        add(scrollAdresse, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(lblDateAbo, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        add(txtDateAbonnement, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(lblDateEntree, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        add(txtDateEntree, gbc);
    }

    /**
     * Remplit les champs avec les données de l'abonné
     */
    private void populateFields() {
        if (abonne != null) {
            txtNom.setText(abonne.getNom());
            txtAdresse.setText(abonne.getAdresse());
            txtDateAbonnement.setText(abonne.getDateAbonnement().toString());
            txtDateEntree.setText(abonne.getDateEntree().toString());
        }
    }

    /**
     * Récupère l'abonné avec les données du formulaire
     * Retourne null si validation échoue
     */
    public Abonne getAbonne() {
        try {
            // Validation
            String nom = txtNom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String dateAboStr = txtDateAbonnement.getText().trim();
            String dateEntreeStr = txtDateEntree.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Tous les champs sont obligatoires",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            LocalDate dateAbo = LocalDate.parse(dateAboStr);
            LocalDate dateEntree = LocalDate.parse(dateEntreeStr);

            if (abonne == null) {
                // Nouvel abonné
                return new Abonne(0, nom, adresse, dateAbo, dateEntree);
            } else {
                // Modification
                abonne.setNom(nom);
                abonne.setAdresse(adresse);
                abonne.setDateAbonnement(dateAbo);
                abonne.setDateEntree(dateEntree);
                return abonne;
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