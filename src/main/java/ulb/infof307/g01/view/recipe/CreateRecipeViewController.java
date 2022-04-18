package ulb.infof307.g01.view.recipe;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur de Vue pour l'interface de Création de Recettes
 */
public class CreateRecipeViewController extends ViewController<CreateRecipeViewController.CreateRecipeListener> implements Initializable {
    @FXML
    private TableView<Product> ingredientTableView;
    @FXML
    private TableColumn<Product, String> tableColumnProduct, tableColumnUnity;
    @FXML
    private TableColumn<Product, Integer> tableColumnQuantityOrNumber;
    @FXML
    private TextArea preparationTextArea;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> dietComboBox;
    @FXML
    private Spinner<Integer> nbPersonSpinner;
    @FXML
    private TextField recipeNameTextField;
    @FXML
    private Button cancelButton;

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

        nbPersonSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        this.onlyIntValue(nbPersonSpinner);
        tableColumnProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableColumnUnity.setCellValueFactory(new PropertyValueFactory<>("nameUnity"));
    }

    public void onSubmitButton() {
        String diet = dietComboBox.getSelectionModel().getSelectedItem();
        String type = typeComboBox.getSelectionModel().getSelectedItem();
        int nbPerson = nbPersonSpinner.getValue();
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

    public void fillProductsTable(List<Product> productsList) {
        ingredientTableView.setItems(FXCollections.observableArrayList(productsList));
    }

    public void prefillFields(String recipeName, String recipePreparation, String recipeType, String recipeDiet,
                              int recipeNbPerson, List<Product> productsList) {
        recipeNameTextField.setText(recipeName);
        recipeNameTextField.setDisable(true);
        preparationTextArea.setText(recipePreparation);
        typeComboBox.getSelectionModel().select(recipeType);
        dietComboBox.getSelectionModel().select(recipeDiet);
        nbPersonSpinner.getValueFactory().setValue(recipeNbPerson);
        fillProductsTable(productsList);

    }

    public void setCancelButtonToModifyRecipe() {
        cancelButton.setOnAction(event -> listener.onCancelModifyButton());
    }

    public interface CreateRecipeListener {
        void onSubmitButton(String diet, String type, int nbPerson, String preparation, String recipeName);
        void onModifyProductsButton();
        void onCancelButton();
        void onCancelModifyButton();
    }
}
