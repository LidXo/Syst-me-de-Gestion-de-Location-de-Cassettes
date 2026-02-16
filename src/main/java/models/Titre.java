package models;

import java.util.Objects;

/**
 * Classe entité POJO
 * Représente un titre/film
 *
 * Correspond à la table TITRE :
 *   - id_titre INT PRIMARY KEY AUTO_INCREMENT
 *   - nom_titre VARCHAR(100) NOT NULL
 *   - auteur VARCHAR(100) NOT NULL
 *   - duree INT NOT NULL CHECK (duree > 0)
 *   - id_categorie INT NOT NULL (Clé Étrangère)
 *
 * IMPORTANT : L'attribut duree est inclus
 * (Correction ÉTAPE 2 : ajout de duree qui manquait)
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class Titre {

    // Attributs privés (correspondent aux colonnes)
    private int idTitre;
    private String nomTitre;
    private String auteur;
    private int duree;           // ← IMPORTANT : Durée en minutes
    private int idCategorie;     // ← Clé Étrangère vers CATEGORIE


    /**
     * Constructeur vide
     */
    public Titre() {
    }


    /**
     * Constructeur avec paramètres
     *
     * @param idTitre ID unique (auto-généré)
     * @param nomTitre Nom du film (ex: "Terminator")
     * @param auteur Réalisateur (ex: "James Cameron")
     * @param duree Durée en minutes (ex: 107)
     * @param idCategorie ID de la catégorie (ex: 1 pour "Action")
     */
    public Titre(int idTitre, String nomTitre, String auteur, int duree, int idCategorie) {
        this.idTitre = idTitre;
        this.nomTitre = nomTitre;
        this.auteur = auteur;
        this.duree = duree;
        this.idCategorie = idCategorie;
    }


    // ═══════════════════════════════════════════════════════════
    // GETTERS ET SETTERS
    // ═══════════════════════════════════════════════════════════

    public int getIdTitre() {
        return idTitre;
    }

    public void setIdTitre(int idTitre) {
        this.idTitre = idTitre;
    }

    public String getNomTitre() {
        return nomTitre;
    }

    public void setNomTitre(String nomTitre) {
        this.nomTitre = nomTitre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Récupérer la durée du film en minutes
     * @return Durée en minutes (ex: 107)
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Définir la durée du film
     * @param duree Durée en minutes (doit être > 0)
     */
    public void setDuree(int duree) {
        if (duree <= 0) {
            throw new IllegalArgumentException("La durée doit être positive");
        }
        this.duree = duree;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES STANDARD
    // ═══════════════════════════════════════════════════════════

    /**
     * Représentation textuelle
     */
    @Override
    public String toString() {
        return "Titre{" +
                "idTitre=" + idTitre +
                ", nomTitre='" + nomTitre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", duree=" + duree +
                ", idCategorie=" + idCategorie +
                '}';
    }


    /**
     * Comparaison entre deux objets Titre
     * Deux titres sont égaux s'ils ont le même ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Titre titre = (Titre) o;
        return idTitre == titre.idTitre;
    }


    /**
     * Hash code basé sur l'ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(idTitre);
    }
}