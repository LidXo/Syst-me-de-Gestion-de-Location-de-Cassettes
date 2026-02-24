package clubvideo.view;

import clubvideo.util.UIStylesDark;
import javax.swing.*;
import java.awt.*;

public class ContactPanel extends JPanel {

    public ContactPanel() {
        setLayout(new BorderLayout(24, 0));
        setBackground(UIStylesDark.BG);
        setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // ── Infos ──
        JPanel infoCard = new JPanel(new BorderLayout());
        infoCard.setBackground(UIStylesDark.BG2);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStylesDark.BORDER, 1),
            BorderFactory.createEmptyBorder(32, 32, 32, 32)));
        infoCard.setPreferredSize(new Dimension(360, 0));

        JLabel titleInfo = new JLabel("Coordonnées");
        titleInfo.setFont(UIStylesDark.FONT_H2);
        titleInfo.setForeground(UIStylesDark.TEXT);
        titleInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JPanel items = new JPanel(new GridLayout(4, 1, 0, 24));
        items.setBackground(UIStylesDark.BG2);

        items.add(createContactItem("📍", "ADRESSE",   "Lomé, Togo"));
        items.add(createContactItem("📞", "TÉLÉPHONE", "97 31 77 52"));
        items.add(createContactItem("✉️", "EMAIL",     "contact@clubvideo.tg"));
        items.add(createContactItem("🕒", "HORAIRES",  "Lun – Sam : 8h00 – 20h00"));

        JTextArea desc = new JTextArea(
            "Pour toute question concernant votre abonnement,\n" +
            "une location ou le catalogue, n'hésitez pas\n" +
            "à nous contacter.");
        desc.setBackground(UIStylesDark.BG2);
        desc.setForeground(UIStylesDark.MUTED);
        desc.setFont(UIStylesDark.FONT_BODY);
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBorder(BorderFactory.createEmptyBorder(32, 0, 0, 0));

        infoCard.add(titleInfo, BorderLayout.NORTH);
        infoCard.add(items,     BorderLayout.CENTER);
        infoCard.add(desc,      BorderLayout.SOUTH);

        // ── Formulaire ──
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(UIStylesDark.BG2);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStylesDark.BORDER, 1),
            BorderFactory.createEmptyBorder(32, 32, 32, 32)));

        JLabel titleForm = new JLabel("Envoyer un message");
        titleForm.setFont(UIStylesDark.FONT_H2);
        titleForm.setForeground(UIStylesDark.TEXT);
        titleForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStylesDark.BG2);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 0, 8, 0);
        gc.weightx = 1.0;
        gc.gridx = 0;

        JTextField fNom   = UIStylesDark.styledField(20);
        JTextField fEmail = UIStylesDark.styledField(20);
        JTextArea  fMsg   = new JTextArea(6, 20);

        fMsg.setBackground(UIStylesDark.BG);
        fMsg.setForeground(UIStylesDark.TEXT);
        fMsg.setCaretColor(UIStylesDark.ACCENT);
        fMsg.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStylesDark.BORDER, 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        fMsg.setFont(UIStylesDark.FONT_BODY);
        fMsg.setLineWrap(true);
        fMsg.setWrapStyleWord(true);

        addField(form, gc, 0, "Nom complet *", fNom);
        addField(form, gc, 2, "Email", fEmail);

        gc.gridy = 4;
        form.add(UIStylesDark.sectionLabel("Message *"), gc);
        gc.gridy = 5;
        form.add(fMsg, gc);

        JLabel err = new JLabel(" ");
        err.setForeground(UIStylesDark.RED);
        err.setFont(UIStylesDark.FONT_SMALL);
        gc.gridy = 6;
        form.add(err, gc);

        JButton send = UIStylesDark.accentButton("Envoyer le message");
        send.setPreferredSize(new Dimension(200, 44));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setBackground(UIStylesDark.BG2);
        btnPanel.add(send);

        gc.gridy = 7;
        gc.insets = new Insets(24, 0, 0, 0);
        form.add(btnPanel, gc);

        send.addActionListener(e -> {
            if (fNom.getText().trim().isEmpty() || fMsg.getText().trim().isEmpty()) {
                err.setText("Le nom et le message sont requis.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                "Message envoyé avec succès !\nMerci " + fNom.getText().trim() + ".",
                "Confirmation", JOptionPane.INFORMATION_MESSAGE);

            fNom.setText("");
            fEmail.setText("");
            fMsg.setText("");
            err.setText(" ");
        });

        formCard.add(titleForm, BorderLayout.NORTH);
        formCard.add(form,      BorderLayout.CENTER);

        add(infoCard, BorderLayout.WEST);
        add(formCard, BorderLayout.CENTER);
    }

    private JPanel createContactItem(String icon, String label, String value) {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setBackground(UIStylesDark.BG2);

        JLabel ico = new JLabel(icon, SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        ico.setPreferredSize(new Dimension(48, 48));
        ico.setOpaque(true);
        ico.setBackground(new Color(243, 244, 246)); // Light gray circle
        ico.setForeground(UIStylesDark.ACCENT);
        // Note: Swing doesn't support rounded corners easily on JLabels without custom painting
        // For simplicity we keep it square or use a border
        ico.setBorder(BorderFactory.createLineBorder(UIStylesDark.BORDER, 1));

        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 4));
        txt.setBackground(UIStylesDark.BG2);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(UIStylesDark.MUTED);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.PLAIN, 15));
        val.setForeground(UIStylesDark.TEXT);

        txt.add(lbl);
        txt.add(val);

        p.add(ico, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private void addField(JPanel p, GridBagConstraints gc, int row, String lbl, JComponent f) {
        gc.gridy = row;
        p.add(UIStylesDark.sectionLabel(lbl), gc);
        gc.gridy = row + 1;
        p.add(f, gc);
    }
}
