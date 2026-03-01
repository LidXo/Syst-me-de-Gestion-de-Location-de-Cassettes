package clubvideo.dao;

import clubvideo.model.Cassette;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CassetteDAO {

    private Cassette map(ResultSet rs) throws SQLException {
        return new Cassette(
                rs.getInt("no_cassette"),
                rs.getDate("date_achat").toLocalDate(),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getInt("duree"),
                rs.getBigDecimal("prix"),
                rs.getString("categorie"));
    }

    public List<Cassette> findAll() throws SQLException {
        List<Cassette> list = new ArrayList<>();
        String sql = "SELECT * FROM CASSETTE ORDER BY no_cassette";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    public List<Cassette> findByTitre(String titre) throws SQLException {
        List<Cassette> list = new ArrayList<>();
        String sql = "SELECT * FROM CASSETTE WHERE titre LIKE ? ORDER BY no_cassette";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, "%" + titre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(map(rs));
            }
        }
        return list;
    }

    public Cassette findById(int id) throws SQLException {
        String sql = "SELECT * FROM CASSETTE WHERE no_cassette = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        }
        return null;
    }

    public void insert(Cassette c) throws SQLException {
        String sql = "INSERT INTO CASSETTE (no_cassette, date_achat, titre, auteur, duree, prix, categorie) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, c.getNoCassette());
            ps.setDate(2, Date.valueOf(c.getDateAchat()));
            ps.setString(3, c.getTitre());
            ps.setString(4, c.getAuteur());
            ps.setInt(5, c.getDuree());
            ps.setBigDecimal(6, c.getPrix());
            ps.setString(7, c.getCategorie());
            ps.executeUpdate();
        }
    }

    public void update(Cassette c) throws SQLException {
        String sql = "UPDATE CASSETTE SET date_achat=?, titre=?, auteur=?, duree=?, prix=?, categorie=? WHERE no_cassette=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(c.getDateAchat()));
            ps.setString(2, c.getTitre());
            ps.setString(3, c.getAuteur());
            ps.setInt(4, c.getDuree());
            ps.setBigDecimal(5, c.getPrix());
            ps.setString(6, c.getCategorie());
            ps.setInt(7, c.getNoCassette());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM CASSETTE WHERE no_cassette = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM CASSETTE";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
