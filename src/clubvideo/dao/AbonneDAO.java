package clubvideo.dao;

import clubvideo.model.Abonne;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonneDAO {

    private Abonne map(ResultSet rs) throws SQLException {
        return new Abonne(
                rs.getInt("no_abonne"),
                rs.getString("nom_abonne"),
                rs.getString("adresse_abonne"),
                rs.getDate("date_abonnement").toLocalDate(),
                rs.getDate("date_entree").toLocalDate(),
                rs.getInt("nombre_location"));
    }

    public List<Abonne> findAll() throws SQLException {
        List<Abonne> list = new ArrayList<>();
        String sql = "SELECT * FROM ABONNE ORDER BY no_abonne";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    public Abonne findById(int id) throws SQLException {
        String sql = "SELECT * FROM ABONNE WHERE no_abonne = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        }
        return null;
    }

    public void insert(Abonne a) throws SQLException {
        String sql = "INSERT INTO ABONNE (no_abonne, nom_abonne, adresse_abonne, date_abonnement, date_entree, nombre_location) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, a.getNoAbonne());
            ps.setString(2, a.getNomAbonne());
            ps.setString(3, a.getAdresseAbonne());
            ps.setDate(4, Date.valueOf(a.getDateAbonnement()));
            ps.setDate(5, Date.valueOf(a.getDateEntree()));
            ps.setInt(6, a.getNombreLocation());
            ps.executeUpdate();
        }
    }

    public void update(Abonne a) throws SQLException {
        String sql = "UPDATE ABONNE SET nom_abonne=?, adresse_abonne=?, date_abonnement=?, date_entree=?, nombre_location=? WHERE no_abonne=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, a.getNomAbonne());
            ps.setString(2, a.getAdresseAbonne());
            ps.setDate(3, Date.valueOf(a.getDateAbonnement()));
            ps.setDate(4, Date.valueOf(a.getDateEntree()));
            ps.setInt(5, a.getNombreLocation());
            ps.setInt(6, a.getNoAbonne());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ABONNE WHERE no_abonne = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM ABONNE";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
