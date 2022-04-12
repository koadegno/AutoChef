package ulb.infof307.g01.view.shoppingList;

import javafx.fxml.FXML;
import ulb.infof307.g01.view.WindowHomeController;
import ulb.infof307.g01.view.ViewController;

/**
 * Classe qui permet d'afficher la fenetre principal de la liste de courses avec deux boutons
 * qui dirige chacun vers une autre sous fenetre de liste de courses
 */

public class HomeShoppingListController extends ViewController<HomeShoppingListController.HomeShoppingListListener> {

    @FXML
    public void onMyShoppingListButtonClick() { listener.onMyShoppingListsButtonClick(); }

    @FXML
    public void onCreateShoppingListButtonClick() { listener.onCreateShoppingListsButtonClick(); }

    @FXML
    public void onBackButtonClick() { listener.onBackButtonClick(); }

    public interface HomeShoppingListListener {
        void onMyShoppingListsButtonClick();
        void onCreateShoppingListsButtonClick();
        void onBackButtonClick();
    }
}
