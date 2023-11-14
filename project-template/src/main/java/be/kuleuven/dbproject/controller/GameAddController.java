package be.kuleuven.dbproject.controller;


import java.util.List;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.enums.Console;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameAddController {

    @FXML
    private Button submitGameBtn;

    @FXML
    private ComboBox consoleDropDown, winkelDropDown, genreIDDropDown;

    @FXML
    private TextField aantalStock, aantalUitgeleend, gameID, naam, kostPijs;

    @FXML
    private TextArea beschrijving;

    private List<Winkel> winkels;

    private List<Genre> genres;

    private DbConnection dbConnection;

    private GameController gameController;
 
    //to do => zien hoe we winkel naar gaan fixen en genre (hier voor denk ik ook weer een neum). 


    public void initialize(){        
        submitGameBtn.setOnAction(e -> submitGame());
    }

    public void setupDropDown(DbConnection dbConnection){
        //zien of we dit niet voledig door de db kunnen laten doen. b.v => geen lijst bij houden van alle winkels en genres maar eerder gwn met db aan de han van de naam de id zoeken. 
        //zoals bij genres. 
        winkels = dbConnection.getWinkels();
        genres = dbConnection.getGenres();

        ObservableList<String> listGenreNames = FXCollections.observableArrayList();
        ObservableList<String> listWinkelNames = FXCollections.observableArrayList();

        for (Genre genre : genres) {
            listGenreNames.add(genre.getNaam());
        }

        for (Winkel winkel : winkels) {
            listWinkelNames.add(winkel.getFullAdressWithID());
        }

        ObservableList<Console> listInstance = FXCollections.observableArrayList();
        listInstance.addAll(Console.values());
        consoleDropDown.setItems(listInstance);

        genreIDDropDown.setItems(listGenreNames);
        winkelDropDown.setItems(listWinkelNames);
    }

    private boolean submitGame(){

        var aantalStock = Integer.parseInt(this.aantalStock.getText());
        Console console = (Console) consoleDropDown.getValue();
        var kostPrijs = Double.parseDouble(this.kostPijs.getText());
        var genre = (String) genreIDDropDown.getValue();
        var naam = this.naam.getText();
        var beschrijving = this.beschrijving.getText();
        Integer winkelID = null;
        String nameWinkel = (String) winkelDropDown.getValue();

        var genreID = dbConnection.getGenreIdByName(genre);

        for (Winkel winkel : winkels) {
            if(nameWinkel.contains(winkel.getFullAdressWithID())){
                winkelID = winkel.getWinkelID();
                break;
            }
        }
        
        //logica voor te zien of iets null is of niet 
        Game tempgame = new Game(aantalStock, 0, console, 0,winkelID, kostPrijs, genreID, naam, beschrijving);
        
        try {
            dbConnection.postGame(tempgame);

            gameController.initTable();

            var stage = (Stage) submitGameBtn.getScene().getWindow();
            stage.close();

            return true;
        } catch (Exception e) {
            System.out.println("er is iet misgegaan met de game "+naam);
            System.out.println(e);
            return false;
        }
        
    }

    public void setParentController(GameController gameController){
        this.gameController = gameController;
    }

    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
}
