package be.kuleuven.dbproject.controller;

import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class PaneHolderController {


    @FXML
    private AnchorPane tmpPane;

    @FXML
    private GameController gameController;

    private User user;

    public void initialize() {
        
    }

    public void setUser(User user){
        this.user = user;
    }

    public void changeChildTo(String id, DbConnection dbConnection) throws IOException{
        var resourceName = id + ".fxml";
        var pane = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
        var rootLoader = (AnchorPane) pane.load();
        var childController = pane.getController();

        if(childController.getClass() == GameController.class){
            var gameController = (GameController) childController;
            gameController.setDbConnection(dbConnection);
            gameController.setUser(user);
        }
        
        else if(childController.getClass() == UitgeleendeGameController.class){
            var uitgeleendeGameController = (UitgeleendeGameController) childController;
            uitgeleendeGameController.setDbConnection(dbConnection);
            uitgeleendeGameController.setUser(user);
        }

        else if(childController.getClass() == AdminController.class){
            var AdminController = (AdminController) childController;
            AdminController.setdbConnection(dbConnection);
            AdminController.setGenre();
            AdminController.setUitgever();
            AdminController.setWinkel();
            AdminController.setUser();
        }

        rootLoader.autosize();
        tmpPane.getChildren().setAll(rootLoader);
    }

}
