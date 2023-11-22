package be.kuleuven.dbproject.model;

import javax.persistence.*;

@Entity
public class Extra {

    //interface maken voor Extra en Game zodat deze in een TabelView bewaart kunnen worden !!
    @Column(name = "aantal_in_stock")
    private int stock;

    @Column(name = "aantal_verkocht")
    private int verkocht;

    @Column(name = "extraID")
    @GeneratedValue
    @Id
    private int extraID;

    @Column(name = "winkelID")
    private int winkelID;

    @Column(name = "uitgeverID")
    private int uitgeverID;

    @Column(name = "kostprijs")
    private int kostprijs;

    @Column(name = "type")
    private int type;

    @Column(name = "naam")
    private int naam;

    @PrePersist
    private void prePersist() {
        System.out.println("Bezig met het bezigen van het opslaan van extra " + this);
    }

    public Extra(){
        
    }

    public Extra(int stock, int verkocht, int extraID, int winkelID, int uitgeverID, int kostprijs, int type, int naam) {
        this.stock = stock;
        this.verkocht = verkocht;
        this.extraID = extraID;
        this.winkelID = winkelID;
        this.uitgeverID = uitgeverID;
        this.kostprijs = kostprijs;
        this.type = type;
        this.naam = naam;
    }


    public int getStock() {
        return this.stock;
    }

    public int getVerkocht() {
        return this.verkocht;
    }


    public int getExtraID() {
        return this.extraID;
    }

    public int getWinkelID() {
        return this.winkelID;
    }

    public int getUitgeverID() {
        return this.uitgeverID;
    }

    public int getKostprijs() {
        return this.kostprijs;
    }

    public int getType() {
        return this.type;
    }

    public int getNaam() {
        return this.naam;
    }

}
