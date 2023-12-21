package be.kuleuven.dbproject.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
public class Factuur {
    
    @Column(name = "factuurID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="factuur")
    @Id
    private int factuurID;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "userID")
    private User user;

    @Column(name = "kostprijs")
    private double kostPrijs;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "gameID")
    private Game game;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "extraID")
    private Extra extra;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "winkelID")
    private Winkel winkel;

    @Transient
    private String gameName;

    @Transient
    private Double gamePrice;

    public Factuur(){

    }


    public Factuur(int factuurID, User user, double kostprijs, Game game, Extra extra, Winkel winkel) {
        this.factuurID = factuurID;
        this.user = user;
        this.kostPrijs = kostprijs;
        this.game = game;
        this.extra = extra;
        this.winkel = winkel;
        System.out.println("------------------hier------------------");
        if(game != null) {
            this.gameName = game.getNaam();
            this.gamePrice = game.getKostPrijs();
        }
        else {
            this.gameName = extra.getNaam();
            this.gamePrice = extra.getKostPrijs();
        }
    }


    public int getFactuurID() {
        return this.factuurID;
    }

    public User getUser() {
        return this.user;
    }

    public double getKostPrijs() {
        return this.kostPrijs;
    }

    public Game getGame() {
        return this.game;
    }

    public Extra getExtra() {
        return this.extra;
    }

    public Winkel getWinkel() {
        return this.winkel;
    }

    public String getGameName() {
        return this.gameName;
    }

    public Double getGamePrice() {
        return this.gamePrice;
    }

    public void updateFactuur() {
        if(game != null) {
            this.gameName = game.getNaam();
            this.gamePrice = game.getKostPrijs();
        }
        else if(extra != null){
            this.gameName = extra.getNaam();
            this.gamePrice = extra.getKostPrijs();
        }
    }

}
