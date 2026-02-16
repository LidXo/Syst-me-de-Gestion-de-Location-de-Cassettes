package dao;

import models.Titre;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour la table TITRE
 *
 * OPÉRATIONS :
 * - CRUD standard : create, read, readAll, update, delete
 * - Spécialisée : findByCategorie(idCategorie)
 *
 * IMPORTANT : Gère l'attribut duree (durée en minutes)
 * Cette classe gère également les clés étrangères vers CATEGORIE
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class TitreDAO {

    private Connection connection;


    /**
     * Constructeur - Initialise la connexion
     */
    public TitreDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION CREATE
    // ═══════════════════════════════════════════════════════════

    /**
     * CREATE : Insérer un nouveau titre
     *
     * SQL : INSERT INTO TITRE (nom_titre, auteur, duree, id_categorie) VALUES (?, ?, ?, ?)
     *
     * @param titre L'objet Titre à insérer
     * @return true si succès, false sinon
     */
    public boolean create(Titre titre) {
        String sql = "INSERT INTO TITRE (nom_titre, auteur, duree, id_categorie) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, titre.getNomTitre());
            stmt.setString(2, titre.getAuteur());
            stmt.setInt(3, titre.getDuree());          // ← Inserrer duree
            stmt.setInt(4, titre.getIdCategorie());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✓ Titre créé : " + titre.getNomTitre() +
                        " (" + titre.getDuree() + " min)");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la création du titre");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ (Lire un titre par ID)
    // ═══════════════════════════════════════════════════════════

    /**
     * READ : Récupérer un titre par son ID
     *
     * SQL : SELECT * FROM TITRE WHERE id_titre = ?
     *
     * @param id L'ID du titre
     * @return L'objet Titre ou null si non trouvé
     */
    public Titre read(int id) {
        String sql = "SELECT * FROM TITRE WHERE id_titre = ?";
        Titre titre = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                titre = new Titre(
                        rs.getInt("id_titre"),
                        rs.getString("nom_titre"),
                        rs.getString("auteur"),
                        rs.getInt("duree"),                // ← Récupérer duree
                        rs.getInt("id_categorie")
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture du titre");
            e.printStackTrace();
        }

        return titre;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION READ ALL
    // ═══════════════════════════════════════════════════════════

    /**
     * READ ALL : Récupérer tous les titres
     *
     * SQL : SELECT * FROM TITRE
     *
     * @return Liste de tous les Titre
     */
    public List<Titre> readAll() {
        String sql = "SELECT * FROM TITRE";
        List<Titre> titres = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Titre titre = new Titre(
                        rs.getInt("id_titre"),
                        rs.getString("nom_titre"),
                        rs.getString("auteur"),
                        rs.getInt("duree"),                // ← Récupérer duree
                        rs.getInt("id_categorie")
                );
                titres.add(titre);
            }

            System.out.println("✓ " + titres.size() + " titres récupérés");

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la lecture des titres");
            e.printStackTrace();
        }

        return titres;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION UPDATE
    // ═══════════════════════════════════════════════════════════

    /**
     * UPDATE : Modifier un titre
     *
     * SQL : UPDATE TITRE SET nom_titre = ?, auteur = ?, duree = ?, id_categorie = ? WHERE id_titre = ?
     *
     * @param titre L'objet Titre à modifier
     * @return true si succès, false sinon
     */
    public boolean update(Titre titre) {
        String sql = "UPDATE TITRE SET nom_titre = ?, auteur = ?, duree = ?, id_categorie = ? WHERE id_titre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, titre.getNomTitre());
            stmt.setString(2, titre.getAuteur());
            stmt.setInt(3, titre.getDuree());          // ← Modifier duree
            stmt.setInt(4, titre.getIdCategorie());
            stmt.setInt(5, titre.getIdTitre());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✓ Titre modifié : " + titre.getNomTitre());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification du titre");
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION DELETE
    // ═══════════════════════════════════════════════════════════

    /**
     * DELETE : Supprimer un titre
     *
     * SQL : DELETE FROM TITRE WHERE id_titre = ?
     *
     * IMPORTANT : Respecte la contrainte FK
     * - Si des CASSETTE dépendent de ce titre
     * - La suppression sera refusée (ON DELETE RESTRICT)
     *
     * @param id L'ID du titre
     * @return true si succès, false sinon
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM TITRE WHERE id_titre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✓ Titre supprimé");
                return true;
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key")) {
                System.err.println("✗ Impossible de supprimer : des cassettes dépendent de ce titre");
            } else {
                System.err.println("✗ Erreur lors de la suppression du titre");
            }
            e.printStackTrace();
        }

        return false;
    }


    // ═══════════════════════════════════════════════════════════
    // OPÉRATION SEARCH (Spécialisée)
    // ═══════════════════════════════════════════════════════════

    /**
     * RECHERCHE : Trouver tous les titres d'une catégorie
     *
     * SQL : SELECT * FROM TITRE WHERE id_categorie = ? ORDER BY nom_titre
     *
     * EXEMPLE D'UTILISATION :
     * List<Titre> actionFilms = titreDAO.findByCategorie(1);  // 1 = "Action"
     * for (Titre t : actionFilms) {
     *     System.out.println(t.getNomTitre() + " (" + t.getDuree() + " min)");
     * }
     *
     * @param idCategorie L'ID de la catégorie
     * @return Liste des titres de cette catégorie
     */
    public List<Titre> findByCategorie(int idCategorie) {
        String sql = "SELECT * FROM TITRE WHERE id_categorie = ? ORDER BY nom_titre";
        List<Titre> titres = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idCategorie);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Titre titre = new Titre(
                        rs.getInt("id_titre"),
                        rs.getString("nom_titre"),
                        rs.getString("auteur"),
                        rs.getInt("duree"),                // ← Récupérer duree
                        rs.getInt("id_categorie")
                );
                titres.add(titre);
            }

            if (!titres.isEmpty()) {
                System.out.println("✓ " + titres.size() + " titre(s) trouvé(s) pour la catégorie");
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de titres par catégorie");
            e.printStackTrace();
        }

        return titres;
    }


    /**
     * RECHERCHE : Trouver un titre par son nom
     *
     * @param nomTitre Le nom du film
     * @return L'objet Titre ou null
     */
    public Titre findByNom(String nomTitre) {
        String sql = "SELECT * FROM TITRE WHERE nom_titre = ?";
        Titre titre = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, nomTitre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                titre = new Titre(
                        rs.getInt("id_titre"),
                        rs.getString("nom_titre"),
                        rs.getString("auteur"),
                        rs.getInt("duree"),
                        rs.getInt("id_categorie")
                );
            }

        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche par nom");
            e.printStackTrace();
        }

        return titre;
    }
}