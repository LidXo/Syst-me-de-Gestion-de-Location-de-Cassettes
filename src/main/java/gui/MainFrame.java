package gui;

import gui.cassettes.CassetteListPanel;
import gui.abonnes.AbonneListPanel;
import gui.locations.LocationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * MainFrame - Fenêtre principale de l'application
 *
 * Fenêtre principale avec menu de navigation latéral et gestion des panneaux.
 * Design moderne et fluide.
 *
 * @author Club Vidéo
 * @version 2.0
 */
public class MainFrame extends JFrame {

    private JPanel contentPane;
    private CardLayout cardLayout;
    private JPanel sideMenu;
    private Color primaryColor = new Color(41, 128, 185);
    private Color hoverColor = new Color(52, 152, 219);
    private Color selectedColor = new Color(31, 97, 141);

    public MainFrame() {
        setTitle("Club Vidéo - Système de Gestion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        
        // Utilisation d'un layout principal
        setLayout(new BorderLayout());

        createSideMenu();
        createMainPanel();

        setVisible(true);
    }

    /**
     * Crée le menu latéral moderne
     */
    private void createSideMenu() {
        sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(44, 62, 80));
        sideMenu.setPreferredSize(new Dimension(250, getHeight()));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Logo / Titre
        JLabel lblTitle = new JLabel("CLUB VIDÉO");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideMenu.add(lblTitle);
        
        sideMenu.add(Box.createVerticalStrut(10));
        JLabel lblSubtitle = new JLabel("Gestion de Location");
        lblSubtitle.setForeground(new Color(189, 195, 199));
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideMenu.add(lblSubtitle);
        
        sideMenu.add(Box.createVerticalStrut(40));

        // Boutons de menu
        addMenuButton("🏠  Accueil", "accueil");
        addMenuButton("📼  Cassettes", "cassettes");
        addMenuButton("👥  Abonnés", "abonnes");
        addMenuButton("🔄  Locations", "locations");
        
        sideMenu.add(Box.createVerticalGlue());
        
        addMenuButton("📧  Contact", "contact");
        addMenuButton("🚪  Quitter", "exit");
        
        sideMenu.add(Box.createVerticalStrut(20));

        add(sideMenu, BorderLayout.WEST);
    }

    /**
     * Ajoute un bouton au menu latéral
     */
    private void addMenuButton(String text, String actionCommand) {
        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setMaximumSize(new Dimension(250, 50));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        
        btnPanel.add(lbl, BorderLayout.CENTER);
        
        // Effet de survol et clic
        btnPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnPanel.getBackground() != selectedColor) {
                    btnPanel.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btnPanel.getBackground() != selectedColor) {
                    btnPanel.setBackground(new Color(44, 62, 80));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (actionCommand.equals("exit")) {
                    quitter();
                } else {
                    resetMenuSelection();
                    btnPanel.setBackground(selectedColor);
                    showPanel(actionCommand);
                }
            }
        });
        
        sideMenu.add(btnPanel);
    }
    
    private void resetMenuSelection() {
        for (Component c : sideMenu.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(new Color(44, 62, 80));
            }
        }
    }

    /**
     * Crée le panel principal avec CardLayout
     */
    private void createMainPanel() {
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        contentPane.setBackground(Color.WHITE);

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