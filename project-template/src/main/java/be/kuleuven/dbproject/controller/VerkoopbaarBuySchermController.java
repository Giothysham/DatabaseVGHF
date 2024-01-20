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
    private TableView<VerkoopbaarInterface> tblVerkoopbaar;

    @FXML 
    private TableColumn<VerkoopbaarInterface, String> naamColumn;

    @FXML 
    private TableColumn<VerkoopbaarInterface, Console> consoleColumn;
    
    @FXML 
    private TableColumn<VerkoopbaarInterface, Double>priceColumn;

    @FXML 
    private TableColumn<VerkoopbaarInterface, Type> typeColumn;

    private VerkoopbaarController parentController;

    private ArrayList<String> checkoutListID;

    private ArrayList<VerkoopbaarInterface> checkoutListVerkoopbaar;

    private UserApi userApi;

    private User user;

    private VerkoopbaarInterface verkoopbaar;

    private VerkoopbaarApiInterface verkoopbaarApi;

    public void initialize(){
        naamColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Double>("kostPrijs"));
        
        removeBtn.setOnAction(e -> deleteVerkoopbaar());
        buyBtn.setOnAction(e -> buyVerkoopbaar(this.checkoutListVerkoopbaar, this.user));
    }

    public void setdbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
        checkoutListID = new ArrayList<>();
        checkoutListVerkoopbaar = new ArrayList<>();
    }

    public void deleteVerkoopbaar(){
        VerkoopbaarInterface Verkoopbaar = tblVerkoopbaar.getSelectionModel().getSelectedItem();
        checkoutListID.remove(Integer.toString(Verkoopbaar.getID()));
        parentController.setCheckoutList(checkoutListID);
        checkoutListVerkoopbaar.clear();
        populateListAndSetPrice(checkoutListID);
        tblVerkoopbaar.getItems().setAll(checkoutListVerkoopbaar);
    }

    public void setparentController(VerkoopbaarController controller){
        this.parentController = controller;
    }

    public void setWantToRent(ArrayList<String> wantToRentList) {
        this.checkoutListID = wantToRentList;

        if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
            verkoopbaarApi = new GameApi(dbConnection, user);
        }

        else if (verkoopbaar.getClass().isAssignableFrom(Extra.class)){
            verkoopbaarApi = new ExtraApi(dbConnection, user);
        }

        populateListAndSetPrice(wantToRentList);

        tblVerkoopbaar.getItems().setAll(this.checkoutListVerkoopbaar);
    }
    
    private void populateListAndSetPrice(ArrayList<String> wantToRentList){
        Double price = 0.0;

        for(String id: checkoutListID){
            try{
                if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
                    verkoopbaarApi = new GameApi(dbConnection, user);
                }

                else if (verkoopbaar.getClass().isAssignableFrom(Extra.class)){
                    verkoopbaarApi = new ExtraApi(dbConnection, user);
                }
                
                var tempVerkoopbaar = verkoopbaarApi.getVerkoopbaarById(id);
                this.checkoutListVerkoopbaar.add(tempVerkoopbaar);
                price += tempVerkoopbaar.getKostPrijs();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }

        amountTxt.setText( " "+price + "$");
    }

    public void buyVerkoopbaar(List<VerkoopbaarInterface> wantToRentList, User user){
        try {
            userApi.createFactuurForVerkoopbaar(wantToRentList, user);

            // if(wantToRentList.get(0).getClass().isAssignableFrom(Game.class)){
            //     var tempList = new ArrayList<Game>();

            //     for(VerkoopbaarInterface verkoopbaar: wantToRentList){
            //         tempList.add((Game) verkoopbaar);
            //     }

            //     user.addToListGames(tempList);
            // } TODO: gebeurt nu in factuur functie hierboven, hopelijk is dit niet erg (anders wordt het nooit gecommit, zie inner join)

            var window = (Stage) removeBtn.getScene().getWindow();
            parentController.updateOrSearchTable(true);
            parentController.setCheckoutList(new ArrayList<>());
            parentController.removeFilters();
            window.close();
        } 
        catch (Exception e) {
            var window = (Stage) removeBtn.getScene().getWindow();
            window.close();
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
            e.printStackTrace();
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setProduct(VerkoopbaarInterface product) {
        verkoopbaar = product;

        if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
            TableColumn<VerkoopbaarInterface,Console> consoleColumn = new TableColumn<>("console");
            consoleColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Console>("console"));
            tblVerkoopbaar.getColumns().add(consoleColumn);
        } 
        else if(verkoopbaar.getClass().isAssignableFrom(Extra.class)){
            TableColumn<VerkoopbaarInterface,Type> typeColumn = new TableColumn<>("type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Type>("type"));
            tblVerkoopbaar.getColumns().add(typeColumn);
        }
    }
}
