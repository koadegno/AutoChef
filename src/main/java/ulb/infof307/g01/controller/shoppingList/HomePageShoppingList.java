package ulb.infof307.g01.controller.shoppingList;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.HomeShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewController;

public class HomePageShoppingList extends Controller implements HomeShoppingListViewController.Listener, ListenerBackPreviousWindow {


    public HomePageShoppingList(Stage primaryStage,ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
    }
    /**
     * Affiche la page principale des Listes de Courses.
     * @see HomeShoppingListViewController
     * */
    public void displayHomeShoppingList(){
        HomeShoppingListViewController homeShoppingListViewController = new HomeShoppingListViewController();
        homeShoppingListViewController.setListener(this);
        loadFXML(homeShoppingListViewController, "HomeShoppingList.fxml");
    }

    /**
     * Affiche la page qui liste les ShoppingList
     * @see ulb.infof307.g01.model.ShoppingList
     * //TODO: @see Contrôlleur de vue liste ShoppingList
     */
    @Override
    public void onMyShoppingListsButtonClick() {
        UserShoppingListViewController windowUserShoppingListsController = new UserShoppingListViewController();
        loadFXML(windowUserShoppingListsController, "ShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(windowUserShoppingListsController, this);

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(false);
    }

    /**
     * Affiche la page de création de Liste de courses
     * @see ulb.infof307.g01.model.ShoppingList
     * //TODO: @see Contrôlleur de vue ShoppingList principal
     */
    @Override
    public void onCreateShoppingListsButtonClick() {
        CreateUserShoppingListViewController createUserShoppingListViewController = new CreateUserShoppingListViewController();
        loadFXML(createUserShoppingListViewController, "ShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(createUserShoppingListViewController, this );

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(true);
    }

    /**
     * Revient à la page d'accueil de l'application.
     * @see HomePageViewController
     * */
    @Override
    public void onBackButtonClick() {
        listenerBackPreviousWindow.onReturn();
    }


    @Override
    public void logout() { userLogout(); }

    @Override
    public void onReturn() { displayHomeShoppingList(); }
}
