package be.kuleuven.dbproject.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ProjectMainController {

    //laat buttons mappen door het gebruiken van een lijst !!

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnBeheerScherm1;
    @FXML
    private Button btnBeheerScherm2;
    @FXML
    private Button btnConfigAttaches;
    @FXML 
    private PaneHolderController paneHolderController ;
    @FXML
    private VBox childerenBox;
    @FXML
    private BorderPane borderPaneMain;
    @FXML
    private Button btnGames;

    //private DbConnection dbConnection;

    public void initialize() throws IOException {

        //paneHolderController.setDbConnection(this.dbConnection);

        borderPaneMain.setRight(null);

        btnBeheerScherm1.setOnAction(e -> {
            try {
                //optimalizeren => bekijken !!!!!
                btnGames.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnConfigAttaches.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnBeheerScherm2.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnBeheerScherm1.setStyle("-fx-background-color: #F8F7F3; -fx-text-fill: #4f46e5;");
                paneHolderController.changeChildTo("beheerscherm1");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        btnBeheerScherm2.setOnAction(e -> {
            try {
                //optimalizeren
                btnGames.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnConfigAttaches.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnBeheerScherm2.setStyle("-fx-background-color: #F8F7F3; -fx-text-fill: #4f46e5;");
                btnBeheerScherm1.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                paneHolderController.changeChildTo("beheerscherm2");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        btnConfigAttaches.setOnAction(e -> {
            try {
                //optimalizeren
                btnGames.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnConfigAttaches.setStyle("-fx-background-color: #F8F7F3; -fx-text-fill: #4f46e5");
                btnBeheerScherm2.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnBeheerScherm1.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                paneHolderController.changeChildTo("beheerattaches");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        btnGames.setOnAction(e -> {
            try {
                //optimalizeren
                btnGames.setStyle("-fx-background-color: #F8F7F3; -fx-text-fill: #4f46e5;");
                btnConfigAttaches.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnBeheerScherm2.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                btnBeheerScherm1.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white;");
                paneHolderController.changeChildTo("gamescherm");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        //vragen hoe of we een aparte tab maken, of dat je altijd inlog scherm krijgt en daarna pas naar main aplicatie moogt.  
        
    }

    // public void setdbConnection(DbConnection dbConnection){
    //     this.dbConnection = dbConnection;
    // }

}
