package be.kuleuven.dbproject.model;

import javax.persistence.*;

@Entity
public class Extra {

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
    private int type; //TODO: aanpassen naar enum met verschillende types, zie games

    @Column(name = "naam")
    private String naam;

    @Transient
    private Integer tempStock;

    @PrePersist
    private void prePersist() {
        System.out.println("Bezig met het bezigen van het opslaan van extra " + this);
    }

    public Extra(){
        
    }

    public Extra(int stock, int verkocht, int extraID, int winkelID, int uitgeverID, int kostprijs, int type, String naam) {
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

    public String getNaam() {
        return this.naam;
    }

    public Integer getTempStock(){
        return tempStock;
    }

    public void setTempStock(Integer tempStock){
        this.tempStock = tempStock;
    }

    public void setTempToStock(){
        this.stock = tempStock;
        this.verkocht = tempStock + verkocht;
    }

}
