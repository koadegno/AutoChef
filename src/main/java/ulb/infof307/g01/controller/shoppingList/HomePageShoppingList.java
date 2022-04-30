package ulb.infof307.g01.controller.shoppingList;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.shoppingList.CreateShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.HomeShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.ModifyShoppingListViewController;

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
        ModifyShoppingListViewController windowUserShoppingListController = new ModifyShoppingListViewController();
        loadFXML(windowUserShoppingListController, "ShoppingList.fxml");
        ModifyShoppingListController modifyShoppingListController = new ModifyShoppingListController(windowUserShoppingListController, this);
        //Initialise la page avec les informations de la bdd
        modifyShoppingListController.initInformationShoppingList(false);
    }

    /**
     * Affiche la page de création de Liste de courses
     * @see ulb.infof307.g01.model.ShoppingList
     * //TODO: @see Contrôlleur de vue ShoppingList principal
     */
    @Override
    public void onCreateShoppingListsButtonClick() {
        CreateShoppingListViewController createShoppingListViewController = new CreateShoppingListViewController();
        loadFXML(createShoppingListViewController, "ShoppingList.fxml");
        CreateShoppingListController createShoppingListController = new CreateShoppingListController(createShoppingListViewController, this);

        //Initialise la page avec les informations de la bdd
        createShoppingListController.initInformationShoppingList(true);
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
