package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.enums.Type;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FilterSchermController {

    @FXML
    private Menu consoleMenu, winkelMenu, genreMenu;

    @FXML
    private HBox hboxConsoleMenu, hboxWinkelMenu, hboxGenreMenu;

    @FXML
    private Button returnBtn;

    private GameController gameParentController;

    private ExtraController extraParentController;

    //TODO => vragen aan wouter of dit een beetje oke is => ook hoe auto resize.
    public void initialize(){
        returnBtn.setOnAction(e ->{ 
            gameParentController.updateOrSearchTable(false);
            extraParentController.updateOrSearchTable(false);
            var stage = (Stage) returnBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setParentController(GameController parenController){
        gameParentController = parenController;
    }

    public void setParentController(ExtraController parenController){
        extraParentController = parenController;
    }

    public void setUpFilters(DbConnection dbConnection, GameApi gameApi){
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);

        var searchconsole = gameApi.getSearchConsole();

        if(searchconsole != null){
            consoleMenu.setText(searchconsole.name());
            //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(searchconsole, consoleMenu,"Console", gameApi,hboxConsoleMenu));
        }

        for(Console console: Console.values()){
            var menuItem = new MenuItem(console.name());
            menuItem.setOnAction(e ->{
                gameApi.creatSearchQuerry((console));
                consoleMenu.setText(console.name());
                //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(console, consoleMenu,"Console", gameApi,hboxConsoleMenu));
            });
            consoleMenu.getItems().add(menuItem);
        }   

        var searchWinkel = gameApi.getSearchWinkel();

        if(searchWinkel != null){
            winkelMenu.setText(searchWinkel.getSmallAdress());
           // hboxWinkelMenu.getChildren().add(setRemoveFilterButton(searchWinkel, winkelMenu,"Winkel", gameApi,hboxWinkelMenu));
        }

        for(Winkel winkel: winkelApi.getWinkels()){
            var menuItem = new MenuItem(winkel.getFullAdressWithID());
            menuItem.setOnAction(e -> {
                gameApi.creatSearchQuerry(winkel);
                winkelMenu.setText(winkel.getSmallAdress());
                //hboxWinkelMenu.getChildren().add(setRemoveFilterButton(winkel, winkelMenu,"Winkel", gameApi, hboxWinkelMenu));
            });
            winkelMenu.getItems().add(menuItem);
        }

        var searchGenre = gameApi.getSearchGenre();

        if(searchGenre != null){
            genreMenu.setText(searchGenre.getNaam());
            //hboxGenreMenu.getChildren().add(setRemoveFilterButton(searchGenre, genreMenu,"Genre", gameApi,hboxGenreMenu));
        }

        for(Genre genre: genreApi.getGenres()){
            var menuItem = new MenuItem(genre.getNaam());
            menuItem.setOnAction(e -> {
                gameApi.creatSearchQuerry(genre);
                genreMenu.setText(genre.getNaam());
                //hboxGenreMenu.getChildren().add(setRemoveFilterButton(genre, genreMenu,"Genre", gameApi, hboxGenreMenu));
            });
            genreMenu.getItems().add(menuItem);
        }
    }

    public void setUpFilters(DbConnection dbConnection, ExtraApi extraApi){
        var winkelApi = new WinkelApi(dbConnection);

        var searchtype = extraApi.getSearchType();

        if(searchtype != null){
            consoleMenu.setText(searchtype.name());
            //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(searchconsole, consoleMenu,"Console", gameApi,hboxConsoleMenu));
        }

        for(Type type: Type.values()){
            var menuItem = new MenuItem(type.name());
            menuItem.setOnAction(e ->{
                extraApi.creatSearchQuerry((type));
                consoleMenu.setText(type.name());
                //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(console, consoleMenu,"Console", gameApi,hboxConsoleMenu));
            });
            consoleMenu.getItems().add(menuItem);
        }   

        var searchWinkel = extraApi.getSearchWinkel();

        if(searchWinkel != null){
            winkelMenu.setText(searchWinkel.getSmallAdress());
           // hboxWinkelMenu.getChildren().add(setRemoveFilterButton(searchWinkel, winkelMenu,"Winkel", gameApi,hboxWinkelMenu));
        }

        for(Winkel winkel: winkelApi.getWinkels()){
            var menuItem = new MenuItem(winkel.getFullAdressWithID());
            menuItem.setOnAction(e -> {
                extraApi.creatSearchQuerry(winkel);
                winkelMenu.setText(winkel.getSmallAdress());
                //hboxWinkelMenu.getChildren().add(setRemoveFilterButton(winkel, winkelMenu,"Winkel", gameApi, hboxWinkelMenu));
            });
            winkelMenu.getItems().add(menuItem);
        }
    }

    public <T> Button setRemoveFilterButton(T variable,MenuButton menuButton,String menuNameWhenRemoved,GameApi gameApi,HBox menuHbox){
        var removeButton = new Button();
        removeButton.setText("X");
        removeButton.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white;");
        removeButton.setOnAction(e -> {
            menuButton.setText(menuNameWhenRemoved);
            gameApi.removeFilterByClass(variable);
            menuHbox.getChildren().remove(removeButton);
        });
        return removeButton;
    }

    public <T> Button setRemoveFilterButton(T variable,MenuButton menuButton,String menuNameWhenRemoved,ExtraApi extraApi,HBox menuHbox){
        var removeButton = new Button();
        removeButton.setText("X");
        removeButton.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white;");
        removeButton.setOnAction(e -> {
            menuButton.setText(menuNameWhenRemoved);
            extraApi.removeFilterByClass(variable);
            menuHbox.getChildren().remove(removeButton);
        });
        return removeButton;
    }
    
}
