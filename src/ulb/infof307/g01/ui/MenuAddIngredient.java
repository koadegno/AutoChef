package ulb.infof307.g01.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ulb.infof307.g01.cuisine.Product;
import static java.lang.Integer.parseInt;

public class MenuAddIngredient {

    private Stage primaryStage;
    private Product myProduct;

    public MenuAddIngredient(Stage _primaryStage){
        this.primaryStage = _primaryStage;
    }

    public void addProdcut(TableView table) {
        final Stage dialogAddProduct = new Stage();
        dialogAddProduct.initModality(Modality.APPLICATION_MODAL);
        dialogAddProduct.initOwner(primaryStage);
        VBox dialogVbox = new VBox();
        dialogAddProduct.setTitle("Choisir un produit");

        ToggleGroup quantityOrNumber = new ToggleGroup(); //bouton choix du type de valeur
        RadioButton quantity = new RadioButton("Quantité (kg)");
        quantity.setToggleGroup(quantityOrNumber);
        quantity.setSelected(true);
        RadioButton numberOfPiece = new RadioButton("Nombre de pièce");
        numberOfPiece.setToggleGroup(quantityOrNumber);

        TextField textNumberOrQuantity = new TextField();
        textNumberOrQuantity.setPromptText("valeur numérique du produit");

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

        ComboBox<String> listProduct = new ComboBox<>(); //barre de recherche
        listProduct.setEditable(true);

        //TODO; Appeler la base de donnée pour remplir la comboBox
        listProduct.setItems(FXCollections.observableArrayList(createListIngredient()));

        listProduct.getEditor().textProperty().addListener(observable -> {
            //TODO: afficher direct qd une lettre est noté?
            List<String> myProduct =  createListIngredient(); //TODO: remplacer par db
            listProduct.setItems(FXCollections.observableList(myProduct));
        });

        Button btnConfirm = new Button("Confirmer");
        dialogVbox.getChildren().addAll(listProduct, quantity, numberOfPiece,textNumberOrQuantity, btnConfirm);
        btnConfirm.setOnAction(e-> {
                //recupere les informations du produit
                addProdcutInTheTable(table, listProduct.getEditor().textProperty().getValue(), textNumberOrQuantity.getText());
                dialogAddProduct.close();
                });

        Scene dialogScene = new Scene(dialogVbox, 700, 600);
        dialogAddProduct.setScene(dialogScene);
        dialogAddProduct.show();

        //TODO: RAJOUTER BOUTON + ET -

    }
    private void addProdcutInTheTable(TableView table, String chooseNameProduct, String chooseNumberProduct ){
        if(!Objects.equals(chooseNameProduct, "") && !Objects.equals(chooseNumberProduct, "")) {
            int toIntchooseNumberProduct = parseInt(chooseNumberProduct);
            myProduct = new Product(chooseNameProduct, toIntchooseNumberProduct);
            table.getItems().add(myProduct);
        }
    }

    private List<String> createListIngredient(){ //fonction test
        List<String> ingredient = new ArrayList<String>();
        ingredient.add("Tomate");
        ingredient.add("Fraise");
        return ingredient;
    }


}
