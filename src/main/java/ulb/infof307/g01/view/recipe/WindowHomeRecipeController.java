package ulb.infof307.g01.view.recipe;

import javafx.event.ActionEvent;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.WindowHomeController;

public class WindowHomeRecipeController extends Window {

    public void displayMain() {
        this.loadFXML("HomeRecipe.fxml");
    }

    public void returnMainMenu() { // use by fxml
        WindowHomeController windowHomeController = new WindowHomeController();
        windowHomeController.displayMain(primaryStage);
    }

    public void displayViewRecipeWindow(ActionEvent actionEvent) {
        WindowViewRecipeController viewMyRecipe = new WindowViewRecipeController();
        viewMyRecipe.displayMain();
    }

    public void displayCreateRecipeWindow(ActionEvent actionEvent) {
        WindowCreateRecipeController createRecipe = new WindowCreateRecipeController();
        createRecipe.displayMain();
    }
}
