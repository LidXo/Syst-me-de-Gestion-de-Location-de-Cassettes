package clubvideo.dao;

import clubvideo.model.Categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public List<Categorie> findAll() throws SQLException {
        List<Categorie> list = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIE ORDER BY categorie";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Categorie(rs.getString("categorie"), rs.getString("libelle_categorie")));
            }
        }
        return list;
    }

    public Categorie findById(String code) throws SQLException {
        String sql = "SELECT * FROM CATEGORIE WHERE categorie = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categorie(rs.getString("categorie"), rs.getString("libelle_categorie"));
                }
            }
        }
        return null;
    }

    public void insert(Categorie c) throws SQLException {
        String sql = "INSERT INTO CATEGORIE (categorie, libelle_categorie) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, c.getCategorie());
            ps.setString(2, c.getLibelleCategorie());
            ps.executeUpdate();
        }
    }

    public void update(Categorie c) throws SQLException {
        String sql = "UPDATE CATEGORIE SET libelle_categorie=? WHERE categorie=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, c.getLibelleCategorie());
            ps.setString(2, c.getCategorie());
            ps.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM CATEGORIE WHERE categorie = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM CATEGORIE";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
