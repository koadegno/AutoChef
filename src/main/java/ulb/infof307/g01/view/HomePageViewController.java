package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * La classe {@code HomePageViewController} représente le contrôleur de Vue
 * pour la page principale de l'application qui s'affiche lorsque le programme
 * est lancé. Elle permet à l'utilisateur de se diriger vers
 * la page des Listes de Courses, Recettes, Menus ou map.
 * @see ulb.infof307.g01.model.ShoppingList
 * @see ulb.infof307.g01.model.Recipe
 * @see ulb.infof307.g01.model.Menu
 * */
public class HomePageViewController extends ViewController<HomePageViewController.HomePageListener> {

    @FXML
    public Button shoppingListButton;
    @FXML
    public Button menuButton;
    @FXML
    public Button mapButton;

    @FXML
    public void redirectToShoppingList() {listener.onShoppingListButtonClick();}

    @FXML
    public void redirectToMenu() {listener.onMenuButtonClick();}

    @FXML
    public void redirectToMap(){listener.onMapButtonClick();}

    @FXML
    public void redirectToRecipe(){listener.onRecipeButtonClick();}

    public void logout() {
        listener.logout();
    }

    /**
     * Interface qui doit être implémenté par les {@code Controller} qui utilisent
     * {@code HomePageViewController}
     */
    public interface HomePageListener {
        void onShoppingListButtonClick();
        void onMenuButtonClick();
        void onMapButtonClick();
        void onRecipeButtonClick();
        void logout();
    }
}
