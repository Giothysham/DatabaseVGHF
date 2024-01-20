package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateAccountTweedeController {

    @FXML
    private TextField achterNaamTxtField, voorNaamTxtField, telefoonNummerTxtField;

    @FXML
    private Text foutMeldingTxt;

    @FXML
    private Button nextBtn;
    
    private DbConnection dbConnection;

    private User user;

    public void initialize(){
        nextBtn.setOnAction(e -> goToNextScrean());
    }

    private void goToNextScrean(){
        var achterNaam = achterNaamTxtField.getText();
        var voorNaam = voorNaamTxtField.getText(); 
        var telefoonNummer = telefoonNummerTxtField.getText();  

        System.out.println("matches: "+ !telefoonNummer.matches("\\d+"));
        System.out.println("lengthe: "+ (telefoonNummer.length() != 10));
        System.out.println("totaal: "+ (telefoonNummer.length() != 10 || !telefoonNummer.matches("\\d+")));

        if(achterNaam.isBlank() || voorNaam.isBlank() || telefoonNummer.isBlank()){
            foutMeldingTxt.setText("Vul alle velden in");
            foutMeldingTxt.setFill(Color.RED);
            return;
        }
        else if(telefoonNummer.length() != 10 || !telefoonNummer.matches("\\d+")){
            foutMeldingTxt.setText("fout in tel nummer, + teken moet er niet bij");
            foutMeldingTxt.setFill(Color.RED);
        }
        else{
            user.setAchternaam(achterNaam);
            user.setVoornaam(voorNaam);
            user.setTelefoonnummer(telefoonNummer);
            try {
                openWindow("createaccountschermderde.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void openWindow(String ID) throws IOException {

        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(ID));
        Parent root = loader.load();
        var controller = loader.getController();
        
        if(controller.getClass() == CreateAccountDerdeController.class){
            var createAccountDerdeController = (CreateAccountDerdeController) controller;
            createAccountDerdeController.setDbConnection(this.dbConnection);
            createAccountDerdeController.setUser(this.user);
            createAccountDerdeController.setUpOnCloseRequest(stage);
            var window = (Stage) nextBtn.getScene().getWindow();
            window.close();
        }
        else if(controller.getClass() == LoginController.class){
            var LoginController = (LoginController) controller;
            LoginController.setUserApi(dbConnection);
        }
        else{
            throw new RuntimeException("controller niet gevonden");
        }

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ ID);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void setUpOnClose(Stage stage){
        stage.setOnCloseRequest(e -> {
            try {
                openWindow("loginscherm.fxml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setUser(User user){
        this.user = user;
    }
}
