package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
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

public class ExtraController {

    @FXML
    private Button extraAddBtn, deleteBtn, buyBtn, extraSearchBtn, addToCartBtn;

    @FXML
    private GameAddController extraAddController; //aanpassen naar extra add controller 

    @FXML
    private TextField autoCompleteSearch;

    @FXML
    private TableColumn<Extra,String> naamColumn;

    @FXML
    private TableColumn<Extra,Double> priceColumn;

    @FXML
    private TableColumn<Extra,Integer> avaibleColumn;

    @FXML
    private TableColumn<Extra,String> typeColumn;

    @FXML
    private TableColumn<Extra,Uitgever> uitgeverColumn;

    @FXML
    private TableView<Extra> tblExtras;

    private ArrayList<String> wantToBuyList; //no clue wat dees is 

    private ArrayList<String> autoCompleteWords;

    private ArrayList<Extra> listExtra;

    private DbConnection dbConnection;

    private User user;

    public void initialize(){
        //______________________________________________________
        //implimenteer remove extra (Done)
        //______________________________________________________
        wantToBuyList = new ArrayList<>();

        extraAddBtn.setOnAction(e -> openNewWindow("buyextrascherm", null));
        addToCartBtn.setOnAction(e -> addToListExtra());
        deleteBtn.setOnAction(e -> removeSelectedExtras());
        extraSearchBtn.setOnAction(e -> updateOrSearchTable(false));

        //alles met try/catch in een deel?
        buyBtn.setOnAction(e -> {openNewWindow("buyextrascherm",null);});

        listExtra = new ArrayList<Extra>();

        autoCompleteWords = new ArrayList<String>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Extra, String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Extra,Double>("kostprijs"));//TODO: laat alleen int zien, niet sure why
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<Extra,Integer>("stock"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Extra,String>("type"));
        //uitgeverColumn.setCellValueFactory(new PropertyValueFactory<Extra, Uitgever>("uitgever")); //TODO: moet nog gefixt worden, ook toevoegen aan game
    }

    private void removeSelectedExtras() {
        var extraApi = new ExtraApi(dbConnection);
        var tempList = tblExtras.getSelectionModel().getSelectedItems();
        
        extraApi.deleteExtra(tempList);
        
        updateOrSearchTable(true);
    }

    public void addToListExtra(){
        var tempList = tblExtras.getSelectionModel().getSelectedItems();

        for (int i = 0; i<tempList.size(); i++) {
            //vragen of dit hard code cava is. 
            var extraID = ((Extra) tempList.get(i)).getExtraID();
            wantToBuyList.add(Integer.toString(extraID));
        }
    }

    public void updateOrSearchTable(Boolean update){
        //samen bekijke. 

        listExtra.clear();
        tblExtras.getItems().clear();
        var extraApi = new ExtraApi(dbConnection);

        if(update){
            listExtra = (ArrayList<Extra>) extraApi.getExtras();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                listExtra = (ArrayList<Extra>) extraApi.SearchExtraByName(autoCompleteText);
            }
            else{
                listExtra = (ArrayList<Extra>) extraApi.getExtras();
            }
        }
        
        tblExtras.getItems().setAll(listExtra);
    }

    public void initTable() {
        var extraApi = new ExtraApi(dbConnection);

        listExtra = (ArrayList<Extra>) extraApi.getExtras();

        for(Extra extra: listExtra){
            if(!autoCompleteWords.contains(extra.getNaam())){
                autoCompleteWords.add(extra.getNaam());
            }
        }

        TextFields.bindAutoCompletion(autoCompleteSearch, autoCompleteWords);

        tblExtras.setOnMouseClicked(mouseEvent -> {onClickGame(mouseEvent);});

        tblExtras.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tblExtras.getItems().addAll(listExtra);

    }

    @FXML
    public void onClickGame(MouseEvent event) {
        if(event.getClickCount() == 2 && tblExtras.getSelectionModel().getSelectedItem() != null){
            Extra extraSelected = tblExtras.getSelectionModel().getSelectedItem();
            switch(user.getBevoegdheid()){
                case 1:
                    openNewWindow("gameaddscherm", extraSelected);
                break;

                case 0:
                    openNewWindow("moreinfogame",extraSelected);
                break;
            }
        }
    }

    public void setwantToRentList(ArrayList<String> wantToBuyList){
        this.wantToBuyList = wantToBuyList;
    }

    public void openNewWindow(String id, Extra extraSelected){
        var resourceName = id + ".fxml";
        try {

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();
            var controller = loader.getController();
        //     //TODO: mag uit commentaar wanneer klassen worden aangemaakt of classenaam aanpassen naar algemeen en daar in steken
        //     if(controller.getClass() == ExtraAddController.class){
        //         ExtraAddController extraAddController = (ExtraAddController) controller; 
        //         extraAddController.setupDropDown(dbConnection);
        //         extraAddController.setDbConnection(dbConnection);
        //         extraAddController.setParentController(this);
                
        //         //iets beter maken game != null is geen goede opl
        //         if(user.getBevoegdheid() == 1 && extraSelected != null){
        //             extraAddController.setUpdate(true);
        //             extraAddController.initializeUpdate(extraSelected);
        //         }else{
        //             extraAddController.setUpdate(false);
        //         }
        //     }
        //     else if(controller.getClass() == MoreInfoExtraController.class){
        //         MoreInfoExtraController moreInfoExtraController = (MoreInfoExtraController) controller;
        //         moreInfoExtraController.setdbConnection(dbConnection);
        //         moreInfoExtraController.setGame(extraSelected);
        //     }
            if(controller.getClass() == BuyExtraSchermController.class){
                BuyExtraSchermController buyExtraSchermController = (BuyExtraSchermController) controller;
                buyExtraSchermController.setdbConnection(dbConnection);
                buyExtraSchermController.setWantToRent(wantToBuyList);
                buyExtraSchermController.setparentController(this);
                buyExtraSchermController.setUser(user);
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
            extraAddBtn.setDisable(false);
            deleteBtn.setDisable(false);
        }
        else{
            extraAddBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

}

