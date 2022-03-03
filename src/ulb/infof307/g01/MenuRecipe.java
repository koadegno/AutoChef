package ulb.infof307.g01;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuRecipe {
    private final Stage primaryStage;

    public MenuRecipe(Stage _primaryStage){
        this.primaryStage = _primaryStage;

    }

    public void menu(){
        VBox label = new VBox();
        //Boutons
        Button buttonViewRecipe = new Button("Mes recettes");
        Button buttonCreateRecipe = new Button("Créer une recette");
        Button buttonReturnMenu = new Button("Retour");
        //Actions sur les boutons
        buttonReturnMenu.setOnAction(e-> { menu();});
        buttonViewRecipe.setOnAction(e-> {this.viewRecipe();});
        buttonCreateRecipe.setOnAction(e-> {this.createRecipe();});
        //
        label.setAlignment(Pos.CENTER);
        label.getChildren().addAll(buttonViewRecipe,buttonCreateRecipe,buttonReturnMenu); //Tous les boutons dans le label

        Scene scene = new Scene(label, 720, 630); //Fenetre principal + boutons


        primaryStage.setTitle("Menu recette");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void createRecipe(){
    }

    private void viewRecipe() {
    }

    private void addProduct(){
    }
}
