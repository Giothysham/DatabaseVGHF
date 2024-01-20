package be.kuleuven.dbproject.controller;

import java.io.IOException;
import java.util.ArrayList;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Game;
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
    private Button logOutBtn,adminBtn,btnUitgeleendeGames,btnExtras, btnGames, factuurBtn;
    @FXML 
    private PaneHolderController paneHolderController ;
    @FXML
    private VBox childerenBox;
    @FXML
    private BorderPane borderPaneMain;

    @FXML
    private Label naamTxt,achternaamTxt;

    //private DbConnection dbConnection;

    public void initialize() {

        try {
            paneHolderController.changeChildTo("homepage",dbConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listOfButtons = new ArrayList<>();

        listOfButtons.add(adminBtn);
        listOfButtons.add(btnGames);
        listOfButtons.add(btnUitgeleendeGames);
        listOfButtons.add(btnExtras);
        listOfButtons.add(factuurBtn);

        logOutBtn.setOnAction(e -> {
            try {
                logOut();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnGames.setOnAction(e -> {
            try {
                changeButtonStyle(listOfButtons, e);
                paneHolderController.setProduct(new Game());
                paneHolderController.changeChildTo("verkoopbaarscherm",dbConnection);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnUitgeleendeGames.setOnAction(e -> {
            try {
                changeButtonStyle(listOfButtons, e);
                paneHolderController.setUser(user);
                paneHolderController.changeChildTo("uitgeleendegamescherm",dbConnection);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        factuurBtn.setOnAction(e ->
        {
            try {
                changeButtonStyle(listOfButtons, e);
                paneHolderController.setUser(user);
                paneHolderController.changeChildTo("factuurscherm",dbConnection);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        );

        adminBtn.setOnAction(e -> {
            changeButtonStyle(listOfButtons, e);
            try {
                paneHolderController.changeChildTo("adminscherm", dbConnection);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnExtras.setOnAction(e -> {
            changeButtonStyle(listOfButtons, e);
            try {
                paneHolderController.setProduct(new Extra());
                paneHolderController.changeChildTo("verkoopbaarscherm", dbConnection);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
    }

    public void changeButtonStyle(ArrayList<Button> buttonList,ActionEvent mouseAction){
        for(Button button: buttonList){
            if(mouseAction.getSource().equals(button)){
                button.setStyle("-fx-background-color: #F4F4F4; -fx-text-fill: #4f46e5;");
            }else{
                button.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
            }
        }
    }

    private void logOut() throws IOException{
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

    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setUser(User user){
        this.user = user;
        naamTxt.setText(user.getVoornaam());
        achternaamTxt.setText(user.getAchternaam());

        if(user.getBevoegdheid() != 1){
            adminBtn.setDisable(true);
        }

        paneHolderController.setUser(user);
    }
}
