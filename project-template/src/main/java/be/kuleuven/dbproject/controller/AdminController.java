package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import be.kuleuven.dbproject.model.api.UserApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminController {
    
    @FXML
    private Button addWinkelBtn ,removeUitgeverBtn ,removeWinkelBtn ,removeGenreBtn , addGenreBtn, addUitgeverBtn, maakAdminBtn, removeAdmingBtn;

    @FXML
    private TableView<Winkel> tblWinkels;

    @FXML
    private TableView<Genre> tblGenre;

    @FXML
    private TableView<Uitgever> tblUitgever;

    @FXML
    private TableView<User> tblUser;

    @FXML
    private TableColumn<Winkel, Integer> idColumnWinkel; 

    @FXML
    private TableColumn<Winkel, String> nameColumnWinkel; 

    @FXML
    private TableColumn<Genre, Integer> idGenreColumn; 

    @FXML
    private TableColumn<Genre, String> nameGenreColumn; 

    @FXML
    private TableColumn<Uitgever, Integer> idUitgeverColumn; 

    @FXML
    private TableColumn<Uitgever, String> nameUitgeverColumn; 

    @FXML
    private TableColumn<User, String> voornaamColumn, achternaamColumn, emailColumn, tellColumn;

    @FXML
    private TableColumn<User, Integer> userIdColumn, bevoegdColumn;

    private DbConnection dbConnection;

    private UitgeverApi uitgeverApi;

    private ExtraApi extraApi;

    private GameApi gameApi;

    private WinkelApi WinkelApi;

    private GenreApi genreApi;

    private UserApi userApi;

    public void initialize(){
        idColumnWinkel.setCellValueFactory(new PropertyValueFactory<Winkel, Integer>("winkelID"));
        nameColumnWinkel.setCellValueFactory(new PropertyValueFactory<Winkel,String>("adres"));

        idGenreColumn.setCellValueFactory(new PropertyValueFactory<Genre,Integer>("genreID"));
        nameGenreColumn.setCellValueFactory(new PropertyValueFactory<Genre,String>("naam"));

        idUitgeverColumn.setCellValueFactory(new PropertyValueFactory<Uitgever, Integer>("uitgeverID"));
        nameUitgeverColumn.setCellValueFactory(new PropertyValueFactory<Uitgever,String>("naam"));

        userIdColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userId"));
        voornaamColumn.setCellValueFactory(new PropertyValueFactory<User,String>("voornaam"));
        achternaamColumn.setCellValueFactory(new PropertyValueFactory<User,String>("achternaam"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        tellColumn.setCellValueFactory(new PropertyValueFactory<User,String>("telefoonnummer"));
        bevoegdColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("bevoegdheid"));

        tblWinkels.setOnMouseClicked(mouseEvent -> handleWinkelKlick(mouseEvent));
        tblGenre.setOnMouseClicked(mouseEvent -> handleGenreKlick(mouseEvent));
        tblUitgever.setOnMouseClicked(mouseEvent -> handleUitgeverKlick(mouseEvent));
        tblUser.setOnMouseClicked(mouseEvent -> handleUserKlick(mouseEvent));

        addGenreBtn.setOnAction(e -> {
            try {
                openNewWindow("genreaddscherm", null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        addWinkelBtn.setOnAction(e ->{
            try {
                openNewWindow("winkeladdscherm", null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        addUitgeverBtn.setOnAction(e -> {
            try {
                openNewWindow("uitgeveraddscherm", null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        maakAdminBtn.setOnAction(e -> switchAdmin(true));

        removeUitgeverBtn.setOnAction(e -> {
            var uitgever  = tblUitgever.getSelectionModel().getSelectedItem();
            
            if(!extraApi.gebruiktUitgever(uitgever) && !gameApi.gebruiktUitgever(uitgever)){
                uitgeverApi.deleteSelectedUitgever(uitgever);
                this.setUitgever();
            } else{
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Uitgever is in gebruikt");
                a.show();
            }
        });

        removeWinkelBtn.setOnAction(e ->{
            var winkel = tblWinkels.getSelectionModel().getSelectedItem();
            
            if(!extraApi.gebruiktWinkel(winkel) && !gameApi.gebruiktWinkel(winkel)){
                WinkelApi.deleteSelectedWinkel(winkel);
                this.setWinkel();
            } else{
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Winkel is in gebruikt");
                a.show();
            }
        });

        removeGenreBtn.setOnAction(e -> {
        
        var genre = tblGenre.getSelectionModel().getSelectedItem();
            
        if(!gameApi.gebruiktGenre(genre)){
            genreApi.deleteSelectedGenre(genre);
            this.setGenre();
        } else{
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Genre is in gebruikt");
            a.show();
        }
    });

        removeAdmingBtn.setOnAction(e -> switchAdmin(false));
    }

    private void switchAdmin(boolean admin) {
        var user = tblUser.getSelectionModel().getSelectedItem();

        if(user.getBevoegdheid() != 1 && admin){
            user.setBevoegdheid(admin);
        }
        else if(user.getBevoegdheid() != 0 && !admin){
            user.setBevoegdheid(admin);
        }

        userApi.updateUser(user);

        this.setUser();
    }

    private void handleUserKlick(MouseEvent event){
        var selectedItem = tblUser.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && selectedItem != null){
            try {
                openNewWindow("moreinfouserscherm", selectedItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUitgeverKlick(MouseEvent event){
        var selectedItem = tblUitgever.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && selectedItem != null){
            try {
                openNewWindow("uitgeveraddscherm", selectedItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleGenreKlick(MouseEvent event) {
        var selectedItem = tblGenre.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && selectedItem != null){
            try {
                openNewWindow("genreaddscherm", selectedItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }   

    private void handleWinkelKlick(MouseEvent event) {
        var selected = tblWinkels.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && selected != null){
            try {
                openNewWindow("winkeladdscherm", selected);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private <T> void openNewWindow(String name, T variable) throws IOException{
        String resourceName = name+".fxml";
        
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
        var root = loader.load();
        var childController = loader.getController();

        
        if(childController.getClass() == GenreAddController.class){
            var genreAddController = (GenreAddController) childController;
            genreAddController.setParentController(this); 
            genreAddController.setDbConnection(dbConnection);

            if(variable != null){
                var genre = (Genre) variable;
                genreAddController.setGenre(genre);
            }
            
        }
        else if(childController.getClass() == WinkelAddController.class){
            var winkelAddController = (WinkelAddController) childController;
            winkelAddController.setDbConnection(dbConnection);
            winkelAddController.setParentController(this);

            if(variable != null && variable.getClass() == Winkel.class){
                var winkel = (Winkel) variable;
                winkelAddController.setWinkel(winkel.getWinkelID());
            }
        }
        else if(childController.getClass() == UitgeverAddController.class){
            var uitgeverAddScherm = (UitgeverAddController) childController;
            uitgeverAddScherm.setDbConnection(dbConnection); 
            uitgeverAddScherm.setParentController(this);

            if(variable != null && variable.getClass() == Uitgever.class){
                var uitgever = (Uitgever) variable;
                uitgeverAddScherm.setUitgever(uitgever.getUitgeverID());
            }
        }
        else if(childController.getClass() == MoreInfoUserController.class){
            var moreInfoUserController = (MoreInfoUserController) childController;
            if(variable != null && variable.getClass() == User.class){
                var user = (User) variable;
                moreInfoUserController.setUpClass(dbConnection, user);
            }
        }

        var scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ resourceName);
        stage.initOwner(ProjectMain.getRootStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void setWinkel(){
        tblWinkels.getItems().clear();

        var winkelApi = new WinkelApi(dbConnection);
        var winkelList = winkelApi.getWinkels();

        tblWinkels.getItems().addAll(winkelList);
    }

    public void setGenre(){
        tblGenre.getItems().clear();

        var genreApi = new GenreApi(dbConnection);
        var genreList = genreApi.getGenres();
        
        tblGenre.getItems().addAll(genreList);
    }

    public void setUitgever(){
        tblUitgever.getItems().clear();

        var uitgeverApi = new UitgeverApi(dbConnection);
        var uitgeverList = uitgeverApi.getUitgevers();

        tblUitgever.getItems().addAll(uitgeverList);
    }

    public void setUser(){
        tblUser.getItems().clear();

        var userApi = new UserApi(dbConnection);
        var userList = userApi.getUsers();

        tblUser.getItems().addAll(userList);
    }

    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
        uitgeverApi = new UitgeverApi(dbConnection);
        extraApi = new ExtraApi(dbConnection, null);
        gameApi = new GameApi(dbConnection, null);
        WinkelApi = new WinkelApi(dbConnection);
        genreApi = new GenreApi(dbConnection);
        userApi = new UserApi(dbConnection);
    }
}
