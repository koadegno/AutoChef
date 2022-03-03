package ulb.infof307.g01;

import ulb.infof307.g01.cuisine.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Vector;

public class MenuCreateShoppingList{

    private final Stage primaryStage;
    private final MenuAddIngredient menuAddIngredient;
    private final Button btnReturnOldMenu;
    private TableView table = new TableView();
    private Vector<Product> myIngredient = new Vector<>();


    public MenuCreateShoppingList(Stage _primaryStage, Button btnReturnOldMenu){
        this.btnReturnOldMenu = btnReturnOldMenu;
        this.primaryStage = _primaryStage;
        menuAddIngredient =  new MenuAddIngredient(primaryStage);
    }

    public void displayMenuCreateShoppingList(){
        myIngredient = createListIngredient(); //TODO: base de donnée ici

        VBox label = new VBox();
        createTableList();

        Button btnAddProduct = new Button("Ajouter un produit");
        btnAddProduct.setOnAction(e->{menuAddIngredient.addProdcut(table);;});

        Button btnValid = new Button("Valide");
        btnValid.setOnAction(e-> {getIngredient();});

        label.getChildren().addAll(table,btnAddProduct, btnReturnOldMenu,btnValid);
        Scene scene = new Scene(label, 720, 630); //Fenetre pour creer des courses
        primaryStage.setTitle("Créer une liste de courses");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void createTableList() {
        table.setEditable(false);

        TableColumn firstNameCol = new TableColumn("Produit");
        TableColumn lastNameCol = new TableColumn("Quantité");

        firstNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));

        myIngredient.add(new Product("hey",322));
        myIngredient.add(new Product("zerrrrrrze",32));

        final ObservableList<Product> data = FXCollections.observableArrayList(myIngredient);
        table.setItems(data); //TODO: remplir la table de produit avec la bdd
        table.getColumns().addAll(firstNameCol, lastNameCol);

    }

    private Vector<Product> createListIngredient(){ //fonction test
        myIngredient.add(new Product("tomate", 23));
        myIngredient.add(new Product("fraise", 22));
        myIngredient.add(new Product("pomme", 2));
        return myIngredient;
    }

    private void getIngredient(){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox();
        dialog.setTitle("Confirmer avec le nom de la liste de courses");

        TextField nameOfList = new TextField();
        Button btnConfirm = new Button("Confirmer");
        dialogVbox.getChildren().addAll(nameOfList, btnConfirm);

        btnConfirm.setOnAction(e-> {confirmProduct(nameOfList);dialog.close();});

        Scene dialogScene = new Scene(dialogVbox, 500, 300);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private void confirmProduct(TextField nameOfList) {
        String getNameOfList = nameOfList.getText(); //recup nom de la liste de course

        if(getNameOfList != ""){ //TODO: verif le nom avec la bdd aprees recup le nom?

            for (int i=0; i < table.getItems().size(); i++){
                Product product = (Product) table.getItems().get(i);
                myIngredient.add(product);
                System.out.println(product.getName());
            }

            //TODO: envoyer a la base de donnée la liste avec le nom
        }

        btnReturnOldMenu.fire(); //retourner au menu precedent
    }
}
