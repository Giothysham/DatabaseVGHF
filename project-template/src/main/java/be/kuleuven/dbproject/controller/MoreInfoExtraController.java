package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class 
MoreInfoExtraController {

    @FXML
    private Text typeTxt, stockTxt, uitgeverTxt, locatieTxt;

    @FXML
    private Label extraNameTxt; 

    @FXML
    private TextArea moreInfoTxt;

    private DbConnection dbConnection; //TODO: niet nodig?

    public void initialize(){
        //TODO => zorg dat plek waar het aanwezig is te zien is. 
    }

    public void setExtra(Extra extra){
        extraNameTxt.setText(extra.getNaam());

        moreInfoTxt.setText("Extra's don't have a discription yet");
        moreInfoTxt.setEditable(false);
        moreInfoTxt.setWrapText(true);

        var uitgever = extra.getUitgever();
        uitgeverTxt.setText(uitgever.getNaam());

        stockTxt.setText(Integer.toString(extra.getStock()));

        typeTxt.setText(extra.getType().toString());

        locatieTxt.setText(extra.getWinkel().getFullAdressWithID());
    }
    
    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
