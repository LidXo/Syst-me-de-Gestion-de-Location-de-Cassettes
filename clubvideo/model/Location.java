package clubvideo.model;

import java.util.Date;

public class Location {
    private int idCassette;
    private int idAbonne;
    private Date dateLimite;
    private boolean disponibilite;
    private Date dateRetourPrevue;
    // champs joints
    private String titreCassette;
    private String nomAbonne;

    public Location() {}
    public Location(int idCassette, int idAbonne, Date dateLimite,
                    boolean disponibilite, Date dateRetourPrevue) {
        this.idCassette = idCassette;
        this.idAbonne = idAbonne;
        this.dateLimite = dateLimite;
        this.disponibilite = disponibilite;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public int getIdCassette() { return idCassette; }
    public void setIdCassette(int idCassette) { this.idCassette = idCassette; }
    public int getIdAbonne() { return idAbonne; }
    public void setIdAbonne(int idAbonne) { this.idAbonne = idAbonne; }
    public Date getDateLimite() { return dateLimite; }
    public void setDateLimite(Date dateLimite) { this.dateLimite = dateLimite; }
    public boolean isDisponibilite() { return disponibilite; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }
    public Date getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(Date dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }
    public String getTitreCassette() { return titreCassette; }
    public void setTitreCassette(String titreCassette) { this.titreCassette = titreCassette; }
    public String getNomAbonne() { return nomAbonne; }
    public void setNomAbonne(String nomAbonne) { this.nomAbonne = nomAbonne; }
}
