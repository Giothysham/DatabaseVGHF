package be.kuleuven.dbproject.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
public class Uitgever {

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Column(name = "uitgeverID")
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="uitgever")
    @Id
    private int uitgeverID;

    @OneToMany(mappedBy = "uitgever")
    private List<Game> game;

    @OneToMany(mappedBy = "uitgever")
    private List<Extra> extra;

    public Uitgever(){

    }


    public Uitgever(String naam, String beschrijving, int uitgeverID) {
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.uitgeverID = uitgeverID;
    }


    public String getNaam() {
        return this.naam;
    }

    public String getBeschrijving() {
        return this.beschrijving;
    }

    public int getUitgeverID() {
        return this.uitgeverID;
    }
    
    public void setNaam(String naam) {
        this.naam = naam;
    }
    
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }
    
}
