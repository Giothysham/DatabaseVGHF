package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreatAccountController {

    @FXML
    private GridPane creatAccountPane;

    @FXML
    private Button creatAccountBtn;

    private User user;

    private DbConnection dbConnection;

    public void initialize(){
                //fix verder implimenteren
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
            projectMainController.setdbConnection(this.dbConnection);
            projectMainController.setUser(user);
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
    }
    
}
