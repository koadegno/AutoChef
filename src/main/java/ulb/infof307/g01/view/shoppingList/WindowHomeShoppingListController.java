package ulb.infof307.g01.view.shoppingList;

import javafx.fxml.FXML;
import ulb.infof307.g01.view.ViewController;

/**
 * Classe qui permet d'afficher la fenetre principal de la liste de courses avec deux boutons
 * qui dirige chacun vers une autre sous fenetre de liste de courses
 */

public class WindowHomeShoppingListController extends ViewController<WindowHomeShoppingListController.Listener> {

    @FXML
    public void displayUserShoppingListController(){
        listener.displayUserSHoppingListController();}

    @FXML
    public void displayCreateUserShoppingListController(){
        listener.displayCreateUserShoppingListController();
        }

    @FXML
    public void returnHomeShoppingListController() {
        listener.displayHomeShoppingListController();
    }

    public interface Listener{
        void displayUserSHoppingListController();
        void displayCreateUserShoppingListController();
        void displayHomeShoppingListController();
    }
}
