package ulb.infof307.g01.view.recipe;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.ViewController;

import java.util.List;

public class SearchRecipeViewController extends ViewController<SearchRecipeViewController.Listener> {

    @FXML
    private Spinner<Integer> nbPersonSpinner;
    @FXML
    private ComboBox<String> dietComboBox, typeComboBox;
    @FXML
    private CheckBox nbPersonCheckBox;
    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Recipe> recipesTableView;
    @FXML
    private TableColumn<Recipe, String> columnRecipeName;


    public void initialize(List<String> dietList, List<String> typeList, List<Recipe> recipeList) {
        columnRecipeName.setCellValueFactory(new PropertyValueFactory<>("name"));

        dietComboBox.setItems(FXCollections.observableArrayList(dietList));
        typeComboBox.setItems(FXCollections.observableArrayList(typeList));

        dietComboBox.getSelectionModel().selectFirst();
        typeComboBox.getSelectionModel().selectFirst();

        dietComboBox.setVisibleRowCount(10);
        recipesTableView.getItems().addAll(recipeList);


        nbPersonSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );

        this.onlyIntValue(nbPersonSpinner);
    }

    public void refreshRecipesTableView(List<Recipe> recipesList) {
        recipesTableView.getItems().clear();
        recipesTableView.getItems().addAll(recipesList);
    }

    public void onTypeComboBoxSelected() {
        String typeComboBoxSelectedItem = typeComboBox.getSelectionModel().getSelectedItem();
        listener.onTypeComboBoxSelected(typeComboBoxSelectedItem);
    }

    public void onDietComboBoxSelected() {
        String dietComboBoxSelectedItem = dietComboBox.getSelectionModel().getSelectedItem();
        listener.onDietComboBoxSelected(dietComboBoxSelectedItem);
    }

    public void onNbPersonCheckBoxChecked() {
        listener.onNbPersonCheckBoxChecked(nbPersonCheckBox.isSelected());
    }

    public void onNbPersonSpinnerClicked() {
        listener.onNbPersonSpinnerClicked(nbPersonSpinner.getValue());
    }

    public void onNbPersonSpinnerKeyPressed() {
        listener.onNbPersonSpinnerKeyPressed(nbPersonSpinner.getValue());
    }

    public void onRecipesTableViewClicked() {listener.onRecipesTableViewClicked();}
    public void onCancelButtonClicked()  {listener.onCancelButton();} // FIXME : Retourne sur HomeRecipe

    public String getDietComboBoxSelectedItem() {return dietComboBox.getSelectionModel().getSelectedItem();}
    public String getTypeComboBoxSelectedItem() {return typeComboBox.getSelectionModel().getSelectedItem();}
    public int  getNbPersonSpinnerValue() {
        if (nbPersonCheckBox.isSelected())
            return nbPersonSpinner.getValue();
        return 0;
    }

    public void setDisableNbPersonSpinner(boolean value) {
        nbPersonSpinner.setDisable(value);
    }
    public interface Listener {
        void onTypeComboBoxSelected(String recipeType);
        void onDietComboBoxSelected(String recipeDiet);
        void onNbPersonCheckBoxChecked(boolean isChecked);
        void onNbPersonSpinnerClicked   (int recipeNbPerson);
        void onNbPersonSpinnerKeyPressed(int recipeNbPerson);
        void onRecipesTableViewClicked();
        void onCancelButton();
    }
}
