package be.kuleuven.dbproject.controller;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
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

import java.util.List;

public class UitgeleendeGameController {

    @FXML
    private Button searchBtn, returnBtn;

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

        searchBtn.setOnAction(e -> updateOrSearchTable(false));

        returnBtn.setOnAction(e -> returnGame());

        autoCompleteWords = new ArrayList<String>();

        listgames = new ArrayList<Game>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));

        //name is deprecated => inittabel is meer => zien hoe optimalizeren
        //de manieren waarop gefixt => ductape geprogrameer => is bekijke samen. 
    }

    private void returnGame() {
        var tempList = tblUitgeleendeGames.getSelectionModel().getSelectedItems();
        var userApi = new UserApi(dbConnection);

        if(tempList.size() > 0){
            var uitgeleendeGames = user.getUitgeleendeGames();

            for (int i = 0; i<tempList.size(); i++) {
                //vragen of dit hard code cava is. 
                var gameID = ((Game) tempList.get(i)).getID();
                for(Game game: uitgeleendeGames){
                    if(gameID == game.getID()){
                        game.setStock(game.getStock() + 1);
                        uitgeleendeGames.remove(game);
                        break;
                    }
                }
            }
            userApi.updateUser(user);
        } 
        updateOrSearchTable(false);
    }

    public void updateOrSearchTable(Boolean update){
        tblUitgeleendeGames.getItems().clear();

        if(update){
            listgames = user.getUitgeleendeGames();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                // var templist = user.getUitgeleendeGames();
                // for(Game uit: templist){
                //     System.out.println("gothere 1--------------------------------------------");
                //     if(uit.getNaam().contains(autoCompleteText)){
                //         System.out.println("gothere 2--------------------------------------------");
                //         listgames.add(uit);
                //     }
                // } WAYO?
            }
            // TODO: aan wouter fix vragen => nog een bekijken wat dit is
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

        tblUitgeleendeGames.setOnMouseClicked(mouseEvent -> {onClickVerkoopbaar(mouseEvent);});

        tblUitgeleendeGames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tblUitgeleendeGames.getItems().addAll(listgames);

    }

    @FXML
    public void onClickVerkoopbaar(MouseEvent event) {
        if(event.getClickCount() == 2 && tblUitgeleendeGames.getSelectionModel().getSelectedItem() != null){
            Game gameSelected = tblUitgeleendeGames.getSelectionModel().getSelectedItem();
            openNewWindow("moreinfoverkoopbaar",gameSelected);
        }
    }

    public void openNewWindow(String id,Game gameSelected){
        var resourceName = id + ".fxml";
        try {

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();
            var controller = loader.getController();

            if(controller.getClass() == VerkoopbaarMoreInfoController.class){
                VerkoopbaarMoreInfoController verkoopbaarMoreInfoController = (VerkoopbaarMoreInfoController) controller;
                verkoopbaarMoreInfoController.setdbConnection(dbConnection);
                verkoopbaarMoreInfoController.setVerkoopbaar(gameSelected);
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
    }

   public void setUser(User user){
        this.user = user;
        initTable();
    }
}
