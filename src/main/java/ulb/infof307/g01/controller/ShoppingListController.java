package ulb.infof307.g01.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.WindowHomeController;
import ulb.infof307.g01.view.menu.WindowUserMenuListController;
import ulb.infof307.g01.view.shoppingList.*;

import java.io.IOException;
import java.sql.SQLException;

public class ShoppingListController extends Controller implements WindowUserShoppingListsControllerTools.Listener, WindowHomeShoppingListController.Listener  {
    private WindowUserShoppingListsControllerTools windowUserShoppingListsControllerTools;
    private ExportShoppingListView exportShoppingListView;
    private WindowHomeShoppingListController windowHomeShoppingListController;

    public ShoppingListController(){
        this.windowUserShoppingListsControllerTools = new WindowUserShoppingListsControllerTools();
        windowUserShoppingListsControllerTools.setListener(this);

        //TODO: creer un autre controller??
        this.windowHomeShoppingListController = new WindowHomeShoppingListController();
        windowHomeShoppingListController.setListener(this);


    }


    @Override
    public void returnHomeShoppingList() {
        this.displayHomeShoppingListController();
    }

    public void returnToUserMenu(){
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    public void displayExportShoppingList(String currentShoppingListname){
        this.exportShoppingListView = new ExportShoppingListView();
        try {
            popupFXML("exportShoppingList.fxml", exportShoppingListView);
            ShoppingList shoppingListToExport = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListname);
            exportShoppingListView.setShoppingList(shoppingListToExport);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    //Methode Listener de WindowHomeShoppingListController
    @Override
    public void displayHomeShoppingListController(){
        loadFXML(windowHomeShoppingListController, "HomeShoppingList.fxml");
}

    @Override
    public void displayUserSHoppingListController(){
        WindowUserShoppingListsController windowsMyShoppingListsController = new WindowUserShoppingListsController();
        loadFXML(windowsMyShoppingListsController, "CreateUserShoppingList.fxml");

        //Initialise la page avec les informations de la bdd
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();
    }

    @Override
    public void displayCreateUserShoppingListController(){
        WindowCreateUserShoppingListController windowsCreateMyShoppingListController = new WindowCreateUserShoppingListController();
        this.loadFXML(windowsCreateMyShoppingListController, "CreateUserShoppingList.fxml");
        //Initialise la page avec les informations de la bdd
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();

    }



    public void returnHomeController(){
        Stage primaryStage = new Stage();
        //TODO: changer ça -> ? loadFXML("HomePage.fxml");
        WindowHomeController windowHomeController = new WindowHomeController();
        windowHomeController.displayMain(primaryStage);
    }

}
