package clubvideo.model;

import java.io.Serializable;

public class Categorie implements Serializable {

    private String categorie;
    private String libelle_categorie;

    public Categorie() {}

    public Categorie(String categorie, String libelle_categorie) {
        this.categorie         = categorie;
        this.libelle_categorie = libelle_categorie;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public String getCategorie()                { return categorie; }
    public void   setCategorie(String v)        { this.categorie = v; }

    public String getLibelleCategorie()         { return libelle_categorie; }
    public void   setLibelleCategorie(String v) { this.libelle_categorie = v; }

    @Override
    public String toString() { return categorie + " – " + libelle_categorie; }
}
