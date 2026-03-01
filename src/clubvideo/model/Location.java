package clubvideo.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Location implements Serializable {

    private int no_abonne;
    private int no_cassette;
    private LocalDate date_location;

    public Location() {
    }

    public Location(int no_abonne, int no_cassette, LocalDate date_location) {
        this.no_abonne = no_abonne;
        this.no_cassette = no_cassette;
        this.date_location = date_location;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public int getNoAbonne() {
        return no_abonne;
    }

    public void setNoAbonne(int v) {
        this.no_abonne = v;
    }

    public int getNoCassette() {
        return no_cassette;
    }

    public void setNoCassette(int v) {
        this.no_cassette = v;
    }

    public LocalDate getDateLocation() {
        return date_location;
    }

    public void setDateLocation(LocalDate v) {
        this.date_location = v;
    }

    @Override
    public String toString() {
        return "Location[abonne=" + no_abonne + ", cassette=" + no_cassette
                + ", date=" + date_location + "]";
    }
}
