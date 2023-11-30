package be.kuleuven.dbproject.controller;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GameApi;
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

import java.util.List;

public class UitgeleendeGameController {

    @FXML
    private Button gameSearchBtn, returnBtn;

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
    private TableView<Game> tblUitgeleendeGames;

    private ArrayList<String> autoCompleteWords;

    private List<Game> listgames;

    private DbConnection dbConnection;

    private User user;

    public void initialize(){

        gameSearchBtn.setOnAction(e -> updateOrSearchTable(false));

        //addToCartBtn.setOnAction(e -> addToListGames()); return button

        autoCompleteWords = new ArrayList<String>();

        listgames = new ArrayList<Game>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Game,Double>("kostPrijs"));
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<Game,Integer>("stock"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));

        //name is deprecated => inittabel is meer => zien hoe optimalizeren
        //de manieren waarop gefixt => ductape geprogrameer => is bekijke samen. 
    }

    public void updateOrSearchTable(Boolean update){
        //samen bekijke. 
        listgames.clear();
        tblUitgeleendeGames.getItems().clear();
        var gameApi = new GameApi(dbConnection);

        if(update){
            listgames = user.getUitgeleendeGames();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
            //     listgames = (ArrayList<Game>) gameApi.SearchGamesByName(autoCompleteText);
            }
            // TODO: aan wouter fix vragen
            else{
                listgames = user.getUitgeleendeGames();
            }
        }

        tblUitgeleendeGames.getItems().setAll(listgames);
    }

    public void initTable() {
            listgames = user.getUitgeleendeGames();
            
            for(Game game: listgames){
                if(!autoCompleteWords.contains(game.getNaam())){
                    autoCompleteWords.add(game.getNaam());
                }
            }

        TextFields.bindAutoCompletion(autoCompleteSearch, autoCompleteWords);

        tblUitgeleendeGames.setOnMouseClicked(mouseEvent -> {onClickGame(mouseEvent);});

        tblUitgeleendeGames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tblUitgeleendeGames.getItems().addAll(listgames);

    }

    @FXML
    public void onClickGame(MouseEvent event) {
        //recht streeks in onClickGame
        if(event.getClickCount() == 2 && tblUitgeleendeGames.getSelectionModel().getSelectedItem() != null){
            Game gameSelected = tblUitgeleendeGames.getSelectionModel().getSelectedItem();
            openNewWindow("moreinfogame",gameSelected);
        }
    }

    public void openNewWindow(String id,Game gameSelected){
        var resourceName = id + ".fxml";
        try {

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = (GridPane) loader.load();
            var controller = loader.getController();

            if(controller.getClass() == MoreInfoGameController.class){
                MoreInfoGameController moreInfoGameController = (MoreInfoGameController) controller;
                moreInfoGameController.setdbConnection(dbConnection);
                moreInfoGameController.setGame(gameSelected);
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

    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

   public void setUser(User user){
        this.user = user;
        initTable();
    }
}
