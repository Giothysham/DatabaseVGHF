package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.GenreApi;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GenreAddController {

    private DbConnection dbConnection;

    private AdminController parentController;

    private Genre genre;

    @FXML
    private Button submitGenreButton;

    @FXML
    private TextField naamTxt;

    @FXML
    private TextArea beschrijvingTxt;

    public void initialize(){
        submitGenreButton.setOnAction(e -> addOrUpdateGame());
    }

    public void setGenre(Genre genre){
        naamTxt.setText(genre.getNaam());
        beschrijvingTxt.setText(genre.getBeschrijving());
    }

    public void addOrUpdateGame(){
        var name = naamTxt.getText();
        var beschrijving = beschrijvingTxt.getText();

        if(genre != null){
            genre.setNaam(name);
            genre.setBeschrijving(beschrijving);
        }
        else{
            Genre genre = new Genre(name, beschrijving);

            var genreApi = new GenreApi(dbConnection);
            genreApi.postGenre(genre);
        }
        parentController.setGenre();

        var stage = (Stage) naamTxt.getScene().getWindow();
        stage.close();
    }
    
    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setParentController(AdminController parentController){
        this.parentController = parentController;
    }
    
}
