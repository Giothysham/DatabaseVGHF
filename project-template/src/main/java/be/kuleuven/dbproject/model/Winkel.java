package be.kuleuven.dbproject.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
public class Winkel {

    @Column(name = "bezoekers") //TODO: doen we echt saus mee???
    private int bezoekers;

    @Column(name = "winkelID")
    @GeneratedValue(generator="sqlite")

    //TODO: fix => vragen wouter waarom negatieve ?????
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="winkel")
    @Id
    private int winkelID;
    
    @Column(name = "adres")
    private String adres;

    @Column(name = "stad")
    private String stad;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "provincie")
    private String provincie; 

    @Column(name = "land")
    private String land; 

    @OneToMany(mappedBy = "winkel")
    private List<Game> game;

    @OneToMany(mappedBy = "winkel")
    private List<Extra> extra;

    @OneToMany(mappedBy = "winkel")
    private List<Factuur> factuur;

    public Winkel(){
        
    }

    public Winkel(int bezoekers, int winkelID, String adres, String stad, String postcode, String provincie,String land) {
        this.bezoekers = bezoekers;
        this.winkelID = winkelID;
        this.adres = adres;
        this.stad = stad;
        this.postcode = postcode;
        this.provincie = provincie;
        this.land = land;
    }

    public String getFullAdressWithID(){
        String adress = adres+"/"+stad+"/"+provincie+"/"+postcode+"/"+land+"/id: "+winkelID;

        return adress;
    }

    public int getBezoekers() {
        return this.bezoekers;
    }

    public int getWinkelID() {
        return this.winkelID;
    }

    public String getAdres() {
        return this.adres;
    }

    public String getStad() {
        return this.stad;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public String getLand() {
        return this.land;
    }
    
    public String getProvincie() {
        return this.provincie;
    }

    public void setBezoekers(int bezoekers) {
        this.bezoekers = bezoekers;
    }

    public void setWinkelID(int winkelID) {
        this.winkelID = winkelID;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setStad(String stad) {
        this.stad = stad;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setProvincie(String provincie) {
        this.provincie = provincie;
    }
    
    public void setLand(String land) {
        this.land = land;
    }

    
}
