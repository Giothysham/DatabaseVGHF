package be.kuleuven.dbproject.controller;


import java.util.List;

import be.kuleuven.dbproject.ProjectMain;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameAddController {

    @FXML
    private Button submitGameBtn, viewGamePageBtn;

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

        viewGamePageBtn.setOnAction(e -> changeWindowToMoreInfo("moreinfogame"));

        viewGamePageBtn.setDisable(update);

        beschrijving.setWrapText(true);
    }

    public void changeWindowToMoreInfo(String id){
        try{
            var resourceName = id+".fxml";

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();
            var controller = (MoreInfoGameController) loader.getController();

            controller.setdbConnection(dbConnection);
            controller.setGame(game);

            var scene = new Scene((Parent) root);
            stage.setScene(scene);
            stage.setTitle("budo/"+ id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

        genreIDDropDown.setValue(game.getGenre().getNaam());

        winkelDropDown.setValue(game.getWinkel().getFullAdressWithID());

        consoleDropDown.setValue(game.getConsole());
        
        uitgeverIDDropDown.setValue(game.getUitgever().getNaam());
        this.game = game;
    }

    private void updateGame(){ //TODO: aantal verkocht is niet het enige dat niet aangepast kan worden (vb winkel), checken, implementeren of verwijderen
        game.setBeschrijving(this.beschrijving.getText());
        game.setConsole((Console) consoleDropDown.getValue());

        var genre = (String) genreIDDropDown.getValue();
        var genreApi = new GenreApi(dbConnection);
        game.setGenre(genreApi.getGenreByName(genre));

        game.setKostPrijs(Double.parseDouble(this.kostPijs.getText()));
        game.setNaam(this.naam.getText());
        game.setStock(Integer.parseInt(this.aantalStock.getText()));
        
        var uitgeverName = (String) uitgeverIDDropDown.getValue();
        var uitgeverApi = new UitgeverApi(dbConnection);
        game.setUitgever(((Uitgever) uitgeverApi.getUitgeverByName(uitgeverName)));

        gameController.updateOrSearchTable(false);

        var window = (Stage) beschrijving.getScene().getWindow();
        window.close();
    }

    private boolean submitGame(){

        var aantalStock = Integer.parseInt(this.aantalStock.getText());
        Console console = (Console) consoleDropDown.getValue();
        var kostPrijs = Double.parseDouble(this.kostPijs.getText());
        var genreNaam = (String) genreIDDropDown.getValue();
        var naam = this.naam.getText();
        var beschrijving = this.beschrijving.getText();
        Winkel winkel = null;
        Uitgever uitgever = null;
        String nameWinkel = (String) winkelDropDown.getValue();
        String nameUitgever = (String) uitgeverIDDropDown.getValue(); 

        var genreApi = new GenreApi(dbConnection);
        var genre = genreApi.getGenreByName(genreNaam);

        for (Winkel testwinkel : winkels) {
            if(nameWinkel.contains(testwinkel.getFullAdressWithID())){
                winkel = testwinkel;
                break;
            }
        } 

        for(Uitgever testuitgever: uitgevers){
            if(nameUitgever.contains(testuitgever.getNaam())){
                uitgever = testuitgever;
                break;
            }
        }
        
        //logica voor te zien of iets null is of niet 
        Game tempgame = new Game(aantalStock, 0, console, 0,winkel, kostPrijs, genre, naam, beschrijving, uitgever);
        
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
