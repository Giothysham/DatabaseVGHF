package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.enums.Console;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private Button gamesAddBtn, deleteBtn, buyBtn, gameSearchBtn, addToCartBtn;

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

    private ArrayList<String> wantToRentList;

    private ArrayList<String> autoCompleteWords;

    private ArrayList<Game> listgames;

    private DbConnection dbConnection;

    private User user;

    public void initialize(){
        //______________________________________________________
        //implimenteer remove game (Done)
        //______________________________________________________
        wantToRentList = new ArrayList<>();

        gamesAddBtn.setOnAction(e -> openNewWindow("gameaddscherm",null));
        addToCartBtn.setOnAction(e -> addToListGames());
        deleteBtn.setOnAction(e -> removeSelectedGames());
        gameSearchBtn.setOnAction(e -> updateOrSearchTable(false));

        //alles met ty/catch in een deel?
        buyBtn.setOnAction(e -> {openNewWindow("buygamescherm",null);});

        listgames = new ArrayList<Game>();

        autoCompleteWords = new ArrayList<String>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Game,Double>("kostPrijs"));
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<Game,Integer>("stock"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));
    }

    private void removeSelectedGames() {
        var gameApi = new GameApi(dbConnection);
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
        //samen bekijke. 

        listgames.clear();
        tblGames.getItems().clear();
        var gameApi = new GameApi(dbConnection);

        if(update){
            listgames = (ArrayList<Game>) gameApi.getGames();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                listgames = (ArrayList<Game>) gameApi.SearchGamesByName(autoCompleteText);
            }
            else{
                listgames = (ArrayList<Game>) gameApi.getGames();
            }
        }
        
        tblGames.getItems().setAll(listgames);
    }

    public void initTable() {
        var gameApi = new GameApi(dbConnection);

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

}
