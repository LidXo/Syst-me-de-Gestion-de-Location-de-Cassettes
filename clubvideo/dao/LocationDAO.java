package clubvideo.dao;

import clubvideo.model.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    private Location map(ResultSet rs) throws SQLException {
        Location l = new Location();
        l.setIdCassette(rs.getInt("id_cassette"));
        l.setIdAbonne(rs.getInt("id_abonne"));
        l.setDateLimite(rs.getDate("Date_limite"));
        l.setDisponibilite(rs.getBoolean("Disponibilite"));
        l.setDateRetourPrevue(rs.getDate("Date_Retour_prevue"));
        try { l.setTitreCassette(rs.getString("Titre_cas")); }  catch (Exception ignored) {}
        try { l.setNomAbonne(rs.getString("Nom_abo")); }        catch (Exception ignored) {}
        return l;
    }

    public List<Location> findAll() throws SQLException {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT l.*, c.Titre_cas, a.Nom_abo FROM LOCATION l " +
                     "JOIN CASSETTE c ON l.id_cassette = c.id " +
                     "JOIN ABONNEE a ON l.id_abonne = a.id ORDER BY l.Date_limite DESC";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Location> findEnCours() throws SQLException {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT l.*, c.Titre_cas, a.Nom_abo FROM LOCATION l " +
                     "JOIN CASSETTE c ON l.id_cassette = c.id " +
                     "JOIN ABONNEE a ON l.id_abonne = a.id " +
                     "WHERE l.Disponibilite = FALSE ORDER BY l.Date_limite";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public int countLocationsEnCours(int idAbonne) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LOCATION WHERE id_abonne = ? AND Disponibilite = FALSE";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAbonne);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void enregistrerLocation(Location l) throws SQLException {
        if (countLocationsEnCours(l.getIdAbonne()) >= 3) {
            throw new SQLException("LIMITE_ATTEINTE");
        }
        
        String sql = "INSERT INTO LOCATION (id_cassette, id_abonne, Date_limite, Disponibilite, Date_Retour_prevue) " +
                     "VALUES (?, ?, ?, FALSE, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "Date_limite=VALUES(Date_limite), " +
                     "Disponibilite=FALSE, " +
                     "Date_Retour_prevue=VALUES(Date_Retour_prevue)";
                     
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, l.getIdCassette());
            ps.setInt(2, l.getIdAbonne());
            ps.setDate(3, l.getDateLimite() != null ? new java.sql.Date(l.getDateLimite().getTime()) : null);
            ps.setDate(4, l.getDateRetourPrevue() != null ? new java.sql.Date(l.getDateRetourPrevue().getTime()) : null);
            ps.executeUpdate();
        }
    }

    public void enregistrerRetour(int idCassette, int idAbonne) throws SQLException {
        String sql = "UPDATE LOCATION SET Disponibilite = TRUE WHERE id_cassette = ? AND id_abonne = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCassette);
            ps.setInt(2, idAbonne);
            ps.executeUpdate();
        }
    }

    public void delete(int idCassette, int idAbonne) throws SQLException {
        String sql = "DELETE FROM LOCATION WHERE id_cassette = ? AND id_abonne = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCassette);
            ps.setInt(2, idAbonne);
            ps.executeUpdate();
        }
    }
}
