package be.kuleuven.dbproject.controller;


import java.util.List;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.api.GenreApi;
import be.kuleuven.dbproject.model.api.UitgeverApi;
import be.kuleuven.dbproject.model.api.WinkelApi;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.enums.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VerkoopbaarAddController {

    @FXML
    private Button submitVerkoopbaarBtn, viewVerkoopbaarPageBtn;

    @FXML
    private ComboBox<Console> consoleDropDown; //Todo: maken in functie

    @FXML
    private ComboBox<Type> typeDropDown;
    
    @FXML
    private ComboBox<String> winkelDropDown, uitgeverIDDropDown, genreIDDropDown; //Todo: genre weg halen

    @FXML
    private TextField aantalStock, aantalUitgeleend, ID, naam, kostPijs; //TODO: wtf doet ID hier

    @FXML
    private TextArea beschrijving;

    private List<Winkel> winkels;

    private DbConnection dbConnection;

    private VerkoopbaarController verkoopbaarController;

    private boolean update;

    private VerkoopbaarInterface verkoopbaar;

    private List<Uitgever> uitgevers;

    private String product;

    private VerkoopbaarInterface tempVerkoopbaar;

    public void initialize(){        
        submitVerkoopbaarBtn.setOnAction(e -> {
                if(update){
                    updateVerkoopbaar();
                }else{
                    submitVerkoopbaar();
                }
                
            }
        );

        viewVerkoopbaarPageBtn.setOnAction(e -> changeWindowToMoreInfo("moreinfogame"));

        viewVerkoopbaarPageBtn.setDisable(update);

        beschrijving.setWrapText(true); 
    }

    public void changeWindowToMoreInfo(String id){
        try{
            var resourceName = id+".fxml";

            var stage = new Stage();
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            var root = loader.load();

            if(product == "Game"){ //TODO: fixen naar verkoopbaarmoreinfo
                var controller = (MoreInfoGameController) loader.getController();
                controller.setdbConnection(dbConnection);
                controller.setGame(verkoopbaar);
            } else if (product == "Extra"){
                var controller = (MoreInfoExtraController) loader.getController();
                controller.setDbConnection(dbConnection);
                controller.setExtra((Extra) verkoopbaar);
            }

            var scene = new Scene((Parent) root);
            stage.setScene(scene);
            stage.setTitle("budo/"+ id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setupDropDown(DbConnection dbConnection){
        var winkelApi = new WinkelApi(dbConnection);
        var genreApi = new GenreApi(dbConnection);
        var uitgeverApi = new UitgeverApi(dbConnection);

        winkels = winkelApi.getWinkels();
        var genres = genreApi.getGenres(); //TODO: globale parameter van maken?
        uitgevers = uitgeverApi.getUitgevers();


        ObservableList<String> listGenreNames = FXCollections.observableArrayList();
        ObservableList<String> listWinkelNames = FXCollections.observableArrayList(); //TODO: loopt miss mis als null is bij erxtra
        ObservableList<String> listUitgeverNames = FXCollections.observableArrayList(); 

        for(Uitgever uitgever: uitgevers){
            listUitgeverNames.addAll(uitgever.getNaam());
        }

        for (Genre genre : genres) {
            listGenreNames.add(genre.getNaam());
        }

        for (Winkel winkel : winkels) {
            listWinkelNames.add(winkel.getFullAdressWithID());
        }

        if(product == "Game"){
            ObservableList<Console> listInstance = FXCollections.observableArrayList();
            listInstance.addAll(Console.values());
            consoleDropDown.setItems(listInstance);
            
            typeDropDown.setVisible(false);
        } 
        
        else if(product == "Extra"){
            ObservableList<Type> listInstance = FXCollections.observableArrayList(); 
            listInstance.addAll(Type.values()); 
            typeDropDown.setItems(listInstance); 
            
            consoleDropDown.setVisible(false);
            genreIDDropDown.setVisible(false);
            beschrijving.setDisable(true);
        }
        

        uitgeverIDDropDown.setItems(listUitgeverNames);
        genreIDDropDown.setItems(listGenreNames);
        winkelDropDown.setItems(listWinkelNames);
    }

    public void initializeUpdate(VerkoopbaarInterface verkoopbaar){
        aantalStock.setText(Integer.toString(verkoopbaar.getStock()));
        kostPijs.setText(Double.toString(verkoopbaar.getKostPrijs()));
        naam.setText(verkoopbaar.getNaam());
        aantalUitgeleend.setText(Integer.toString(verkoopbaar.getVerkocht()));

        winkelDropDown.setValue(verkoopbaar.getWinkel().getFullAdressWithID());

        if(product == "Game"){
            consoleDropDown.setValue(((Game)verkoopbaar).getConsole());
            genreIDDropDown.setValue(((Game)verkoopbaar).getGenre().getNaam());
            beschrijving.setText(((Game)verkoopbaar).getBeschrijving());
        } else if(product == "Extra"){
            typeDropDown.setValue(((Extra)verkoopbaar).getType()); 
        }

        uitgeverIDDropDown.setValue(verkoopbaar.getUitgever().getNaam());
        this.verkoopbaar = verkoopbaar; //TODO: might give error, was (Game) typecast
    }

    private void updateVerkoopbaar(){ //TODO: aantal verkocht is niet het enige dat niet aangepast kan worden (vb winkel), checken, implementeren of verwijderen
        if(product == "Game"){
            ((Game)verkoopbaar).setBeschrijving(this.beschrijving.getText()); //TODO: misschien uit halen
            ((Game)verkoopbaar).setConsole((Console) consoleDropDown.getValue());
           
            var genre = (String) genreIDDropDown.getValue();
            var genreApi = new GenreApi(dbConnection);
            ((Game)verkoopbaar).setGenre(genreApi.getGenreByName(genre));

        } else if(product == "Extra"){
            ((Extra)verkoopbaar).setType((Type) typeDropDown.getValue());
        }

        verkoopbaar.setKostPrijs(Double.parseDouble(this.kostPijs.getText()));
        verkoopbaar.setNaam(this.naam.getText());
        verkoopbaar.setStock(Integer.parseInt(this.aantalStock.getText()));
        
        var uitgeverName = (String) uitgeverIDDropDown.getValue();
        var uitgeverApi = new UitgeverApi(dbConnection);
        verkoopbaar.setUitgever(((Uitgever) uitgeverApi.getUitgeverByName(uitgeverName)));

        verkoopbaarController.updateOrSearchTable(false); //TODO: huh

        var window = (Stage) beschrijving.getScene().getWindow();
        window.close();
    }

    private boolean submitVerkoopbaar(){

        var aantalStock = Integer.parseInt(this.aantalStock.getText());
        var kostPrijs = Double.parseDouble(this.kostPijs.getText());
        var naam = this.naam.getText();
        var beschrijving = this.beschrijving.getText();
        Winkel winkel = null;
        Uitgever uitgever = null;
        String nameWinkel = (String) winkelDropDown.getValue();
        String nameUitgever = (String) uitgeverIDDropDown.getValue(); 

        for (Winkel testwinkel : winkels) {
            if(nameWinkel.contains(testwinkel.getFullAdressWithID())){
                winkel = testwinkel;
                break;
            }
        } 

        for(Uitgever testuitgever: uitgevers){
            if(nameUitgever.contains(testuitgever.getNaam())){
                uitgever = testuitgever;
                break;
            }
        }

        if(product == "Game"){
            Console console = (Console) consoleDropDown.getValue();
            var genreNaam = (String) genreIDDropDown.getValue();
            var genreApi = new GenreApi(dbConnection);
            var genre = genreApi.getGenreByName(genreNaam);
            tempVerkoopbaar = new Game(aantalStock, 0, console, 0,winkel, kostPrijs, genre, naam, beschrijving, uitgever);

            try {
            var verkoopbaarApi = new GameApi(dbConnection);

            verkoopbaarApi.postVerkoopbaar(tempVerkoopbaar);

            verkoopbaarController.updateOrSearchTable(true); //TODO: huh

            var stage = (Stage) submitVerkoopbaarBtn.getScene().getWindow();
            stage.close();

            return true;
            } catch (Exception e) {
                System.out.println("er is iet misgegaan met de game "+naam);
                System.out.println(e);
                return false;
            }
        } else if(product == "Extra"){
            Type type = (Type) typeDropDown.getValue();
            tempVerkoopbaar= new Extra(aantalStock, 0, 0, winkel, uitgever, kostPrijs, type, naam);

            try {
            var verkoopbaarApi = new ExtraApi(dbConnection);

            verkoopbaarApi.postVerkoopbaar(tempVerkoopbaar);

            verkoopbaarController.updateOrSearchTable(true);

            var stage = (Stage) submitVerkoopbaarBtn.getScene().getWindow();
            stage.close();

            return true;
            } catch (Exception e) {
                System.out.println("er is iet misgegaan met de extra "+naam);
                System.out.println(e);
                return false;
            }
        }
        //logica voor te zien of iets null is of niet 
        System.out.println("geen verkoopbaar geselecteerd");
        return false;
    }

    public void setParentController(VerkoopbaarController verkoopbaarController){
        this.verkoopbaarController = verkoopbaarController;
    }

    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    public void setUpdate(boolean update){
        this.update = update;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
