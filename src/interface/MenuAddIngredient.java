import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MenuAddIngredient {

    private Stage primaryStage;
    private Vector<Product> myIngredient = new Vector<>();

    public MenuAddIngredient(Stage _primaryStage){
        this.primaryStage = _primaryStage;
    }

    public void addProdcut() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialog.setTitle("Choisir un produit");

        ToggleGroup quantityOrNumber = new ToggleGroup();
        RadioButton quantity = new RadioButton("Quantité (kg)");
        quantity.setToggleGroup(quantityOrNumber);
        quantity.setSelected(true);
        RadioButton numberOfPiece = new RadioButton("Nombre de pièce");
        numberOfPiece.setToggleGroup(quantityOrNumber);

        TextField textNumberOrQuantity = new TextField();
        textNumberOrQuantity.textProperty().addListener(new ChangeListener<String>() { //Seulement écrire des nombres
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                String floatOrInt = "[0-9\\d.]"; //TODO: arranger ici parce qu'on peut mettre bcp de .........
                if(numberOfPiece.isSelected()){
                    floatOrInt = "\\d*";
                }

                if (!newValue.matches(floatOrInt)) {
                    textNumberOrQuantity.setText(newValue.replaceAll("[^"+floatOrInt+"]", ""));
                }
            }
        });

        ComboBox<String> listProduct = new ComboBox<>();
        listProduct.setEditable(true);
        //Appeler la base de donnée
        listProduct.setItems(FXCollections.observableArrayList(("tomate"), ("toomate"), ("salade")));

        listProduct.getEditor().textProperty().addListener(observable -> {
            List<String> myProduct =  createListIngredient(); //TODO: remplacer par db
            listProduct.setItems(FXCollections.observableList(myProduct));


        });

        dialogVbox.getChildren().addAll(listProduct);
        Button btnConfirm = new Button("Confirmer"); //TODO: lier avec nom apres
        dialogVbox.getChildren().addAll(quantity, numberOfPiece,textNumberOrQuantity, btnConfirm);
        btnConfirm.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String number = "33"; // Double.parseDouble(textNumberOrQuantity.getText());
                        String ingredient = listProduct.getItems().toString();
                        Product prod = new Product("hhezhr", "hjdez");
                        myIngredient.add(prod);
                        dialog.close();
                        //BOUTON VALID POUR RETOUR
                        //onAddIngredientButtonClick(primaryStage, myIngredient);





                        //TODO: afficher le produit dans

                    }
                });


        Scene dialogScene = new Scene(dialogVbox, 700, 600);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private List<String> createListIngredient(){ //fonction test
        List<String> ingredient = new ArrayList<String>();
        ingredient.add("Tomate");
        ingredient.add("Fraise");
        return ingredient;
    }

    public Vector<Product> getMyIngredient(){
        return myIngredient;
    }

}
