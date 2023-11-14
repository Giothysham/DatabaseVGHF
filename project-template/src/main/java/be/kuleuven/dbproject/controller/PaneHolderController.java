package be.kuleuven.dbproject.controller;

import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class PaneHolderController {


    @FXML
    private AnchorPane tmpPane;

    @FXML
    private GameController gameController;

    public void initialize() {
        
    }

    public void changeChildTo(String id) throws IOException{
        var resourceName = id + ".fxml";
        var pane = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource(resourceName));
        pane.autosize();
        tmpPane.getChildren().setAll(pane);
    }

    // public void setDbConnection(DbConnection dbConnection){
    //     this.dbConnection = dbConnection;
    // }
    
}
