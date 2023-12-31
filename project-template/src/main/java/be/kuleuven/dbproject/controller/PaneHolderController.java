package be.kuleuven.dbproject.controller;

import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class PaneHolderController {


    @FXML
    private AnchorPane tmpPane;

    private User user;

    private VerkoopbaarInterface product;

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

        if(childController.getClass() == VerkoopbaarController.class){
            var verkoopbaarController = (VerkoopbaarController) childController;
            verkoopbaarController.setProduct(product);
            verkoopbaarController.setUser(user);
            verkoopbaarController.setDbConnection(dbConnection);
            verkoopbaarController.setUpFilters();
        }
        
        else if(childController.getClass() == UitgeleendeGameController.class){
            var uitgeleendeGameController = (UitgeleendeGameController) childController;
            uitgeleendeGameController.setUser(user);
            uitgeleendeGameController.setDbConnection(dbConnection);
            
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

    public void setProduct(VerkoopbaarInterface product){
        this.product = product;
    }


}
