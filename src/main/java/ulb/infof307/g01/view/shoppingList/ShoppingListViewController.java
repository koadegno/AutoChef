package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Super Classe contenant les méthodes doublons qu'utilise la fenêtre creation/modif liste de courses
 */

public abstract class ShoppingListViewController extends ViewController<ShoppingListViewController.Listener> implements Initializable {

    private static final int maxQuantity = 10000;
    protected SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxQuantity);
    @FXML
    protected HBox hBoxToCreateProduct;
    @FXML
    protected TableColumn<Product,String> columnQuantityOrNumber,columnProduct, columnUnity;
    @FXML
    protected  TableColumn<Product,Void> columnDelete;
    @FXML
    protected ComboBox<String> comboBoxShoppingNameList;
    @FXML
    protected ComboBox<String> comboBoxListUnity;
    @FXML
    protected ComboBox<String> comboBoxListProduct;
    @FXML
    protected Button btnConfirm, btnSeeShoppingList, btnAddNewProduct, btnExportShoppingList,btnSeeShoppingListOnMap;
    @FXML
    protected Spinner<Integer> spinnerQuantityOrNumber;
    @FXML
    protected TableView<Product> tableViewDisplayProductList;
    @FXML
    public Button returnToMenu, btnSendMail;
    protected String currentShoppingListName;
    @FXML
    protected Menu helpMenuShoppingList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeElementVisibility();
        this.spinnerQuantityOrNumber.setValueFactory(spinnerValueFactory);
        super.onlyIntValue(spinnerQuantityOrNumber);

        columnProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<>("nameUnity"));
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
        returnToMenu.setOnAction((event) -> returnShoppingList());

    }

    protected void activeElementVisibility(){}


    public void removeBorderColor() {
        this.setNodeColor(tableViewDisplayProductList,false);
        this.setNodeColor(hBoxToCreateProduct, false);
    }

    /**
     * Retour au menu precedent : le menu principal de la liste de courses
     */
    @FXML
    public void returnShoppingList() {
        listener.returnHomeShoppingList();
    }

    /**
     * Retour au menu precedent : ShowMenu
     */
    @FXML
    public void returnToMyMenu() {
        listener.returnToUserMenu();
    }

    /**
     * recupere chaque produit de la table view et l'envoie au controller
     */
    public void fillShoppingListToSend() {
        for (int i = 0; i < tableViewDisplayProductList.getItems().size(); i++) {
            listener.addProductToShoppingListToSend(tableViewDisplayProductList.getItems().get(i));
        }
    }


    /**
     * Rajoute les elements (produit, quantite, unite) choisis par l'utilisateur dans le tableView
     */
    @FXML
    public void addElementOfListToComboBoxProduct() {
        //Recupere les elements pour creer un produit
        Object nameProductChoose = comboBoxListProduct.getSelectionModel().getSelectedItem();
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();

        listener.addElementOfList(this, nameProductChoose, quantityOrNumberChoose, nameUnityChoose);
    }

    public void showAddProductError(boolean isError){
        setNodeColor(hBoxToCreateProduct,isError);
    }

    public void clearElementAddProduct(){
        //Nettoyer les combobox et le spinner apres ajout
        comboBoxListProduct.getSelectionModel().clearSelection();
        comboBoxListUnity.getSelectionModel().clearSelection();
        spinnerValueFactory.setValue(0);
    }

    public void initComboBox(List<String> allProduct, List<String> allUnitName) {
        //initialise les combobox avec les informations prises de la base de données
        comboBoxListProduct.setItems(FXCollections.observableArrayList(allProduct));
        comboBoxListUnity.setItems(FXCollections.observableArrayList(allUnitName));
    }

    public Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createColWithButton(TableView<Product> tableViewDisplayProductList ){
        return new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {

                    //Creer un bouton supprimer
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        //Action pour le bouton supprimer
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Product data = getTableView().getItems().get(getIndex());
                            tableViewDisplayProductList.getItems().remove(data);
                        });
                    }

                    //Ajout du bouton supprimer
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                        }
                    }
                };
            }
        };
    }

    public void addProductToTableView(Product product){
        tableViewDisplayProductList.getItems().add(product);
    }

    public void exportShoppingList(){
        listener.exportShoppingList(currentShoppingListName);
    }
    public void sendShoppingListByMail() {
        listener.sendShoppingListByMail(currentShoppingListName);
    }

    public void onSeeShoppingListOnMap(){listener.viewShoppingListOnMap(currentShoppingListName);}
    public void logout(){listener.logout();}


    public interface Listener{
        void returnHomeShoppingList();
        void returnToUserMenu();
        void addElementOfList(ShoppingListViewController shoppingListViewController, Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose);
        void initInformationShoppingList(boolean isCreateUserShoppingListController);
        void exportShoppingList(String currentShoppingListName);
        void sendShoppingListByMail(String currentShoppingListName);

        void seeUserShoppingList(Object nameUserShoppingList);
        void confirmUserModifyShoppingList(String currentShoppingListName);
        void addProductToShoppingListToSend(Product product);
        void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList);
        void cancelRecipeCreation();
        void returnAddedProducts();

        void viewShoppingListOnMap(Object nameUserShoppingList);

        void helpCreateShoppingList();
        void helpModifyShoppingList();
        void logout();
    }
}
