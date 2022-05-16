package ulb.infof307.g01.view.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * La classe gère la vue de la popup qui permet de générer un menu
 */

public class GenerateMenuViewController extends ViewController<GenerateMenuViewController.GenerateMenuListener> implements Initializable {
    public static final int SPINNER_MAX = 1000;
    public static final int SPINNER_MIN = 0;
    GenerateMenuListener listener;
    @FXML
    private Spinner<Integer> vegetarianSpinner;
    @FXML
    private Spinner<Integer> meatSpinner;
    @FXML
    private Spinner<Integer> fishSpinner;
    @FXML
    private Button  cancelButton,validateButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vegetarianSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(SPINNER_MIN, SPINNER_MAX)
        );
        this.onlyIntValue(vegetarianSpinner);
        meatSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(SPINNER_MIN, SPINNER_MAX)
        );
        this.onlyIntValue(meatSpinner);
        fishSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(SPINNER_MIN, SPINNER_MAX)
        );
        this.onlyIntValue(fishSpinner);

        //connecting button
        validateButton.setOnAction(event -> {
            int nbVegetarianDishes = vegetarianSpinner.getValue();
            int nbMeatDishes = meatSpinner.getValue();
            int nbFishDishes = fishSpinner.getValue();
            try {
                listener.addValuesToGenerateMenu(nbVegetarianDishes, nbMeatDishes, nbFishDishes);
            } catch (SQLException e) {
                showErrorSQL();
            }
        });

        cancelButton.setOnAction(event -> listener.cancelGenerateMenu());
    }

    public void setListener(GenerateMenuListener listener) {
        this.listener = listener;
    }

    public interface GenerateMenuListener{
        void addValuesToGenerateMenu(int nbVegetarianDishes, int nbMeatDishes, int nbFishDishes) throws SQLException;
        void cancelGenerateMenu();
    }
}


