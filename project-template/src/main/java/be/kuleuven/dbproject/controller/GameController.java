package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.enums.Console;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private Button gamesAdd, deleteBtn, buyBtn, gameSearchBtn, addToCartBtn;

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
    ///\omzetten naar specefieken tabelView
    //||

    private ArrayList<String> wantToRentList;

    private ArrayList<String> autoCompleteWords;

    private ArrayList<Game> listgames;

    private DbConnection dbConnection;

    public void initialize(){

        wantToRentList = new ArrayList<>();

        gamesAdd.setOnAction(e -> openNewWindow("gameaddscherm",null));
        addToCartBtn.setOnAction(e -> addToListGames());
        deleteBtn.setOnAction(e -> initTable());
        gameSearchBtn.setOnAction(e -> updateOrSearchTable(false));

        //alles met ty/catch in een deel?
        buyBtn.setOnAction(e -> {openNewWindow("buyscherm",null);});

        listgames = new ArrayList<Game>();

        dbConnection = new DbConnection();

        autoCompleteWords = new ArrayList<String>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Game,Double>("kostPrijs"));
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<Game,Integer>("stock"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));

        //name is deprecated => inittabel is meer => zien hoe optimalizeren
        //de manieren waarop gefixt => ductape geprogrameer => is bekijke samen. 
        initTable();
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

        if(update){
            listgames = (ArrayList<Game>) dbConnection.getGames();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                listgames = (ArrayList<Game>) dbConnection.SearchGamesByName(autoCompleteText);
            }
            else{
                listgames = (ArrayList<Game>) dbConnection.getGames();
            }
        }
        
        tblGames.getItems().setAll(listgames);
    }

    public void initTable() {
        listgames = (ArrayList<Game>) dbConnection.getGames();

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
        //recht streeks in onClickGame
        if(event.getClickCount() == 2 && tblGames.getSelectionModel().getSelectedItem() != null){
            Game gameSelected = tblGames.getSelectionModel().getSelectedItem();
            openNewWindow("moreinfogame",gameSelected);
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
            var root = (GridPane) loader.load();
            var controller = loader.getController();

            if(controller.getClass() == GameAddController.class){
                GameAddController gameAddController = (GameAddController) controller; 
                gameAddController.setupDropDown(dbConnection);
                gameAddController.setDbConnection(dbConnection);
                gameAddController.setParentController(this);
            }
            else if(controller.getClass() == MoreInfoGameController.class){
                MoreInfoGameController moreInfoGameController = (MoreInfoGameController) controller;
                moreInfoGameController.setdbConnection(dbConnection);
                moreInfoGameController.setGame(gameSelected);
            }
            else if(controller.getClass() == BuySchermController.class){
                BuySchermController buySchermController = (BuySchermController) controller;
                buySchermController.setdbConnection(dbConnection);
                buySchermController.setWantToRent(wantToRentList);
                buySchermController.setparentController(this);
            }

            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("budo/"+ id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }

}
