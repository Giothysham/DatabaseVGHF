package be.kuleuven.dbproject.controller;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
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


public class BuyExtraSchermController {

    private DbConnection dbConnection;

    @FXML
    private Text amountTxt;

    @FXML
    private Button removeBtn, buyBtn;

    @FXML 
    private TableView<Extra> tblExtras;

    @FXML 
    private TableColumn<Extra, String> naamColumn;

    @FXML 
    private TableColumn<Extra, String> typeColumn;
    
    @FXML 
    private TableColumn<Extra, Double> priceColumn;

    private ExtraController parentController;

    private ArrayList<String> wantToBuyListID;

    private ArrayList<Extra> wantToBuyListExtra;

    private UserApi userApi;

    private User user;

    public void initialize(){
        naamColumn.setCellValueFactory(new PropertyValueFactory<Extra,String>("naam"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Extra,String>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Extra,Double>("kostprijs"));
        //uitgeverColumn.setCellValueFactory(new PropertyValueFactory<Extra, Uitgever>("uitgever")); //TODO: moet nog gefixt worden, ook toevoegen aan game
        removeBtn.setOnAction(e -> deleteExtra());
        buyBtn.setOnAction(e -> buyExtra(this.wantToBuyListExtra, this.user));
    }

    public void setdbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
        wantToBuyListID = new ArrayList<>();
        wantToBuyListExtra = new ArrayList<>();
    }

    public void deleteExtra(){
        Extra extra = tblExtras.getSelectionModel().getSelectedItem();
        wantToBuyListID.remove(Integer.toString(extra.getExtraID()));
        wantToBuyListExtra.remove(extra);
        parentController.setwantToRentList(wantToBuyListID);
        tblExtras.getItems().setAll(wantToBuyListExtra);

        var price = 0.0;
        for(Extra tempExtra: wantToBuyListExtra){
            price =+ tempExtra.getKostprijs();
        }
        amountTxt.setText("" + price + "$");
    }

    public void setparentController(ExtraController controller){
        this.parentController = controller;
    }

    public void setWantToRent(ArrayList<String> wantToBuyList) {
        Double price = 0.0;
        this.wantToBuyListID = wantToBuyList;

        for(String id: wantToBuyListID){
            try{
                var extraApi = new ExtraApi(dbConnection);
            
                var tempExtra = (Extra) extraApi.getExtraById(id);
                this.wantToBuyListExtra.add(tempExtra);
                price += tempExtra.getKostprijs();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        
        amountTxt.setText( " "+price + "$");

        tblExtras.getItems().setAll(this.wantToBuyListExtra);
    }

    public void buyExtra(List<Extra> wantToBuyList, User user){ //TODO: testen of verkocht +1
        try {
            userApi.createFactuurForExtra(wantToBuyList, user);
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
