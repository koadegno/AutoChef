package ulb.infof307.g01.ui.shoppingList;

import javafx.fxml.FXML;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.WindowHomeController;

/**
 * Classe qui permet d'afficher la fenetre principal de la liste de courses avec deux boutons
 * qui dirige chacun vers une autre sous fenetre de liste de courses
 */

public class WindowHomeShoppingListController extends Window {
    public void displayMenuShoppingListController() {
        this.loadFXML("HomeShoppingList.fxml");
    }

    @FXML
    public void displayMyShoppingListController(){
        WindowUserShoppingListsController windowsMyShoppingListsController = new WindowUserShoppingListsController();
        this.loadFXML(windowsMyShoppingListsController, "CreateUserShoppingList.fxml");

        //Initialise la page avec les informations de la bdd
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();

    }

    @FXML
    public void displayCreateShoppingListController(){
        WindowCreateUserShoppingListController windowsCreateMyShoppingListController = new WindowCreateUserShoppingListController();
        this.loadFXML(windowsCreateMyShoppingListController, "CreateUserShoppingList.fxml");
        //Initialise la page avec les informations de la bdd
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();
    }

    @FXML
    public void returnMainMenu() {
        WindowHomeController windowHomeController = new WindowHomeController();
        windowHomeController.displayMain(this.primaryStage);
    }
}
