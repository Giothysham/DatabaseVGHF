package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    
    
}
