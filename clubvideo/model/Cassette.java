package clubvideo.model;

import java.util.Date;

public class Cassette {
    private int id;
    private Date dateAchat;
    private String titreCas;
    private String auteurCas;
    private double prixCas;
    private int dureeCas;
    private int idCategorie;
    private String libelleCategorie; // champ joint pour affichage

    public Cassette() {}
    public Cassette(int id, Date dateAchat, String titreCas, String auteurCas,
                    double prixCas, int dureeCas, int idCategorie) {
        this.id = id;
        this.dateAchat = dateAchat;
        this.titreCas = titreCas;
        this.auteurCas = auteurCas;
        this.prixCas = prixCas;
        this.dureeCas = dureeCas;
        this.idCategorie = idCategorie;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDateAchat() { return dateAchat; }
    public void setDateAchat(Date dateAchat) { this.dateAchat = dateAchat; }
    public String getTitreCas() { return titreCas; }
    public void setTitreCas(String titreCas) { this.titreCas = titreCas; }
    public String getAuteurCas() { return auteurCas; }
    public void setAuteurCas(String auteurCas) { this.auteurCas = auteurCas; }
    public double getPrixCas() { return prixCas; }
    public void setPrixCas(double prixCas) { this.prixCas = prixCas; }
    public int getDureeCas() { return dureeCas; }
    public void setDureeCas(int dureeCas) { this.dureeCas = dureeCas; }
    public int getIdCategorie() { return idCategorie; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }
    public String getLibelleCategorie() { return libelleCategorie; }
    public void setLibelleCategorie(String libelleCategorie) { this.libelleCategorie = libelleCategorie; }

    @Override
    public String toString() { return titreCas; }
}
