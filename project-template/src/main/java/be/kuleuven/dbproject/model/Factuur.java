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

@Entity
public class Factuur {
    
    @Column(name = "factuurID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="factuur")
    @Id
    private int factuurID;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "userID")
    private User user;

    @Column(name = "kostprijs")
    private double kostPrijs;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "gameID")
    private Game game;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "extraID")
    private Extra extra;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "winkelID")
    private Winkel winkel;

    public Factuur(){

    }


    public Factuur(int factuurID, User user, double kostprijs, Game game, Extra extra, Winkel winkel) {
        this.factuurID = factuurID;
        this.user = user;
        this.kostPrijs = kostprijs;
        this.game = game;
        this.extra = extra;
        this.winkel = winkel;
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

}
