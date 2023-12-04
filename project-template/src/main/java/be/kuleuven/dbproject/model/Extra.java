package be.kuleuven.dbproject.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;

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

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "winkelID")
    private Winkel winkel;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "uitgeverID")
    private Uitgever uitgever;

    @Column(name = "kostprijs")
    private double kostprijs;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type; 

    @Column(name = "naam")
    private String naam;

    @OneToMany(mappedBy = "extra")
    private List<Factuur> factuur;

    @Transient
    private Integer tempStock;

    @PrePersist
    private void prePersist() {
        System.out.println("Bezig met het bezigen van het opslaan van extra " + this);
    }

    public Extra(){
        
    }

    public Extra(int stock, int verkocht, int extraID, Winkel winkel, Uitgever uitgever, double kostprijs, Type type, String naam) {
        this.stock = stock;
        this.verkocht = verkocht;
        this.extraID = extraID;
        this.winkel = winkel;
        this.uitgever = uitgever;
        this.kostprijs = kostprijs;
        this.type = type;
        this.naam = naam;
    }


    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getVerkocht() {
        return this.verkocht;
    }

    public int getExtraID() {
        return this.extraID;
    }

    public Winkel getWinkel() {
        return this.winkel;
    }

    public Uitgever getUitgever() {
        return this.uitgever;
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

    public void setTempToStock(){
        var tempverkocht = stock - tempStock;
        this.stock = tempStock;
        this.verkocht = tempverkocht + verkocht;
    }

    public void itemVerkocht(){
        this.verkocht++;
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

    public void setUitgever(Uitgever uitgever) {
        this.uitgever = uitgever;
    }
}
