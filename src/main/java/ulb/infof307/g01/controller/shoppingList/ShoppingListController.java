package ulb.infof307.g01.controller.shoppingList;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.WindowHomeController;
import ulb.infof307.g01.view.menu.WindowUserMenuListController;
import ulb.infof307.g01.view.shoppingList.*;

import java.sql.SQLException;
import java.util.*;


public class ShoppingListController extends Controller implements WindowUserShoppingListsControllerTools.Listener, WindowHomeShoppingListController.Listener  {
    private WindowUserShoppingListsControllerTools windowUserShoppingListsControllerTools;
    private WindowHomeShoppingListController windowHomeShoppingListController;
    private WindowUserShoppingListsController windowUserShoppingListsController;
    private ShoppingList shoppingListToSend;

    public ShoppingListController(){
        this.windowUserShoppingListsControllerTools = new WindowUserShoppingListsControllerTools();
        windowUserShoppingListsControllerTools.setListener(this);

        //TODO: creer un autre controller??
        this.windowHomeShoppingListController = new WindowHomeShoppingListController();
        windowHomeShoppingListController.setListener(this);

    }

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void confirmUserCreateShoppingList(String currentShoppingListName){
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

    public void initInformationShoppingList(){
        try {
            ArrayList<String> allProduct = Configuration.getCurrent().getProductDao().getAllName();
            ArrayList<String> allUnitName = Configuration.getCurrent().getProductUnityDao().getAllName();
            String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
            allUnitName.removeAll(List.of(unitToRemove));
            ArrayList<String> allShoppinListName = Configuration.getCurrent().getShoppingListDao().getAllName();

            windowUserShoppingListsController.initComboBox(allProduct, allUnitName, allShoppinListName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fin Methode Listener de WindowUserShoppingListController

    @Override
    public void returnHomeShoppingList() {
        this.displayHomeShoppingListController();
    }

    public void returnToUserMenu(){
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }


    public void returnHomeController(){
        Stage primaryStage = new Stage();
        //TODO: changer ça -> ? loadFXML("HomePage.fxml");
        WindowHomeController windowHomeController = new WindowHomeController();
        windowHomeController.displayMain(primaryStage);
    }


    //Methode Listener de WindowHomeShoppingListController
    @Override
    public void displayHomeShoppingListController(){
        loadFXML(windowHomeShoppingListController, "HomeShoppingList.fxml");
    }

    @Override
    public void displayUserSHoppingListController(){
        this.windowUserShoppingListsController = new WindowUserShoppingListsController();
        windowUserShoppingListsController.setListener(this);
        loadFXML(windowUserShoppingListsController, "CreateUserShoppingList.fxml");

        //Initialise la page avec les informations de la bdd
        this.initInformationShoppingList();
    }

    @Override
    public void displayCreateUserShoppingListController(){
        WindowCreateUserShoppingListController windowsCreateMyShoppingListController = new WindowCreateUserShoppingListController();
        this.loadFXML(windowsCreateMyShoppingListController, "CreateUserShoppingList.fxml");
        //Initialise la page avec les informations de la bdd
        //windowsCreateMyShoppingListController.initComboBox();

    }

    //Fin Methode Listener de WindowHomeShoppingListController


}
