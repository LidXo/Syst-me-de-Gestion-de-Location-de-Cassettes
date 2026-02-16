package dao;

import models.Location;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour la table LOCATION (Association)
 *
 * OPÉRATIONS :
 * - CRUD standard : create, read, readAll, update, delete
 * - Spécialisées : findByAbonne, findByCassette
 *
 * VALIDATION MÉTIER INTÉGRÉE :
 * - Un abonné ne peut louer plus de 3 cassettes
 * - Vérification automatique au CREATE
 *
 * IMPORTANTE : Clé primaire composite (num_abonne, num_cassette)
 * Cela signifie qu'un abonné ne peut louer la même cassette qu'une fois
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class LocationDAO {

    private Connection connection;


    /**
     * Constructeur - Initialise la connexion
     */
    public LocationDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION CREATE (avec validation métier)
    // ═══════════════════════════════════════════════════════════

    /**
     * CREATE : Créer une nouvelle location
     *
     * VALIDATION MÉTIER :
     * - L'abonné doit avoir < 3 cassettes en location
     * - La cassette ne doit pas être déjà louée par cet abonné
     *
     * SQL : INSERT INTO LOCATION (num_abonne, num_cassette, date_location) VALUES (?, ?, ?)
     *
     * @param location L'objet Location à insérer
     * @return true si succès, false sinon
     */
    public boolean create(Location location) {
        // Étape 1 : Vérifier la règle métier (max 3 cassettes par abonné)
        AbonneDAO abonneDAO = new AbonneDAO();
        if (!abonneDAO.canAddLocation(location.getNumAbonne())) {
            System.err.println("✗ Création échouée : l'abonné a atteint la limite de 3 cassettes");
            return false;
        }

        String sql = "INSERT INTO LOCATION (num_abonne, num_cassette, date_location) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, location.getNumAbonne());
            stmt.setInt(2, location.getNumCassette());
            stmt.setDate(3, java.sql.Date.valueOf(location.getDateLocation()));

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✓ Location créée : Abonné #" + location.getNumAbonne() +
                        " - Cassette #" + location.getNumCassette());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la création de la location");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ (Lire une location spécifique)
    // ═══════════════════════════════════════════════════════════

    /**
     * READ : Récupérer une location par sa clé composite
     *
     * SQL : SELECT * FROM LOCATION WHERE num_abonne = ? AND num_cassette = ?
     *
     * Note : Clé composite = (num_abonne, num_cassette)
     *
     * @param numAbonne Le numéro de l'abonné
     * @param numCassette Le numéro de la cassette
     * @return L'objet Location ou null si non trouvé
     */
    public Location read(int numAbonne, int numCassette) {
        String sql = "SELECT * FROM LOCATION WHERE num_abonne = ? AND num_cassette = ?";
        Location location = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numAbonne);
            stmt.setInt(2, numCassette);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                location = new Location(
                        rs.getInt("num_abonne"),
                        rs.getInt("num_cassette"),
                        rs.getDate("date_location").toLocalDate()
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture de la location");
            e.printStackTrace();
        }

        return location;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ ALL
    // ═══════════════════════════════════════════════════════════

    /**
     * READ ALL : Récupérer toutes les locations
     *
     * SQL : SELECT * FROM LOCATION ORDER BY num_abonne
     *
     * @return Liste de toutes les Location
     */
    public List<Location> readAll() {
        String sql = "SELECT * FROM LOCATION ORDER BY num_abonne";
        List<Location> locations = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Location location = new Location(
                        rs.getInt("num_abonne"),
                        rs.getInt("num_cassette"),
                        rs.getDate("date_location").toLocalDate()
                );
                locations.add(location);
            }

            System.out.println("✓ " + locations.size() + " locations récupérées");

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture des locations");
            e.printStackTrace();
        }

        return locations;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION UPDATE (Renouvellement de location)
    // ═══════════════════════════════════════════════════════════

    /**
     * UPDATE : Modifier une location (renouvellement)
     *
     * SQL : UPDATE LOCATION SET date_location = ? WHERE num_abonne = ? AND num_cassette = ?
     *
     * Utilité : Actualiser la date de location (ex: renouvellement)
     *
     * @param location L'objet Location à modifier
     * @return true si succès, false sinon
     */
    public boolean update(Location location) {
        String sql = "UPDATE LOCATION SET date_location = ? WHERE num_abonne = ? AND num_cassette = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(location.getDateLocation()));
            stmt.setInt(2, location.getNumAbonne());
            stmt.setInt(3, location.getNumCassette());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✓ Location renouvelée : Abonné #" + location.getNumAbonne() +
                        " - Cassette #" + location.getNumCassette());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification de la location");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION DELETE (Retour de cassette)
    // ═══════════════════════════════════════════════════════════

    /**
     * DELETE : Supprimer une location (retour de cassette)
     *
     * SQL : DELETE FROM LOCATION WHERE num_abonne = ? AND num_cassette = ?
     *
     * Utilité : Enregistrer le retour d'une cassette par un abonné
     *
     * @param numAbonne Le numéro de l'abonné
     * @param numCassette Le numéro de la cassette
     * @return true si succès, false sinon
     */
    public boolean delete(int numAbonne, int numCassette) {
        String sql = "DELETE FROM LOCATION WHERE num_abonne = ? AND num_cassette = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numAbonne);
            stmt.setInt(2, numCassette);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✓ Cassette retournée : Abonné #" + numAbonne +
                        " - Cassette #" + numCassette);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression de la location");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATIONS SPÉCIALISÉES
    // ═══════════════════════════════════════════════════════════

    /**
     * RECHERCHE : Trouver toutes les locations d'un abonné
     *
     * SQL : SELECT * FROM LOCATION WHERE num_abonne = ? ORDER BY date_location DESC
     *
     * EXEMPLE D'UTILISATION :
     * List<Location> locationsJean = locationDAO.findByAbonne(1);
     * for (Location loc : locationsJean) {
     *     System.out.println("Cassette #" + loc.getNumCassette() +
     *                        " depuis " + loc.getDateLocation());
     * }
     *
     * @param numAbonne Le numéro de l'abonné
     * @return Liste des locations de cet abonné
     */
    public List<Location> findByAbonne(int numAbonne) {
        String sql = "SELECT * FROM LOCATION WHERE num_abonne = ? ORDER BY date_location DESC";
        List<Location> locations = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numAbonne);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Location location = new Location(
                        rs.getInt("num_abonne"),
                        rs.getInt("num_cassette"),
                        rs.getDate("date_location").toLocalDate()
                );
                locations.add(location);
            }

            if (!locations.isEmpty()) {
                System.out.println("✓ " + locations.size() + " location(s) pour cet abonné");
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de locations par abonné");
            e.printStackTrace();
        }

        return locations;
    }


    /**
     * RECHERCHE : Trouver toutes les locations d'une cassette
     *
     * SQL : SELECT * FROM LOCATION WHERE num_cassette = ? ORDER BY date_location DESC
     *
     * Utilité : Voir l'historique des emprunts d'une cassette
     *
     * @param numCassette Le numéro de la cassette
     * @return Liste des abonnés qui ont loué cette cassette
     */
    public List<Location> findByCassette(int numCassette) {
        String sql = "SELECT * FROM LOCATION WHERE num_cassette = ? ORDER BY date_location DESC";
        List<Location> locations = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numCassette);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Location location = new Location(
                        rs.getInt("num_abonne"),
                        rs.getInt("num_cassette"),
                        rs.getDate("date_location").toLocalDate()
                );
                locations.add(location);
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de locations par cassette");
            e.printStackTrace();
        }

        return locations;
    }


    /**
     * VÉRIFICATION : Vérifier si une cassette est actuellement louée
     *
     * SQL : SELECT COUNT(*) FROM LOCATION WHERE num_cassette = ?
     *
     * @param numCassette Le numéro de la cassette
     * @return true si louée, false si disponible
     */
    public boolean estLouee(int numCassette) {
        String sql = "SELECT COUNT(*) FROM LOCATION WHERE num_cassette = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numCassette);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la vérification");
            e.printStackTrace();
        }

        return false;
    }


    /**
     * STATISTIQUE : Compter le nombre total de locations
     *
     * @return Nombre de locations actives
     */
    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM LOCATION";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage");
            e.printStackTrace();
        }

        return 0;
    }
}