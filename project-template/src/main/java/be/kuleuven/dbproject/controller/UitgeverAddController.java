package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UitgeverAddController {

    private UitgeverApi uitgeverApi;

    private AdminController parentController;

    private Uitgever uitgever;

    @FXML
    private Button submitUitgeverButton;

    @FXML
    private TextArea beschrijvingTxt;

    @FXML
    private TextField naamTxt;

    public void initialize(){
        submitUitgeverButton.setOnAction(e -> addOrUpdateUitgever());
    }

    private void addOrUpdateUitgever() {
        var name = naamTxt.getText();
        var beschrijving = beschrijvingTxt.getText();

        if(uitgever != null){
            uitgever.setBeschrijving(beschrijving);
            uitgever.setNaam(name);

            parentController.setUitgever();

            var stage = (Stage) naamTxt.getScene().getWindow();
            stage.close();
        }
        else{
            if(name != "" && beschrijving != ""){
                var uitgever = new Uitgever(name, beschrijving, 0);
                try{
                    uitgeverApi.postUitgever(uitgever);

                    parentController.setUitgever();

                    var stage = (Stage) naamTxt.getScene().getWindow();
                    stage.close();
                }
                catch(Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("store error");
                    alert.setHeaderText("Error");
                    alert.setContentText("de uitgever kan niet geupdate worden");
                    alert.show();
                }
            }
        }
        
        
    }

    public void setUitgever(Integer id){
        this.uitgever = uitgeverApi.getUitgeverById(id);

        beschrijvingTxt.setText(uitgever.getBeschrijving());
        naamTxt.setText(uitgever.getNaam());
    }

    public void setDbConnection(DbConnection dbConnection){
        this.uitgeverApi = new UitgeverApi(dbConnection);
    }
    
    public void setParentController(AdminController adminController){
        this.parentController = adminController;
    }
}
