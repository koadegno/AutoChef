package ulb.infof307.g01.ui.recipe;

import javafx.event.ActionEvent;
import ulb.infof307.g01.ui.Window;

public class WindowCreateRecipeController extends Window {

    public void returnHomeRecipeWindow(ActionEvent actionEvent) {
        WindowHomeRecipeController myRecipeWindow = new WindowHomeRecipeController();
        myRecipeWindow.displayMain();
    }

    public void displayMain() {
        this.loadFXML("createRecipe.fxml");
    }
}
