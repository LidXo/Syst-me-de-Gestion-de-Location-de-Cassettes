package gui;

import javax.swing.*;
import java.awt.*;

/**
 * AccueilPanel - Page d'accueil de l'application
 *
 * Affiche une présentation de l'application Club Vidéo et
 * permet une navigation rapide vers les sections principales.
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class AccueilPanel extends JPanel {

    public AccueilPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        createContent();
    }

    /**
     * Crée le contenu de la page d'accueil
     */
    private void createContent() {
        // Panel titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JLabel title = new JLabel("Bienvenue au Club Vidéo");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Panel contenu
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Description
        JLabel description = new JLabel("<html>" +
                "<h2>Système de Gestion de Location de Cassettes</h2>" +
                "<p>Bienvenue dans votre application de gestion de club vidéo.</p>" +
                "<p>Utilisez le menu de navigation pour accéder aux différentes sections :</p>" +
                "</html>");
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(description);

        contentPanel.add(Box.createVerticalStrut(20));

        // Boutons de navigation
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        buttonPanel.add(createNavigationButton(
                "📺 Gestion des Cassettes",
                "Ajouter, modifier, supprimer et rechercher des cassettes"
        ));

        buttonPanel.add(createNavigationButton(
                "👥 Gestion des Abonnés",
                "Gérer les informations des clients du club"
        ));

        buttonPanel.add(createNavigationButton(
                "🎬 Gestion des Locations",
                "Enregistrer les locations et les retours de cassettes"
        ));

        buttonPanel.add(createNavigationButton(
                "📧 Contact",
                "Nous contacter pour toute question"
        ));

        contentPanel.add(buttonPanel);

        contentPanel.add(Box.createVerticalStrut(30));

        // Statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(new Color(245, 245, 245));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistiques"));

        statsPanel.add(createStatBox("Cassettes", "13"));
        statsPanel.add(createStatBox("Titres", "11"));
        statsPanel.add(createStatBox("Abonnés", "5"));
        statsPanel.add(createStatBox("Locations", "9"));

        contentPanel.add(statsPanel);

        // Footer
        contentPanel.add(Box.createVerticalStrut(20));
        JLabel footer = new JLabel("Club Vidéo v1.0 - ÉTAPE 5");
        footer.setFont(new Font("Arial", Font.ITALIC, 11));
        footer.setForeground(Color.GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(footer);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Crée un bouton de navigation
     */
    private JPanel createNavigationButton(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(new Color(41, 128, 185));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(new Color(100, 100, 100));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crée une boîte de statistiques
     */
    private JPanel createStatBox(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel labelJLabel = new JLabel(label);
        labelJLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        labelJLabel.setForeground(Color.WHITE);
        labelJLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel valueJLabel = new JLabel(value);
        valueJLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueJLabel.setForeground(Color.WHITE);
        valueJLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(labelJLabel, BorderLayout.NORTH);
        panel.add(valueJLabel, BorderLayout.CENTER);

        return panel;
    }
}