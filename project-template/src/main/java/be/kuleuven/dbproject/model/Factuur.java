package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Factuur {
    
    @Column(name = "factuurID")
    @GeneratedValue
    @Id
    private int factuurID;

    @Column(name = "userID")
    private int userID;

    @Column(name = "kostprijs")
    private double kostPrijs;

    @Column(name = "gameID")
    private int gameID;

    @Column(name = "extraID")
    private int extraID;

    @Column(name = "winkelID")
    private int winkelID;

    public Factuur(){

    }


    public Factuur(int factuurID, int userID, double kostprijs, int gameID, int extraID, int winkelID) {
        this.factuurID = factuurID;
        this.userID = userID;
        this.kostPrijs = kostprijs;
        this.gameID = gameID;
        this.extraID = extraID;
        this.winkelID = winkelID;
    }


    public int getFactuurID() {
        return this.factuurID;
    }

    public int getUserID() {
        return this.userID;
    }

    public double getKostPrijs() {
        return this.kostPrijs;
    }

    public int getGameID() {
        return this.gameID;
    }

    public int getExtraID() {
        return this.extraID;
    }

    public int getWinkelID() {
        return this.winkelID;
    }

}
