package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class 
VerkoopbaarMoreInfoController {

    private DbConnection dbConnection;

    @FXML
    private Text consoleTxt, stockTxt, uitgeverTxt, locatieTxt, genreTxt, typeTxt;

    @FXML
    private Label verkoopbaarNameTxt; 

    @FXML
    private TextArea moreInfoTxt;

    @FXML
    private HBox uitgeverHbox, genreHBox;

    public void initialize(){
        //TODO => zorg dat plek waar het aanwezig is te zien is. 
    }
    
    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public <T> void schermSwitch(String id,T variable){
        try{
            var resourceName = id + ".fxml";
            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();
            var controller = (MoreInfoController) loader.getController();

            controller.setMoreInfo(variable);

            var scene = new Scene((Parent) root);
            stage.setScene(scene);
            stage.setTitle("budo/"+ id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch(Exception e){
            System.out.println("kan "+ id + " niew vinden");
            e.printStackTrace();
        }
    }

    public void setVerkoopbaar(VerkoopbaarInterface Verkoopbaar){
        verkoopbaarNameTxt.setText(Verkoopbaar.getNaam());

        var uitgever = Verkoopbaar.getUitgever();
        uitgeverTxt.setText(uitgever.getNaam());

        stockTxt.setText(Integer.toString(Verkoopbaar.getStock()));

        locatieTxt.setText(Verkoopbaar.getWinkel().getSmallAdress());

        moreInfoTxt.setEditable(false);
        moreInfoTxt.setWrapText(true);

        if(Verkoopbaar.getClass().isAssignableFrom(Game.class)){
            moreInfoTxt.setText(((Game)Verkoopbaar).getBeschrijving());

            consoleTxt.setText(((Game)Verkoopbaar).getConsole().toString());

            genreTxt.setText(((Game)Verkoopbaar).getGenre().getNaam());
            genreHBox.setOnMouseClicked(e -> schermSwitch("moreinfo", ((Game)Verkoopbaar).getGenre()));
        } 

        else if(Verkoopbaar.getClass().isAssignableFrom(Extra.class)){
            typeTxt.setText(((Extra)Verkoopbaar).getType().toString());
        }

        uitgeverHbox.setOnMousePressed(e -> schermSwitch("moreinfo", Verkoopbaar.getUitgever()));
    }
    
}
