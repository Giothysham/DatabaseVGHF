package be.kuleuven.dbproject.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
public class Genre{

    @Column(name = "genreID")
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="genre")
    @Id
    private int genreID;

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @OneToMany(mappedBy = "genre")
    private List<Game> game;

    public Genre(){

    }

    public Genre(String naam, String beschrijving) {
        this.naam = naam;
        this.beschrijving = beschrijving;
    }

    public void setNaam(String naam){
        this.naam = naam;
    }

    public void setBeschrijving(String beschrijving){
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
