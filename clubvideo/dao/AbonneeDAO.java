package clubvideo.dao;

import clubvideo.model.Abonnee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonneeDAO {

    private Abonnee map(ResultSet rs) throws SQLException {
        return new Abonnee(
            rs.getInt("id"),
            rs.getString("Nom_abo"),
            rs.getDate("Date_abo"),
            rs.getString("Adresse_abo")
        );
    }

    public List<Abonnee> findAll() throws SQLException {
        List<Abonnee> list = new ArrayList<>();
        String sql = "SELECT * FROM ABONNEE ORDER BY Nom_abo";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Abonnee findById(int id) throws SQLException {
        String sql = "SELECT * FROM ABONNEE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public Abonnee save(Abonnee a) throws SQLException {
        String sql = "INSERT INTO ABONNEE (Nom_abo, Date_abo, Adresse_abo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, a.getNomAbo());
            ps.setDate(2, a.getDateAbo() != null ? new java.sql.Date(a.getDateAbo().getTime()) : null);
            ps.setString(3, a.getAdresseAbo());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) {
                        a.setId(gk.getInt(1));
                    }
                }
            }
        }
        return a;
    }

    public void update(Abonnee a) throws SQLException {
        String sql = "UPDATE ABONNEE SET Nom_abo=?, Date_abo=?, Adresse_abo=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getNomAbo());
            ps.setDate(2, a.getDateAbo() != null ? new java.sql.Date(a.getDateAbo().getTime()) : null);
            ps.setString(3, a.getAdresseAbo());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // La suppression d'un abonné devrait idéalement être gérée avec des contraintes
        // ON DELETE CASCADE dans la base de données pour nettoyer les locations et la carte.
        // Sans cela, il faut supprimer manuellement les dépendances avant.
        
        // 1. Supprimer la carte
        new CarteAbbonneDAO().deleteByAbonne(id);
        
        // 2. Supprimer les locations (ou les anonymiser)
        // Pour ce projet, on supprime.
        String sqlLoc = "DELETE FROM LOCATION WHERE id_abonne = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sqlLoc)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        // 3. Supprimer l'abonné
        String sqlAbo = "DELETE FROM ABONNEE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sqlAbo)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
