package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
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
    private Button addWinkelBtn, addGenreBtn;

    @FXML
    private TableView<Winkel> tblWinkels;

    @FXML
    private TableView<Genre> tblGenre;

    @FXML
    private TableView<Uitgever> tblUitgever;

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

    private DbConnection dbConnection;

    public void initialize(){
        idColumnWinkel.setCellValueFactory(new PropertyValueFactory<Winkel, Integer>("winkelID"));
        nameColumnWinkel.setCellValueFactory(new PropertyValueFactory<Winkel,String>("adres"));

        idGenreColumn.setCellValueFactory(new PropertyValueFactory<Genre,Integer>("genreID"));
        nameGenreColumn.setCellValueFactory(new PropertyValueFactory<Genre,String>("naam"));

        idUitgeverColumn.setCellValueFactory(new PropertyValueFactory<Uitgever, Integer>("uitgeverID"));
        nameUitgeverColumn.setCellValueFactory(new PropertyValueFactory<Uitgever,String>("naam"));

        tblWinkels.setOnMouseClicked(mouseEvent -> handleWinkelKlick(mouseEvent));
        tblGenre.setOnMouseClicked(mouseEvent -> handleGenreKlick(mouseEvent));
        tblUitgever.setOnMouseClicked(mouseEvent -> handleUitgeverKlick(mouseEvent));

        addGenreBtn.setOnAction(e -> {
            try {
                openNewWindow("genreaddscherm", tblGenre.getSelectionModel().getSelectedItem());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        //implement dubble klick op eender welke zorgt dat je deze kan aanpassen => zorg dat plus en min knop ook werken
    }

    private void handleUitgeverKlick(MouseEvent event){
        if(event.getClickCount() == 2 && tblUitgever.getSelectionModel().getSelectedItem() != null){
            System.out.println("uitgeverklick: " + tblUitgever.getSelectionModel().getSelectedItem().getNaam());
        }
    }

    private void handleGenreKlick(MouseEvent event) {
        var selectedItem = tblGenre.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && selectedItem != null){
            System.out.println("genreKlick : " + tblGenre.getSelectionModel().getSelectedItem().getNaam());
            try {
                openNewWindow("genreaddscherm", selectedItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }   

    private void handleWinkelKlick(MouseEvent event) {
        if(event.getClickCount() == 2 && tblWinkels.getSelectionModel().getSelectedItem() != null){
            System.out.println("winkelklick: " + tblWinkels.getSelectionModel().getSelectedItem().getWinkelID());
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

            if(variable != null && variable.getClass() == Genre.class){
                var genre = (Genre) variable;
                genreAddController.setGenre(genre.getGenreID());
                genreAddController.setUpdate(true);
            }
            else{
                genreAddController.setUpdate(false);
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

    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }
}
