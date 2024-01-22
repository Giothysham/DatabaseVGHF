package be.kuleuven.dbproject.controller;

import java.io.IOException;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class CreatAccountBeginController {

    @FXML
    private GridPane creatAccountPane;

    @FXML
    private Button nextBtn;

    @FXML
    private TextField emailTxtField,wachtwoordTxtField,wachtwoordHerhaalTxtField;

    @FXML
    private Text wachtwoordText, wachtwoordHerhaalText, foutMeldingTxt, emailTxt;
    //10 digits.

    private User user;

    private UserApi userApi;

    private DbConnection dbConnection;

    public void initialize(){
        nextBtn.setOnAction(e -> creatAccount());
    }

    public void creatAccount(){
        var email = emailTxtField.getText();
        var wachtwoord = wachtwoordTxtField.getText();
        var harhaalWachtwoord = wachtwoordHerhaalTxtField.getText();
        var uitgeleendeGame =  new ArrayList<Game>();

        userApi = new UserApi(dbConnection);
        try {
            userApi.checkIfUserExistWithEmail(email);
        } catch (Exception e) {
            foutMeldingTxt.setText("user bestaat al met dit email adres");
            foutMeldingTxt.setFill(Color.RED);
            return;
        }

        if(!email.contains("@")){
            wachtwoordText.setFill(Color.BLACK);
            wachtwoordHerhaalText.setFill(Color.BLACK);
            emailTxt.setFill(Color.RED);
            foutMeldingTxt.setText("fout in de mail");
            foutMeldingTxt.setFill(Color.RED);
        }
        else if(!wachtwoord.equals(harhaalWachtwoord) || wachtwoord == ""){
            wachtwoordText.setFill(Color.RED);
            wachtwoordHerhaalText.setFill(Color.RED);
            emailTxt.setFill(Color.BLACK);
            foutMeldingTxt.setText("wachtwoorden komen niet overeen");
            foutMeldingTxt.setFill(Color.RED);
        }
        else{
            user = new User("","","","","","","","",0,email,wachtwoord,0,uitgeleendeGame);

            try{
                // userApi.creatUser(user);
                openWindow("createaccountschermtweede.fxml");
            }
            catch(Exception e){
                e.printStackTrace();
            }   
        }
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

    private void openWindow(String ID) throws IOException {
        
        var stage = new Stage();
        var loader = new FXMLLoader(getClass().getClassLoader().getResource(ID));
        Parent root = loader.load();
        var controller = loader.getController();

        System.out.println(controller.getClass());
        
        if(controller.getClass() == CreateAccountTweedeController.class){
            var createAccountTweedeController = (CreateAccountTweedeController) controller;
            createAccountTweedeController.setDbConnection(this.dbConnection);
            createAccountTweedeController.setUser(this.user);
            createAccountTweedeController.setUpOnClose(stage);
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

    public void setDbConnection(DbConnection dbConnection){
        this.dbConnection = dbConnection;
        this.userApi = new UserApi(dbConnection);
    }
    
}
