package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private Button gamesAdd, deleteBtn, buyBtn, gameSearchBtn;

    @FXML
    private GameAddController gameAddController;

    @FXML
    private TextField autoCompleteSearch;

    @FXML
    private TableView tblGames;

    private ArrayList<Game> wantToRentList;

    private ArrayList<String> autoCompleteWords;

    private ArrayList<Game> listgames;

    private DbConnection dbConnection;

    public void initialize(){

        gamesAdd.setOnAction(e -> openNewWindow("gameaddscherm"));
        deleteBtn.setOnAction(e -> initTable());
        gameSearchBtn.setOnAction(e -> updateOrSearchTable(false));

        //alles met ty/catch in een deel?
        buyBtn.setOnAction(e -> {
            try {
                buyGames();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        listgames = new ArrayList<Game>();

        wantToRentList = new ArrayList<Game>();

        dbConnection = new DbConnection();

        autoCompleteWords = new ArrayList<String>();

        //name is deprecated => inittabel is meer => zien hoe optimalizeren
        //de manieren waarop gefixt => ductape geprogrameer => is bekijke samen. 
        initTable();
    }

    public void updateOrSearchTable(Boolean update){
        //samen bekijke. 

        listgames.clear();
        tblGames.getItems().clear();
        tblGames.getColumns().clear();

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

        int colIndex = 0;
        for(var colName : new String[]{"name", "price", "avaible","console","ID"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            
            tblGames.getColumns().add(col);
            colIndex++;
        }

        for(Game game : listgames) {
            tblGames.getItems().add(FXCollections.observableArrayList(game.getNaam() + "",game.getKostPrijs() + "",game.getStock() + "", game.getConsole().toString(), game.getGameID() + ""));
        }
    }

    public void buyGames() throws IOException{
        //zien hoe beter kan => implimenteren
        List itemList = (List) tblGames.getSelectionModel().getSelectedItem();

        var wantToRentList = tblGames.getSelectionModel().getSelectedItems();

        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource("buyscherm.fxml"));
        var root = (GridPane) loader.load();
        BuySchermController BuyScherm = (BuySchermController) loader.getController();

        BuyScherm.setdbConnection(dbConnection);
        BuyScherm.setWantToRent(wantToRentList);

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ tblGames.getSelectionModel().getSelectedItem());
        stage.initOwner(ProjectMain.getRootStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void initTable() {
        listgames.clear();
        tblGames.getItems().clear();
        tblGames.getColumns().clear();

        listgames = (ArrayList<Game>) dbConnection.getGames();

        for(Game game: listgames){
            if(!autoCompleteWords.contains(game.getNaam())){
                autoCompleteWords.add(game.getNaam());
            }
        }

        TextFields.bindAutoCompletion(autoCompleteSearch, autoCompleteWords);

        tblGames.setOnMouseClicked(mouseEvent -> {
            try {
                onClickGame(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tblGames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        int colIndex = 0;
        for(var colName : new String[]{"name", "price", "avaible","console","ID"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            
            tblGames.getColumns().add(col);
            colIndex++;
        }

        for(Game game : listgames) {
            tblGames.getItems().add(FXCollections.observableArrayList(game.getNaam() + "",game.getKostPrijs() + "",game.getStock() + "", game.getConsole().toString(), game.getGameID() + ""));
        }
    }

    @FXML
    public void onClickGame(MouseEvent event) throws IOException{

        if(event.getClickCount() == 2 && tblGames.getSelectionModel().getSelectedItem() != null){
            List itemList = (List) tblGames.getSelectionModel().getSelectedItem();

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource("moreinfogame.fxml"));
            var root = (GridPane) loader.load();
            MoreInfoGameController moreInfoGameController = (MoreInfoGameController) loader.getController();

            moreInfoGameController.setdbConnection(dbConnection);
            moreInfoGameController.setGame(itemList.get(4).toString());

            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("budo/"+ tblGames.getSelectionModel().getSelectedItem());
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
    }

    public void openNewWindow(String id){
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = (GridPane) loader.load();
            GameAddController gameAddController = (GameAddController) loader.getController(); 

            //vragne of dit cava is => het is cava ook zo bij andere doen!!
            gameAddController.setupDropDown(dbConnection);
            gameAddController.setDbConnection(dbConnection);
            gameAddController.setParentController(this);

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
