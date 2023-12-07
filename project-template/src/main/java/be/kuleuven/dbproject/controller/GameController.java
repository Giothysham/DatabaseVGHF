package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.VisualFilter;
import be.kuleuven.dbproject.interfaces.BuyScreenInterface;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import be.kuleuven.dbproject.model.enums.Console;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameController implements BuyScreenInterface{

    @FXML
    private Menu consoleMenu, winkelMenu, genreMenu;

    @FXML
    private Button gamesAddBtn, deleteBtn, buyBtn, gameSearchBtn, addToCartBtn; //filterBtn;

    @FXML
    private GameAddController gameAddController;

    @FXML
    private TextField autoCompleteSearch;

    @FXML
    private TableColumn<Game,String> naamColumn;

    @FXML
    private TableColumn<Game,Double> priceColumn;

    @FXML
    private TableColumn<Game,Integer> avaibleColumn;

    @FXML
    private TableColumn<Game,Console> consoleColumn;

    @FXML
    private TableView<Game> tblGames;

    @FXML
    private HBox scrlPaneFilters;

    private ArrayList<VisualFilter> visualFilters;

    private ArrayList<String> wantToRentList;

    private ArrayList<String> autoCompleteWords;

    private ArrayList<Game> listgames;

    private DbConnection dbConnection;

    private User user;

    private GameApi gameApi;

    public void initialize(){
        //______________________________________________________
        //implimenteer remove game (Done)
        //TODO => Mauro zoek uit voor filteren op winkel of console of andere. 
        //______________________________________________________
        wantToRentList = new ArrayList<>();

        gamesAddBtn.setOnAction(e -> openNewWindow("gameaddscherm",null));
        addToCartBtn.setOnAction(e -> addToListGames());
        deleteBtn.setOnAction(e -> removeSelectedGames());
        gameSearchBtn.setOnAction(e -> updateOrSearchTable(false));
        //filterBtn.setOnAction(e -> openNewWindow("filterscherm", null));

        gameSearchBtn.setMaxWidth(Double.MAX_VALUE);
        //filterBtn.setMaxWidth(Double.MAX_VALUE);

        //alles met ty/catch in een deel?
        buyBtn.setOnAction(e -> {openNewWindow("buygamescherm",null);});

        listgames = new ArrayList<Game>();

        autoCompleteWords = new ArrayList<String>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Game,Double>("kostPrijs"));
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<Game,Integer>("stock"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));

        visualFilters = new ArrayList<VisualFilter>();

        //Media media = new Media("out-attach-dubbelklik-op-mij.mp3");  
    }

    private void removeSelectedGames() {
        var tempList = tblGames.getSelectionModel().getSelectedItems();
        
        gameApi.deleteGame(tempList);
        
        updateOrSearchTable(true);
    }

    public void addToListGames(){
        var tempList = tblGames.getSelectionModel().getSelectedItems();

        for (int i = 0; i<tempList.size(); i++) {
            //vragen of dit hard code cava is. 
            var gameID = ((Game) tempList.get(i)).getGameID();
            wantToRentList.add(Integer.toString(gameID));
        }
    }

    public void updateOrSearchTable(Boolean update){

        listgames.clear();
        tblGames.getItems().clear();

        if(update){
            listgames = (ArrayList<Game>) gameApi.getGames();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                listgames = (ArrayList<Game>) gameApi.searchGamesByFilters(autoCompleteText);
            }
            else{
                listgames = (ArrayList<Game>) gameApi.searchGamesByFilters(null);
            }
        }
        
        tblGames.getItems().setAll(listgames);
    }

    public void initTable() {
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);

        listgames = (ArrayList<Game>) gameApi.getGames();

        for(Game game: listgames){
            if(!autoCompleteWords.contains(game.getNaam())){
                autoCompleteWords.add(game.getNaam());
            }
        }

        TextFields.bindAutoCompletion(autoCompleteSearch, autoCompleteWords);

        tblGames.setOnMouseClicked(mouseEvent -> {onClickGame(mouseEvent);});

        tblGames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tblGames.getItems().addAll(listgames);

    }

    @FXML
    public void onClickGame(MouseEvent event) {
        if(event.getClickCount() == 2 && tblGames.getSelectionModel().getSelectedItem() != null){
            Game gameSelected = tblGames.getSelectionModel().getSelectedItem();
            switch(user.getBevoegdheid()){
                case 1:
                    openNewWindow("gameaddscherm", gameSelected);
                    
                break;

                case 0:
                    openNewWindow("moreinfogame",gameSelected);
                break;
            }
        }
    }

    public void setwantToRentList(ArrayList<String> wantToRentList){
        this.wantToRentList = wantToRentList;
    }

    public void openNewWindow(String id,Game gameSelected){
        var resourceName = id + ".fxml";
        try {

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();
            var controller = loader.getController();

            if(controller.getClass() == GameAddController.class){
                GameAddController gameAddController = (GameAddController) controller; 
                gameAddController.setupDropDown(dbConnection);
                gameAddController.setDbConnection(dbConnection);
                gameAddController.setParentController(this);
                
                //iets beter maken game != null is geen goede opl
                if(user.getBevoegdheid() == 1 && gameSelected != null){
                    gameAddController.setUpdate(true);
                    gameAddController.initializeUpdate(gameSelected);
                }else{
                    gameAddController.setUpdate(false);
                }
            }
            else if(controller.getClass() == MoreInfoGameController.class){
                MoreInfoGameController moreInfoGameController = (MoreInfoGameController) controller;
                moreInfoGameController.setdbConnection(dbConnection);
                moreInfoGameController.setGame(gameSelected);
            }
            else if(controller.getClass() == BuyGameSchermController.class){
                BuyGameSchermController buyGameSchermController = (BuyGameSchermController) controller;
                buyGameSchermController.setdbConnection(dbConnection);
                buyGameSchermController.setWantToRent(wantToRentList);
                buyGameSchermController.setparentController(this);
                buyGameSchermController.setUser(user);
            }
            else if(controller.getClass() == FilterSchermController.class){
                FilterSchermController filterSchermController = (FilterSchermController) controller;
                filterSchermController.setParentController(this);
                filterSchermController.setUpFilters(dbConnection, gameApi);
            }

            var scene = new Scene((Parent) root);
            stage.setScene(scene);
            stage.setTitle("budo/"+ id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }

    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
        gameApi = new GameApi(dbConnection);
        initTable();
    }

    public void setUser(User user){
        this.user = user;
        if(user.getBevoegdheid() == 1){
            gamesAddBtn.setDisable(false);
            deleteBtn.setDisable(false);
        }
        else{
            gamesAddBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

    public void setUpFilters(){
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);

        for(Console console: Console.values()){
            var menuItem = new MenuItem(console.name());
            menuItem.setOnAction(e ->{
                gameApi.creatSearchQuerry((console));
                gameApi.searchGamesByFilters(null);
                this.updateOrSearchTable(false);
                addFilterToPane(console);
            });
            consoleMenu.getItems().add(menuItem);
        }   

        for(Winkel winkel: winkelApi.getWinkels()){
            var menuItem = new MenuItem(winkel.getFullAdressWithID());
            menuItem.setOnAction(e -> {
                gameApi.creatSearchQuerry(winkel);
                gameApi.searchGamesByFilters(null);
                this.updateOrSearchTable(false);
                addFilterToPane(winkel);
            });
            winkelMenu.getItems().add(menuItem);
        }

        for(Genre genre: genreApi.getGenres()){
            var menuItem = new MenuItem(genre.getNaam());
            menuItem.setOnAction(e -> {
                gameApi.creatSearchQuerry(genre);
                gameApi.searchGamesByFilters(null);
                this.updateOrSearchTable(false);
                addFilterToPane(genre);
            });
            genreMenu.getItems().add(menuItem);
        }
    }

    private <T> void addFilterToPane(T filter){
        if(gameApi.getSearchConsole() != null){
            searchAndDeleteVisualFilterByType(filter);
        }
        else if(gameApi.getSearchGenre() != null){
            searchAndDeleteVisualFilterByType(filter);
        }
        else if(gameApi.getSearchWinkel() != null){
            searchAndDeleteVisualFilterByType(filter);
        }

        var visualFilter = new VisualFilter<>(filter);
        var visualFilterHbox = visualFilter.getVisualFilter(gameApi, this);

        visualFilters.add(visualFilter);

        scrlPaneFilters.getChildren().add(visualFilterHbox);   
    }

    public <T> void searchAndDeleteVisualFilterByType(T filter){
        for(VisualFilter visualFilter: visualFilters){
            if(visualFilter.getUsedFilter().getClass() == filter.getClass()){
                scrlPaneFilters.getChildren().remove(visualFilter.getVisualFilterHbox());
                visualFilters.remove(visualFilter);
                break;
            }
        }
    }

    public HBox getScrlPaneFilters(){
        return this.scrlPaneFilters;
    }

}
