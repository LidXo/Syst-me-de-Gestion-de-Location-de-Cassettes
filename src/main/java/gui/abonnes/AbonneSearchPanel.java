package gui.abonnes;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * AbonneSearchPanel - Panel de recherche avancée pour abonnés
 *
 * Permet de filtrer les abonnés par :
 * - Nom de l'abonné
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class AbonneSearchPanel extends JPanel {

    private JTextField txtNom;
    private JButton btnRechercher, btnReinitialiser;
    private Consumer<String> onSearch;

    public AbonneSearchPanel(Consumer<String> onSearch) {
        this.onSearch = onSearch;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(200, 220, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createComponents();
    }

    /**
     * Crée les composants de recherche
     */
    private void createComponents() {
        JLabel lblNom = new JLabel("Rechercher par nom :");
        lblNom.setFont(new Font("Arial", Font.PLAIN, 12));

        txtNom = new JTextField(20);
        txtNom.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNom.addActionListener(e -> performSearch());

        btnRechercher = new JButton("🔍 Rechercher");
        btnRechercher.addActionListener(e -> performSearch());

        btnReinitialiser = new JButton("🔄 Réinitialiser");
        btnReinitialiser.addActionListener(e -> resetSearch());

        add(lblNom);
        add(txtNom);
        add(btnRechercher);
        add(btnReinitialiser);
    }

    /**
     * Effectue la recherche
     */
    private void performSearch() {
        String nom = txtNom.getText().trim();
        onSearch.accept(nom);
    }

    /**
     * Réinitialise la recherche
     */
    private void resetSearch() {
        txtNom.setText("");
        onSearch.accept("");
    }
}