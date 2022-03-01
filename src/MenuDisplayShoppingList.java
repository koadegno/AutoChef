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

    private Stage primaryStage;
    private Button btnReturnShoppingList;
    private TableView table = new TableView();

    public MenuDisplayShoppingList(Stage primaryStage, Button btnReturnShoppingList){
        this.primaryStage = primaryStage;
        this.btnReturnShoppingList = btnReturnShoppingList;
    }

    public void displayMenuDisplayShoppingList(){
        VBox label = new VBox();

        ComboBox<String> nameListProduct = new ComboBox<>(); //barre de recherche 
        nameListProduct.setEditable(true);

        //TODO; Appeler la base de donnée pour remplir la comboBox
        nameListProduct.setItems(FXCollections.observableArrayList(("Liste de Mardi"), ("Liste de pauvre")));
        nameListProduct.getEditor().textProperty().addListener(observable -> {
             String nameListWriting = nameListProduct.getEditor().textProperty().getValue();
             //TODO: envoyer nameListWriting a la bdd -> nous enverra une liste de liste de course
             //TODO: remplacer par db qui va remplir par rapport au lettre envoyé
        });

        //Creation des boutons
        Button btnSeeList = new Button("Voir la liste");
        Button btnSaveModify = new Button("Enregistrer les modifications");
        Button btnCancelModify = new Button("Annuler les modufications");
        Button btnAddProduct = new Button("Ajouter un produit");

        btnSeeList.setOnAction(e-> {SeeProduct(nameListProduct.getEditor().textProperty().getValue(), btnAddProduct);});
        btnSaveModify.setOnAction(e-> {//TODO: envoyer les modifications a la bdd : verifier s'il en a fait -> liste de copie?
        });
        btnCancelModify.setOnAction(e-> {//TODO: soit faire une copie ou demander a la bdd de renvoyer la liste
        });

        //tableView pour afficher contenu d'une liste de courses
        createTableView(); 
        
        btnAddProduct.setVisible(false);
        btnAddProduct.setOnAction(e->{addProduct();});

        label.getChildren().addAll(nameListProduct,btnSeeList, table, btnAddProduct, btnReturnShoppingList, btnCancelModify, btnSaveModify);
        Scene scene = new Scene(label, 720, 630);
        primaryStage.setTitle("Afficher liste de courses");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void createTableView() {
        TableColumn firstNameCol = new TableColumn("Produit");
        TableColumn secondNameCol = new TableColumn("Quantité");
        table.setEditable(false);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("produc"));
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

    private void deleteProductValue(Button btnDelete){
    }

    private void SeeProduct(String nameListSelected, Button btnAddProduct) {
        //TODO: envoyer a la bdd le nom de la liste -> nameListSelected -> on recevra la liste de course avec produit
        //TODO: verifier que ce n'est pas du vide le nameListSelected
        Vector<Product> myIngredient = new Vector<Product>(); // TODO: remplir la table de produit
        if(Objects.equals(nameListSelected, "")){
            btnAddProduct.setVisible(false);
        }
        else {
            if(Objects.equals(nameListSelected, "Liste de Mardi")) {
                myIngredient.add(new Product("hey", "hdq"));
                myIngredient.add(new Product("hjxjsey", "hdjkwjq"));
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
                                deleteProductValue(btnDelete);
                                Product data = getTableView().getItems().get(getIndex());
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
