package models;

import java.util.Objects;

/**
 * Classe entité POJO (Plain Old Java Object)
 * Représente une catégorie de film
 *
 * Correspond à la table CATEGORIE :
 *   - id_categorie INT PRIMARY KEY AUTO_INCREMENT
 *   - libelle VARCHAR(50) NOT NULL UNIQUE
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class Categorie {

    // Attributs privés (correspondent aux colonnes)
    private int idCategorie;
    private String libelle;


    /**
     * Constructeur vide
     * Obligatoire pour la sérialisation et ORM
     */
    public Categorie() {
    }


    /**
     * Constructeur avec paramètres
     *
     * @param idCategorie ID unique (auto-généré par la BD)
     * @param libelle Nom de la catégorie (ex: "Action")
     */
    public Categorie(int idCategorie, String libelle) {
        this.idCategorie = idCategorie;
        this.libelle = libelle;
    }


    // ═══════════════════════════════════════════════════════════
    // GETTERS ET SETTERS
    // ═══════════════════════════════════════════════════════════

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES STANDARD (toString, equals, hashCode)
    // ═══════════════════════════════════════════════════════════

    /**
     * Représentation textuelle
     */
    @Override
    public String toString() {
        return "Categorie{" +
                "idCategorie=" + idCategorie +
                ", libelle='" + libelle + '\'' +
                '}';
    }


    /**
     * Comparaison entre deux objets Categorie
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie that = (Categorie) o;
        return idCategorie == that.idCategorie &&
                Objects.equals(libelle, that.libelle);
    }


    /**
     * Hash code pour utilisation en collections (HashMap, HashSet)
     */
    @Override
    public int hashCode() {
        return Objects.hash(idCategorie, libelle);
    }
}