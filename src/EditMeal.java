import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;

public class EditMeal {
    private final Button btnReturnOldMenu;
    //private final MainController mainController;
    /*
    public MainController(Stage _primaryStage, Button btnReturnOldMenu){
        this.btnReturnOldMenu = btnReturnOldMenu;
        this.primaryStage = _primaryStage;
        menuAddIngredient =  new MenuAddIngredient(primaryStage);
    }
    */
    public EditMeal(Stage primaryStage) {
        primaryStage.setTitle("Modifier le calendrier de menu");

        VBox calenderBox = new VBox();
        calenderBox.setAlignment(Pos.CENTER_LEFT);
        TableView calenderView = new TableView<>();
        TableColumn lundi = new TableColumn("Lundi");
        TableColumn mardi = new TableColumn("Mardi");
        TableColumn mercredi = new TableColumn("Mercredi");
        TableColumn jeudi = new TableColumn("Jeudi");
        TableColumn vendredi = new TableColumn("Vendredi");
        TableColumn samedi = new TableColumn("Samedi");
        TableColumn dimanche = new TableColumn("Dimanche");
        calenderView.getColumns().addAll(lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
        calenderBox.getChildren().addAll(calenderView);

        HBox buttonBox = new HBox();
        Button editBtn = new Button("Modifier");
        btnReturnOldMenu = new Button("Retour");
        //editBtn.setOnAction(e-> {calender(primaryStage);});
        //backBtn.setOnAction(this);
        buttonBox.getChildren().addAll(editBtn, btnReturnOldMenu);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(calenderBox);
        borderPane.setBottom(buttonBox);

        Scene scene = new Scene(borderPane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}