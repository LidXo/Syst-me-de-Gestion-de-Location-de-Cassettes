package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe entité POJO
 * Représente un exemplaire physique de cassette
 *
 * Correspond à la table CASSETTE :
 *   - num_cassette INT PRIMARY KEY AUTO_INCREMENT
 *   - date_achat DATE NOT NULL
 *   - prix DECIMAL(5,2) NOT NULL
 *   - id_titre INT NOT NULL (Clé Étrangère vers TITRE)
 *
 * Note : Une cassette = un exemplaire physique d'un titre
 * Exemple : Nous avons 2 exemplaires de "Terminator"
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class Cassette {

    // Attributs privés
    private int numCassette;
    private LocalDate dateAchat;
    private double prix;
    private int idTitre;         // Clé Étrangère vers TITRE


    /**
     * Constructeur vide
     */
    public Cassette() {
    }


    /**
     * Constructeur avec paramètres
     *
     * @param numCassette Numéro de l'exemplaire (auto-généré)
     * @param dateAchat Date d'acquisition de cette cassette
     * @param prix Prix de cette cassette en euros
     * @param idTitre ID du titre correspondant
     */
    public Cassette(int numCassette, LocalDate dateAchat, double prix, int idTitre) {
        this.numCassette = numCassette;
        this.dateAchat = dateAchat;
        this.prix = prix;
        this.idTitre = idTitre;
    }


    // ═══════════════════════════════════════════════════════════
    // GETTERS ET SETTERS
    // ═══════════════════════════════════════════════════════════

    public int getNumCassette() {
        return numCassette;
    }

    public void setNumCassette(int numCassette) {
        this.numCassette = numCassette;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        if (prix <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        this.prix = prix;
    }

    public int getIdTitre() {
        return idTitre;
    }

    public void setIdTitre(int idTitre) {
        this.idTitre = idTitre;
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES STANDARD
    // ═══════════════════════════════════════════════════════════

    /**
     * Représentation textuelle
     */
    @Override
    public String toString() {
        return "Cassette{" +
                "numCassette=" + numCassette +
                ", dateAchat=" + dateAchat +
                ", prix=" + prix +
                ", idTitre=" + idTitre +
                '}';
    }


    /**
     * Deux cassettes sont égales si elles ont le même numéro
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cassette cassette = (Cassette) o;
        return numCassette == cassette.numCassette;
    }


    /**
     * Hash code basé sur le numéro
     */
    @Override
    public int hashCode() {
        return Objects.hash(numCassette);
    }
}