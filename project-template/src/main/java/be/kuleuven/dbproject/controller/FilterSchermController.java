package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
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
    private Menu consoleMenu, winkelMenu, genreMenu, typeMenu;

    @FXML
    private HBox hboxConsoleMenu, hboxWinkelMenu, hboxGenreMenu;

    @FXML
    private Button returnBtn;

    private VerkoopbaarController gameParentController;

    private VerkoopbaarController extraParentController;

    //TODO => niet nodig. het wordt niet meer gebruikt. (alles in commentaar mag weg?)
    public void initialize(){
        returnBtn.setOnAction(e ->{ 
            gameParentController.updateOrSearchTable(false);
            extraParentController.updateOrSearchTable(false);
            var stage = (Stage) returnBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setParentController(VerkoopbaarController parenController){
        gameParentController = parenController;
    }

    public void setUpFilters(DbConnection dbConnection, VerkoopbaarApiInterface filterbaar){
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);

        if(filterbaar.getClass() == GameApi.class){
            var searchconsole = ((GameApi) filterbaar).getSearchConsole();
            
            if(searchconsole != null){
                consoleMenu.setText(searchconsole.name());
                //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(searchconsole, consoleMenu,"Console", gameApi,hboxConsoleMenu));
            }

            for(Console console: Console.values()){
            var menuItem = new MenuItem(console.name());
            menuItem.setOnAction(e ->{
                ((GameApi) filterbaar).creatSearchQuerry((console));
                consoleMenu.setText(console.name());
                //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(console, consoleMenu,"Console", gameApi,hboxConsoleMenu));
            });
            consoleMenu.getItems().add(menuItem);
        }   
        }

        var searchWinkel = filterbaar.getSearchWinkel();

        if(searchWinkel != null){
            winkelMenu.setText(searchWinkel.getSmallAdress());
           // hboxWinkelMenu.getChildren().add(setRemoveFilterButton(searchWinkel, winkelMenu,"Winkel", gameApi,hboxWinkelMenu));
        }

        for(Winkel winkel: winkelApi.getWinkels()){
            var menuItem = new MenuItem(winkel.getFullAdressWithID());
            menuItem.setOnAction(e -> {
                filterbaar.creatSearchQuerry(winkel);
                winkelMenu.setText(winkel.getSmallAdress());
                //hboxWinkelMenu.getChildren().add(setRemoveFilterButton(winkel, winkelMenu,"Winkel", gameApi, hboxWinkelMenu));
            });
            winkelMenu.getItems().add(menuItem);
        }

        if(filterbaar.getClass() == GameApi.class){
            var searchGenre = ((GameApi) filterbaar).getSearchGenre();

            if(searchGenre != null){
                genreMenu.setText(searchGenre.getNaam());
                //hboxGenreMenu.getChildren().add(setRemoveFilterButton(searchGenre, genreMenu,"Genre", gameApi,hboxGenreMenu));
            }

            for(Genre genre: genreApi.getGenres()){
                var menuItem = new MenuItem(genre.getNaam());
                menuItem.setOnAction(e -> {
                    ((GameApi) filterbaar).creatSearchQuerry(genre);
                    genreMenu.setText(genre.getNaam());
                    //hboxGenreMenu.getChildren().add(setRemoveFilterButton(genre, genreMenu,"Genre", gameApi, hboxGenreMenu));
                });
                genreMenu.getItems().add(menuItem);
            }
        }

         if(filterbaar.getClass().isAssignableFrom(ExtraApi.class)){

            var searchtype = ((ExtraApi) filterbaar).getSearchType();

            if(searchtype != null){
                typeMenu.setText(searchtype.name());
                //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(searchconsole, consoleMenu,"Console", gameApi,hboxConsoleMenu));
            }

            for(Type type: Type.values()){
                var menuItem = new MenuItem(type.name());
                menuItem.setOnAction(e ->{
                    ((ExtraApi) filterbaar).creatSearchQuerry((type));
                    typeMenu.setText(type.name());
                    //hboxConsoleMenu.getChildren().add(setRemoveFilterButton(console, consoleMenu,"Console", gameApi,hboxConsoleMenu));
                });
                typeMenu.getItems().add(menuItem);
            }
        }
    }

    public <T> Button setRemoveFilterButton(T variable,MenuButton menuButton,String menuNameWhenRemoved,VerkoopbaarApiInterface filterbaar,HBox menuHbox){
        var removeButton = new Button();
        removeButton.setText("X");
        removeButton.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white;");
        removeButton.setOnAction(e -> {
            menuButton.setText(menuNameWhenRemoved);
            filterbaar.removeFilterByClass(variable);
            menuHbox.getChildren().remove(removeButton);
        });
        return removeButton;
    }
    
}
