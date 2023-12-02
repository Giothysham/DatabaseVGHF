package be.kuleuven.dbproject.controller;


import java.util.List;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import be.kuleuven.dbproject.model.enums.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExtraAddController {

    @FXML
    private Button submitExtraBtn;

    @FXML
    private ComboBox typeDropDown, winkelDropDown, uitgeverIDDropDown;

    @FXML
    private TextField aantalStock, aantalVerkocht, extraID, naam, kostPijs;

    @FXML
    private TextArea beschrijving;

    private List<Winkel> winkels;

    private DbConnection dbConnection;

    private ExtraController extraController;

    private boolean update;

    private Extra extra;

    private List<Uitgever> uitgevers;

    public void initialize(){        
        submitExtraBtn.setOnAction(e -> {
                if(update){
                    updateExtra();
                }else{
                    submitExtra();
                }
                
            }
        );

        // beschrijving.setWrapText(true);
    }

    public void setupDropDown(DbConnection dbConnection){
        var winkelApi = new WinkelApi(dbConnection);
        var uitgeverApi = new UitgeverApi(dbConnection);

        winkels = winkelApi.getWinkels();
        uitgevers = uitgeverApi.getUitgevers();

        ObservableList<String> listWinkelNames = FXCollections.observableArrayList();
        ObservableList<String> listUitgeverNames = FXCollections.observableArrayList(); 

        for(Uitgever uitgever: uitgevers){
            listUitgeverNames.addAll(uitgever.getNaam());
        }

        for (Winkel winkel : winkels) {
            listWinkelNames.add(winkel.getFullAdressWithID());
        }

        ObservableList<Type> listInstance = FXCollections.observableArrayList(); 
        listInstance.addAll(Type.values()); 
        typeDropDown.setItems(listInstance);

        uitgeverIDDropDown.setItems(listUitgeverNames);
        winkelDropDown.setItems(listWinkelNames);
    }

    public void initializeUpdate(Extra extra){
        aantalStock.setText(Integer.toString(extra.getStock()));
        kostPijs.setText(Double.toString(extra.getKostprijs()));
        naam.setText(extra.getNaam());
        aantalVerkocht.setText(Integer.toString(extra.getVerkocht()));

        WinkelApi winkelApi = new WinkelApi(dbConnection);
        var winkel = winkelApi.getWinkelById(extra.getWinkelID());
        winkelDropDown.setValue(winkel.getFullAdressWithID());

        typeDropDown.setValue(extra.getType());
        
        UitgeverApi uitgeverApi = new UitgeverApi(dbConnection);
        var uitgever = uitgeverApi.getUitgeverById(extra.getUitgeverID());
        uitgeverIDDropDown.setValue(uitgever.getNaam());
        this.extra = extra;
    }

    private void updateExtra(){
        extra.setType((Type) typeDropDown.getValue());

        extra.setKostprijs(Double.parseDouble(this.kostPijs.getText()));
        extra.setNaam(this.naam.getText());
        extra.setStock(Integer.parseInt(this.aantalStock.getText()));

        var uitgeverName = (String) uitgeverIDDropDown.getValue();
        var uitgeverApi = new UitgeverApi(dbConnection);
        extra.setUitgever(((Uitgever) uitgeverApi.getUitgeverByName(uitgeverName)).getUitgeverID());

        var window = (Stage) typeDropDown.getScene().getWindow();
        window.close();
    }

    private boolean submitExtra(){

        var aantalStock = Integer.parseInt(this.aantalStock.getText());
        Type type = (Type) typeDropDown.getValue();
        var kostPrijs = Double.parseDouble(this.kostPijs.getText());
        var naam = this.naam.getText();
        Integer winkelID = null;
        Integer uitgeverID = null;
        String nameWinkel = (String) winkelDropDown.getValue();
        String nameUitgever = (String) uitgeverIDDropDown.getValue(); 

        for (Winkel winkel : winkels) {
            if(nameWinkel.contains(winkel.getFullAdressWithID())){
                winkelID = winkel.getWinkelID();
                break;
            }
        }

        for(Uitgever uitgever: uitgevers){
            if(nameUitgever.contains(uitgever.getNaam())){
                uitgeverID = uitgever.getUitgeverID();
                break;
            }
        }
        
        //logica voor te zien of iets null is of niet 
        Extra tempextra= new Extra(aantalStock, 0, 0, winkelID, uitgeverID, kostPrijs, type, naam);
        
        try {
            var extraApi = new ExtraApi(dbConnection);

            extraApi.postExtra(tempextra);

            extraController.updateOrSearchTable(true);

            var stage = (Stage) submitExtraBtn.getScene().getWindow();
            stage.close();

            return true;
        } catch (Exception e) {
            System.out.println("er is iet misgegaan met de extra "+naam);
            System.out.println(e);
            return false;
        }
        
    }

    public void setParentController(ExtraController extraController){
        this.extraController = extraController;
    }

    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    public void setUpdate(boolean update){
        this.update = update;
    }
}
