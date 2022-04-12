package ulb.infof307.g01.ui.tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.menu.WindowCreateMenuController;

import java.net.URL;
import java.util.ResourceBundle;


public class GenerateMenuDialog extends Window implements Initializable {
    WindowCreateMenuController mainController = null;
    @FXML
    private Spinner vegetarianSpinner,meatSpinner,fishSpinner;
    @FXML
    private Button  cancelButton,validateButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vegetarianSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        this.onlyIntValue(vegetarianSpinner);
        meatSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        this.onlyIntValue(meatSpinner);
        fishSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        this.onlyIntValue(fishSpinner);

        //connecting button
        validateButton.setOnAction(event -> {
            int nbVegetarianDishes = (int) vegetarianSpinner.getValue();
            int nbMeatDishes = (int) meatSpinner.getValue();
            int nbFishDishes = (int) fishSpinner.getValue();
            this.mainController.addValuesToGenerateMenu(nbVegetarianDishes, nbMeatDishes, nbFishDishes);
        });

        cancelButton.setOnAction(event -> {
            this.mainController.cancelGeneratingMenu();
        });
    }

    public void setMainController(WindowCreateMenuController mainController) {
        this.mainController = mainController;
    }
}


