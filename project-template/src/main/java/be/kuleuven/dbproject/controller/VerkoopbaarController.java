package be.kuleuven.dbproject.controller;

import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.VisualFilter;
import be.kuleuven.dbproject.interfaces.BuyScreenInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.enums.Type;
import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
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


public class VerkoopbaarController implements BuyScreenInterface{

    @FXML
    private Menu consoleMenu, winkelMenu, genreMenu, typeMenu;

    @FXML
    private Button VerkoopbaarAddBtn, deleteBtn, buyBtn, searchBtn, addToCartBtn; //filterBtn;

    @FXML
    private VerkoopbaarAddController verkoopbaarAddController;

    @FXML
    private TextField autoCompleteSearch;

    @FXML
    private TableColumn<VerkoopbaarInterface,String> naamColumn;

    @FXML
    private TableColumn<VerkoopbaarInterface,Double> priceColumn;

    @FXML
    private TableColumn<VerkoopbaarInterface,Integer> avaibleColumn;

    @FXML
    private TableView<VerkoopbaarInterface> tblVerkoopbaar;

    @FXML
    private HBox scrlPaneFilters;

    private ArrayList<VisualFilter> visualFilters;

    private ArrayList<String> checkoutList;

    private ArrayList<String> autoCompleteWords;

    private ArrayList<VerkoopbaarInterface> verkoobareLijst;

    private DbConnection dbConnection;

    private User user;

    private VerkoopbaarApiInterface verkoopbaarApi;

    private VerkoopbaarInterface verkoopbaar;

    public void initialize(){
        checkoutList = new ArrayList<>();

        VerkoopbaarAddBtn.setOnAction(e -> openNewWindow("verkoopbaaraddscherm",null));
        addToCartBtn.setOnAction(e -> addToVerkoobareLijst());
        deleteBtn.setOnAction(e -> removeSelectedVerkoopbaar());
        searchBtn.setOnAction(e -> updateOrSearchTable(false));
        //filterBtn.setOnAction(e -> openNewWindow("filterscherm", null));

        searchBtn.setMaxWidth(Double.MAX_VALUE);
        //filterBtn.setMaxWidth(Double.MAX_VALUE);

        //alles met ty/catch in een deel?
        buyBtn.setOnAction(e -> {openNewWindow("buyverkoopbaarscherm",null);});

        verkoobareLijst = new ArrayList<VerkoopbaarInterface>();

        autoCompleteWords = new ArrayList<String>();

        naamColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,String>("naam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Double>("kostPrijs"));
        avaibleColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Integer>("stock"));

        visualFilters = new ArrayList<VisualFilter>();  
    }

    private void removeSelectedVerkoopbaar() {
        var tempList = tblVerkoopbaar.getSelectionModel().getSelectedItems();
        
        verkoopbaarApi.deleteVerkoopbaar(tempList);
        
        updateOrSearchTable(true);
    }

    public void addToVerkoobareLijst(){
        var tempList = tblVerkoopbaar.getSelectionModel().getSelectedItems();

        for (int i = 0; i<tempList.size(); i++) {
            //vragen of dit hard code cava is. 
            var ID = ((VerkoopbaarInterface) tempList.get(i)).getID();
            checkoutList.add(Integer.toString(ID));
        }
    }

    public void updateOrSearchTable(Boolean update){

        verkoobareLijst.clear();
        tblVerkoopbaar.getItems().clear();

        if(update){
            verkoobareLijst = (ArrayList<VerkoopbaarInterface>) verkoopbaarApi.getVerkoopbaar();
        }
        else{
            var autoCompleteText = autoCompleteSearch.getText();
            if(autoCompleteText.length() != 0){
                verkoobareLijst = (ArrayList<VerkoopbaarInterface>) verkoopbaarApi.searchVerkoopbaarByFilters(autoCompleteText);
            }
            else{
                verkoobareLijst = (ArrayList<VerkoopbaarInterface>) verkoopbaarApi.searchVerkoopbaarByFilters(null);
            }
        }
        
        tblVerkoopbaar.getItems().setAll(verkoobareLijst);
    }

    public void initTable() {
        verkoobareLijst = (ArrayList<VerkoopbaarInterface>) verkoopbaarApi.getVerkoopbaar();

        for(VerkoopbaarInterface verkoopbaar: verkoobareLijst){
            if(!autoCompleteWords.contains(verkoopbaar.getNaam())){
                autoCompleteWords.add(verkoopbaar.getNaam());
            }
        }

        TextFields.bindAutoCompletion(autoCompleteSearch, autoCompleteWords);

        tblVerkoopbaar.setOnMouseClicked(mouseEvent -> {onClickVerkoopbaar(mouseEvent);});

        tblVerkoopbaar.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tblVerkoopbaar.getItems().addAll(verkoobareLijst);

    }

    @FXML
    public void onClickVerkoopbaar(MouseEvent event) {
        if(event.getClickCount() == 2 && tblVerkoopbaar.getSelectionModel().getSelectedItem() != null){
            VerkoopbaarInterface verkoopbaarSelected = tblVerkoopbaar.getSelectionModel().getSelectedItem();
            switch(user.getBevoegdheid()){
                case 1:
                    openNewWindow("verkoopbaaraddscherm", verkoopbaarSelected);
                    
                break;

                case 0:
                    openNewWindow("moreinfoverkoopbaar", verkoopbaarSelected);
                break;
            }
        }
    }

    public void setCheckoutList(ArrayList<String> checkoutList){
        this.checkoutList = checkoutList;
    }

    public void openNewWindow(String id, VerkoopbaarInterface verkoopbaarSelected){
        var resourceName = id + ".fxml";
        try {

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();
            var controller = loader.getController();

            if(controller.getClass() == VerkoopbaarAddController.class){
                VerkoopbaarAddController verkoopbaarAddController = (VerkoopbaarAddController) controller;
                verkoopbaarAddController.setProduct(verkoopbaar);
                verkoopbaarAddController.setupDropDown(dbConnection);
                verkoopbaarAddController.setDbConnection(dbConnection);
                verkoopbaarAddController.setParentController(this);
                
                //TODO: iets beter maken verkoopbaar != null is geen goede opl
                if(user.getBevoegdheid() == 1 && verkoopbaarSelected != null){
                    verkoopbaarAddController.setUpdate(true);
                    verkoopbaarAddController.initializeUpdate(verkoopbaarSelected);
                }else{
                    verkoopbaarAddController.setUpdate(false);
                }
            }
            else if(controller.getClass() == VerkoopbaarMoreInfoController.class){
                VerkoopbaarMoreInfoController verkoopbaarMoreInfoController = (VerkoopbaarMoreInfoController) controller;
                verkoopbaarMoreInfoController.setdbConnection(dbConnection);
                verkoopbaarMoreInfoController.setVerkoopbaar(verkoopbaarSelected);
            }
            else if(controller.getClass() == VerkoopbaarBuySchermController.class){
                VerkoopbaarBuySchermController verkoopbaarBuySchermController = (VerkoopbaarBuySchermController) controller;
                verkoopbaarBuySchermController.setProduct(verkoopbaar);
                verkoopbaarBuySchermController.setdbConnection(dbConnection);
                verkoopbaarBuySchermController.setWantToRent(checkoutList);
                verkoopbaarBuySchermController.setparentController(this);
                verkoopbaarBuySchermController.setUser(user);
            }
            else if(controller.getClass() == FilterSchermController.class){
                FilterSchermController filterSchermController = (FilterSchermController) controller;
                filterSchermController.setParentController(this);
                filterSchermController.setUpFilters(dbConnection, verkoopbaarApi);
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
        if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
            verkoopbaarApi = new GameApi(dbConnection, user);
        }
        else if(verkoopbaar.getClass().isAssignableFrom(Extra.class)){
            verkoopbaarApi = new ExtraApi(dbConnection, user);
        }
        initTable();
    }

    public void setUser(User user){
        this.user = user;
        if(user.getBevoegdheid() == 1){
            VerkoopbaarAddBtn.setDisable(false);
            deleteBtn.setDisable(false);
        }
        else{
            VerkoopbaarAddBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

    public void setUpFilters(){
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);

        if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
            for(Console console: Console.values()){
                var menuItem = new MenuItem(console.name());
                menuItem.setOnAction(e ->{
                    verkoopbaarApi.creatSearchQuerry((console));
                    verkoopbaarApi.searchVerkoopbaarByFilters(null);
                    this.updateOrSearchTable(false);
                    addFilterToPane(console);
                });
                consoleMenu.getItems().add(menuItem);
            }  
            
            for(Genre genre: genreApi.getGenres()){
                var menuItem = new MenuItem(genre.getNaam());
                menuItem.setOnAction(e -> {
                    verkoopbaarApi.creatSearchQuerry(genre);
                    verkoopbaarApi.searchVerkoopbaarByFilters(null);
                    this.updateOrSearchTable(false);
                    addFilterToPane(genre);
                });
                genreMenu.getItems().add(menuItem);
            }
        }

        else if(verkoopbaar.getClass().isAssignableFrom(Extra.class)){
            for(Type type: Type.values()){
                var menuItem = new MenuItem(type.name());
                menuItem.setOnAction(e ->{
                    verkoopbaarApi.creatSearchQuerry((type));
                    verkoopbaarApi.searchVerkoopbaarByFilters(null);
                    this.updateOrSearchTable(false);
                    addFilterToPane(type);
                });
            typeMenu.getItems().add(menuItem);
        }
        }
        

        

        for(Winkel winkel: winkelApi.getWinkels()){
            var menuItem = new MenuItem(winkel.getFullAdressWithID());
            menuItem.setOnAction(e -> {
                verkoopbaarApi.creatSearchQuerry(winkel);
                verkoopbaarApi.searchVerkoopbaarByFilters(null);
                this.updateOrSearchTable(false);
                addFilterToPane(winkel);
            });
            winkelMenu.getItems().add(menuItem);
        }
    }

    private <T> void addFilterToPane(T filter){

        //TODO mauro optimize
        
        if(verkoopbaarApi.getSearchWinkel() != null){
            searchAndDeleteVisualFilterByType(filter);
        }

        if(verkoopbaarApi.getClass().isAssignableFrom(GameApi.class)){
            if(((GameApi)verkoopbaarApi).getSearchConsole() != null){
                searchAndDeleteVisualFilterByType(filter);
            }
            else if(((GameApi)verkoopbaarApi).getSearchGenre() != null){
                searchAndDeleteVisualFilterByType(filter);
            }
        } 

        else if(verkoopbaarApi.getClass().isAssignableFrom(ExtraApi.class)){
            if(((ExtraApi)verkoopbaarApi).getSearchType() != null){
                searchAndDeleteVisualFilterByType(filter);
            }
        }


        var visualFilter = new VisualFilter<>(filter);
        var visualFilterHbox = visualFilter.getVisualFilter(verkoopbaarApi, this);

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

    public void setProduct(VerkoopbaarInterface product){
        verkoopbaar = product;

        if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
            TableColumn<VerkoopbaarInterface,Console> consoleColumn = new TableColumn<>("console");
            consoleColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Console>("console"));
            tblVerkoopbaar.getColumns().add(consoleColumn);

            typeMenu.setVisible(false);
            
        } 
        else if(verkoopbaar.getClass().isAssignableFrom(Extra.class)){
            TableColumn<VerkoopbaarInterface,Type> typeColumn = new TableColumn<>("type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<VerkoopbaarInterface,Type>("type"));
            tblVerkoopbaar.getColumns().add(typeColumn);

            consoleMenu.setVisible(false);
            genreMenu.setVisible(false);
        }

        
    }

}
