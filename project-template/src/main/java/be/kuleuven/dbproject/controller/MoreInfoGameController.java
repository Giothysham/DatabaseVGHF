package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class 
MoreInfoGameController {

    private DbConnection dbConnection;

    private Game game;

    @FXML
    private Text gameNameTxt; 

    @FXML
    private TextArea moreInfoTxt;

    public void initialize(){

    }
    
    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setGame(Game game){
        gameNameTxt.setText(game.getNaam());

        //moreInfo to vertical fit. 
        moreInfoTxt.setText(game.getBeschrijving());
    }
    
}
