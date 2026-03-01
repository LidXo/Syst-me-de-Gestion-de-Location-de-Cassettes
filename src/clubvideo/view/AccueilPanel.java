package clubvideo.view;

import clubvideo.dao.AbonneDAO;
import clubvideo.dao.CassetteDAO;
import clubvideo.dao.CategorieDAO;
import clubvideo.dao.LocationDAO;
import clubvideo.util.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * AccueilPanel — Dashboard Premium Light.
 * Affiche les statistiques sous forme de cartes colorées et un message de
 * bienvenue.
 */
public class AccueilPanel extends JPanel implements Refreshable {

    private final CassetteDAO casDAO = new CassetteDAO();
    private final AbonneDAO aboDAO = new AbonneDAO();
    private final LocationDAO locDAO = new LocationDAO();
    private final CategorieDAO catDAO = new CategorieDAO();

    private final JLabel lblCas = new JLabel("0");
    private final JLabel lblAbo = new JLabel("0");
    private final JLabel lblLoc = new JLabel("0");
    private final JLabel lblCat = new JLabel("0");

    public AccueilPanel() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BG_MAIN);
        buildUI();
        refresh();
    }

    private void buildUI() {
        // Layout principal : Padding global pour respirer (comme dans le screenshot)
        setBorder(BorderFactory.createEmptyBorder(40, 48, 40, 48));

        // ── Header: Titre + Date ──────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel title = new JLabel("Tableau de bord");
        title.setFont(UIStyles.FONT_TITLE);
        title.setForeground(UIStyles.TEXT_DARK);

        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.FRENCH));
        JLabel date = new JLabel(dateStr);
        date.setFont(UIStyles.FONT_BODY);
        date.setForeground(UIStyles.TEXT_LIGHT);

        header.add(title, BorderLayout.WEST);
        header.add(date, BorderLayout.EAST);

        // ── Stats Row: 4 cartes avec bordures colorées ────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 30, 0));
        statsRow.setOpaque(false);

        statsRow.add(UIStyles.makePremiumStatCard("CASSETTES", "Catalogue complet", lblCas, UIStyles.PRIMARY));
        statsRow.add(UIStyles.makePremiumStatCard("ABONNÉS", "Membres actifs", lblAbo, UIStyles.SUCCESS));
        statsRow.add(UIStyles.makePremiumStatCard("LOCATIONS", "En cours", lblLoc, UIStyles.WARNING));
        statsRow.add(UIStyles.makePremiumStatCard("CATÉGORIES", "Genres", lblCat, UIStyles.ACCENT));

        // ── Welcome Card Area ─────────────────────────────────────────────────
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(Color.WHITE);
        welcome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER, 1),
                BorderFactory.createEmptyBorder(40, 48, 40, 48)));

        JLabel welcomeTitle = new JLabel("Bienvenue dans votre espace de gestion");
        welcomeTitle.setFont(new Font("Segoe UI Bold", Font.PLAIN, 24));
        welcomeTitle.setForeground(UIStyles.TEXT_DARK);

        JTextArea desc = new JTextArea(
                "Utilisez le menu de gauche pour naviguer entre les différentes sections.\n" +
                        "Vous pouvez gérer vos cassettes, vos abonnés et suivre les locations en temps réel.");
        desc.setFont(UIStyles.FONT_BODY);
        desc.setForeground(UIStyles.TEXT_MEDIUM);
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setOpaque(false);
        desc.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        welcome.add(welcomeTitle, BorderLayout.NORTH);
        welcome.add(desc, BorderLayout.CENTER);

        // Assemblage final
        JPanel center = new JPanel(new BorderLayout(0, 40));
        center.setOpaque(false);
        center.add(statsRow, BorderLayout.NORTH);
        center.add(welcome, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        try {
            lblCas.setText(String.valueOf(casDAO.count()));
            lblAbo.setText(String.valueOf(aboDAO.count()));
            lblLoc.setText(String.valueOf(locDAO.count()));
            lblCat.setText(String.valueOf(catDAO.count()));
        } catch (Exception ex) {
            lblCas.setText("N/A");
            lblAbo.setText("N/A");
            lblLoc.setText("N/A");
            lblCat.setText("N/A");
            System.err.println("Erreur de rafraîchissement dashboard (mode hors-ligne?): " + ex.getMessage());
        }
    }
}
