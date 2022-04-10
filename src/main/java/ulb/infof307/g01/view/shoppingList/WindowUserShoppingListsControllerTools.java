package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.mail.MailView;
import ulb.infof307.g01.view.menu.WindowUserMenuListController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Super Classe contenant les methodes doublons qu'utilise la fenetre creation/modif liste de courses
 */

public class WindowUserShoppingListsControllerTools extends Window {
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
    protected Button returnToMenu, btnSendMail;
    protected ArrayList<String> allUnitName = null;
    protected ArrayList<String> allProduct = null;
    protected ArrayList<String> allShoppinListName = null;
    protected String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
    protected String currentShoppingListname;

    protected void removeBorderColor() {
        this.setNodeColor(tableViewDisplayProductList,false);
        this.setNodeColor(hBoxToCreateProduct, false);
    }

    /**
     * Inialise les ComboBox avec les elements de la bdd dans une liste : produit, unité, nom de liste de courses
     */
    public void initShoppingListElement() {
        try {
            allProduct = Configuration.getCurrent().getProductDao().getAllName();
            allUnitName = Configuration.getCurrent().getProductUnityDao().getAllName();
            allUnitName.removeAll(List.of(unitToRemove));
            allShoppinListName = Configuration.getCurrent().getShoppingListDao().getAllName();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retour au menu precedent : le menu principal de la liste de courses
     * @throws IOException
     */
    @FXML
    public void returnShoppingList() throws IOException {
        this.loadFXML("HomeShoppingList.fxml");

    }

    /**
     * Retour au menu precedent : ShowMenu
     */
    @FXML
    public void returnToMyMenu() {
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    protected void fillShoppingListToSend(ShoppingList shoppingListToSend) {
        // ajout de chaque produit de la table dans une nvl shoppingList
        for (int i = 0; i < tableViewDisplayProductList.getItems().size(); i++) {
            Product product = (Product) tableViewDisplayProductList.getItems().get(i);
            shoppingListToSend.add(product);
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
     * Rajoute les elements (produit, quantite, unite) choisis par l'utilisateur dans le tableView
     */
    @FXML
    public void addElementOfListToComboBoxProduct() {
        this.removeBorderColor();
        //Recuper les elements choisi pour un produit
        Object nameProductChoose = comboBoxListProduct.getSelectionModel().getSelectedItem();
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();
        Product myProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            //Cree le produit pour le mettre dans le tableView
            myProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());
            tableViewDisplayProductList.getItems().add(myProduct);

            //Nettoyer les combobox et le spinner
            comboBoxListProduct.getSelectionModel().clearSelection();
            comboBoxListUnity.getSelectionModel().clearSelection();
            spinnerValueFactory.setValue(0);
        } else {
            setNodeColor(hBoxToCreateProduct,true);
        }
    }

    public void initComboBox() {
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

    public void exportShoppingList(){
        ExportShoppingListView exportShoppingListView = new ExportShoppingListView();
        try {
            popupFXML("exportShoppingList.fxml", exportShoppingListView);
            ShoppingList shoppingListToExport = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListname);
            exportShoppingListView.setShoppingList(shoppingListToExport);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendShoppingListByMail() throws SQLException {

        ShoppingList shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListname);
        MailView mailView = new MailView();
        try {
            popupFXML("createMail.fxml", mailView);
            mailView.setShoppingListToMail(shoppingList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
