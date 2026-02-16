package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe entité POJO
 * Représente une location (association entre Abonne et Cassette)
 *
 * Correspond à la table LOCATION :
 *   - num_abonne INT NOT NULL (PK composite, FK)
 *   - num_cassette INT NOT NULL (PK composite, FK)
 *   - date_location DATE NOT NULL
 *
 * Clé primaire composite : (num_abonne, num_cassette)
 * Cela signifie qu'un abonné ne peut louer qu'une seule fois la même cassette
 *
 * RÈGLE MÉTIER : Un abonné ne peut avoir que 3 locations maximum
 *
 * @author Club Vidéo
 * @version 1.0
 */
public class Location {

    // Attributs privés
    private int numAbonne;       // FK vers ABONNE (clé composite)
    private int numCassette;     // FK vers CASSETTE (clé composite)
    private LocalDate dateLocation;


    /**
     * Constructeur vide
     */
    public Location() {
    }


    /**
     * Constructeur avec paramètres
     *
     * @param numAbonne Numéro de l'abonné qui loue
     * @param numCassette Numéro de la cassette louée
     * @param dateLocation Date de la location (dernière date si renouvelée)
     */
    public Location(int numAbonne, int numCassette, LocalDate dateLocation) {
        this.numAbonne = numAbonne;
        this.numCassette = numCassette;
        this.dateLocation = dateLocation;
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

    public int getNumCassette() {
        return numCassette;
    }

    public void setNumCassette(int numCassette) {
        this.numCassette = numCassette;
    }

    public LocalDate getDateLocation() {
        return dateLocation;
    }

    public void setDateLocation(LocalDate dateLocation) {
        this.dateLocation = dateLocation;
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES UTILES
    // ═══════════════════════════════════════════════════════════

    /**
     * Obtenir le nombre de jours depuis la location
     */
    public long getJoursDepuisLocation() {
        return java.time.temporal.ChronoUnit.DAYS.between(dateLocation, LocalDate.now());
    }


    /**
     * Vérifier si la location est ancienne (plus de 30 jours)
     */
    public boolean estAncienne() {
        return getJoursDepuisLocation() > 30;
    }


    // ═══════════════════════════════════════════════════════════
    // MÉTHODES STANDARD
    // ═══════════════════════════════════════════════════════════

    /**
     * Représentation textuelle
     */
    @Override
    public String toString() {
        return "Location{" +
                "numAbonne=" + numAbonne +
                ", numCassette=" + numCassette +
                ", dateLocation=" + dateLocation +
                '}';
    }


    /**
     * Deux locations sont égales si elles ont les mêmes clés composites
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return numAbonne == location.numAbonne &&
                numCassette == location.numCassette;
    }


    /**
     * Hash code basé sur la clé composite
     */
    @Override
    public int hashCode() {
        return Objects.hash(numAbonne, numCassette);
    }
}