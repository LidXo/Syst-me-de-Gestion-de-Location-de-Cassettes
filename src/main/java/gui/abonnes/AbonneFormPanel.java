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
 * @version 1.0
 */
public class AbonneFormPanel extends JPanel {

    private JTextField txtNom;
    private JTextArea txtAdresse;
    private JTextField txtDateAbonnement;
    private JTextField txtDateEntree;
    private Abonne abonne;

    public AbonneFormPanel(Abonne abonne) {
        this.abonne = abonne;
        setLayout(new GridLayout(5, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(500, 300));

        createComponents();
        if (abonne != null) {
            populateFields();
        }
    }

    /**
     * Crée les composants du formulaire
     */
    private void createComponents() {
        // Nom
        JLabel lblNom = new JLabel("Nom :");
        txtNom = new JTextField();

        // Adresse
        JLabel lblAdresse = new JLabel("Adresse :");
        txtAdresse = new JTextArea(3, 30);
        txtAdresse.setLineWrap(true);
        txtAdresse.setWrapStyleWord(true);
        JScrollPane scrollAdresse = new JScrollPane(txtAdresse);

        // Date d'abonnement
        JLabel lblDateAbo = new JLabel("Date d'abonnement (YYYY-MM-DD) :");
        txtDateAbonnement = new JTextField();
        txtDateAbonnement.setText(LocalDate.now().toString());

        // Date d'entrée
        JLabel lblDateEntree = new JLabel("Date d'entrée (YYYY-MM-DD) :");
        txtDateEntree = new JTextField();
        txtDateEntree.setText(LocalDate.now().toString());

        add(lblNom);
        add(txtNom);
        add(lblAdresse);
        add(scrollAdresse);
        add(lblDateAbo);
        add(txtDateAbonnement);
        add(lblDateEntree);
        add(txtDateEntree);
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