package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Winkel {

    @Column(name = "bezoekers")
    private int bezoekers;

    @Column(name = "winkelID")
    @GeneratedValue
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
    
}
