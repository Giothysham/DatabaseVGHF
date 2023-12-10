package be.kuleuven.dbproject;

import be.kuleuven.dbproject.interfaces.BuyScreenInterface;
import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.ExtraApi;
import be.kuleuven.dbproject.model.enums.Console;
import be.kuleuven.dbproject.model.enums.Type;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class VisualFilter<T> {

    private T usedFilter;

    private HBox visualFilterHbox;

    public VisualFilter(T filter){
        this.usedFilter = filter;
    }
    
    public HBox getVisualFilter(VerkoopbaarApiInterface verkoopbaarApi,BuyScreenInterface parentController){

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

        text.setFill(Paint.valueOf("#000000"));

        button.setOnAction(e -> {
            verkoopbaarApi.removeFilterByClass(usedFilter);
            parentController.getScrlPaneFilters().getChildren().remove(visualFilterHbox);
            parentController.updateOrSearchTable(false);
        });

        vbox.getChildren().add(text);
        visualFilterHbox.setStyle("-fx-background-color: #DBDAE5;  -fx-border-radius: 5 5 0 5; -fx-background-radius: 5 5 0 5;");

        button.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white; -fx-font-weight: bold; ");

        visualFilterHbox.getChildren().addAll(vbox, button);

        return visualFilterHbox;
    }

    public HBox getVisualFilter(ExtraApi extraApi,BuyScreenInterface parentController){

        //TODO: fix in een ding => edmond
        visualFilterHbox = new HBox();
        visualFilterHbox.setSpacing(5.0);

        var button = new Button();
        button.setText("X");

        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        var text = new Text();

        text.setFill(Paint.valueOf("#000000"));

        if(usedFilter.getClass() == Winkel.class){
            text.setText(((Winkel) usedFilter).getSmallAdress());
        }
        else if(usedFilter.getClass() == Type.class){
            text.setText(((Type) usedFilter).toString());
        }

        button.setOnAction(e -> {
            extraApi.removeFilterByClass(usedFilter);
            parentController.getScrlPaneFilters().getChildren().remove(visualFilterHbox);
            parentController.updateOrSearchTable(false);
        });

        vbox.getChildren().add(text);
        visualFilterHbox.setStyle("-fx-background-color: #DBDAE5;  -fx-border-radius: 5 5 0 5; -fx-background-radius: 5 5 0 5;");

        button.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white; -fx-font-weight: bold; ");

        button.setStyle("-fx-background-color: #FA0000; -fx-text-fill: white;");

        visualFilterHbox.getChildren().addAll(vbox, button);

        return visualFilterHbox;
    }

    public HBox getVisualFilterHbox(){
        return this.visualFilterHbox;
    }

    public T getUsedFilter(){
        return usedFilter;
    }
}
