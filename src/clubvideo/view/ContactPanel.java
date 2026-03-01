package clubvideo.view;

import clubvideo.util.UIStyles;
import javax.swing.*;
import java.awt.*;

public class ContactPanel extends JPanel implements Refreshable {

    public ContactPanel() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BG_MAIN);
        buildUI();
    }

    private void buildUI() {
        JPanel titleBar = makeTitleBar("Contact & Support");

        JPanel body = new JPanel(new GridLayout(1, 2, 20, 0));
        body.setBackground(UIStyles.BG_MAIN);
        body.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        body.add(buildInfoCard());
        body.add(buildMessageForm());

        add(titleBar, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }

    // ── Carte d'informations ──────────────────────────────────────────────────
    private JPanel buildInfoCard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIStyles.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        JLabel title = new JLabel("Coordonnées de l'administrateur");
        title.setFont(UIStyles.FONT_HEADER);
        title.setForeground(UIStyles.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(18));

        Object[][] contacts = {
                { "Responsable", "Admin Système VideoClub" },
                { "E-mail", "admin@videoclub.fr" },
                { "Téléphone", "+228 70 28 92 12" },
                { "Adresse", "12 Rue du Cinéma, 75001 Paris" },
                { "Support", "Lun – Ven, 09h00 – 18h00" },
                { "Site web", "www.videoclub.fr" },
        };

        for (Object[] row : contacts) {
            JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            line.setBackground(UIStyles.BG_CARD);
            line.setAlignmentX(Component.LEFT_ALIGNMENT);
            line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

            JLabel txt = new JLabel("<html><b>" + row[0] + " :</b>  " + row[1] + "</html>");
            txt.setFont(UIStyles.FONT_BODY);
            txt.setForeground(UIStyles.TEXT_MEDIUM);

            line.add(txt);
            p.add(line);
            p.add(Box.createVerticalStrut(6));
        }

        // Badge DB status
        p.add(Box.createVerticalStrut(14));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(sep);
        p.add(Box.createVerticalStrut(10));

        JLabel dbTitle = new JLabel("Informations techniques");
        dbTitle.setFont(UIStyles.FONT_HEADER);
        dbTitle.setForeground(UIStyles.PRIMARY);
        dbTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(dbTitle);
        p.add(Box.createVerticalStrut(8));

        String[][] tech = {
                { "Base de données", "MySQL 8+ — club_video" },
                { "Technologie UI", "Java Swing (AWT/JDBC)" },
                { "Architecture", "MVC — DAO Pattern" },
                { "Connexion", "jdbc:mysql://localhost:3306/club_video" },
        };
        for (String[] t : tech) {
            JLabel l = new JLabel("<html><b>" + t[0] + " : </b>" + t[1] + "</html>");
            l.setFont(UIStyles.FONT_SMALL);
            l.setForeground(UIStyles.TEXT_MEDIUM);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(l);
            p.add(Box.createVerticalStrut(4));
        }

        return p;
    }

    // ── Formulaire de message ─────────────────────────────────────────────────
    private JPanel buildMessageForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(UIStyles.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        JLabel title = new JLabel("Envoyer un message");
        title.setFont(UIStyles.FONT_HEADER);
        title.setForeground(UIStyles.PRIMARY);
        GridBagConstraints gt = new GridBagConstraints();
        gt.gridx = 0;
        gt.gridy = 0;
        gt.gridwidth = 2;
        gt.anchor = GridBagConstraints.WEST;
        gt.insets = new Insets(0, 0, 18, 0);
        p.add(title, gt);

        JTextField fNom = UIStyles.makeField();
        JTextField fEmail = UIStyles.makeField();
        JTextField fSujet = UIStyles.makeField();
        JTextArea taMsg = new JTextArea(6, 28);
        taMsg.setFont(UIStyles.FONT_BODY);
        taMsg.setLineWrap(true);
        taMsg.setWrapStyleWord(true);
        taMsg.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        String[] lbls = { "Nom *", "E-mail *", "Sujet *" };
        JTextField[] flds = { fNom, fEmail, fSujet };
        for (int i = 0; i < 3; i++) {
            GridBagConstraints gl = new GridBagConstraints();
            gl.gridx = 0;
            gl.gridy = i + 1;
            gl.anchor = GridBagConstraints.WEST;
            gl.insets = new Insets(6, 0, 6, 14);
            p.add(UIStyles.makeLabel(lbls[i]), gl);
            GridBagConstraints gf = new GridBagConstraints();
            gf.gridx = 1;
            gf.gridy = i + 1;
            gf.fill = GridBagConstraints.HORIZONTAL;
            gf.weightx = 1;
            gf.insets = new Insets(6, 0, 6, 0);
            p.add(flds[i], gf);
        }

        GridBagConstraints glMsg = new GridBagConstraints();
        glMsg.gridx = 0;
        glMsg.gridy = 4;
        glMsg.anchor = GridBagConstraints.NORTHWEST;
        glMsg.insets = new Insets(6, 0, 6, 14);
        p.add(UIStyles.makeLabel("Message *"), glMsg);

        GridBagConstraints gta = new GridBagConstraints();
        gta.gridx = 1;
        gta.gridy = 4;
        gta.fill = GridBagConstraints.BOTH;
        gta.weightx = 1;
        gta.weighty = 1;
        gta.insets = new Insets(6, 0, 6, 0);
        p.add(new JScrollPane(taMsg), gta);

        JButton btnSend = UIStyles.makeButton("Envoyer", UIStyles.PRIMARY);
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = 1;
        gb.gridy = 5;
        gb.anchor = GridBagConstraints.WEST;
        gb.insets = new Insets(18, 0, 0, 0);
        p.add(btnSend, gb);

        btnSend.addActionListener(e -> {
            if (fNom.getText().isBlank() || fEmail.getText().isBlank() || taMsg.getText().isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Veuillez remplir tous les champs obligatoires (*).", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "✅ Message envoyé à l'administrateur.\nRéponse sous 24h ouvrées.",
                    "Message envoyé", JOptionPane.INFORMATION_MESSAGE);
            fNom.setText("");
            fEmail.setText("");
            fSujet.setText("");
            taMsg.setText("");
        });

        return p;
    }

    private JPanel makeTitleBar(String text) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIStyles.FONT_TITLE);
        lbl.setForeground(UIStyles.TEXT_DARK);
        bar.add(lbl, BorderLayout.WEST);
        return bar;
    }

    @Override
    public void refresh() {
    }
}
