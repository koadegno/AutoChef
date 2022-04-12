package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * La classe {@code HomePageController} représente le contrôleur de Vue
 * pour la page principale de l'application qui s'affiche lorsque le programme
 * est lancé. Elle permet à l'utilisateur de se diriger vers
 * la page des Listes de Courses, Recettes, Menus ou Map.
 * @see ulb.infof307.g01.model.ShoppingList
 * @see ulb.infof307.g01.model.Recipe
 * @see ulb.infof307.g01.model.Menu
 * */
public class HomePageController extends ViewController<HomePageController.HomePageListener> {

    @FXML
    public Button shoppingListButton;
    @FXML
    public Button menuButton;
    @FXML
    public Button mapButton;

    @FXML
    public void onShoppingListButtonClick() {listener.onShoppingListButtonClick();}

    @FXML
    public void onMenuButtonClick() {listener.onMenuButtonClick();}

    @FXML
    public void onMapButtonClick(){listener.onMapButtonClick();}

    @FXML
    public void onRecipeButtonClick(){listener.onRecipeButtonClick();}

    @FXML
    public void onQuitButtonClick(){
        listener.onQuitButtonClick();
    }

    /**
     * Interface qui doit être implémenté par les {@code Controller} qui utilisent
     * {@code HomePageController}
     */
    public interface HomePageListener {
        void onShoppingListButtonClick();
        void onMenuButtonClick();
        void onMapButtonClick();
        void onRecipeButtonClick();
        void onQuitButtonClick();
    }
}