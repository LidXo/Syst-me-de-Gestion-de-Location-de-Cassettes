package clubvideo.model;

public class Categorie {
    private int id;
    private String libelleCat;

    public Categorie() {}
    public Categorie(int id, String libelleCat) {
        this.id = id;
        this.libelleCat = libelleCat;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelleCat() { return libelleCat; }
    public void setLibelleCat(String libelleCat) { this.libelleCat = libelleCat; }

    @Override
    public String toString() { return libelleCat; }
}
