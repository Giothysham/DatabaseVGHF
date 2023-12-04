package be.kuleuven.dbproject.model;

import javax.persistence.*;

import be.kuleuven.dbproject.model.enums.Type;

@Entity
public class Extra {

    @Column(name = "aantal_in_stock")
    private int stock;

    @Column(name = "aantal_verkocht")
    private int verkocht;

    @Column(name = "extraID")
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="sqliteGameIdTable")
    @Id
    private int extraID;

    @Column(name = "winkelID")
    private int winkelID; //TODO: edmond veranderd naar inner joins + exra

    @Column(name = "uitgeverID")
    private int uitgeverID; //TODO: edmond veranderd naar inner joins + exra

    @Column(name = "kostprijs")
    private double kostprijs;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type; 

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

    public Extra(int stock, int verkocht, int extraID, int winkelID, int uitgeverID, double kostprijs, Type type, String naam) {
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

    public double getKostprijs() {
        return this.kostprijs;
    }

    public Type getType() {
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

    public void itemVerkocht(){
        this.verkocht++;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setType(Type type){
        this.type = type;
    }

    public void setKostprijs(double kostprijs) {
        this.kostprijs = kostprijs;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setUitgever(Integer uitgeverID) {
        this.uitgeverID = uitgeverID;
    }
}
