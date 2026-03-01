package clubvideo.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class TestConnection {

    /**
     * Teste la connexion à la base de données.
     * @return message de statut lisible
     */
    public static String test() {
        try {
            Connection conn = DatabaseConnection.getInstance();
            DatabaseMetaData meta = conn.getMetaData();
            return "✅ Connexion établie — " + meta.getDatabaseProductName()
                   + " " + meta.getDatabaseProductVersion()
                   + " | URL : " + meta.getURL();
        } catch (Exception e) {
            return "❌ Connexion échouée : " + e.getMessage();
        }
    }

    public static boolean isConnected() {
        try {
            Connection conn = DatabaseConnection.getInstance();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
