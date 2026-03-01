package clubvideo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Cassette implements Serializable {

    private int        no_cassette;
    private LocalDate  date_achat;
    private String     titre;
    private String     auteur;
    private int        duree;          // minutes
    private BigDecimal prix;
    private String     categorie;      // FK → CATEGORIE

    public Cassette() {}

    public Cassette(int no_cassette, LocalDate date_achat, String titre,
                    String auteur, int duree, BigDecimal prix, String categorie) {
        this.no_cassette = no_cassette;
        this.date_achat  = date_achat;
        this.titre       = titre;
        this.auteur      = auteur;
        this.duree       = duree;
        this.prix        = prix;
        this.categorie   = categorie;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public int        getNoCassette()            { return no_cassette; }
    public void       setNoCassette(int v)       { this.no_cassette = v; }

    public LocalDate  getDateAchat()             { return date_achat; }
    public void       setDateAchat(LocalDate v)  { this.date_achat = v; }

    public String     getTitre()                 { return titre; }
    public void       setTitre(String v)         { this.titre = v; }

    public String     getAuteur()                { return auteur; }
    public void       setAuteur(String v)        { this.auteur = v; }

    public int        getDuree()                 { return duree; }
    public void       setDuree(int v)            { this.duree = v; }

    public BigDecimal getPrix()                  { return prix; }
    public void       setPrix(BigDecimal v)      { this.prix = v; }

    public String     getCategorie()             { return categorie; }
    public void       setCategorie(String v)     { this.categorie = v; }

    @Override
    public String toString() { return "[" + no_cassette + "] " + titre; }
}
