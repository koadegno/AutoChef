package ulb.infof307.g01.view.recipe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * La classe gère la vue pour l'affichage d'une recette favorite
 */

public class FavoriteRecipeViewController extends ViewController<FavoriteRecipeViewController.Listener> implements Initializable {
    @FXML
    TableView<Recipe> favoriteRecipeTableView;
    @FXML
    TableColumn<Recipe, String> favoriteRecipeColumn;
    @Override

    public void initialize(URL location, ResourceBundle resources) {
        favoriteRecipeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void OnRecipeTableView() {
        Recipe recipe = favoriteRecipeTableView.getSelectionModel().getSelectedItem();
        if(recipe != null) listener.onFavoriteRecipesTableViewClicked(recipe);
    }

    public void displayFavoriteRecipe(List<Recipe> userFavoriteRecipe) {
        favoriteRecipeTableView.getItems().addAll(userFavoriteRecipe);
    }
    public void onReturnButton() {
        listener.onCancelButton();
    }

    public void logout() {
        listener.logout();
    }

    public  interface Listener {
        void logout();
        void onCancelButton();
        void onFavoriteRecipesTableViewClicked(Recipe recipe);
    }
}
