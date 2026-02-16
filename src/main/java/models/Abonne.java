package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe entité POJO
 * Représente un abonné (client) du club vidéo
 *
 * Correspond à la table ABONNE :
 *   - num_abonne INT PRIMARY KEY AUTO_INCREMENT
 *   - nom VARCHAR(50) NOT NULL
 *   - adresse VARCHAR(200) NOT NULL
 *   - date_abonnement DATE NOT NULL
 *   - date_entree DATE NOT NULL
 *
 * RÈGLE MÉTIER : Un abonné peut louer au maximum 3 cassettes
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class Abonne {

    // Attributs privés
    private int numAbonne;
    private String nom;
    private String adresse;
    private LocalDate dateAbonnement;
    private LocalDate dateEntree;


    /**
     * Constructeur vide
     */
    public Abonne() {
    }


    /**
     * Constructeur avec paramètres
     *
     * @param numAbonne Numéro unique du client (auto-généré)
     * @param nom Nom complet du client
     * @param adresse Adresse complète
     * @param dateAbonnement Date de création de l'abonnement
     * @param dateEntree Date d'activation du compte
     */
    public Abonne(int numAbonne, String nom, String adresse,
                  LocalDate dateAbonnement, LocalDate dateEntree) {
        this.numAbonne = numAbonne;
        this.nom = nom;
        this.adresse = adresse;
        this.dateAbonnement = dateAbonnement;
        this.dateEntree = dateEntree;
    }


    // ═══════════════════════════════════════════════════════════
    // GETTERS ET SETTERS
    // ═══════════════════════════════════════════════════════════

    public int getNumAbonne() {
        return numAbonne;
    }

    public void setNumAbonne(int numAbonne) {
        this.numAbonne = numAbonne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateAbonnement() {
        return dateAbonnement;
    }

    public void setDateAbonnement(LocalDate dateAbonnement) {
        this.dateAbonnement = dateAbonnement;
    }

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES UTILES
    // ═══════════════════════════════════════════════════════════

    /**
     * Obtenir le nombre de jours depuis l'abonnement
     */
    public long getJoursDepuisAbonnement() {
        return java.time.temporal.ChronoUnit.DAYS.between(dateAbonnement, LocalDate.now());
    }


    /**
     * Vérifier si l'abonné est actif
     */
    public boolean estActif() {
        return dateEntree.isBefore(LocalDate.now()) || dateEntree.isEqual(LocalDate.now());
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES STANDARD
    // ═══════════════════════════════════════════════════════════

    /**
     * Représentation textuelle
     */
    @Override
    public String toString() {
        return "Abonne{" +
                "numAbonne=" + numAbonne +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateAbonnement=" + dateAbonnement +
                ", dateEntree=" + dateEntree +
                '}';
    }


    /**
     * Deux abonnés sont égaux s'ils ont le même numéro
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abonne abonne = (Abonne) o;
        return numAbonne == abonne.numAbonne;
    }


    /**
     * Hash code basé sur le numéro
     */
    @Override
    public int hashCode() {
        return Objects.hash(numAbonne);
    }
}