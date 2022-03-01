import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;


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
        Button buttonFour = new Button("Accueil");

        label.setAlignment(Pos.CENTER);

        label.getChildren().addAll(buttonOne,buttonTwo,buttonThree, buttonFour); //Tous les boutons dans le label
        Scene scene = new Scene(label, 720, 630); //Fenetre principal + boutons

        this.menuCreateShoppingList = new MenuCreateShoppingList(primaryStage);
        buttonTwo.setOnAction(e-> menuCreateShoppingList.displayMenuCreateShoppingList());
        buttonFour.setOnAction(e->{try{
                                        MainController mainController = new MainController();
                                        mainController.displayMain(primaryStage);
                                    }catch (Exception exception){
                                        exception.printStackTrace();}
                                });
        primaryStage.setTitle("Mon Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
