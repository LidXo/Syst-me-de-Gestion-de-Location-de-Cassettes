package clubvideo.dao;

import clubvideo.model.Categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public List<Categorie> findAll() throws SQLException {
        List<Categorie> list = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIE ORDER BY Libelle_cat";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Categorie(rs.getInt("id"), rs.getString("Libelle_cat")));
            }
        }
        return list;
    }

    public Categorie findById(int id) throws SQLException {
        String sql = "SELECT * FROM CATEGORIE WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Categorie(rs.getInt("id"), rs.getString("Libelle_cat"));
        }
        return null;
    }

    public boolean save(Categorie c) throws SQLException {
        String sql = "INSERT INTO CATEGORIE (Libelle_cat) VALUES (?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, c.getLibelleCat());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Categorie c) throws SQLException {
        String sql = "UPDATE CATEGORIE SET Libelle_cat = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, c.getLibelleCat());
            ps.setInt(2, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM CATEGORIE WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
