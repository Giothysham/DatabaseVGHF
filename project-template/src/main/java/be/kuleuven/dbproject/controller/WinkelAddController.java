package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.WinkelApi;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class WinkelAddController {

    private Winkel winkel;

    private AdminController parentController;

    private WinkelApi winkelApi;

    @FXML
    private Button submitWinkelButton;

    @FXML
    private TextField winkelStraatNaamTxt, stadTxt,postcodeTxt,provincieTxt,landTxt;

    public void initialize(){
        submitWinkelButton.setOnAction(e -> addOrUpdateWinkel());
    }

    private void addOrUpdateWinkel() {
        var winkelStraatNaam = winkelStraatNaamTxt.getText();
        var stad = stadTxt.getText();
        var postcode = postcodeTxt.getText();
        var provincie = provincieTxt.getText();
        var land = landTxt.getText();

        if(winkel != null){
            winkel.setAdres(winkelStraatNaam);
            winkel.setStad(stad);
            winkel.setPostcode(postcode);
            winkel.setProvincie(provincie);
            winkel.setLand(land);

            parentController.setWinkel();

            var stage = (Stage) stadTxt.getScene().getWindow();
            stage.close();
        }
        else{
            if(winkelStraatNaam != "" && stad != "" && stad != "" && postcode != "" && provincie != "" && land != ""){
                var winkel = new Winkel(0,0,winkelStraatNaam,stad, postcode,provincie,land); 
                try{
                    winkelApi.postWinkel(winkel);
                }
                catch(Exception e){
                    var alert = new Alert(AlertType.ERROR);
                    alert.setTitle("store error");
                    alert.setHeaderText("Error");
                    alert.setContentText("the winkel could not be updated");
                    alert.show();
                }

                parentController.setWinkel();

                var stage = (Stage) stadTxt.getScene().getWindow();
                stage.close();
            }
        }
        
    }

    public void setWinkel(Integer id){
        this.winkel = winkelApi.getWinkelById(id);

        winkelStraatNaamTxt.setText(winkel.getAdres());
        stadTxt.setText(winkel.getStad());
        postcodeTxt.setText(winkel.getPostcode());
        provincieTxt.setText(winkel.getProvincie());
        landTxt.setText(winkel.getLand());
    }

    public void setDbConnection(DbConnection dbConnection){
        this.winkelApi = new WinkelApi(dbConnection);
    }

    public void setParentController(AdminController parentController){
        this.parentController = parentController;
    }
    
}
