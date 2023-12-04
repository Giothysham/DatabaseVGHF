package be.kuleuven.dbproject.controller;


import java.util.List;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
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
    private ComboBox<Console> consoleDropDown;
    
    @FXML
    private ComboBox<String> winkelDropDown, uitgeverIDDropDown, genreIDDropDown;

    @FXML
    private TextField aantalStock, aantalUitgeleend, gameID, naam, kostPijs;

    @FXML
    private TextArea beschrijving;

    private List<Winkel> winkels;

    private DbConnection dbConnection;

    private GameController gameController;

    private boolean update;

    private Game game;

    private List<Uitgever> uitgevers;

    public void initialize(){        
        submitGameBtn.setOnAction(e -> {
                if(update){
                    updateGame();
                }else{
                    submitGame();
                }
                
            }
        );

        beschrijving.setWrapText(true);
    }

    public void setupDropDown(DbConnection dbConnection){
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);
        var uitgeverApi = new UitgeverApi(dbConnection);

        winkels = winkelApi.getWinkels();
        var genres = genreApi.getGenres();
        uitgevers = uitgeverApi.getUitgevers();


        ObservableList<String> listGenreNames = FXCollections.observableArrayList();
        ObservableList<String> listWinkelNames = FXCollections.observableArrayList();
        ObservableList<String> listUitgeverNames = FXCollections.observableArrayList(); 

        for(Uitgever uitgever: uitgevers){
            listUitgeverNames.addAll(uitgever.getNaam());
        }

        for (Genre genre : genres) {
            listGenreNames.add(genre.getNaam());
        }

        for (Winkel winkel : winkels) {
            listWinkelNames.add(winkel.getFullAdressWithID());
        }

        ObservableList<Console> listInstance = FXCollections.observableArrayList();
        listInstance.addAll(Console.values());
        consoleDropDown.setItems(listInstance);

        uitgeverIDDropDown.setItems(listUitgeverNames);
        genreIDDropDown.setItems(listGenreNames);
        winkelDropDown.setItems(listWinkelNames);
    }

    public void initializeUpdate(Game game){
        aantalStock.setText(Integer.toString(game.getStock()));
        kostPijs.setText(Double.toString(game.getKostPrijs()));
        naam.setText(game.getNaam());
        beschrijving.setText(game.getBeschrijving());
        aantalUitgeleend.setText(Integer.toString(game.getVerkocht()));

        GenreApi genreApi= new GenreApi(dbConnection);
        var genre = genreApi.getGenreById(game.getGenreID());
        genreIDDropDown.setValue(genre.getNaam());

        WinkelApi winkelApi = new WinkelApi(dbConnection);
        var winkel = winkelApi.getWinkelById(game.getWinkelID());
        winkelDropDown.setValue(winkel.getFullAdressWithID());

        consoleDropDown.setValue(game.getConsole());
        
        UitgeverApi uitgeverApi = new UitgeverApi(dbConnection);
        var uitgever = uitgeverApi.getUitgeverById(game.getUitgever());
        uitgeverIDDropDown.setValue(uitgever.getNaam());
        this.game = game;
    }

    private void updateGame(){
        game.setBeschrijving(this.beschrijving.getText());
        game.setConsole((Console) consoleDropDown.getValue());

        var genre = (String) genreIDDropDown.getValue();
        var genreApi = new GenreApi(dbConnection);
        game.setGenreID(genreApi.getGenreIdByName(genre));

        game.setKostPrijs(Double.parseDouble(this.kostPijs.getText()));
        game.setNaam(this.naam.getText());
        game.setStock(Integer.parseInt(this.aantalStock.getText()));
        //game.setVerkocht(Integer.parseInt(this.aantalUitgeleend.getText())); // TODO: bestaat niet?, methodes moeten aangemaakt worden

        var uitgeverName = (String) uitgeverIDDropDown.getValue();
        var uitgeverApi = new UitgeverApi(dbConnection);
        game.setUitgever(((Uitgever) uitgeverApi.getUitgeverByName(uitgeverName)).getUitgeverID());

        gameController.updateOrSearchTable(true);

        var window = (Stage) beschrijving.getScene().getWindow();
        window.close();
    }

    private boolean submitGame(){

        var aantalStock = Integer.parseInt(this.aantalStock.getText());
        Console console = (Console) consoleDropDown.getValue();
        var kostPrijs = Double.parseDouble(this.kostPijs.getText());
        var genre = (String) genreIDDropDown.getValue();
        var naam = this.naam.getText();
        var beschrijving = this.beschrijving.getText();
        Integer winkelID = null;
        Integer uitgeverID = null;
        String nameWinkel = (String) winkelDropDown.getValue();
        String nameUitgever = (String) uitgeverIDDropDown.getValue(); 

        var genreApi = new GenreApi(dbConnection);
        var genreID = genreApi.getGenreIdByName(genre);

        for (Winkel winkel : winkels) {
            if(nameWinkel.contains(winkel.getFullAdressWithID())){
                winkelID = winkel.getWinkelID();
                break;
            }
        }

        for(Uitgever uitgever: uitgevers){
            if(nameUitgever.contains(uitgever.getNaam())){
                uitgeverID = uitgever.getUitgeverID();
                break;
            }
        }
        
        //logica voor te zien of iets null is of niet 
        Game tempgame = new Game(aantalStock, 0, console, 0,winkelID, kostPrijs, genreID, naam, beschrijving, uitgeverID);
        
        try {
            var gameApi = new GameApi(dbConnection);

            gameApi.postGame(tempgame);

            gameController.updateOrSearchTable(true);

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
    
    public void setUpdate(boolean update){
        this.update = update;
    }
}
