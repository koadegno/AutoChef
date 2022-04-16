package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.view.ViewController;

import java.util.ArrayList;

/**
 * Super Classe contenant les méthodes doublons qu'utilise la fenêtre creation/modif liste de courses
 */

public abstract class ShoppingListViewController extends ViewController<ShoppingListViewController.Listener> {
    protected SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
    @FXML
    protected HBox hBoxToCreateProduct;
    @FXML
    protected TableColumn columnQuantityOrNumber,columnDelete,columnProduct, columnUnity;
    @FXML
    protected ComboBox<String> comboBoxShoppingNameList;
    @FXML
    protected ComboBox<String> comboBoxListUnity;
    @FXML
    protected ComboBox<String> comboBoxListProduct;
    @FXML
    protected Button btnConfirm, btnSeeShoppingList, btnAddNewProduct, btnExportShoppingList;
    @FXML
    protected Spinner<Integer> spinnerQuantityOrNumber;
    @FXML
    protected TableView tableViewDisplayProductList;
    @FXML
    public Button returnToMenu, btnSendMail;
    protected String currentShoppingListName;


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
     * Ajout de chaque produit de la table dans une nouvelle liste de courses par le controller
     */
    public void fillShoppingListToSend() {
        for (int i = 0; i < tableViewDisplayProductList.getItems().size(); i++) {
            Product product = (Product) tableViewDisplayProductList.getItems().get(i);
            listener.addProductToShoppingListToSend(product);
        }
    }

    /**
     * Empeche d'ecrire autre chose qu'un int dans le textField
     * @param newValue : string recup du textField
     */
    protected void OnlyIntOrFloatTextFieldUnity(String newValue) {
        if (!newValue.matches("^\\d*")) {
            spinnerQuantityOrNumber.getEditor().setText("0");
            spinnerValueFactory.setValue(0);
        }
    }

    /**
     * Rajoute-les elements (produit, quantite, unite) choisis par l'utilisateur dans le tableView
     */
    @FXML
    public void addElementOfListToComboBoxProduct() {
        //Recupere les elements choisi pour un produit
        Object nameProductChoose = comboBoxListProduct.getSelectionModel().getSelectedItem();
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();

        listener.addElementOfList(nameProductChoose, quantityOrNumberChoose, nameUnityChoose);
    }

    public void showAddProductError(boolean isError){
        setNodeColor(hBoxToCreateProduct,isError);
    }

    public void clearElementAddProduct(){
        //Nettoyer les combobox et le spinner
        comboBoxListProduct.getSelectionModel().clearSelection();
        comboBoxListUnity.getSelectionModel().clearSelection();
        spinnerValueFactory.setValue(0);
    }

    public void initComboBox(ArrayList<String> allProduct, ArrayList<String> allUnitName) {
        comboBoxListProduct.setItems(FXCollections.observableArrayList(allProduct));
        comboBoxListUnity.setItems(FXCollections.observableArrayList(allUnitName));
    }

    public Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createColWithButton(TableView tableViewDisplayProductList ){
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

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
                return cell;
            }
        };
        return cellFactory;
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


    public interface Listener{
        void returnHomeShoppingList();
        void returnToUserMenu();
        void addElementOfList(Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose);
        void initInformationShoppingList(boolean isCreateUserShoppingListController);
        void exportShoppingList(String currentShoppingListName);
        void sendShoppingListByMail(String currentShoppingListName);

        void seeUserShoppingList(Object nameUserShoppingList);
        void confirmUserModifyShoppingList(String currentShoppingListName);
        void addProductToShoppingListToSend(Product product);

        void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList);

        void cancelRecipeCreation();

        void returnAddedProducts();
    }
}
