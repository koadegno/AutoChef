import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ListeDeCourseApplication {

    private Stage _primaryStage;
    private MenuCreateShoppingList menuCreateShoppingList;

    public ListeDeCourseApplication(Stage primaryStage){
        this._primaryStage = primaryStage;
    }

    protected void menu(Stage primary){
        Stage primaryStage = this._primaryStage;
        VBox label = new VBox(10);
        Button buttonOne = new Button("Mes listes de courses");
        Button buttonTwo = new Button("Créer une liste de courses");
        Button buttonThree = new Button("Modifier une liste de courses");

        label.setAlignment(Pos.CENTER);

        label.getChildren().addAll(buttonOne,buttonTwo,buttonThree); //Tous les boutons dans le label
        Scene scene = new Scene(label, 720, 630); //Fenetre principal + boutons

        this.menuCreateShoppingList = new MenuCreateShoppingList(primaryStage);
        buttonTwo.setOnAction(e-> {menuCreateShoppingList.displayMenuCreateShoppingList();});

        primaryStage.setTitle("Mon Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private List<String> createListIngredient(){ //fonction test
        List<String> ingredient = new ArrayList<String>();
        ingredient.add("Tomate");
        ingredient.add("Fraise");
        return ingredient;
    }

    protected void readFile() { //je me suis cassée la tete pour rien
        String file = "src/test.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
