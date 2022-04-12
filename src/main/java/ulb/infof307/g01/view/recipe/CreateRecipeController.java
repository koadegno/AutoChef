package ulb.infof307.g01.view.recipe;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import ulb.infof307.g01.view.ViewController;

/**
 * Contrôleur de Vue pour l'interface de Création de Recettes
 */
public class CreateRecipeController extends ViewController<CreateRecipeController.CreateRecipeListener> {
    @FXML
    private TableView ingredientTableView;
    @FXML
    private TableColumn tableColumnProduct, tableColumnQuantityOrNumber, tableColumnUnity;
    @FXML
    private TextArea preparationTextArea;
    @FXML
    private ComboBox typeComboBox;
    @FXML
    private ComboBox dietComboBox;
    @FXML
    private Spinner nbPersonSpinner;
    @FXML
    private TextField recipeNameTextField;

    public void onSubmitButton() {
        int dietIndex = dietComboBox.getSelectionModel().getSelectedIndex();
        int typeIndex = typeComboBox.getSelectionModel().getSelectedIndex();
        int nbPerson = (int) nbPersonSpinner.getValue();
        String preparation = preparationTextArea.getText();
        String recipeName = recipeNameTextField.getText();

        listener.onSubmitButton(dietIndex, typeIndex, nbPerson, preparation, recipeName);
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

    public interface CreateRecipeListener {
        void onSubmitButton(int dietIndex, int typeIndex, int nbPerson, String preparation, String recipeName);
        void onModifyProductsButton();
        void onCancelButton();
    }
}
