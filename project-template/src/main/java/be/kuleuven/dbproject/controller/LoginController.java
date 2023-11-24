package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    @FXML
    private Button loginBtn;

    @FXML
    private Button crtAccountBtn;

    @FXML
    private TextField emailTxtField;

    @FXML
    private TextField wachtwoordTxtField;

    private UserApi userApi;

    private DbConnection dbConnection;

    private User user;
    
    public void initialize(){
        loginBtn.setOnAction(e -> checkLoginCredentials());

        emailTxtField.setText("vranckx.mauro@student.uhasselt.be");
        wachtwoordTxtField.setText("test");

        crtAccountBtn.setOnAction(e -> {
            try {
                changeWindow("createaccount");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void checkLoginCredentials(){
        var email = emailTxtField.getText();
        var wachtwoord = wachtwoordTxtField.getText();
        try {
            this.user = userApi.chekUserAndWachtwoord(email, wachtwoord);
            System.out.println(user);
            changeWindow("main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeWindow(String id) throws IOException{
        
        Stage oldStage = (Stage) loginBtn.getScene().getWindow();
        oldStage.close();
        
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(id));
        Parent root = (BorderPane) loader.load();
        var controller = loader.getController();


        if(controller.getClass() == ProjectMainController.class){
            var projectMainController = (ProjectMainController) controller;
            projectMainController.setdbConnection(this.dbConnection);
            projectMainController.setUser(user);
        }

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ id);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void setUserApi(DbConnection dbConnection){
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
    }
}
