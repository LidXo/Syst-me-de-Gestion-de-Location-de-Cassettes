package clubvideo.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Abonne implements Serializable {

    private int       no_abonne;
    private String    nom_abonne;
    private String    adresse_abonne;
    private LocalDate date_abonnement;
    private LocalDate date_entree;
    private int       nombre_location;   // 0 ≤ nombre_location ≤ 3

    public Abonne() {}

    public Abonne(int no_abonne, String nom_abonne, String adresse_abonne,
                   LocalDate date_abonnement, LocalDate date_entree, int nombre_location) {
        this.no_abonne       = no_abonne;
        this.nom_abonne      = nom_abonne;
        this.adresse_abonne  = adresse_abonne;
        this.date_abonnement = date_abonnement;
        this.date_entree     = date_entree;
        this.nombre_location = nombre_location;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public int       getNoAbonne()                   { return no_abonne; }
    public void      setNoAbonne(int v)              { this.no_abonne = v; }

    public String    getNomAbonne()                  { return nom_abonne; }
    public void      setNomAbonne(String v)          { this.nom_abonne = v; }

    public String    getAdresseAbonne()              { return adresse_abonne; }
    public void      setAdresseAbonne(String v)      { this.adresse_abonne = v; }

    public LocalDate getDateAbonnement()             { return date_abonnement; }
    public void      setDateAbonnement(LocalDate v)  { this.date_abonnement = v; }

    public LocalDate getDateEntree()                 { return date_entree; }
    public void      setDateEntree(LocalDate v)      { this.date_entree = v; }

    public int       getNombreLocation()             { return nombre_location; }
    public void      setNombreLocation(int v)        { this.nombre_location = v; }

    @Override
    public String toString() { return "[" + no_abonne + "] " + nom_abonne; }
}
