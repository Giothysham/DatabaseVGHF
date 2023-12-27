package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateAccountDerdeController {

    private User user;
    
    @FXML
    private TextField adresTxtField, stadTxtField, postcodeTxtField, provincieTxtField, landTxtField;

    @FXML
    private Text foutMeldingTxt;

    @FXML
    private Button nextBtn;

    private DbConnection dbConnection;

    public void initialize(){
        nextBtn.setOnAction(e -> goToMainProgramm());
    }

    private void goToMainProgramm() {
        var adres = adresTxtField.getText();
        var stad = stadTxtField.getText();
        var postcode = postcodeTxtField.getText();
        var provincie = provincieTxtField.getText();
        var land = landTxtField.getText();
        
        if(adres.isBlank() || stad.isBlank() || postcode.isBlank() || provincie.isBlank() || land.isBlank()){
            foutMeldingTxt.setText("Vul alle velden in");
            return;
        }
        else{
            user.setAdres(adres);
            user.setStad(stad);
            user.setPostcode(postcode);
            user.setProvincie(provincie);
            user.setLand(land);
            try {
                var userApi = new UserApi(dbConnection);
                userApi.creatUser(user);
                openWindow("main.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUpOnCloseRequest(Stage stage) {
        stage.setOnCloseRequest(e -> {
            try {
                openWindow("loginscherm.fxml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void openWindow(String ID) throws IOException {
        
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(ID));
        Parent root = loader.load();
        var controller = loader.getController();

        System.out.println(controller.getClass());
        
        if(controller.getClass() == ProjectMainController.class){
            var projectMainController = (ProjectMainController) controller;
            projectMainController.setDbConnection(this.dbConnection);
            projectMainController.setUser(this.user);
            var window = (Stage) nextBtn.getScene().getWindow();
            window.close();
        }
        else if(controller.getClass() == LoginController.class){
            var LoginController = (LoginController) controller;
            LoginController.setUserApi(dbConnection);
        }

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ ID);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }
    
}
