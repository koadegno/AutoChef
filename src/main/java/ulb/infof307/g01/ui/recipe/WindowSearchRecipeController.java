package ulb.infof307.g01.ui.recipe;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.tools.UtilisationContrat;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Permet de rechercher une recette sur base de filtre
 * @param <T> Le type de la window qui fait appelle à nous
 * Retourne la recette choisie par l'utilisateur sinon previent que
 * la recherche est annuler
 */
public class WindowSearchRecipeController<T extends UtilisationContrat<Recipe>> extends Window implements Initializable {
    private ArrayList<String> dietList ;
    private ArrayList<String> typeList ;
    private ArrayList<Recipe>  recipeName;
    private  T mainController; //l'objet appelant qui implement les methodes add et cancel

    @FXML
    ComboBox recipeDietComboBox, recipeTypeComboBox;
    @FXML
    Spinner numberOfPersonSpinner;
    @FXML
    TableView recipeTableView;
    @FXML
    TableColumn columnRecipeName;
    @FXML
    Button cancelSearchRecipeButton;
    @FXML
    CheckBox activateSpinnerCheckBox;

    public WindowSearchRecipeController() throws SQLException {
        this.dietList = Configuration.getCurrent().getRecipeCategoryDao().getAllName();
        this.typeList = Configuration.getCurrent().getRecipeTypeDao().getAllName();
        this.recipeName = Configuration.getCurrent().getRecipeDao().getRecipeWhere(null, null, 0);
        this.dietList.add(0, "Tout");
        this.typeList.add(0, "Tout");
    }


    //override par les enfant mais pas abstract car par défaut le bouton qui lui est connecté ne fait rien
    @FXML
    public void confirmRecipe() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnRecipeName.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        recipeDietComboBox.setItems(FXCollections.observableArrayList(dietList));
        recipeTypeComboBox.setItems(FXCollections.observableArrayList(typeList));
        this.recipeTableView.getItems().addAll(recipeName);
        recipeDietComboBox.getSelectionModel().selectFirst();
        recipeTypeComboBox.getSelectionModel().selectFirst();
        recipeDietComboBox.setVisibleRowCount(10);
        numberOfPersonSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        this.onlyIntValue(numberOfPersonSpinner);
        this.numberOfPersonSpinner.setVisible(false);
    }

    /**
     * L'user à annuler la recherche d'une recette , on previent l'appelant que le contrat est fini
     */
    public void returnToCreateMenu() {
        this.mainController.cancel();
    }

    /**
     * On previent l'appelant que le contrat est fini
     * Donne l'objet demander et construit pas l'utilisateur
     */
    public void addRecipe(){
        int idx = recipeTableView.getSelectionModel().getSelectedIndex();
        if(idx > -1) this.mainController.add(this.recipeName.get(idx));
    }


    public void refreshTableView( Event actionEvent) throws SQLException{
        int dietIndex = recipeDietComboBox.getSelectionModel().getSelectedIndex();
        int typeIndex = recipeTypeComboBox.getSelectionModel().getSelectedIndex();
        int nbPerson = (int) numberOfPersonSpinner.getValue();

        String dietCondition = null; //all diet
        String typeCondition = null; //all type

        // get new recipe's list from Database
        if (dietIndex > 0) dietCondition = dietList.get(dietIndex);
        if (typeIndex > 0) typeCondition = typeList.get(typeIndex);
        if(!this.activateSpinnerCheckBox.isSelected()) nbPerson =0;
        this.recipeName = Configuration.getCurrent().getRecipeDao().getRecipeWhere(dietCondition, typeCondition, nbPerson);
        this.recipeTableView.getItems().clear();
        this.recipeTableView.getItems().addAll(recipeName);

    }

    public void checkBoxEvent(MouseEvent mouseEvent) throws SQLException{
        this.numberOfPersonSpinner.setVisible(this.activateSpinnerCheckBox.isSelected());
        this.refreshTableView(mouseEvent);
    }

    /**
     *
     * @param mainControler Un pointeur vers l'objet appelant qui s'est mis en attente
     */
    public void setMainController(T mainControler) {
        this.mainController = mainControler;
    }
}
