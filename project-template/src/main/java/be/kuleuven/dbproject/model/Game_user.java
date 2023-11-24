package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Game_user {
    @Column(name = "userID")
    @Id
    private int userID;

    @Column(name = "gameID")
    private int gameID;


    public Game_user(int userID, int gameID) {
        this.userID = userID;
        this.gameID = gameID;
    }


    public int getUserID() {
        return this.userID;
    }

    public int getGameID() {
        return this.gameID;
    }

}
