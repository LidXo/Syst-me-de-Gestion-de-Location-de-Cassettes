package clubvideo.view;

import clubvideo.model.Abonne;
import clubvideo.util.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Dialogue affichant la carte d'abonné stylisée selon la référence visuelle.
 */
public class SubscriberCardDialog extends JDialog {

    public SubscriberCardDialog(Frame owner, Abonne abonne) {
        super(owner, "Carte d'Abonné", true);
        setSize(520, 320);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Card Border Panel ────────────────────────────────────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIStyles.PRIMARY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(5, 5, getWidth() - 10, getHeight() - 10);
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header: VIDÉO CLUB — CARTE MEMBRE
        JLabel header = new JLabel("VIDÉO CLUB — CARTE MEMBRE", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI Bold", Font.PLAIN, 18));
        header.setForeground(UIStyles.PRIMARY);
        card.add(header, BorderLayout.NORTH);

        // Center: Nom + Adresse
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel name = new JLabel(abonne.getNomAbonne().toUpperCase(), SwingConstants.CENTER);
        name.setFont(new Font("Segoe UI Bold", Font.PLAIN, 32));
        name.setForeground(UIStyles.TEXT_DARK);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        center.add(name, gbc);

        JLabel addr = new JLabel(abonne.getAdresseAbonne(), SwingConstants.CENTER);
        addr.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addr.setForeground(UIStyles.TEXT_MEDIUM);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        center.add(addr, gbc);

        // Dates
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datesStr = "Inscrit le : " + abonne.getDateAbonnement().format(df) +
                "   |   Entrée le : " + abonne.getDateEntree().format(df);
        JLabel dates = new JLabel(datesStr, SwingConstants.CENTER);
        dates.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dates.setForeground(UIStyles.TEXT_LIGHT);
        gbc.gridy = 2;
        center.add(dates, gbc);

        card.add(center, BorderLayout.CENTER);

        // Footer: N° 000000
        String idStr = String.format("%06d", abonne.getNoAbonne());
        JLabel footer = new JLabel("N°  " + idStr, SwingConstants.CENTER);
        footer.setFont(new Font("Monospaced", Font.BOLD, 20)); // Style matriciel
        footer.setForeground(UIStyles.TEXT_DARK);
        card.add(footer, BorderLayout.SOUTH);

        content.add(card, BorderLayout.CENTER);
        add(content);
    }
}
