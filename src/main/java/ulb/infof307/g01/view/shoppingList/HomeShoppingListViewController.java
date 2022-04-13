package ulb.infof307.g01.view.shoppingList;

import javafx.fxml.FXML;
import ulb.infof307.g01.view.ViewController;

/**
 * Classe qui permet d'afficher la fenetre principal de la liste de courses avec deux boutons
 * qui dirige chacun vers une autre sous fenetre de liste de courses
 */

public class HomeShoppingListViewController extends ViewController<HomeShoppingListViewController.Listener> {

    /**
     * Affiche la page pour voir et modifier une liste de courses
     */
    @FXML
    public void displayUserShoppingListController(){
        listener.onMyShoppingListsButtonClick();}

    /**
     * Affiche la page pour créer une liste de courses
     */
    @FXML
    public void displayCreateUserShoppingListController(){
        listener.onCreateShoppingListsButtonClick();
        }

    /**
     * Affiche la page pour retourner au menu précédent : la page principal d'une liste de shopping
      */
    @FXML
    public void returnHomeShoppingListController() {
        listener.onBackButtonClick();
    }

    public interface Listener{
        void onMyShoppingListsButtonClick();
        void onCreateShoppingListsButtonClick();
        void onBackButtonClick();
    }
}
