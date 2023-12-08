package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.VisualFilter;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import be.kuleuven.dbproject.model.enums.Type;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import be.kuleuven.dbproject.interfaces.BuyScreenInterface;

public class ExtraController implements BuyScreenInterface {

    @FXML
    private Menu typeMenu, winkelMenu;

    @FXML
    private Button extraAddBtn, deleteBtn, buyBtn, extraSearchBtn, addToCartBtn;

    @FXML
    private GameAddController extraAddController; 

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
    private TableView<Extra> tblExtras;

    @FXML
    private HBox scrlPaneFilters;

    private ArrayList<String> wantToBuyList;

    private ArrayList<String> autoCompleteWords;

    private ArrayList<Extra> listExtra;

    private DbConnection dbConnection;

    private User user;

    private ArrayList<VisualFilter> visualFilters;

    private ExtraApi extraApi;

    public void initialize(){
        //______________________________________________________
        //implimenteer remove extra (Done)
        //______________________________________________________
        wantToBuyList = new ArrayList<>();

        extraAddBtn.setOnAction(e -> openNewWindow("extraaddscherm", null));
        addToCartBtn.setOnAction(e -> addToListExtra());
        deleteBtn.setOnAction(e -> removeSelectedExtras());
        extraSearchBtn.setOnAction(e -> updateOrSearchTable(false));

        extraSearchBtn.setMaxWidth(Double.MAX_VALUE);
        //alles met try/catch in een deel?
        buyBtn.setOnAction(e -> {openNewWindow("buyextrascherm",null);});

        listExtra = new ArrayList<Extra>();

        autoCompleteWords = new ArrayList<String>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<Extra, String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Extra,Double>("kostprijs"));
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<Extra,Integer>("stock"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Extra,String>("type"));

        visualFilters = new ArrayList<VisualFilter>();
    }

    private void removeSelectedExtras() {
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

        if(update){
            listExtra = (ArrayList<Extra>) extraApi.getExtras();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                System.out.println("got here at update-----------------------------------------------");
                listExtra = (ArrayList<Extra>) extraApi.searchExtraByFilters(autoCompleteText);
            }
            else{
                listExtra = (ArrayList<Extra>) extraApi.searchExtraByFilters(null);
            }
        }
        
        tblExtras.getItems().setAll(listExtra);
    }

    public void initTable() {

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
                    openNewWindow("extraaddscherm", extraSelected);
                    
                break;

                case 0:
                    openNewWindow("moreinfoextra",extraSelected);
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
        
            if(controller.getClass() == ExtraAddController.class){
                ExtraAddController extraAddController = (ExtraAddController) controller; 
                extraAddController.setupDropDown(dbConnection);
                extraAddController.setDbConnection(dbConnection);
                extraAddController.setParentController(this);
                
                //iets beter maken game != null is geen goede opl
                if(user.getBevoegdheid() == 1 && extraSelected != null){
                    extraAddController.setUpdate(true);
                    extraAddController.initializeUpdate(extraSelected);
                }else{
                    extraAddController.setUpdate(false);
                }
            }
            else if(controller.getClass() == MoreInfoExtraController.class){
                MoreInfoExtraController moreInfoExtraController = (MoreInfoExtraController) controller;
                moreInfoExtraController.setDbConnection(dbConnection);
                moreInfoExtraController.setExtra(extraSelected);
            }
            if(controller.getClass() == BuyExtraSchermController.class){
                BuyExtraSchermController buyExtraSchermController = (BuyExtraSchermController) controller;
                buyExtraSchermController.setdbConnection(dbConnection);
                buyExtraSchermController.setWantToRent(wantToBuyList);
                buyExtraSchermController.setparentController(this);
                buyExtraSchermController.setUser(user);
            }
            else if(controller.getClass() == FilterSchermController.class){
                FilterSchermController filterSchermController = (FilterSchermController) controller;
                filterSchermController.setParentController(this);
                filterSchermController.setUpFilters(dbConnection, extraApi);
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
        extraApi = new ExtraApi(dbConnection);
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

     public void setUpFilters(){
        var winkelApi = new WinkelApi(dbConnection);

        for(Type type: Type.values()){
            var menuItem = new MenuItem(type.name());
            menuItem.setOnAction(e ->{
                System.out.println("gothere------------------------------------------------");
                extraApi.creatSearchQuerry((type));
                extraApi.searchExtraByFilters(null);
                this.updateOrSearchTable(false);
                addFilterToPane(type);
            });
            typeMenu.getItems().add(menuItem);
        }   

        for(Winkel winkel: winkelApi.getWinkels()){
            var menuItem = new MenuItem(winkel.getFullAdressWithID());
            menuItem.setOnAction(e -> {
                extraApi.creatSearchQuerry(winkel);
                extraApi.searchExtraByFilters(null);
                this.updateOrSearchTable(false);
                addFilterToPane(winkel);
            });
            winkelMenu.getItems().add(menuItem);
        }
    }

    private <T> void addFilterToPane(T filter){
        if(extraApi.getSearchType() != null){
            searchAndDeleteVisualFilterByType(filter);
        }
        else if(extraApi.getSearchWinkel() != null){
            searchAndDeleteVisualFilterByType(filter);
        }

        var visualFilter = new VisualFilter<>(filter);
        var visualFilterHbox = visualFilter.getVisualFilter(extraApi, this);

        visualFilters.add(visualFilter);

        scrlPaneFilters.getChildren().add(visualFilterHbox);   
    }

    public <T> void searchAndDeleteVisualFilterByType(T filter){
        for(VisualFilter visualFilter: visualFilters){
            if(visualFilter.getUsedFilter().getClass() == filter.getClass()){
                scrlPaneFilters.getChildren().remove(visualFilter.getVisualFilterHbox());
                visualFilters.remove(visualFilter);
                break;
            }
        }
    }

    public HBox getScrlPaneFilters(){
        return this.scrlPaneFilters;
    }

}

