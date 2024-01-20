package be.kuleuven.dbproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomePageController {
    @FXML
    private VBox HeadVbox;

    @FXML
    private Label homePageText;

    @FXML
    private Hyperlink hyperLink;

    public void initialize(){
        homePageText.setWrapText(true);
        homePageText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        homePageText.setText("Welkom to the database application of the video game history foundation. In this aplication you can find information about the video games and the companies that made them. An order can also be placed to buy the video games.");
    }
    
}
