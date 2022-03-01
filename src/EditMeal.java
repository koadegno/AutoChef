import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class EditMeal {
    Button button;
    Button mealModifier;
    TableView mealTable = new TableView<>();

    public void calender(Stage primaryStage){
        VBox meaLayout = new VBox();
        meaLayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(meaLayout, 900, 750);
        mealModifier = new Button("Modifier Menus");

        TableColumn lundi = new TableColumn("Lundi");
        TableColumn mardi = new TableColumn("Mardi");
        TableColumn mercredi = new TableColumn("Mercredi");
        TableColumn jeudi = new TableColumn("Jeudi");
        TableColumn vendredi = new TableColumn("Vendredi");
        TableColumn samedi = new TableColumn("Samedi");
        TableColumn dimanche = new TableColumn("Dimanche");

        mealTable.getColumns().addAll(lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
        meaLayout.getChildren().addAll(mealModifier,mealTable); //, meaList);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void choseDay(Stage primaryStage){
        ComboBox<String> meaList = new ComboBox<>();
        meaList.setEditable(true);
        VBox meaLayout = new VBox();
        meaLayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(meaLayout, 900, 750);
        //Appeler la base de données

        meaList.setItems(FXCollections.observableArrayList(("Pasta"), ("Riz"), ("Hamburger"), ("Frites"),
                ("Fruits de Mer"), ("Fruits de Ciel"), ("Pommes de Terre"), ("Pommes de Ciel"),
                ("Patates douce"), ("Pommes mechante")));
        meaList.getEditor().textProperty().addListener(observable -> {
            String text1 = meaList.getEditor().textProperty().getValue();
            //System.out.println("input : " + text1);
            /*
            Permet de recup la lettre entree pour ensuite demander a la bdd d'envoyer les
            repas commencant par cette lettre
            */
        });
        meaLayout.getChildren().addAll(meaList); //, meaList);
        //mealModifier.setOnAction(e-> {choseDay(meaList.getEditor().textProperty().getValue());});
        primaryStage.setScene(scene);
    }

    public EditMeal(Stage primaryStage) {
        //public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Creer/Afficher un menu");
        button = new Button("Creer un menu");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        button.setOnAction(e-> {calender(primaryStage);});

        Scene scene = new Scene(layout, 900, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}