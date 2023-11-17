package be.kuleuven.dbproject.controller;

import java.util.ArrayList;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


public class BuySchermController {

    private DbConnection dbConnection;

    @FXML
    private Text amountTxt;

    @FXML
    private Button removeBtn;

    @FXML 
    private TableView<Game> tblGames;

    @FXML 
    private TableColumn<Game, String> naamColumn;

    @FXML 
    private TableColumn<Game, Console> consoleColumn;
    
    @FXML 
    private TableColumn<Game, Double>priseColumn;

    private GameController parentController;

    private ArrayList<String> wantToRentListID;

    private ArrayList<Game> wantToRentListGame;

    public void initialize(){
        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));
        priseColumn.setCellValueFactory(new PropertyValueFactory<Game,Double>("kostPrijs"));
        removeBtn.setOnAction(e -> deleteGame());
    }

    public void setdbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
        wantToRentListID = new ArrayList<>();
        wantToRentListGame = new ArrayList<>();
    }

    public void deleteGame(){
        Game game = tblGames.getSelectionModel().getSelectedItem();
        wantToRentListID.remove(Integer.toString(game.getGameID()));
        wantToRentListGame.remove(game);
        parentController.setwantToRentList(wantToRentListID);
        tblGames.getItems().setAll(wantToRentListGame);

        var price = 0.0;
        for(Game tempGame: wantToRentListGame){
            price =+ tempGame.getKostPrijs();
        }
        amountTxt.setText( " "+price + "$");
    }

    public void setparentController(GameController controller){
        this.parentController = controller;
    }

    public void setWantToRent(ArrayList<String> wantToRentList) {
        Double price = 0.0;
        this.wantToRentListID = wantToRentList;

        for(String id: wantToRentListID){
            var tempGame = (Game) dbConnection.getGameById(id);
            this.wantToRentListGame.add(tempGame);
            price += tempGame.getKostPrijs();
        }
        
        amountTxt.setText( " "+price + "$");

        tblGames.getItems().setAll(this.wantToRentListGame);
    }
}
