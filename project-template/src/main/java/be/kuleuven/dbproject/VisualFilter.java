package be.kuleuven.dbproject;

import be.kuleuven.dbproject.interfaces.BuyScreenInterface;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.GameApi;
import be.kuleuven.dbproject.model.enums.Console;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VisualFilter<T> {

    private T usedFilter;

    private HBox visualFilterHbox;

    public VisualFilter(T filter){
        this.usedFilter = filter;
    }
    
    public HBox getVisualFilter(GameApi gameApi,BuyScreenInterface parentController){

        visualFilterHbox = new HBox();
        visualFilterHbox.setSpacing(5.0);

        var button = new Button();
        button.setText("X");

        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        var text = new Text();

        if(usedFilter.getClass() == Winkel.class){
            text.setText(((Winkel) usedFilter).getSmallAdress());
        }
        else if(usedFilter.getClass() == Console.class){
            text.setText(((Console) usedFilter).toString());
        }
        else if(usedFilter.getClass() == Genre.class){
            text.setText(((Genre) usedFilter).getNaam());
        }

        button.setOnAction(e -> {
            gameApi.removeFilterByClass(usedFilter);
            parentController.getScrlPaneFilters().getChildren().remove(visualFilterHbox);
            parentController.updateOrSearchTable(false);
            gameApi.removeFilterByClass(usedFilter);
        });

        vbox.getChildren().add(text);

        button.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white;");

        visualFilterHbox.getChildren().addAll(vbox, button);

        //nog bekijken geeft null

        System.out.println("_________________________________________"+visualFilterHbox);

        return visualFilterHbox;
    }

    public HBox getVisualFilterHbox(){
        return this.visualFilterHbox;
    }

    public T getUsedFilter(){
        return usedFilter;
    }
}
