package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity
public class Game_user {
    @Column(name = "userID")
    private int userID;

    @Column(name = "gameID")
    private int gameID;

    @Column(name = "coppelID")
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="game_user")
    @Id
    private int coppelID;


    public Game_user(int userID, int gameID, int coppelID) {
        this.userID = userID;
        this.gameID = gameID;
        this.coppelID = coppelID;
    }

    public Game_user(){

    }

    public int getUserID() {
        return this.userID;
    }

    public int getGameID() {
        return this.gameID;
    }

}
