import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class ListeDeCourseApplication {

    private final Stage primaryStage;
    private MenuCreateShoppingList menuCreateShoppingList;
    private MenuDisplayShoppingList menuDisplayShoppingList;

    public ListeDeCourseApplication(Stage _primaryStage){
        this.primaryStage = _primaryStage;

    }

    protected void menu(){
        VBox label = new VBox();

        Button buttonOne = new Button("Mes listes de courses");
        Button buttonTwo = new Button("Créer une liste de courses");

        label.setAlignment(Pos.CENTER);

        label.getChildren().addAll(buttonOne,buttonTwo); //Tous les boutons dans le label
        Scene scene = new Scene(label, 720, 630); //Fenetre principal + boutons

        Button btnReturnShoppingList = new Button("Retour");
        btnReturnShoppingList.setOnAction(e-> { menu();});

        this.menuCreateShoppingList = new MenuCreateShoppingList(primaryStage, btnReturnShoppingList);
        buttonTwo.setOnAction(e-> {menuCreateShoppingList.displayMenuCreateShoppingList();});

        this.menuDisplayShoppingList = new MenuDisplayShoppingList(primaryStage, btnReturnShoppingList);
        buttonOne.setOnAction(e-> {menuDisplayShoppingList.displayMenuDisplayShoppingList();});

        primaryStage.setTitle("Mon Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
