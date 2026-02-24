package clubvideo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton de connexion à la base de données MySQL.
 * Modifiez les constantes selon votre environnement.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://127.0.0.1:3306/clubvideo?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "lidao2";
    private static final String PASSWORD = "Lidao@2005";  // Modifier si nécessaire

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✔ Connexion MySQL établie.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL introuvable : " + e.getMessage());
            }
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance != null) {
            try { instance.close(); System.out.println("Connexion fermée."); }
            catch (SQLException ignored) {}
        }
    }
}
