package clubvideo.dao;

import clubvideo.model.Cassette;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CassetteDAO {

    private Cassette map(ResultSet rs) throws SQLException {
        Cassette c = new Cassette();
        c.setId(rs.getInt("id"));
        c.setDateAchat(rs.getDate("Date_Achat"));
        c.setTitreCas(rs.getString("Titre_cas"));
        c.setAuteurCas(rs.getString("Auteur_cas"));
        c.setPrixCas(rs.getDouble("Prix_cas"));
        c.setDureeCas(rs.getInt("Duree_cas"));
        c.setIdCategorie(rs.getInt("id_categorie"));
        try { c.setLibelleCategorie(rs.getString("Libelle_cat")); } catch (Exception ignored) {}
        return c;
    }

    public List<Cassette> findAll() throws SQLException {
        List<Cassette> list = new ArrayList<>();
        String sql = "SELECT c.*, cat.Libelle_cat FROM CASSETTE c " +
                     "LEFT JOIN CATEGORIE cat ON c.id_categorie = cat.id ORDER BY c.Titre_cas";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Cassette> findDisponibles() throws SQLException {
        List<Cassette> list = new ArrayList<>();
        String sql = "SELECT c.*, cat.Libelle_cat FROM CASSETTE c " +
                     "LEFT JOIN CATEGORIE cat ON c.id_categorie = cat.id " +
                     "WHERE c.id NOT IN (SELECT id_cassette FROM LOCATION WHERE Disponibilite = FALSE)";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Cassette findById(int id) throws SQLException {
        String sql = "SELECT c.*, cat.Libelle_cat FROM CASSETTE c " +
                     "LEFT JOIN CATEGORIE cat ON c.id_categorie = cat.id WHERE c.id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public boolean save(Cassette c) throws SQLException {
        String sql = "INSERT INTO CASSETTE (Date_Achat, Titre_cas, Auteur_cas, Prix_cas, Duree_cas, id_categorie) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setDate(1, c.getDateAchat() != null ? new java.sql.Date(c.getDateAchat().getTime()) : null);
            ps.setString(2, c.getTitreCas());
            ps.setString(3, c.getAuteurCas());
            ps.setDouble(4, c.getPrixCas());
            ps.setInt(5, c.getDureeCas());
            if (c.getIdCategorie() > 0) ps.setInt(6, c.getIdCategorie()); else ps.setNull(6, Types.INTEGER);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Cassette c) throws SQLException {
        String sql = "UPDATE CASSETTE SET Date_Achat=?, Titre_cas=?, Auteur_cas=?, Prix_cas=?, Duree_cas=?, id_categorie=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setDate(1, c.getDateAchat() != null ? new java.sql.Date(c.getDateAchat().getTime()) : null);
            ps.setString(2, c.getTitreCas());
            ps.setString(3, c.getAuteurCas());
            ps.setDouble(4, c.getPrixCas());
            ps.setInt(5, c.getDureeCas());
            if (c.getIdCategorie() > 0) ps.setInt(6, c.getIdCategorie()); else ps.setNull(6, Types.INTEGER);
            ps.setInt(7, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM CASSETTE WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
