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
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MenuCreateShoppingList{

    private final Stage primaryStage;
    private MenuAddIngredient menuAddIngredient;
    private final Button btnReturnOldMenu;
    private TableView table = new TableView();
    private Vector<Product> myIngredient = new Vector<>();


    public MenuCreateShoppingList(Stage _primaryStage, Button btnReturnOldMenu){
        this.btnReturnOldMenu = btnReturnOldMenu;
        this.primaryStage = _primaryStage;
        menuAddIngredient =  new MenuAddIngredient(primaryStage, myIngredient);
    }

    public void displayMenuCreateShoppingList(){
        myIngredient = createListIngredient(); //base de donnée ici

        VBox label = new VBox();
        createTableList();
        Button btnAddProduct = new Button("Ajouter un produit");


        btnAddProduct.setOnAction(e->{
            menuAddIngredient.addProdcut(); //affichage du menu pour rajouter un produit
            myIngredient = menuAddIngredient.getMyIngredient(); //TODO: remplacer par bdd
            setTable(myIngredient);});


        Button btnValid = new Button("Valide");
        btnValid.setOnAction(e-> {
            getIngredient();
            //TODO: recup les infos -> la liste le fait mais pour etre sur plus tard essayer de recup
            //direct du tableView mais pas ici qu'on envoie a la bdd
        });

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

        firstNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("produc"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));

        setTable(myIngredient); //remplir la table de produit
        table.getColumns().addAll(firstNameCol, lastNameCol);

    }

    public void setTable(Vector<Product> myIngredient){
        final ObservableList<Product> data = FXCollections.observableArrayList(myIngredient);
        table.setItems(data);
    }

    private Vector<Product> createListIngredient(){ //fonction test
        myIngredient.add(new Product("tomate", "23.2"));
        myIngredient.add(new Product("fraise", "22.2"));
        myIngredient.add(new Product("pomme", "0.2"));
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

        btnConfirm.setOnAction(e-> {
                String getNameOfList = nameOfList.getText(); //recup nom de la liste de course
                btnReturnOldMenu.fire(); //retourner au menu precedent
                dialog.close();
                //TODO: verif le nom avec la bdd aprees recup le nom?
                //TODO: envoyer la liste a la bdd avec nom
        });

        Scene dialogScene = new Scene(dialogVbox, 500, 300);
        dialog.setScene(dialogScene);
        dialog.show();

    }
}
