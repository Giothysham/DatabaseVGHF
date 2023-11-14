package be.kuleuven.dbproject.controller;

import java.util.ArrayList;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.api.DbConnection;
import javafx.collections.ObservableList;

public class BuySchermController {

    private DbConnection dbConnection;

    private ObservableList wantToRentList;

    public void setdbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    //implimenteren. 

    public void setWantToRent(ObservableList wantToRentList2) {
        this.wantToRentList = wantToRentList2;
        System.out.println("_______________________________");
        System.out.println(wantToRentList2);
        System.out.println("_______________________________");
    }
}
