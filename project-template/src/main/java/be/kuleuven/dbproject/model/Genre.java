package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Genre {

    @Id
    @Column(name = "genreID")
    @GeneratedValue
    private int genreID;

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    public Genre(){

    }

    public Genre(int genreID, String naam, String beschrijving) {
        this.genreID = genreID;
        this.naam = naam;
        this.beschrijving = beschrijving;
    }


    public int getGenreID() {
        return this.genreID;
    }

    public String getNaam() {
        return this.naam;
    }

    public String getBeschrijving() {
        return this.beschrijving;
    }

}
