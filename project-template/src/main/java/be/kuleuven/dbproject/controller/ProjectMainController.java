package be.kuleuven.dbproject.controller;

import java.io.IOException;
import java.util.ArrayList;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProjectMainController {

    //laat buttons mappen door het gebruiken van een lijst !!
    private DbConnection dbConnection;

    private User user;

    private ArrayList<Button> listOfButtons;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnBeheerScherm1,logOutBtn,btnBeheerScherm2,btnConfigAttaches;
    @FXML 
    private PaneHolderController paneHolderController ;
    @FXML
    private VBox childerenBox;
    @FXML
    private BorderPane borderPaneMain;
    @FXML
    private Button btnGames;

    @FXML
    private Label naamTxt,achternaamTxt;

    //private DbConnection dbConnection;

    public void initialize() {

        listOfButtons = new ArrayList<>();

        listOfButtons.add(btnBeheerScherm1);
        listOfButtons.add(btnBeheerScherm2);
        listOfButtons.add(btnConfigAttaches);
        listOfButtons.add(btnGames);

        logOutBtn.setOnAction(e -> {
            try {
                changeWindow();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnGames.setOnAction(e -> {
            try {
                changeButtonStyle(listOfButtons, e);
                paneHolderController.setUser(user);
                paneHolderController.changeChildTo("gamescherm",dbConnection);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
    }

    public void changeButtonStyle(ArrayList<Button> buttonList,ActionEvent mouseAction){
        for(Button button: buttonList){
            if(mouseAction.getSource().equals(button)){
                button.setStyle("-fx-background-color: #F8F7F3; -fx-text-fill: #4f46e5;");
            }else{
                button.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
            }
        }
    }

    private void changeWindow() throws IOException{
        Stage oldStage = (Stage) btnGames.getScene().getWindow();
        oldStage.close();

        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource("loginscherm.fxml"));
        var root = loader.load();
        
        var parentClass = (LoginController) loader.getController();
        parentClass.setUserApi(dbConnection);

        Scene scene = new Scene((Parent) root);
        stage.setTitle("store softwear by budo");
        stage.setScene(scene);
        stage.show();
    }

    public void setdbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setUser(User user){
        this.user = user;
        naamTxt.setText(user.getVoornaam());
        achternaamTxt.setText(user.getAchternaam());
    }
}
