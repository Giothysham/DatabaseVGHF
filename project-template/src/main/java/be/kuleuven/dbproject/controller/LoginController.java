package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    @FXML
    private Button loginBtn, crtAccountBtn;

    @FXML
    private ImageView eyeImageView;

    @FXML
    private TextField emailTxtField;

    @FXML
    private TextField wachtwoordTxtFieldVissible;

    @FXML
    private PasswordField wachtwoordTxtFieldInvissible;

    @FXML
    private HBox wachtwoordHbox;

    private UserApi userApi;

    private DbConnection dbConnection;

    private String wachtwoord;

    private User user;

    private boolean showPassword;
    
    public void initialize(){
        loginBtn.setOnAction(e -> checkLoginCredentials());

        showPassword = false;

        emailTxtField.setText("vranckx.mauro@student.uhasselt.be");

        wachtwoord = "";

        crtAccountBtn.setOnAction(e -> {
            try {
                changeWindow("createaccountschermbegin.fxml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        eyeImageView.setOnMousePressed(e -> togglePassword());

        wachtwoordTxtFieldInvissible = new PasswordField();
        wachtwoordTxtFieldVissible = new TextField();

        showPassword = true;

        togglePassword();
    }

    private void togglePassword(){
        showPassword =! showPassword;
        if(showPassword){
            wachtwoord = wachtwoordTxtFieldInvissible.getText();
            wachtwoordHbox.getChildren().remove(wachtwoordTxtFieldInvissible);
            wachtwoordHbox.getChildren().add(wachtwoordTxtFieldVissible);
            wachtwoordTxtFieldVissible.setText(wachtwoord);
            wachtwoordTxtFieldVissible.textProperty().addListener((ods, oldText, newText) -> wachtwoord = newText);
        }
        else{
            wachtwoord = wachtwoordTxtFieldVissible.getText();
            wachtwoordHbox.getChildren().remove(wachtwoordTxtFieldVissible);
            wachtwoordHbox.getChildren().add(wachtwoordTxtFieldInvissible);
            wachtwoordTxtFieldInvissible.setText(wachtwoord);
            wachtwoordTxtFieldInvissible.textProperty().addListener((ods, oldText, newText) -> wachtwoord = newText);
        }
    }

    private void checkLoginCredentials(){
        var email = emailTxtField.getText();
        try {
            this.user = userApi.chekUserAndWachtwoord(email, wachtwoord);
            changeWindow("main.fxml");
        } catch (Exception e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
            e.printStackTrace();
        }
    }

    private void changeWindow(String id) throws IOException{
        
        Stage oldStage = (Stage) loginBtn.getScene().getWindow();
        oldStage.close();
        
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(id));
        Parent root = loader.load();
        var controller = loader.getController();


        if(controller.getClass() == ProjectMainController.class){
            var projectMainController = (ProjectMainController) controller;
            projectMainController.setDbConnection(this.dbConnection);
            projectMainController.setUser(user);
        }
        else if(controller.getClass() == CreatAccountBeginController.class){
            var CreatAccountController = (CreatAccountBeginController) controller;
            CreatAccountController.setDbConnection(dbConnection);
            CreatAccountController.setUpOnClose(stage);
        }

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("budo/"+ id);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void setUserApi(DbConnection dbConnection){
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
    }
}
