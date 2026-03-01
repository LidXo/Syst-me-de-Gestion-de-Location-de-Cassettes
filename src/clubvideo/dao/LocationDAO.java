package clubvideo.dao;

import clubvideo.model.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    private Location map(ResultSet rs) throws SQLException {
        return new Location(
                rs.getInt("no_abonne"),
                rs.getInt("no_cassette"),
                rs.getDate("date_location").toLocalDate());
    }

    public List<Location> findAll() throws SQLException {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM LOCATION ORDER BY date_location DESC";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    /**
     * Retourne les détails des locations avec noms des abonnés et titres des
     * cassettes (pour le tableau)
     */
    public List<Object[]> findAllDetailed() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT l.no_abonne, a.nom_abonne, l.no_cassette, c.titre, l.date_location " +
                "FROM LOCATION l " +
                "JOIN ABONNE a ON l.no_abonne = a.no_abonne " +
                "JOIN CASSETTE c ON l.no_cassette = c.no_cassette " +
                "ORDER BY l.date_location DESC";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getInt("no_abonne"),
                        rs.getString("nom_abonne"),
                        rs.getInt("no_cassette"),
                        rs.getString("titre"),
                        rs.getDate("date_location").toLocalDate()
                });
            }
        }
        return list;
    }

    public void insert(Location l) throws SQLException {
        String sql = "INSERT INTO LOCATION (no_abonne, no_cassette, date_location) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, l.getNoAbonne());
            ps.setInt(2, l.getNoCassette());
            ps.setDate(3, Date.valueOf(l.getDateLocation()));
            ps.executeUpdate();
        }
    }

    /**
     * Insère ou met à jour une location (UPSERT)
     */
    public void insertOrUpdate(Location l) throws SQLException {
        String sql = "INSERT INTO LOCATION (no_abonne, no_cassette, date_location) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE date_location = VALUES(date_location)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, l.getNoAbonne());
            ps.setInt(2, l.getNoCassette());
            ps.setDate(3, Date.valueOf(l.getDateLocation()));
            ps.executeUpdate();
        }
    }

    public void delete(int noAbo, int noCas) throws SQLException {
        String sql = "DELETE FROM LOCATION WHERE no_abonne = ? AND no_cassette = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, noAbo);
            ps.setInt(2, noCas);
            ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM LOCATION";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
