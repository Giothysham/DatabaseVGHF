package be.kuleuven.dbproject.controller;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.enums.Type;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
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


public class VerkoopbaarBuySchermController {

    private DbConnection dbConnection;

    @FXML
    private Text amountTxt;

    @FXML
    private Button removeBtn, buyBtn;

    @FXML 
    private TableView<VerkoopbaarInterface> tblGames;

    @FXML 
    private TableColumn<VerkoopbaarInterface, String> naamColumn;

    @FXML 
    private TableColumn<VerkoopbaarInterface, Console> consoleColumn;
    
    @FXML 
    private TableColumn<VerkoopbaarInterface, Double>priceColumn;

    @FXML 
    private TableColumn<VerkoopbaarInterface, Type> typeColumn;

    private VerkoopbaarController parentController;

    private ArrayList<String> wantToRentListID;

    private ArrayList<VerkoopbaarInterface> wantToRentListGame;

    private UserApi userApi;

    private User user;

    private String product;

    private VerkoopbaarApiInterface verkoopbaarApi;

    public void initialize(){
        naamColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Double>("kostPrijs"));
        
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
        VerkoopbaarInterface game = tblGames.getSelectionModel().getSelectedItem();
        wantToRentListID.remove(Integer.toString(game.getID()));
        wantToRentListGame.remove(game);
        parentController.setCheckoutList(wantToRentListID);
        tblGames.getItems().setAll(wantToRentListGame);

        var price = 0.0;
        for(VerkoopbaarInterface tempGame: wantToRentListGame){
            price =+ tempGame.getKostPrijs();
        }
        amountTxt.setText( " " + price + "$");
    }

    public void setparentController(VerkoopbaarController controller){
        this.parentController = controller;
    }

    public void setWantToRent(ArrayList<String> wantToRentList) {
        Double price = 0.0;
        this.wantToRentListID = wantToRentList;

        for(String id: wantToRentListID){
            try{
                if(product == "Game"){
                    verkoopbaarApi = new GameApi(dbConnection);
                }

                else if (product == "Extra"){
                    verkoopbaarApi = new ExtraApi(dbConnection);
                }
                
                var tempGame = verkoopbaarApi.getVerkoopbaarById(id);
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

    public void buyGames(List<VerkoopbaarInterface> wantToRentList, User user){
        try {
            if(wantToRentList.get(0).getClass().isAssignableFrom(Game.class)){
                
                var tempList = new ArrayList<Game>();

                for(VerkoopbaarInterface verkoopbaar: wantToRentList){
                    tempList.add((Game) verkoopbaar);
                }

                user.addToListGames(tempList);
            }
            userApi.createFactuurForVerkoopbaar(wantToRentList, user);
            var window = (Stage) removeBtn.getScene().getWindow();
            parentController.updateOrSearchTable(true);
            parentController.setCheckoutList(new ArrayList<>());
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

    public void setProduct(String product) {
        this.product = product;

        if(product == "Game"){
            TableColumn<VerkoopbaarInterface,Console> consoleColumn = new TableColumn<>("console");
            consoleColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Console>("console"));
            tblGames.getColumns().add(consoleColumn);
        } 
        else if(product == "Extra"){
            TableColumn<VerkoopbaarInterface,Type> typeColumn = new TableColumn<>("type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Type>("type"));
            tblGames.getColumns().add(typeColumn);
        }
    }
}
