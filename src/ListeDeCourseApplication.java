import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class ListeDeCourseApplication {

    private final Stage primaryStage;
    private MenuCreateShoppingList menuCreateShoppingList;

    public ListeDeCourseApplication(Stage _primaryStage){
        this.primaryStage = _primaryStage;

    }

    protected void menu(){
        VBox label = new VBox();

        Button buttonOne = new Button("Mes listes de courses");
        Button buttonTwo = new Button("Créer une liste de courses");
        Button buttonThree = new Button("Modifier une liste de courses");

        label.setAlignment(Pos.CENTER);

        label.getChildren().addAll(buttonOne,buttonTwo,buttonThree); //Tous les boutons dans le label
        Scene scene = new Scene(label, 720, 630); //Fenetre principal + boutons

        Button btnReturnCreateShoppingList = new Button("Retour");
        btnReturnCreateShoppingList.setOnAction(e-> { menu();});

        this.menuCreateShoppingList = new MenuCreateShoppingList(primaryStage, btnReturnCreateShoppingList);
        buttonTwo.setOnAction(e-> {menuCreateShoppingList.displayMenuCreateShoppingList();});

        primaryStage.setTitle("Mon Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
