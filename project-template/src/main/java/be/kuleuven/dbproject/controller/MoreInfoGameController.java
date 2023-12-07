package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class 
MoreInfoGameController {

    private DbConnection dbConnection;

    @FXML
    private Text consoleTxt, stockTxt, uitgeverTxt;

    @FXML
    private Label gameNameTxt; 

    @FXML
    private TextArea moreInfoTxt;

    public void initialize(){
        //TODO => zorg dat plek waar het aanwezig is te zien is. 
    }
    
    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setGame(Game game){
        gameNameTxt.setText(game.getNaam());

        moreInfoTxt.setText(game.getBeschrijving());
        moreInfoTxt.setEditable(false);
        moreInfoTxt.setWrapText(true);

        //var uitgeverApi = new UitgeverApi(dbConnection);
        // Uitgever uitgever = uitgeverApi.getUitgeverById(game.getUitgever());
        var uitgever = game.getUitgever();
        uitgeverTxt.setText(uitgever.getNaam());

        stockTxt.setText(Integer.toString(game.getStock()));

        consoleTxt.setText(game.getConsole().toString());
    }
    
}
