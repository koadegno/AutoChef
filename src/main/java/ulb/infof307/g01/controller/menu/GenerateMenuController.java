package ulb.infof307.g01.controller.menu;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.view.tools.GenerateMenuDialog;

public class GenerateMenuController extends Controller implements GenerateMenuDialog.GenerateMenuListener {
    Menu menu;

    public GenerateMenuController(Stage primaryStage,Controller menuController){
        setStage(primaryStage);

    }


    @Override
    public void addValuesToGenerateMenu(int nbVegetarianDishes, int nbMeatDishes, int nbFishDishes) {

    }

    @Override
    public void cancelGenerateMenu() {
    }



}
