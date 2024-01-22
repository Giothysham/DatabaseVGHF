package be.kuleuven.dbproject.controller;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.enums.Console;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MoreInfoUserController {

    @FXML
    private Label tellText, nameText, emailText;

    @FXML
    private TableColumn<Game,String> naamColumn;
    
    @FXML
    private TableColumn<Game,Console> consoleColumn;

    @FXML
    private TableView<Game> tblUitgeleendeGames;

    private DbConnection dbConnection;

    public void initialize() {
        naamColumn.setCellValueFactory(new PropertyValueFactory<Game,String>("naam"));
        consoleColumn.setCellValueFactory(new PropertyValueFactory<Game,Console>("console"));
    }

    public void setUpClass(DbConnection dbConnection,User user){
        dbConnection = this.dbConnection;

        tellText.setText(user.getTelefoonnummer());
        nameText.setText(user.getVoornaam() + " "+ user.getAchternaam());
        emailText.setText(user.getEmail());

        var listgames = user.getUitgeleendeGames();
        tblUitgeleendeGames.getItems().setAll(listgames);
    }
    
}
