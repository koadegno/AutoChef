import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MenuCreateShoppingList extends ListeDeCourseApplication{

    private final Stage primaryStage;
    private MenuAddIngredient menuAddIngredient;
    private ListeDeCourseApplication listeDeCourseApplication;
    private TableView table = new TableView();


    public MenuCreateShoppingList(Stage _primaryStage){
        super(_primaryStage);
        this.primaryStage = _primaryStage;
    }

    public void displayMenuCreateShoppingList(){
        List<String> ingredients = createListIngredient(); //remplacer par la bdd
        Vector<Product> myIngredient = null; //envoyer a la bdd un vector

        Button btnAddProduct = new Button("Ajouter un produit");
        VBox label = new VBox(ingredients.size());
        //btnConfirm.setId()
        label.getChildren().addAll(btnAddProduct);

        TableView table = createTableList(myIngredient);


        btnAddProduct.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        menuAddIngredient = new MenuAddIngredient(primaryStage);
                        menuAddIngredient.addProdcut();
                        Vector<Product> myIngredient = menuAddIngredient.getMyIngredient();
                        final ObservableList<Product> data = FXCollections.observableArrayList(
                                new Product("fzf", 23));
                        table.setItems(data);
                        //addProdcut(primaryStage);
                        //faire la classe Product

                    }
                });

        listeDeCourseApplication = new ListeDeCourseApplication(primaryStage);
        Button btnReturn = new Button("Retour");
        btnReturn.setOnAction(e-> {
            super.menu();
            //listeDeCourseApplication.menu();
        });

        Button btnValid = new Button("Valide");
        btnValid.setOnAction(e-> {}); //getIngredient

        btnValid.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        getIngredient();
                        //TODO: recup les infos
                        listeDeCourseApplication.menu();
                        //TODO: refaire le retour

                    }
                });


        label.getChildren().addAll(table, btnReturn,btnValid);
        Scene scene = new Scene(label, 720, 630); //Fenetre pour creer des courses
        primaryStage.setTitle("Créer une liste de courses");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private TableView createTableList(Vector<Product> myIngredient) {
        table.setEditable(false);

        TableColumn firstNameCol = new TableColumn("Produc");
        TableColumn lastNameCol = new TableColumn("Quantity");


        firstNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("produc"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));


        final ObservableList<Product> data = FXCollections.observableArrayList(
                new Product("tomate", 23));
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol);

        //table.getItems().add(new Product("tomate", 23.2));

        return table;
    }

    private List<String> createListIngredient(){ //fonction test
        List<String> ingredient = new ArrayList<String>();
        ingredient.add("Tomate");
        ingredient.add("Fraise");
        return ingredient;
    }

    private void getIngredient(){
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
}
