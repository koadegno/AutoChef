package ulb.infof307.g01.view.recipe;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Contrôleur de Vue pour l'interface de Création de Recettes
 */
public class CreateRecipeViewController extends ViewController<CreateRecipeViewController.CreateRecipeListener> implements Initializable {
    @FXML
    private TableView ingredientTableView;
    @FXML
    private TableColumn tableColumnProduct, tableColumnQuantityOrNumber, tableColumnUnity;
    @FXML
    private TextArea preparationTextArea;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> dietComboBox;
    @FXML
    private Spinner nbPersonSpinner;
    @FXML
    private TextField recipeNameTextField;

    /**
     * Initialise la scène
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ArrayList<String> recipeCategoriesList = Configuration.getCurrent().getRecipeCategoryDao().getAllName();
            ArrayList<String> recipeTypesList = Configuration.getCurrent().getRecipeTypeDao().getAllName();

            dietComboBox.setItems(FXCollections.observableArrayList(recipeCategoriesList));
            typeComboBox.setItems(FXCollections.observableArrayList(recipeTypesList));
        } catch (SQLException e) {
            showErrorSQL();
        }
    }

    public void onSubmitButton() {
        String diet = dietComboBox.getSelectionModel().getSelectedItem();
        String type = typeComboBox.getSelectionModel().getSelectedItem();
        int nbPerson = (int) nbPersonSpinner.getValue();
        String preparation = preparationTextArea.getText();
        String recipeName = recipeNameTextField.getText();

        listener.onSubmitButton(diet, type, nbPerson, preparation, recipeName);
    }

    public void onModifyProductsButton() {listener.onModifyProductsButton();}

    public void onCancelButton() {listener.onCancelButton();}

    /**
     * Appelé lorsque la {@link ComboBox} {@code diet} à une valeur invalide
     */
    public void dietComboBoxError() {
        setNodeColor(dietComboBox, true);
    }

    /**
     * Appelé lorsque la {@link ComboBox} {@code type} à une valeur invalide
     */
    public void typeComboBoxError() {
        setNodeColor(typeComboBox, true);
    }

    /**
     * Appelé lorsque la {@link TextArea} {@code preparation} à une valeur invalide
     */
    public void preparationTextAreaError() {
        setNodeColor(preparationTextArea, true);
    }

    /**
     * Appelé lorsque la {@link TextField} {@code recipeName} à une valeur invalide
     */
    public void recipeNameTextFieldError() {
        setNodeColor(recipeNameTextField, true);
    }

    public void nbPersonSpinnerError() {
        setNodeColor(nbPersonSpinner, true);
    }

    public interface CreateRecipeListener {
        void onSubmitButton(String diet, String type, int nbPerson, String preparation, String recipeName);
        void onModifyProductsButton();
        void onCancelButton();
    }
}
