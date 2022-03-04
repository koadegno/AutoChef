package ulb.infof307.g01.ui;

import ulb.infof307.g01.cuisine.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.Objects;
import java.util.Vector;

public class MenuDisplayShoppingList {

    private Vector<Product> myIngredient = new Vector<>();
    private Stage primaryStage;
    private Button btnReturnShoppingList;
    private TableView table = new TableView();
    private String nameListProduct;

    public MenuDisplayShoppingList(Stage primaryStage, Button btnReturnShoppingList){
        this.primaryStage = primaryStage;
        this.btnReturnShoppingList = btnReturnShoppingList;
    }

    public void displayMenuDisplayShoppingList(){
        VBox label = new VBox();

        ComboBox<String> searchBarNameListProduct = new ComboBox<>(); //barre de recherche
        searchBarNameListProduct.setEditable(true);

        //TODO; Appeler la base de donnée pour remplir la comboBox
        searchBarNameListProduct.setItems(FXCollections.observableArrayList(("Liste de Mardi"), ("Liste de pauvre")));
        searchBarNameListProduct.getEditor().textProperty().addListener(observable -> {
             String nameListWriting = searchBarNameListProduct.getEditor().textProperty().getValue();
             //TODO: envoyer nameListWriting a la bdd -> nous enverra une liste de liste de course
             //TODO: remplacer par db qui va remplir par rapport au lettre envoyé
        });

        //Creation des boutons
        Button btnSeeList = new Button("Voir la liste");
        Button btnSaveModify = new Button("Enregistrer les modifications");
        Button btnCancelModify = new Button("Annuler les modifications");
        Button btnAddProduct = new Button("Ajouter un produit");

        btnSeeList.setOnAction(e-> {
            nameListProduct = searchBarNameListProduct.getEditor().textProperty().getValue();
            SeeProduct(nameListProduct, btnAddProduct);});

        btnSaveModify.setOnAction(e-> {saveShoppingList();});
        btnCancelModify.setOnAction(e-> {SeeProduct(nameListProduct, btnAddProduct);});

        //tableView pour afficher contenu d'une liste de courses
        createTableView(); 
        
        btnAddProduct.setVisible(false);
        btnAddProduct.setOnAction(e->{addProduct();});

        label.getChildren().addAll(searchBarNameListProduct,btnSeeList, table, btnAddProduct, btnReturnShoppingList, btnCancelModify, btnSaveModify);
        Scene scene = new Scene(label, 720, 630);
        primaryStage.setTitle("Afficher liste de courses");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void saveShoppingList(){
        Vector<Product> myIngredientCopy = new Vector<>();
        for (int i=0; i < table.getItems().size(); i++){
            Product product = (Product) table.getItems().get(i);
            myIngredientCopy.add(product);
        }
        if(!myIngredientCopy.equals(myIngredient)){
            //TODO: envoyer a la base de donne les modifs
            System.out.println("je suis diff");
        }
    }

    private void createTableView() {
        TableColumn firstNameCol = new TableColumn("Produit");
        TableColumn secondNameCol = new TableColumn("Quantité");
        table.setEditable(false);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        secondNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        table.getColumns().addAll(firstNameCol, secondNameCol);
        addButtonToTable();
    }

    private void addProduct(){ //TODO:corriger ca
        MenuAddIngredient menuAddIngredient = new MenuAddIngredient(primaryStage);
        menuAddIngredient.addProdcut(table);
        //TODO: remplir notre liste avec ça pour envoyer a la bdd
        //TODO: verifier si l'utilisateur a rajouter qql ch

    }

    private void modifyProductValue(Button btnModify) {
        //TODO: faire en sorte de pouvoir ecrire pour le nombre
        if (Objects.equals(btnModify.getText(), "Modifier")){
            btnModify.setText("Ne plus modifier") ;
            table.setEditable(true);
            //TODO: modifier ici
        }
        else{
            btnModify.setText("Modifier") ;
            table.setEditable(false);
        }
    }

    private void SeeProduct(String nameListSelected, Button btnAddProduct) {
        //TODO: envoyer a la bdd le nom de la liste -> nameListSelected -> on recevra la liste de course avec produit
        //TODO: verifier que ce n'est pas du vide le nameListSelected
        // TODO: remplir la table de produit
        myIngredient.clear();
        if(Objects.equals(nameListSelected, "")){
            btnAddProduct.setVisible(false);
        }
        else {
            if(Objects.equals(nameListSelected, "Liste de Mardi")) {
                myIngredient.add(new Product("hey", 343));
                myIngredient.add(new Product("hjxjsey", 424));
                btnAddProduct.setVisible(true);
            }
        }

        final ObservableList<Product> data = FXCollections.observableArrayList(myIngredient);
        table.setItems(data);

    }

    private void addButtonToTable() {
        TableColumn<Product, Void> thirdNameCol = new TableColumn("Modifier le produit");
        TableColumn<Product, Void> lastNameCol = new TableColumn<>("Supprimer le produit");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButton(true);
        thirdNameCol.setCellFactory(cellFactory);
        cellFactory = createColWithButton(false);
        lastNameCol.setCellFactory(cellFactory);

        table.getColumns().addAll(thirdNameCol, lastNameCol);

    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createColWithButton(boolean isModify){
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

                        private final Button btnModify = new Button("Modifier");
                        private final Button btnDelete = new Button("Supprimer");
                        {
                            btnModify.setOnAction((ActionEvent event) -> {
                                modifyProductValue(btnModify);
                                Product data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedDataModify: " + data.getQuantity());
                            });

                            btnDelete.setOnAction((ActionEvent event) -> {
                                Product data = getTableView().getItems().get(getIndex());
                                table.getItems().remove(data);
                                System.out.println("selectedDataDelete: " + data.getQuantity());
                            });
                        }



                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            if(isModify) {setGraphic(btnModify);}
                            else {setGraphic(btnDelete);}
                        }
                    }
                };
                return cell;
            }
        };
        return cellFactory;
    }

}
