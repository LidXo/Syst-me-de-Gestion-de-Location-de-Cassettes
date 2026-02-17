package gui.abonnes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * AbonneSearchPanel - Panel de recherche avancée pour abonnés
 *
 * Permet de filtrer les abonnés par :
 * - Nom de l'abonné
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.0
 */
public class AbonneSearchPanel extends JPanel {

    private JTextField txtNom;
    private JButton btnRechercher, btnReinitialiser;
    private Consumer<String> onSearch;

    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(245, 245, 245);

    public AbonneSearchPanel(Consumer<String> onSearch) {
        this.onSearch = onSearch;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createComponents();
    }

    /**
     * Crée les composants de recherche
     */
    private void createComponents() {
        JLabel lblNom = new JLabel("Rechercher par nom :");
        lblNom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNom.setForeground(new Color(50, 50, 50));

        txtNom = new JTextField(25);
        txtNom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNom.addActionListener(e -> performSearch());

        btnRechercher = createStyledButton("🔍 Rechercher", primaryColor);
        btnRechercher.addActionListener(e -> performSearch());

        btnReinitialiser = createStyledButton("🔄 Réinitialiser", new Color(149, 165, 166));
        btnReinitialiser.addActionListener(e -> resetSearch());

        add(lblNom);
        add(txtNom);
        add(btnRechercher);
        add(btnReinitialiser);
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