package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.ProjectMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginBtn;
    
    public void initialize(){
        loginBtn.setOnAction(e -> {});
    }

    private void changeWindow(String id) throws IOException{
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(id));
        var root = (GridPane) loader.load();
        var controller = loader.getController();

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ id);
        stage.initOwner(ProjectMain.getRootStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }
}
