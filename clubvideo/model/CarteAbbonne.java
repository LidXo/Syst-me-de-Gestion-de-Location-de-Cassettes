package clubvideo.model;

public class CarteAbbonne {
    private int id;
    private String nomAbo;
    private String adresseAbo;
    private int idAbonne;

    public CarteAbbonne() {}
    public CarteAbbonne(int id, String nomAbo, String adresseAbo, int idAbonne) {
        this.id = id;
        this.nomAbo = nomAbo;
        this.adresseAbo = adresseAbo;
        this.idAbonne = idAbonne;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomAbo() { return nomAbo; }
    public void setNomAbo(String nomAbo) { this.nomAbo = nomAbo; }
    public String getAdresseAbo() { return adresseAbo; }
    public void setAdresseAbo(String adresseAbo) { this.adresseAbo = adresseAbo; }
    public int getIdAbonne() { return idAbonne; }
    public void setIdAbonne(int idAbonne) { this.idAbonne = idAbonne; }
}
