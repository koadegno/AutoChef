package ulb.infof307.g01.controller.shoppingList;

import org.sqlite.SQLiteException;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.MailController;
import ulb.infof307.g01.controller.MainController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.menu.WindowUserMenuListController;
import ulb.infof307.g01.view.shoppingList.*;

import java.sql.SQLException;
import java.util.*;


public class ShoppingListController extends Controller implements ShoppingListViewController.Listener  {
    private ShoppingListViewController shoppingListViewController;
    private UserShoppingListViewViewController userShoppingListViewViewController;
    private CreateUserShoppingListViewController createUserShoppingListViewController;
    private MainController mainController;
    private ShoppingList shoppingListToSend;

    public ShoppingListController(CreateUserShoppingListViewController createUserShoppingListViewController, MainController mainController){
        this.shoppingListViewController = this.createUserShoppingListViewController = createUserShoppingListViewController;
        initShoppingListController(mainController);
    }

    public ShoppingListController(UserShoppingListViewViewController userShoppingListViewViewController, MainController mainController){
        this.shoppingListViewController = this.userShoppingListViewViewController = userShoppingListViewViewController;
        initShoppingListController(mainController);
    }

    private void initShoppingListController(MainController mainController) {
        this.mainController = mainController;
        this.shoppingListViewController.setListener(this);
    }

    //Methode Listener de CreateUserShoppingListViewController

    public void confirmUserModifyShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){

        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            createUserShoppingListViewController.showNameUserCreateShoppingListError();
        }
        else if(sizeTableViewDisplayProductList == 0){ // table view est vide
            createUserShoppingListViewController.showIsEmptyTableViewError();
        }
        else {
            this.shoppingListToSend = new ShoppingList(shoppingListName);
            createUserShoppingListViewController.fillShoppingListToSend(shoppingListToSend);
            try {
                Configuration.getCurrent().getShoppingListDao().insert(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                createUserShoppingListViewController.showNameUserCreateShoppingListError();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // else tout ce passe bien
            //TODO: voir ici cmt faire
            displayHomeShoppingListController();
        }
    }

    //Fin Methode Listener de CreateUserShoppingListViewController

    //Methode Listener de WindowUserShoppingListController

    @Override
    public void seeUserShoppingList(Object nameUserShoppingList){
        if(Objects.equals(nameUserShoppingList, null)){ //nom est null
            userShoppingListViewViewController.isVisibleElementToModifyMyShoppingList(false);
        }
        else{
            String currentShoppingListName = (String) nameUserShoppingList;
            try {
                // afficher les produits de la liste de course dans la table
                ShoppingList shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
                Vector<Product> productOfShoppingList =  new Vector<>(shoppingList);
                userShoppingListViewViewController.addProductListToTableView(productOfShoppingList);
                userShoppingListViewViewController.isVisibleElementToModifyMyShoppingList(true);
                userShoppingListViewViewController.setCurrentShoppingListName(currentShoppingListName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void confirmUserModifyShoppingList(String currentShoppingListName){
        try {
            //Recupere liste de courses chez la bdd
            ShoppingList shoppingListInDataBase = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
            this.shoppingListToSend = new ShoppingList(shoppingListInDataBase.getName(), shoppingListInDataBase.getId());

            //Renvoie liste de courses chez la bdd
            userShoppingListViewViewController.fillShoppingListToSend(shoppingListToSend);
            Configuration.getCurrent().getShoppingListDao().update(shoppingListToSend);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProductToShoppingListToSend(Product product){
        shoppingListToSend.add(product);
    }

    public void initInformationShoppingList(boolean isCreateUserShoppingListController){
        try {
            ArrayList<String> allProduct = Configuration.getCurrent().getProductDao().getAllName();
            ArrayList<String> allUnitName = Configuration.getCurrent().getProductUnityDao().getAllName();
            String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
            allUnitName.removeAll(List.of(unitToRemove));
            ArrayList<String> allShoppinListName = Configuration.getCurrent().getShoppingListDao().getAllName();

            if(isCreateUserShoppingListController) createUserShoppingListViewController.initComboBox(allProduct, allUnitName);
            else userShoppingListViewViewController.initComboBox(allProduct, allUnitName, allShoppinListName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fin Methode Listener de WindowUserShoppingListController

    //Methode Listener de WindowShoppingListControllerTools

    public void addElementOfList(Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose){
        //TODO: pq on faisait appel a remove??
        shoppingListViewController.showAddProductError(false);

        Product userProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            //Cree le produit pour le mettre dans le tableView
            userProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());

            shoppingListViewController.addProductToTableView(userProduct);
            shoppingListViewController.clearElementAddProduct();
        } else {
            shoppingListViewController.showAddProductError(true);
        }
    }

    @Override
    public void returnHomeShoppingList() {
        this.displayHomeShoppingListController();
    }

    public void returnToUserMenu(){
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    public void exportShoppingList(String currentShoppingListName){
        System.out.println("nom = " + currentShoppingListName);
        ExportShoppingListController exportShoppingListController = new ExportShoppingListController(currentShoppingListName);
    }

    public void sendShoppingListByMail(String currentShoppingListName){
        ShoppingList shoppingList = null;
        try {
            shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MailController mailController = new MailController(shoppingList);
        mailController.initMailView();
    }
    //Fin Methode Listener de WindowUserShoppingListController

    //Methode Listener de HomeShoppingListViewController


    //TODO: ne devrait pas se trouver dans la HomeSHopping
    public void displayHomeShoppingListController(){
        mainController.onShoppingListButtonClick();
       }


    //Fin Methode Listener de HomeShoppingListViewController


}
