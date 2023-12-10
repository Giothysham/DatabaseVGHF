package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MoreInfoController { //TODO: wtf is uw nut hier

    @FXML
    private Label topNameTxt ;

    @FXML
    private TextArea moreInfoTxt;
    
    public MoreInfoController(){
         
    } 

    public <T> void setMoreInfo(T moreInfoVariable){

        if(moreInfoVariable.getClass() == Genre.class){
            var genre = (Genre) moreInfoVariable;
            topNameTxt.setText(genre.getNaam());
            moreInfoTxt.setText(genre.getBeschrijving());
        }
        else if(moreInfoVariable.getClass() == Uitgever.class){
            var uitgever = (Uitgever) moreInfoVariable;
            topNameTxt.setText(uitgever.getNaam());
            moreInfoTxt.setText(uitgever.getBeschrijving());
        }

        moreInfoTxt.setEditable(false);
        moreInfoTxt.setWrapText(true);
    }

}
