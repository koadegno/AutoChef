package ulb.infof307.g01.controller;

import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.menu.WindowShowMenuController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;

public class MenuController extends Controller {
    private WindowShowMenuController windowShowMenuController;

    public MenuController(WindowShowMenuController windowShowMenuController){
        //TODO: changer ça avec le MVC of course
        this.windowShowMenuController = windowShowMenuController;
    }

    public void displayCreateUserShoppingList(){
        CreateUserShoppingListViewController createUserShoppingListViewController = new CreateUserShoppingListViewController();
        loadFXML(createUserShoppingListViewController, "CreateUserShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(createUserShoppingListViewController);

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(true);
        fillShoppingList(createUserShoppingListViewController);
    }

    public void fillShoppingList(CreateUserShoppingListViewController viewController){
        ShoppingList shoppingList =  windowShowMenuController.fillShoppingList(viewController);
        viewController.fillTableViewWithExistentShoppingList(shoppingList);
    }
}
