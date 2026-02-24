package clubvideo.model;

import java.util.Date;

public class Abonnee {
    private int id;
    private String nomAbo;
    private Date dateAbo;
    private String adresseAbo;

    public Abonnee() {}
    public Abonnee(int id, String nomAbo, Date dateAbo, String adresseAbo) {
        this.id = id;
        this.nomAbo = nomAbo;
        this.dateAbo = dateAbo;
        this.adresseAbo = adresseAbo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomAbo() { return nomAbo; }
    public void setNomAbo(String nomAbo) { this.nomAbo = nomAbo; }
    public Date getDateAbo() { return dateAbo; }
    public void setDateAbo(Date dateAbo) { this.dateAbo = dateAbo; }
    public String getAdresseAbo() { return adresseAbo; }
    public void setAdresseAbo(String adresseAbo) { this.adresseAbo = adresseAbo; }

    @Override
    public String toString() { return nomAbo; }
}
