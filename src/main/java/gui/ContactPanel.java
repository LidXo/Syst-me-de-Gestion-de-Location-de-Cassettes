package gui;

import javax.swing.*;
import java.awt.*;

/**
 * ContactPanel - Page de contact
 *
 * Affiche les informations de contact du Club Vidéo
 * et un formulaire pour envoyer un message.
 *
 * @author Club Vidéo - ÉTAPE 5
 * @version 1.0
 */
public class ContactPanel extends JPanel {

    public ContactPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        createContent();
    }

    /**
     * Crée le contenu de la page contact
     */
    private void createContent() {
        // Panel titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        JLabel title = new JLabel("Nous Contacter");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Panel contenu
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Info contact
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informations de Contact"));

        JLabel phoneLabel = new JLabel("📞 Téléphone : +33 (0)7 31 77 52");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel emailLabel = new JLabel("📧 Email : formationfacile.hitt.tg@gmail.com");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel addressLabel = new JLabel("📍 Adresse : Club Vidéo - Togo");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        infoPanel.add(phoneLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(addressLabel);

        contentPanel.add(infoPanel);

        contentPanel.add(Box.createVerticalStrut(30));

        // Formulaire de message
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Formulaire de Contact"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel nameLabel = new JLabel("Nom :");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel emailContactLabel = new JLabel("Votre email :");
        emailContactLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField emailContactField = new JTextField();
        emailContactField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel messageLabel = new JLabel("Message :");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextArea messageArea = new JTextArea(8, 40);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        JButton sendButton = new JButton("Envoyer");
        sendButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendButton.setBackground(new Color(41, 128, 185));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
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