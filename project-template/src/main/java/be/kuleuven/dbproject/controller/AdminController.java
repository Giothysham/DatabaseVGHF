package be.kuleuven.dbproject.controller;

import java.io.IOException;

import javax.persistence.EntityManager;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import be.kuleuven.dbproject.model.api.UserApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    private EntityManager entityManager;

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

        //fix
        //als een van de bedrijven ofzo wordt verwijderd dan ook games van deze verwijderen => implementeren. 
        //zorg dat als ze op verwijderen klikken dat ze een ben je zker pop up krijgen. 
        removeUitgeverBtn.setOnAction(e -> deleteSelectedUitgever());

        removeWinkelBtn.setOnAction(e -> deleteSelectedWinkel());

        removeGenreBtn.setOnAction(e -> deleteSelectedGenre());

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

        this.setUser();
    }

    private void deleteSelectedUitgever(){
        Uitgever uitgever  = tblUitgever.getSelectionModel().getSelectedItem();
        entityManager.getTransaction().begin();
        entityManager.remove(uitgever);
        entityManager.getTransaction().commit();
        this.setUitgever();
    }

    private void deleteSelectedWinkel(){
        var winkel = tblWinkels.getSelectionModel().getSelectedItem();
        entityManager.getTransaction().begin();
        entityManager.remove(winkel);
        entityManager.getTransaction().commit();
        this.setWinkel();
    }

    private void deleteSelectedGenre(){
        var genre = tblGenre.getSelectionModel().getSelectedItem();
        entityManager.getTransaction().begin();
        entityManager.remove(genre);
        entityManager.getTransaction().commit();
        this.setGenre();
    }

    private void handleUitgeverKlick(MouseEvent event){
        var selectedItem = tblUitgever.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && selectedItem != null){
            //System.out.println("uitgeverklick: " + tblUitgever.getSelectionModel().getSelectedItem().getNaam());
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
            //System.out.println("genreKlick : " + tblGenre.getSelectionModel().getSelectedItem().getNaam());
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
            //System.out.println("winkelklick: " + tblWinkels.getSelectionModel().getSelectedItem().getWinkelID());
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
        var root = (GridPane) loader.load();
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
        else if(childController.getClass() == UitgeverAddScherm.class){
            var uitgeverAddScherm = (UitgeverAddScherm) childController;
            uitgeverAddScherm.setDbConnection(dbConnection); 
            uitgeverAddScherm.setParentController(this);

            if(variable != null && variable.getClass() == Uitgever.class){
                var uitgever = (Uitgever) variable;
                uitgeverAddScherm.setUitgever(uitgever.getUitgeverID());
            }
        }

        var scene = new Scene(root);
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
        this.entityManager = dbConnection.getEntityManager();
    }
}
