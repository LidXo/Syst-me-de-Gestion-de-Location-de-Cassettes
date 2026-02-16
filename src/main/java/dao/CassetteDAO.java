package dao;

import models.Cassette;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour la table CASSETTE
 *
 * OPÉRATIONS :
 * - CRUD standard : create, read, readAll, update, delete
 * - Spécialisées : findByTitre, countDisponibles
 *
 * Une cassette = un exemplaire physique d'un titre
 * Exemple : 2 exemplaires de "Terminator" = 2 cassettes
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class CassetteDAO {

    private Connection connection;


    /**
     * Constructeur - Initialise la connexion
     */
    public CassetteDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION CREATE
    // ═══════════════════════════════════════════════════════════

    /**
     * CREATE : Insérer une nouvelle cassette
     *
     * SQL : INSERT INTO CASSETTE (date_achat, prix, id_titre) VALUES (?, ?, ?)
     *
     * @param cassette L'objet Cassette à insérer
     * @return true si succès, false sinon
     */
    public boolean create(Cassette cassette) {
        String sql = "INSERT INTO CASSETTE (date_achat, prix, id_titre) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(cassette.getDateAchat()));
            stmt.setDouble(2, cassette.getPrix());
            stmt.setInt(3, cassette.getIdTitre());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✓ Cassette créée - Titre ID: " + cassette.getIdTitre() +
                        " - Prix: " + cassette.getPrix() + "€");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la création de la cassette");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ (Lire une cassette par ID)
    // ═══════════════════════════════════════════════════════════

    /**
     * READ : Récupérer une cassette par son numéro
     *
     * SQL : SELECT * FROM CASSETTE WHERE num_cassette = ?
     *
     * @param id Le numéro de la cassette
     * @return L'objet Cassette ou null si non trouvé
     */
    public Cassette read(int id) {
        String sql = "SELECT * FROM CASSETTE WHERE num_cassette = ?";
        Cassette cassette = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cassette = new Cassette(
                        rs.getInt("num_cassette"),
                        rs.getDate("date_achat").toLocalDate(),
                        rs.getDouble("prix"),
                        rs.getInt("id_titre")
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture de la cassette");
            e.printStackTrace();
        }

        return cassette;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ ALL
    // ═══════════════════════════════════════════════════════════

    /**
     * READ ALL : Récupérer toutes les cassettes
     *
     * SQL : SELECT * FROM CASSETTE
     *
     * @return Liste de tous les Cassette
     */
    public List<Cassette> readAll() {
        String sql = "SELECT * FROM CASSETTE";
        List<Cassette> cassettes = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cassette cassette = new Cassette(
                        rs.getInt("num_cassette"),
                        rs.getDate("date_achat").toLocalDate(),
                        rs.getDouble("prix"),
                        rs.getInt("id_titre")
                );
                cassettes.add(cassette);
            }

            System.out.println("✓ " + cassettes.size() + " cassettes récupérées");

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture des cassettes");
            e.printStackTrace();
        }

        return cassettes;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION UPDATE
    // ═══════════════════════════════════════════════════════════

    /**
     * UPDATE : Modifier une cassette
     *
     * SQL : UPDATE CASSETTE SET date_achat = ?, prix = ?, id_titre = ? WHERE num_cassette = ?
     *
     * @param cassette L'objet Cassette à modifier
     * @return true si succès, false sinon
     */
    public boolean update(Cassette cassette) {
        String sql = "UPDATE CASSETTE SET date_achat = ?, prix = ?, id_titre = ? WHERE num_cassette = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(cassette.getDateAchat()));
            stmt.setDouble(2, cassette.getPrix());
            stmt.setInt(3, cassette.getIdTitre());
            stmt.setInt(4, cassette.getNumCassette());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✓ Cassette #" + cassette.getNumCassette() + " modifiée");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification de la cassette");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION DELETE
    // ═══════════════════════════════════════════════════════════

    /**
     * DELETE : Supprimer une cassette
     *
     * SQL : DELETE FROM CASSETTE WHERE num_cassette = ?
     *
     * IMPORTANT : Respecte la contrainte FK
     * - Si cette cassette est louée (dans LOCATION)
     * - La suppression sera refusée (ON DELETE RESTRICT)
     *
     * @param id Le numéro de la cassette
     * @return true si succès, false sinon
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM CASSETTE WHERE num_cassette = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✓ Cassette supprimée");
                return true;
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key")) {
                System.err.println("✗ Impossible de supprimer : cette cassette est actuellement louée");
            } else {
                System.err.println("✗ Erreur lors de la suppression de la cassette");
            }
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATIONS SPÉCIALISÉES
    // ═══════════════════════════════════════════════════════════

    /**
     * RECHERCHE : Trouver tous les exemplaires d'un titre
     *
     * SQL : SELECT * FROM CASSETTE WHERE id_titre = ? ORDER BY num_cassette
     *
     * EXEMPLE D'UTILISATION :
     * List<Cassette> terminatorCassettes = cassetteDAO.findByTitre(1);
     * System.out.println("Nous avons " + terminatorCassettes.size() + " exemplaires de Terminator");
     *
     * @param idTitre L'ID du titre
     * @return Liste de tous les exemplaires de ce titre
     */
    public List<Cassette> findByTitre(int idTitre) {
        String sql = "SELECT * FROM CASSETTE WHERE id_titre = ? ORDER BY num_cassette";
        List<Cassette> cassettes = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTitre);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cassette cassette = new Cassette(
                        rs.getInt("num_cassette"),
                        rs.getDate("date_achat").toLocalDate(),
                        rs.getDouble("prix"),
                        rs.getInt("id_titre")
                );
                cassettes.add(cassette);
            }

            if (!cassettes.isEmpty()) {
                System.out.println("✓ " + cassettes.size() + " exemplaire(s) trouvé(s)");
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de cassettes par titre");
            e.printStackTrace();
        }

        return cassettes;
    }


    /**
     * STATISTIQUE : Compter le nombre de cassettes disponibles (non louées)
     *
     * SQL : SELECT COUNT(*) FROM CASSETTE c
     *       WHERE c.num_cassette NOT IN (SELECT num_cassette FROM LOCATION)
     *
     * EXEMPLE D'UTILISATION :
     * int dispo = cassetteDAO.countDisponibles();
     * System.out.println("Cassettes disponibles : " + dispo);
     *
     * @return Nombre de cassettes non louées
     */
    public int countDisponibles() {
        String sql = "SELECT COUNT(*) FROM CASSETTE c " +
                "WHERE c.num_cassette NOT IN (SELECT num_cassette FROM LOCATION)";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("✓ " + count + " cassette(s) disponible(s)");
                return count;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage des cassettes disponibles");
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * STATISTIQUE : Compter le nombre total de cassettes
     *
     * @return Nombre total de cassettes
     */
    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM CASSETTE";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage total");
            e.printStackTrace();
        }

        return 0;
    }
}