package gui.cassettes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * CassetteSearchPanel - Panel de recherche avancée pour cassettes
 *
 * Permet de filtrer les cassettes par :
 * - Titre du film
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.0
 */
public class CassetteSearchPanel extends JPanel {

    private JTextField txtTitre;
    private JButton btnRechercher, btnReinitialiser;
    private Consumer<String> onSearch;

    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(245, 245, 245);

    public CassetteSearchPanel(Consumer<String> onSearch) {
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
        JLabel lblTitre = new JLabel("Rechercher par titre :");
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitre.setForeground(new Color(50, 50, 50));

        txtTitre = new JTextField(25);
        txtTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTitre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTitre.addActionListener(e -> performSearch());

        btnRechercher = createStyledButton("🔍 Rechercher", primaryColor);
        btnRechercher.addActionListener(e -> performSearch());

        btnReinitialiser = createStyledButton("🔄 Réinitialiser", new Color(149, 165, 166));
        btnReinitialiser.addActionListener(e -> resetSearch());

        add(lblTitre);
        add(txtTitre);
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