package gui.cassettes;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * CassetteSearchPanel - Panel de recherche avancée pour cassettes
 *
 * Permet de filtrer les cassettes par :
 * - Titre du film
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class CassetteSearchPanel extends JPanel {

    private JTextField txtTitre;
    private JButton btnRechercher, btnReinitialiser;
    private Consumer<String> onSearch;

    public CassetteSearchPanel(Consumer<String> onSearch) {
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
        JLabel lblTitre = new JLabel("Rechercher par titre :");
        lblTitre.setFont(new Font("Arial", Font.PLAIN, 12));

        txtTitre = new JTextField(20);
        txtTitre.setFont(new Font("Arial", Font.PLAIN, 12));
        txtTitre.addActionListener(e -> performSearch());

        btnRechercher = new JButton("🔍 Rechercher");
        btnRechercher.addActionListener(e -> performSearch());

        btnReinitialiser = new JButton("🔄 Réinitialiser");
        btnReinitialiser.addActionListener(e -> resetSearch());

        add(lblTitre);
        add(txtTitre);
        add(btnRechercher);
        add(btnReinitialiser);
    }

    /**
     * Effectue la recherche
     */
    private void performSearch() {
        String titre = txtTitre.getText().trim();
        onSearch.accept(titre);
    }

    /**
     * Réinitialise la recherche
     */
    private void resetSearch() {
        txtTitre.setText("");
        onSearch.accept("");
    }
}