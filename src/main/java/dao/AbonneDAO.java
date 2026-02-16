package dao;

import models.Abonne;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour la table ABONNE
 *
 * OPÉRATIONS :
 * - CRUD standard : create, read, readAll, update, delete
 * - Spécialisées : findByNom, countLocations, canAddLocation
 *
 * RÈGLE MÉTIER IMPORTANTE :
 * Un abonné ne peut louer MAXIMUM 3 cassettes en même temps
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class AbonneDAO {

    private Connection connection;


    /**
     * Constructeur - Initialise la connexion
     */
    public AbonneDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION CREATE
    // ═══════════════════════════════════════════════════════════

    /**
     * CREATE : Insérer un nouveau abonné
     *
     * SQL : INSERT INTO ABONNE (nom, adresse, date_abonnement, date_entree) VALUES (?, ?, ?, ?)
     *
     * @param abonne L'objet Abonne à insérer
     * @return true si succès, false sinon
     */
    public boolean create(Abonne abonne) {
        String sql = "INSERT INTO ABONNE (nom, adresse, date_abonnement, date_entree) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, abonne.getNom());
            stmt.setString(2, abonne.getAdresse());
            stmt.setDate(3, java.sql.Date.valueOf(abonne.getDateAbonnement()));
            stmt.setDate(4, java.sql.Date.valueOf(abonne.getDateEntree()));

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✓ Abonné créé : " + abonne.getNom());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la création de l'abonné");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ (Lire un abonné par ID)
    // ═══════════════════════════════════════════════════════════

    /**
     * READ : Récupérer un abonné par son numéro
     *
     * SQL : SELECT * FROM ABONNE WHERE num_abonne = ?
     *
     * @param id Le numéro de l'abonné
     * @return L'objet Abonne ou null si non trouvé
     */
    public Abonne read(int id) {
        String sql = "SELECT * FROM ABONNE WHERE num_abonne = ?";
        Abonne abonne = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                abonne = new Abonne(
                        rs.getInt("num_abonne"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getDate("date_abonnement").toLocalDate(),
                        rs.getDate("date_entree").toLocalDate()
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture de l'abonné");
            e.printStackTrace();
        }

        return abonne;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ ALL
    // ═══════════════════════════════════════════════════════════

    /**
     * READ ALL : Récupérer tous les abonnés
     *
     * SQL : SELECT * FROM ABONNE
     *
     * @return Liste de tous les Abonne
     */
    public List<Abonne> readAll() {
        String sql = "SELECT * FROM ABONNE";
        List<Abonne> abonnes = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Abonne abonne = new Abonne(
                        rs.getInt("num_abonne"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getDate("date_abonnement").toLocalDate(),
                        rs.getDate("date_entree").toLocalDate()
                );
                abonnes.add(abonne);
            }

            System.out.println("✓ " + abonnes.size() + " abonnés récupérés");

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture des abonnés");
            e.printStackTrace();
        }

        return abonnes;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION UPDATE
    // ═══════════════════════════════════════════════════════════

    /**
     * UPDATE : Modifier un abonné
     *
     * SQL : UPDATE ABONNE SET nom = ?, adresse = ?, date_abonnement = ?, date_entree = ?
     *       WHERE num_abonne = ?
     *
     * @param abonne L'objet Abonne à modifier
     * @return true si succès, false sinon
     */
    public boolean update(Abonne abonne) {
        String sql = "UPDATE ABONNE SET nom = ?, adresse = ?, date_abonnement = ?, date_entree = ? WHERE num_abonne = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, abonne.getNom());
            stmt.setString(2, abonne.getAdresse());
            stmt.setDate(3, java.sql.Date.valueOf(abonne.getDateAbonnement()));
            stmt.setDate(4, java.sql.Date.valueOf(abonne.getDateEntree()));
            stmt.setInt(5, abonne.getNumAbonne());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✓ Abonné modifié : " + abonne.getNom());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification de l'abonné");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION DELETE
    // ═══════════════════════════════════════════════════════════

    /**
     * DELETE : Supprimer un abonné
     *
     * SQL : DELETE FROM ABONNE WHERE num_abonne = ?
     *
     * IMPORTANT : Avec ON DELETE CASCADE sur LOCATION
     * - Les locations de cet abonné seront AUTOMATIQUEMENT supprimées
     *
     * @param id Le numéro de l'abonné
     * @return true si succès, false sinon
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM ABONNE WHERE num_abonne = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✓ Abonné supprimé (ses locations aussi)");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression de l'abonné");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATIONS SPÉCIALISÉES
    // ═══════════════════════════════════════════════════════════

    /**
     * RECHERCHE : Trouver un abonné par son nom
     *
     * SQL : SELECT * FROM ABONNE WHERE nom = ?
     *
     * @param nom Le nom de l'abonné
     * @return L'objet Abonne ou null si non trouvé
     */
    public Abonne findByNom(String nom) {
        String sql = "SELECT * FROM ABONNE WHERE nom = ?";
        Abonne abonne = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                abonne = new Abonne(
                        rs.getInt("num_abonne"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getDate("date_abonnement").toLocalDate(),
                        rs.getDate("date_entree").toLocalDate()
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche d'abonné par nom");
            e.printStackTrace();
        }

        return abonne;
    }


    /**
     * STATISTIQUE : Compter les locations d'un abonné
     *
     * SQL : SELECT COUNT(*) FROM LOCATION WHERE num_abonne = ?
     *
     * RÈGLE MÉTIER : Limite maximum = 3 cassettes
     *
     * EXEMPLE D'UTILISATION :
     * int count = abonneDAO.countLocations(1);  // Jean Dupont (num 1)
     * if (count < 3) {
     *     System.out.println("Il peut emprunter une cassette");
     * }
     *
     * @param numAbonne Le numéro de l'abonné
     * @return Nombre de cassettes actuellement louées
     */
    public int countLocations(int numAbonne) {
        String sql = "SELECT COUNT(*) FROM LOCATION WHERE num_abonne = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numAbonne);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("✓ Abonné #" + numAbonne + " a " + count + " location(s)");
                return count;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage des locations");
            e.printStackTrace();
        }

        return -1;  // Retourner -1 en cas d'erreur
    }


    /**
     * VALIDATION RÈGLE MÉTIER : Vérifier si l'abonné peut emprunter une nouvelle cassette
     *
     * RÈGLE : Un abonné ne peut avoir que 3 cassettes maximum
     *
     * EXEMPLE D'UTILISATION :
     * if (abonneDAO.canAddLocation(1)) {
     *     // Créer une nouvelle location
     *     locationDAO.create(new Location(1, 5, LocalDate.now()));
     * } else {
     *     System.out.println("✗ L'abonné a atteint la limite de 3 cassettes");
     * }
     *
     * @param numAbonne Le numéro de l'abonné
     * @return true si < 3 cassettes, false sinon
     */
    public boolean canAddLocation(int numAbonne) {
        int count = countLocations(numAbonne);

        if (count < 0) {
            System.err.println("✗ Impossible de vérifier le nombre de locations");
            return false;
        }

        if (count >= 3) {
            System.out.println("⚠ Abonné #" + numAbonne + " a atteint la limite de 3 cassettes");
            return false;
        }

        System.out.println("✓ Abonné #" + numAbonne + " peut emprunter (" + (3 - count) + " place(s) restante(s))");
        return true;
    }
}