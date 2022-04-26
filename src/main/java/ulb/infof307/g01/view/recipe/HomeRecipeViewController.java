package ulb.infof307.g01.view.recipe;

import javafx.event.ActionEvent;
import ulb.infof307.g01.view.ViewController;

public class HomeRecipeViewController extends ViewController<HomeRecipeViewController.HomeRecipeListener> {

    public void onUserRecipesButtonClick() {listener.onUserRecipesButtonClick();}
    public void onCreateRecipeButtonClick() {listener.onNewRecipeButtonClick();}
    public void onBackButtonClick() {listener.onBackButtonClick();}

    public void logout() {
        listener.logout();
    }
    public interface HomeRecipeListener {
        void onUserRecipesButtonClick();
        void onNewRecipeButtonClick();
        void onBackButtonClick();

        void logout();
    }
}
