package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class CreatAccountController {

    @FXML
    private GridPane creatAccountPane;

    @FXML
    private Button creatAccountBtn;

    @FXML
    private TextField achterNaamTxtField, voorNaamTxtField, straatNaamTxt, stadTxtField,postcodeTxtField, provincieTxtField,landTxtField,telefoonNummerTxtField,emailTxtField,wachtwoordTxtField,wachtwoordHerhaalTxtField;

    @FXML
    private Text wachtwoordText, wachtwoordHerhaalText, foutMeldingTxt, telText, emailTxt;
    //10 digits.

    private User user;

    private UserApi userApi;

    private DbConnection dbConnection;

    public void initialize(){
        creatAccountBtn.setOnAction(e -> creatAccount());
    }

    public void creatAccount(){
        var voorNaam = voorNaamTxtField.getText();
        var achternaam = achterNaamTxtField.getText();
        var straatNaam = straatNaamTxt.getText();
        var stad = stadTxtField.getText();
        var postcode = postcodeTxtField.getText();
        var provincie = provincieTxtField.getText();
        var land = landTxtField.getText();
        var tel = telefoonNummerTxtField.getText();
        var email = emailTxtField.getText();
        var wachtwoord = wachtwoordTxtField.getText();
        var harhaalWachtwoord = wachtwoordHerhaalTxtField.getText();
        var uitgeleendeGame =  new ArrayList<Game>();

        if(tel.length()  != 10){
            wachtwoordText.setFill(Color.BLACK);
            wachtwoordHerhaalText.setFill(Color.BLACK);
            emailTxt.setFill(Color.BLACK);
            telText.setFill(Color.RED);
            foutMeldingTxt.setText("fout in tel nummer");
            foutMeldingTxt.setFill(Color.RED);
        }
        else if(!email.contains("@")){
            telText.setFill(Color.BLACK);
            wachtwoordText.setFill(Color.BLACK);
            wachtwoordHerhaalText.setFill(Color.BLACK);
            emailTxt.setFill(Color.RED);
            foutMeldingTxt.setText("fout in de mail");
            foutMeldingTxt.setFill(Color.RED);
        }
        else if(!wachtwoord.equals(harhaalWachtwoord) || wachtwoord == ""){
            telText.setFill(Color.BLACK);
            wachtwoordText.setFill(Color.RED);
            wachtwoordHerhaalText.setFill(Color.RED);
            emailTxt.setFill(Color.BLACK);
            foutMeldingTxt.setText("wachtwoorden komen niet overeen");
            foutMeldingTxt.setFill(Color.RED);
        }
        else{
            user = new User(achternaam,voorNaam,tel,straatNaam,stad,postcode,provincie,land,0,email,wachtwoord,0,uitgeleendeGame);

            try{
                userApi.creatUser(user);
                openWindow("main.fxml");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }   
        }
    }

    public void setUpOnClose(Stage stage){
        stage.setOnCloseRequest(e -> {
            try {
                openWindow("loginscherm.fxml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void openWindow(String ID) throws IOException {
        
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(ID));
        Parent root = loader.load();
        var controller = loader.getController();

        
        if(controller.getClass() == ProjectMainController.class){
            var projectMainController = (ProjectMainController) controller;
            projectMainController.setDbConnection(this.dbConnection);
            projectMainController.setUser(user);
            var window = (Stage) creatAccountBtn.getScene().getWindow();
            window.close();
        }
        else if(controller.getClass() == LoginController.class){
            var LoginController = (LoginController) controller;
            LoginController.setUserApi(dbConnection);
        }

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ ID);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
    }
    
}
