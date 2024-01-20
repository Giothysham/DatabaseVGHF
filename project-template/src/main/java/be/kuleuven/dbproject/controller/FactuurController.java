package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Factuur;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.UserApi;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FactuurController {
    
    @FXML
    private TableColumn<Factuur,Integer> factuurIDColumn;

    @FXML
    private TableColumn<Factuur,String> nameColumn;

    @FXML
    private TableColumn<Factuur,Double> priceColumn;

    @FXML
    private TableView<Factuur> tblFactuur;

    private User user;

    public void initialize(){
        factuurIDColumn.setCellValueFactory(new PropertyValueFactory<Factuur,Integer>("factuurID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Factuur,String>("gameName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Factuur,Double>("gamePrice"));
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setTable(DbConnection dbConnection){
        UserApi userApi = new UserApi(dbConnection);
        var factuurList = userApi.getFactuurForUser(user);
        for(Factuur factuur: factuurList){
            factuur.updateFactuur();
        }
        tblFactuur.getItems().setAll(factuurList);
    }

}
