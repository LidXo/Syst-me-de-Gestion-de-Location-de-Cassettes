package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ContactPanel - Page de contact
 *
 * Affiche les informations de contact du Club Vidéo
 * et un formulaire pour envoyer un message.
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 2.0
 */
public class ContactPanel extends JPanel {

    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color panelBackgroundColor = Color.WHITE;

    public ContactPanel() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        createContent();
    }

    /**
     * Crée le contenu de la page contact
     */
    private void createContent() {
        // Panel titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JLabel title = new JLabel("Nous Contacter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Panel contenu
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Info contact
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        infoPanel.setBackground(panelBackgroundColor);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), "Informations de Contact", 0, 0, new Font("Segoe UI", Font.BOLD, 14), primaryColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel phoneLabel = new JLabel("📞 Téléphone : +33 (0)7 31 77 52");
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel emailLabel = new JLabel("📧 Email : formationfacile.hitt.tg@gmail.com");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel addressLabel = new JLabel("📍 Adresse : Club Vidéo - Togo");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        infoPanel.add(phoneLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(addressLabel);

        contentPanel.add(infoPanel);

        contentPanel.add(Box.createVerticalStrut(30));

        // Formulaire de message
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(panelBackgroundColor);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), "Formulaire de Contact", 0, 0, new Font("Segoe UI", Font.BOLD, 14), primaryColor),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel nameLabel = new JLabel("Nom :");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel emailContactLabel = new JLabel("Votre email :");
        emailContactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTextField emailContactField = new JTextField();
        emailContactField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel messageLabel = new JLabel("Message :");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JTextArea messageArea = new JTextArea(8, 40);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        JButton sendButton = createStyledButton("Envoyer", primaryColor);
        sendButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendButton.addActionListener(e -> sendMessage(nameField, emailContactField, messageArea));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailContactLabel);
        formPanel.add(emailContactField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(messageLabel);
        formPanel.add(messageScroll);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(sendButton);

        contentPanel.add(formPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
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
     * Envoie le message de contact
     */
    private void sendMessage(JTextField nameField, JTextField emailField, JTextArea messageArea) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String message = messageArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simulation d'envoi
        JOptionPane.showMessageDialog(this,
                "✓ Merci " + name + " !\n\nVotre message a été envoyé à " + email + "\n\n" +
                        "Nous vous répondrons très rapidement.",
                "Message envoyé",
                JOptionPane.INFORMATION_MESSAGE);

        // Réinitialiser le formulaire
        nameField.setText("");
        emailField.setText("");
        messageArea.setText("");
    }
}