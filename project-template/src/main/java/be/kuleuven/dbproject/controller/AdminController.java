package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {
    
    @FXML
    private Button addWinkelBtn;

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

        //implement dubble klick op eender welke zorgt dat je deze kan aanpassen => zorg dat plus en min knop ook werken
    }

    public void setWinkel(){
        var winkelApi = new WinkelApi(dbConnection);
        var winkelList = winkelApi.getWinkels();

        tblWinkels.getItems().addAll(winkelList);
    }

    public void setGenre(){
        var genreApi = new GenreApi(dbConnection);
        var genreList = genreApi.getGenres();
        
        tblGenre.getItems().addAll(genreList);
    }

    public void setUitgever(){
        var uitgeverApi = new UitgeverApi(dbConnection);
        var uitgeverList = uitgeverApi.getUitgevers();

        tblUitgever.getItems().addAll(uitgeverList);
    }

    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }
}
