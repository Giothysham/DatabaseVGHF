package be.kuleuven.dbproject.controller;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class BuyGameSchermController {

    private DbConnection dbConnection;

    @FXML
    private Text amountTxt;

    @FXML
    private Button removeBtn, buyBtn;

    @FXML 
    private TableView<Game> tblGames;

    @FXML 
    private TableColumn<Game, String> naamColumn;

    @FXML 
    private TableColumn<Game, Console> consoleColumn;
    
    @FXML 
    private TableColumn<Game, Double>priceColumn;

    private GameController parentController;

    private ArrayList<String> wantToRentListID;

    private ArrayList<Game> wantToRentListGame;

    private UserApi userApi;

    private User user;

    public void initialize(){
        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Game,Double>("kostPrijs"));
        //uitgeverColumn.setCellValueFactory(new PropertyValueFactory<Extra, Uitgever>("uitgever")); //TODO: moet nog gefixt worden, ook toevoegen aan game, extra & buy extra
                                                                                                     //verwarrend, innerjoin nodig met naam? want alleen ID gegeven en niet naam (ook aan game toepassen)
        removeBtn.setOnAction(e -> deleteGame());
        buyBtn.setOnAction(e -> buyGames(this.wantToRentListGame, this.user));
    }

    public void setdbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
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
        amountTxt.setText( " " + price + "$");
    }

    public void setparentController(GameController controller){
        this.parentController = controller;
    }

    public void setWantToRent(ArrayList<String> wantToRentList) {
        Double price = 0.0;
        this.wantToRentListID = wantToRentList;

        for(String id: wantToRentListID){
            try{
                var gameApi = new GameApi(dbConnection);
            
                var tempGame = (Game) gameApi.getGameById(id);
                this.wantToRentListGame.add(tempGame);
                price += tempGame.getKostPrijs();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        
        amountTxt.setText( " "+price + "$");

        tblGames.getItems().setAll(this.wantToRentListGame);
    }

    public void buyGames(List<Game> wantToRentList, User user){
        try {
            user.addToListGames(wantToRentList);
            userApi.createFactuurForGame(wantToRentList, user);
            var window = (Stage) removeBtn.getScene().getWindow();
            parentController.updateOrSearchTable(true);
            parentController.setwantToRentList(new ArrayList<>());
            window.close();
        } catch (Exception e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
            e.printStackTrace();
        }
    }

    public void setUser(User user){
        this.user = user;
    }
}
