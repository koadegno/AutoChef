package ulb.infof307.g01.controller.shoppingList;

import javafx.stage.Stage;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.MailController;
import ulb.infof307.g01.controller.MainController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.WindowHomeController;
import ulb.infof307.g01.view.menu.WindowUserMenuListController;
import ulb.infof307.g01.view.shoppingList.*;

import java.sql.SQLException;
import java.util.*;


public class ShoppingListController extends Controller implements WindowUserShoppingListsControllerTools.Listener  {
    private WindowUserShoppingListsControllerTools windowUserShoppingListsControllerTools;
    private WindowUserShoppingListsController windowUserShoppingListsController;
    private WindowCreateUserShoppingListController windowCreateUserShoppingListController;
    private MainController mainController;
    private ShoppingList shoppingListToSend;

    public ShoppingListController(WindowCreateUserShoppingListController windowCreateUserShoppingListController, MainController mainController){
        this.windowUserShoppingListsControllerTools = this.windowCreateUserShoppingListController = windowCreateUserShoppingListController;
        this.mainController = mainController;
        this.windowCreateUserShoppingListController.setListener(this);
        this.windowUserShoppingListsControllerTools.setListener(this);
    }

    public ShoppingListController(WindowUserShoppingListsController windowUserShoppingListsController,MainController mainController){
        this.windowUserShoppingListsControllerTools = this.windowUserShoppingListsController = windowUserShoppingListsController;
        this.mainController = mainController;
        this.windowUserShoppingListsController.setListener(this);
        this.windowUserShoppingListsControllerTools.setListener(this);

    }

    public ShoppingListController(){
        this.windowUserShoppingListsControllerTools = new WindowUserShoppingListsControllerTools();
        windowUserShoppingListsControllerTools.setListener(this);
    }

    //Methode Listener de WindowCreateUserShoppingListController

    public void confirmUserModifyShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){

        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            windowCreateUserShoppingListController.showNameUserCreateShoppingListError();
        }
        else if(sizeTableViewDisplayProductList == 0){ // table view est vide
            windowCreateUserShoppingListController.showIsEmptyTableViewError();
        }
        else {
            this.shoppingListToSend = new ShoppingList(shoppingListName);
            windowCreateUserShoppingListController.fillShoppingListToSend(shoppingListToSend);
            try {
                Configuration.getCurrent().getShoppingListDao().insert(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                windowCreateUserShoppingListController.showNameUserCreateShoppingListError();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // else tout ce passe bien
            //TODO: voir ici cmt faire
            displayHomeShoppingListController();
        }
    }

    //Fin Methode Listener de WindowCreateUserShoppingListController

    //Methode Listener de WindowUserShoppingListController

    @Override
    public void seeUserShoppingList(Object nameUserShoppingList){
        if(Objects.equals(nameUserShoppingList, null)){ //nom est null
            windowUserShoppingListsController.isVisibleElementToModifyMyShoppingList(false);
        }
        else{
            String currentShoppingListName = (String) nameUserShoppingList;
            try {
                // afficher les produits de la liste de course dans la table
                ShoppingList shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
                Vector<Product> productOfShoppingList =  new Vector<>(shoppingList);
                windowUserShoppingListsController.addProductListToTableView(productOfShoppingList);
                windowUserShoppingListsController.isVisibleElementToModifyMyShoppingList(true);
                windowUserShoppingListsController.setCurrentShoppingListName(currentShoppingListName);
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
            windowUserShoppingListsController.fillShoppingListToSend(shoppingListToSend);
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

            if(isCreateUserShoppingListController) windowCreateUserShoppingListController.initComboBox(allProduct, allUnitName);
            else windowUserShoppingListsController.initComboBox(allProduct, allUnitName, allShoppinListName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fin Methode Listener de WindowUserShoppingListController

    //Methode Listener de WindowShoppingListControllerTools

    public void addElementOfList(Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose){
        //TODO: pq on faisait appel a remove??
        windowUserShoppingListsControllerTools.showAddProductError(false);

        Product userProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            //Cree le produit pour le mettre dans le tableView
            userProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());

            windowUserShoppingListsControllerTools.addProductToTableView(userProduct);
            windowUserShoppingListsControllerTools.clearElementAddProduct();
        } else {
            windowUserShoppingListsControllerTools.showAddProductError(true);
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

    //Methode Listener de WindowHomeShoppingListController


    //TODO: ne devrait pas se trouver dans la HomeSHopping
    public void displayHomeShoppingListController(){
        mainController.onShoppingListButtonClick();
       }


    //Fin Methode Listener de WindowHomeShoppingListController


}
