package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe singleton pour gérer la connexion JDBC à MySQL
 *
 * PATTERN SINGLETON :
 * - Une seule instance de DatabaseConnection pour toute l'application
 * - Évite de créer plusieurs connexions inutilement
 * - Centralise la gestion des connexions
 *
 * UTILISATION :
 * ============
 * Connection conn = DatabaseConnection.getInstance().getConnection();
 * DatabaseConnection.getInstance().testConnection();
 * DatabaseConnection.getInstance().closeConnection();
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class DatabaseConnection {

    // Instance unique (singleton)
    private static DatabaseConnection instance;

    // Connexion à la base de données
    private Connection connection;

    // Propriétés de configuration
    private String url;
    private String user;
    private String password;
    private String driver;


    /**
     * Constructeur privé (pour respecter le pattern singleton)
     *
     * IMPORTANT : Le constructeur est privé pour éviter :
     *   ❌ DatabaseConnection conn = new DatabaseConnection();
     *
     * À la place, utiliser :
     *   ✓ DatabaseConnection.getInstance().getConnection();
     */
    private DatabaseConnection() {
        loadProperties();
        connect();
    }


    /**
     * Charger les propriétés depuis database.properties
     *
     * PROCESSUS :
     * 1. Charger le fichier database.properties
     * 2. Lire les paramètres (url, user, password, driver)
     * 3. Stocker dans les variables de classe
     */
    private void loadProperties() {
        Properties props = new Properties();

        try {
            // Charger le fichier depuis les ressources
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input == null) {
                System.err.println("ERREUR : Fichier database.properties non trouvé!");
                System.err.println("Créez le fichier src/main/resources/database.properties");
                return;
            }

            // Charger les propriétés
            props.load(input);
            input.close();

            // Lire les paramètres
            this.driver = props.getProperty("db.driver");
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");

            System.out.println("✓ Fichier database.properties chargé");
            System.out.println("  - URL : " + this.url);
            System.out.println("  - User : " + this.user);

        } catch (IOException e) {
            System.err.println("ERREUR lors du chargement de database.properties");
            e.printStackTrace();
        }
    }


    /**
     * Établir la connexion à la base de données
     *
     * ÉTAPES :
     * 1. Charger le driver JDBC
     * 2. Établir la connexion avec les paramètres
     * 3. Afficher le statut
     */
    private void connect() {
        try {
            // Étape 1 : Charger le driver JDBC MySQL
            Class.forName(driver);
            System.out.println("✓ Driver JDBC chargé : " + driver);

            // Étape 2 : Créer la connexion
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("✓ Connexion établie à la base de données");
            System.out.println("  - Statut : " + (connection.isValid(2) ? "Valide" : "Invalide"));

        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR : Driver JDBC non trouvé!");
            System.err.println("Assurez-vous que mysql-connector-java est dans le classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERREUR : Connexion à la base de données échouée");
            System.err.println("Vérifiez les paramètres dans database.properties");
            e.printStackTrace();
        }
    }


    /**
     * Obtenir l'instance singleton de DatabaseConnection
     *
     * PATTERN SINGLETON :
     * - Première fois : crée une instance
     * - Fois suivantes : retourne la même instance
     *
     * @return L'instance unique de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }


    /**
     * Obtenir la connexion JDBC
     *
     * UTILISATION :
     * Connection conn = DatabaseConnection.getInstance().getConnection();
     *
     * @return La connexion JDBC à la base de données
     */
    public Connection getConnection() {
        try {
            // Vérifier que la connexion est toujours valide
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠ Connexion fermée, reconnexion...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("ERREUR lors de la vérification de la connexion");
            e.printStackTrace();
        }

        return connection;
    }


    /**
     * Fermer la connexion
     *
     * À appeler avant de quitter l'application
     *
     * UTILISATION :
     * DatabaseConnection.getInstance().closeConnection();
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("ERREUR lors de la fermeture de la connexion");
            e.printStackTrace();
        }
    }


    /**
     * Tester la connexion
     *
     * UTILISATION :
     * DatabaseConnection.getInstance().testConnection();
     */
    public void testConnection() {
        try {
            if (connection != null && connection.isValid(2)) {
                System.out.println("✓ Connexion VALIDE");

                // Tester une requête simple
                java.sql.Statement stmt = connection.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM CATEGORIE");

                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("✓ Nombre de catégories : " + count);
                }

                rs.close();
                stmt.close();
            } else {
                System.out.println("❌ Connexion INVALIDE");
            }
        } catch (SQLException e) {
            System.err.println("ERREUR lors du test de connexion");
            e.printStackTrace();
        }
    }

}