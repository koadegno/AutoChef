package com.example.liste_de_courses;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class ListeDeCourseApplication extends Application  {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        menu(primaryStage);


    }
    private void menu(Stage primaryStage){
        VBox label = new VBox(10);
        Button buttonOne = new Button("Mes listes de courses");
        Button buttonTwo = new Button("Créer une liste de courses");
        Button buttonThree = new Button("Modifier une liste de courses");

        label.setAlignment(Pos.CENTER);

        label.getChildren().addAll(buttonOne,buttonTwo,buttonThree); //Tous les boutons dans le label
        Scene scene = new Scene(label, 720, 630); //Fenetre principal + boutons

        Vector<Product> myIngredient = null;
        buttonTwo.setOnAction(e-> {onAddIngredientButtonClick(primaryStage, myIngredient);});

        primaryStage.setTitle("Mon Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onAddIngredientButtonClick(Stage primaryStage, Vector<Product> myIngredient) {
        List<String> ingredien = createListIngredient(); //remplacer par la bdd

        Button btnAddProduct = new Button("Ajouter un produit");
        Button btnConfirm = new Button("Confirmer");
        VBox label = new VBox(ingredien.size());
        //btnConfirm.setId()
        label.getChildren().addAll(btnAddProduct, btnConfirm);

        btnAddProduct.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addProdcut(primaryStage);

                    }
                });

        Button btnReturn = new Button("Retour");
        btnReturn.setOnAction(e-> {menu(primaryStage);});

        Button btnValid = new Button("Valide");
        btnValid.setOnAction(e-> {getIngredient(primaryStage);});

        TableView table = createTableList(myIngredient);

        label.getChildren().addAll(table, btnReturn,btnValid);
        Scene scene = new Scene(label, 720, 630); //Fenetre pour creer des courses
        primaryStage.setTitle("Créer une liste de courses");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    private TableView createTableList(Vector<Product> myIngredient) {
        TableView table = new TableView();
        table.setEditable(false);

        TableColumn firstNameCol = new TableColumn("Produc");
        TableColumn lastNameCol = new TableColumn("Quantity");


        firstNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("produc"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));


        System.out.println("JE SUUUUUUUUUUUIS LA");
        final ObservableList<Product> data = FXCollections.observableArrayList(
                new Product("tomate", "23.2"));
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol);

        //table.getItems().add(new Product("tomate", 23.2));

        return table;
    }

    private void addProdcut(Stage primaryStage) {
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
                        Vector<Product> myIngredient = new Vector<>();
                        String number = "33"; // Double.parseDouble(textNumberOrQuantity.getText());
                        String ingredient = listProduct.getItems().toString();
                        Product prod = new Product(ingredient, number);
                        myIngredient.add(prod);
                        onAddIngredientButtonClick(primaryStage, myIngredient);
                        dialog.close();



                        //TODO: afficher le produit dans

                    }
                });

        Scene dialogScene = new Scene(dialogVbox, 700, 600);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void getIngredient(Stage primaryStage){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox();
        dialog.setTitle("Confirmer avec le nom de la liste de courses");

        TextField nameOfList = new TextField();
        nameOfList.setText("Donne un nom de liste de courses");
        Button btnConfirm = new Button("Confirmer");
        Button btnReturn = new Button("Retour");
        dialogVbox.getChildren().addAll(nameOfList, btnReturn, btnConfirm);

        btnReturn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();

                    }
                });

        btnConfirm.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String getNameOfList = nameOfList.getText();
                        System.out.println(getNameOfList);
                        dialog.close();
                        //TODO: verif le nom avec la bdd aprees recup le nom

                    }
                });

        Scene dialogScene = new Scene(dialogVbox, 500, 300);
        dialog.setScene(dialogScene);
        dialog.show();

        //TODO: recup all info

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
