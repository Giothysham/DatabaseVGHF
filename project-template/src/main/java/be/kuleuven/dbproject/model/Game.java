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

import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.enums.Console;

@Entity
public class Game implements VerkoopbaarInterface{

    @Column(name = "aantal_in_stock", updatable = true)
    private int stock;
    
    @Column(name = "aantal_verkocht", updatable = true)
    private int verkocht;

    @Enumerated(EnumType.STRING)
    @Column(name = "console")
    private Console console;

    @Column(name = "gameID")
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="sqliteGameIdTable")
    @Id
    private int gameID;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "winkelID")
    private Winkel winkel;

    @Column(name = "kostprijs")
    private double kostPrijs;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "genreID")
    private Genre genre;

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Transient
    private Integer tempStock;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "uitgeverID")
	private Uitgever uitgever;

    @OneToMany(mappedBy = "game")
    private List<Factuur> factuur;

    @PrePersist
    private void prePersist() {
        System.out.println("Bezig met het bezigen van het opslaan van game " + this);
    }

    public Game(){

    }

    public Game(int stock, int verkocht, Console console, int gameID, Winkel winkel, double kostPrijs, Genre genre, String naam, String beschrijving,Uitgever uitgever) {
        super();
        this.stock = stock;
        this.verkocht = verkocht;
        this.console = console;
        this.gameID = gameID;
        this.winkel = winkel;
        this.kostPrijs = kostPrijs;
        this.genre = genre;
        this.naam = naam;
        this.uitgever = uitgever;
        this.beschrijving = beschrijving;
    }

    public int getStock() {
        return this.stock;
    }

    public int getVerkocht() {
        return this.verkocht;
    }

    public Console getConsole() {
        return this.console;
    }

    public int getID() {
        return this.gameID;
    }

    public Winkel getWinkel() {
        return this.winkel;
    }

    public double getKostPrijs() {
        return this.kostPrijs;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public String getNaam() {
        return this.naam;
    }

    public String getBeschrijving() {
        return this.beschrijving;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setVerkocht(int verkocht) { 
        this.verkocht = verkocht;
    }
    
    public void setConsole(Console console) {
        this.console = console;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setWinkelID(Winkel winkel) {
        this.winkel = winkel;
    }

    public void setKostPrijs(double kostPrijs) {
        this.kostPrijs = kostPrijs;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
    
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }


    public Uitgever getUitgever() {
        return this.uitgever;
    }

    public void setUitgever(Uitgever uitgever) {
        this.uitgever = uitgever;
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

}