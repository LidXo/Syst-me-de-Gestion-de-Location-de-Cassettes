package dao;

import models.Categorie;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour la table CATEGORIE
 *
 * PATTERN DAO :
 * - Sépare la logique métier de l'accès aux données
 * - Toutes les opérations SQL dans cette classe
 * - Encapsulation de la base de données
 *
 * OPÉRATIONS CRUD :
 * - CREATE : create(categorie)
 * - READ : read(id), readAll()
 * - UPDATE : update(categorie)
 * - DELETE : delete(id)
 * - SEARCH : findByLibelle(libelle)
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class CategorieDAO {

    private Connection connection;


    /**
     * Constructeur - Initialise la connexion
     */
    public CategorieDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION CREATE
    // ═══════════════════════════════════════════════════════════

    /**
     * CREATE : Insérer une nouvelle catégorie
     *
     * SQL : INSERT INTO CATEGORIE (libelle) VALUES (?)
     *
     * @param categorie L'objet Categorie à insérer
     * @return true si succès, false sinon
     */
    public boolean create(Categorie categorie) {
        String sql = "INSERT INTO CATEGORIE (libelle) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Prévention des injections SQL : utiliser PreparedStatement
            stmt.setString(1, categorie.getLibelle());

            // Exécuter l'insertion
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✓ Catégorie créée : " + categorie.getLibelle());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la création de la catégorie");
            System.err.println("  Message : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ (Lire une catégorie par ID)
    // ═══════════════════════════════════════════════════════════

    /**
     * READ : Récupérer une catégorie par son ID
     *
     * SQL : SELECT * FROM CATEGORIE WHERE id_categorie = ?
     *
     * @param id L'ID de la catégorie
     * @return L'objet Categorie ou null si non trouvé
     */
    public Categorie read(int id) {
        String sql = "SELECT * FROM CATEGORIE WHERE id_categorie = ?";
        Categorie categorie = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                categorie = new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("libelle")
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture de la catégorie");
            e.printStackTrace();
        }

        return categorie;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ ALL (Lire toutes les catégories)
    // ═══════════════════════════════════════════════════════════

    /**
     * READ ALL : Récupérer toutes les catégories
     *
     * SQL : SELECT * FROM CATEGORIE
     *
     * @return Liste de tous les Categorie
     */
    public List<Categorie> readAll() {
        String sql = "SELECT * FROM CATEGORIE";
        List<Categorie> categories = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categorie cat = new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("libelle")
                );
                categories.add(cat);
            }

            System.out.println("✓ " + categories.size() + " catégories récupérées");

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture des catégories");
            e.printStackTrace();
        }

        return categories;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION UPDATE
    // ═══════════════════════════════════════════════════════════

    /**
     * UPDATE : Modifier une catégorie
     *
     * SQL : UPDATE CATEGORIE SET libelle = ? WHERE id_categorie = ?
     *
     * @param categorie L'objet Categorie à modifier
     * @return true si succès, false sinon
     */
    public boolean update(Categorie categorie) {
        String sql = "UPDATE CATEGORIE SET libelle = ? WHERE id_categorie = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, categorie.getLibelle());
            stmt.setInt(2, categorie.getIdCategorie());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✓ Catégorie modifiée : " + categorie.getLibelle());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification de la catégorie");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION DELETE
    // ═══════════════════════════════════════════════════════════

    /**
     * DELETE : Supprimer une catégorie
     *
     * SQL : DELETE FROM CATEGORIE WHERE id_categorie = ?
     *
     * IMPORTANT : Respecte la contrainte FK
     * - Si des TITRE dépendent de cette catégorie
     * - La suppression sera refusée (ON DELETE RESTRICT)
     *
     * @param id L'ID de la catégorie
     * @return true si succès, false sinon
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM CATEGORIE WHERE id_categorie = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✓ Catégorie supprimée");
                return true;
            }

        } catch (SQLException e) {
            // Erreur FK probablement
            if (e.getMessage().contains("foreign key")) {
                System.err.println("✗ Impossible de supprimer : des titres dépendent de cette catégorie");
            } else {
                System.err.println("✗ Erreur lors de la suppression de la catégorie");
            }
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION SEARCH (Recherche spécialisée)
    // ═══════════════════════════════════════════════════════════

    /**
     * RECHERCHE : Trouver une catégorie par son libellé
     *
     * SQL : SELECT * FROM CATEGORIE WHERE libelle = ?
     *
     * @param libelle Le libellé de la catégorie
     * @return L'objet Categorie ou null si non trouvé
     */
    public Categorie findByLibelle(String libelle) {
        String sql = "SELECT * FROM CATEGORIE WHERE libelle = ?";
        Categorie categorie = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                categorie = new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("libelle")
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de catégorie");
            e.printStackTrace();
        }

        return categorie;
    }
}