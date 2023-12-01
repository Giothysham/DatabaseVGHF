package be.kuleuven.dbproject.model;



import javax.persistence.*;

import be.kuleuven.dbproject.model.enums.Console;

@Entity
public class Game {

    @Column(name = "aantal_in_stock", updatable = true)
    private int stock;
    
    @Column(name = "aantal_uitgeleend", updatable = true)
    private int verkocht;

    @Enumerated(EnumType.STRING)
    @Column(name = "console")
    private Console console;

    @Column(name = "gameID")
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="sqliteGameIdTable")
    @Id
    private int gameID;

    @Column(name = "winkelID")
    private int winkelID;

    @Column(name = "kostprijs")
    private double kostPrijs;

    @Column(name = "genreID")
    private int genreID;

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Column(name = "uitgeverID")
    private Integer uitgeverID;

    @Transient
    private Integer tempStock;

    @PrePersist
    private void prePersist() {
        System.out.println("Bezig met het bezigen van het opslaan van extra " + this);
    }

    public Game(){

    }

    public Game(int stock, int verkocht, Console console, int gameID, int winkelID, double kostPrijs, int genreID, String naam, String beschrijving,Integer uitgeverID) {
        super();
        this.stock = stock;
        this.verkocht = verkocht;
        this.console = console;
        this.gameID = gameID;
        this.winkelID = winkelID;
        this.kostPrijs = kostPrijs;
        this.genreID = genreID;
        this.naam = naam;
        this.uitgeverID = uitgeverID;
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

    public int getGameID() {
        return this.gameID;
    }

    public int getWinkelID() {
        return this.winkelID;
    }

    public double getKostPrijs() {
        return this.kostPrijs;
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

    public void setWinkelID(int winkelID) {
        this.winkelID = winkelID;
    }

    public void setKostPrijs(double kostPrijs) {
        this.kostPrijs = kostPrijs;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
    
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }


    public Integer getUitgever() {
        return this.uitgeverID;
    }

    public void setUitgever(Integer uitgeverID) {
        this.uitgeverID = uitgeverID;
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