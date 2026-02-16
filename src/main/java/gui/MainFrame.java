package gui;

import gui.cassettes.CassetteListPanel;
import gui.abonnes.AbonneListPanel;
import gui.locations.LocationPanel;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame - Fenêtre principale de l'application
 *
 * Fenêtre principale avec menu de navigation et gestion des panneaux.
 * Permet de naviguer entre :
 * - Accueil
 * - Gestion des Cassettes
 * - Gestion des Abonnés
 * - Gestion des Locations
 * - Contact
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class MainFrame extends JFrame {

    private JPanel contentPane;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Club Vidéo - Système de Gestion de Location de Cassettes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        createMenuBar();
        createMainPanel();

        setVisible(true);
    }

    /**
     * Crée la barre de menu
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Gestion
        JMenu menuGestion = new JMenu("Gestion");

        JMenuItem itemAccueil = new JMenuItem("Accueil");
        itemAccueil.addActionListener(e -> showPanel("accueil"));
        menuGestion.add(itemAccueil);

        menuGestion.addSeparator();

        JMenuItem itemCassettes = new JMenuItem("Cassettes");
        itemCassettes.addActionListener(e -> showPanel("cassettes"));
        menuGestion.add(itemCassettes);

        JMenuItem itemAbonnes = new JMenuItem("Abonnés");
        itemAbonnes.addActionListener(e -> showPanel("abonnes"));
        menuGestion.add(itemAbonnes);

        JMenuItem itemLocations = new JMenuItem("Locations");
        itemLocations.addActionListener(e -> showPanel("locations"));
        menuGestion.add(itemLocations);

        menuGestion.addSeparator();

        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> quitter());
        menuGestion.add(itemQuitter);

        // Menu Aide
        JMenu menuAide = new JMenu("Aide");

        JMenuItem itemContact = new JMenuItem("Contact");
        itemContact.addActionListener(e -> showPanel("contact"));
        menuAide.add(itemContact);

        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(e -> showAPropos());
        menuAide.add(itemAPropos);

        menuBar.add(menuGestion);
        menuBar.add(menuAide);

        setJMenuBar(menuBar);
    }

    /**
     * Crée le panel principal avec CardLayout
     */
    private void createMainPanel() {
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        // Ajouter les panneaux
        contentPane.add(new AccueilPanel(), "accueil");
        contentPane.add(new CassetteListPanel(), "cassettes");
        contentPane.add(new AbonneListPanel(), "abonnes");
        contentPane.add(new LocationPanel(), "locations");
        contentPane.add(new ContactPanel(), "contact");

        add(contentPane, BorderLayout.CENTER);

        // Afficher accueil par défaut
        cardLayout.show(contentPane, "accueil");
    }

    /**
     * Affiche un panel spécifique
     */
    private void showPanel(String panelName) {
        cardLayout.show(contentPane, panelName);
    }

    /**
     * Affiche la boîte "À propos"
     */
    private void showAPropos() {
        JOptionPane.showMessageDialog(this,
                "Club Vidéo v1.0\n\n" +
                        "Système de Gestion de Location de Cassettes Vidéo\n\n" +
                        "Développé en Java Swing\n" +
                        "ÉTAPE 5 - Interface Graphique\n\n" +
                        "© 2026 Club Vidéo - Tous droits réservés",
                "À propos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Quitter l'application
     */
    private void quitter() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir quitter ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}