package clubvideo.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe utilitaire pour tester la connexion à la base de données.
 * Exécutez cette classe (main) pour vérifier que les paramètres dans DatabaseConnection sont corrects.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   TEST DE CONNEXION À LA BASE DE DONNÉES");
        System.out.println("==========================================");

        Connection conn = null;
        try {
            System.out.println("Tentative de connexion...");
            // Appel de la méthode getInstance() de DatabaseConnection
            conn = DatabaseConnection.getInstance();

            if (conn != null && !conn.isClosed()) {
                System.out.println("\n[SUCCÈS] Connexion établie avec succès !");
                System.out.println(" - URL  : " + conn.getMetaData().getURL());
                System.out.println(" - User : " + conn.getMetaData().getUserName());
                System.out.println(" - Driver Version : " + conn.getMetaData().getDriverVersion());
            } else {
                System.err.println("\n[ÉCHEC] L'instance de connexion est nulle ou fermée.");
            }

        } catch (SQLException e) {
            System.err.println("\n[ERREUR CRITIQUE] Impossible de se connecter.");
            System.err.println("Message : " + e.getMessage());
            System.err.println("Code SQL : " + e.getSQLState());
            e.printStackTrace();
        } finally {
            // Fermeture propre pour le test
            DatabaseConnection.closeConnection();
            System.out.println("\n==========================================");
            System.out.println("             FIN DU TEST");
            System.out.println("==========================================");
        }
    }
}
