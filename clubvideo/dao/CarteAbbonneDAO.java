package clubvideo.dao;

import clubvideo.model.CarteAbbonne;
import java.sql.*;

public class CarteAbbonneDAO {

    public CarteAbbonne findByAbonne(int idAbonne) throws SQLException {
        String sql = "SELECT * FROM CARTE_ABBONNE WHERE id_abonne = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAbonne);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CarteAbbonne(
                        rs.getInt("id"), 
                        rs.getString("Nom_abo"),
                        rs.getString("Adresse_abo"), 
                        rs.getInt("id_abonne")
                    );
                }
            }
        }
        return null;
    }

    public void save(CarteAbbonne c) throws SQLException {
        String sql = "INSERT INTO CARTE_ABBONNE (Nom_abo, Adresse_abo, id_abonne) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNomAbo());
            ps.setString(2, c.getAdresseAbo());
            ps.setInt(3, c.getIdAbonne());
            ps.executeUpdate();
        }
    }

    public void update(CarteAbbonne c) throws SQLException {
        String sql = "UPDATE CARTE_ABBONNE SET Nom_abo=?, Adresse_abo=? WHERE id_abonne=?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNomAbo());
            ps.setString(2, c.getAdresseAbo());
            ps.setInt(3, c.getIdAbonne());
            ps.executeUpdate();
        }
    }

    public void deleteByAbonne(int idAbonne) throws SQLException {
        String sql = "DELETE FROM CARTE_ABBONNE WHERE id_abonne = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAbonne);
            ps.executeUpdate();
        }
    }
}
