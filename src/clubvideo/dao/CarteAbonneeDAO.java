package clubvideo.dao;

import clubvideo.model.CarteAbonnee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteAbonneeDAO {

    private CarteAbonnee map(ResultSet rs) throws SQLException {
        return new CarteAbonnee(
                rs.getInt("no_abonne"),
                rs.getString("nom_abonne"),
                rs.getString("adresse_abonne"),
                rs.getDate("date_abonnement").toLocalDate(),
                rs.getDate("date_entree").toLocalDate());
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────
    public List<CarteAbonnee> findAll() throws SQLException {
        List<CarteAbonnee> list = new ArrayList<>();
        String sql = "SELECT * FROM CARTE_ABONNEE ORDER BY no_abonne";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    // ── READ ONE ─────────────────────────────────────────────────────────────
    public CarteAbonnee findById(int id) throws SQLException {
        String sql = "SELECT * FROM CARTE_ABONNEE WHERE no_abonne = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        }
        return null;
    }

    // ── INSERT ───────────────────────────────────────────────────────────────
    public void insert(CarteAbonnee c) throws SQLException {
        String sql = "INSERT INTO CARTE_ABONNEE(no_abonne, nom_abonne, adresse_abonne,"
                + " date_abonnement, date_entree) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, c.getNoAbonne());
            ps.setString(2, c.getNomAbonne());
            ps.setString(3, c.getAdresseAbonne());
            ps.setDate(4, Date.valueOf(c.getDateAbonnement()));
            ps.setDate(5, Date.valueOf(c.getDateEntree()));
            ps.executeUpdate();
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM CARTE_ABONNEE WHERE no_abonne = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
