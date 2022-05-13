package ulb.infof307.g01.view.recipe;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ulb.infof307.g01.view.ViewController;

/**
 * La classe gère la vue pour voir et modifier ses recettes
 */

public class UserRecipesViewController extends ViewController<UserRecipesViewController.UserRecipesListener> {

    @FXML
    private Menu helpRecipeMenu;
    @FXML
    private TextField recipeSearchTextField;
    @FXML
    private TextArea recipeTextArea;
    @FXML
    private Button deleteRecipeButton, modifyRecipeButton, cancelButton,seeAllRecipesButton,importJSONButton;
    @FXML
    private CheckBox favoriteCheckBox;

    Boolean readOnly = false;

    public void onRecipeSearchTextFieldSubmit(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER)
            listener.onRecipeSearchTextFieldSubmit(recipeSearchTextField.getText());
    }

    public void onModifyRecipeButtonClick()         {listener.onModifyRecipeButtonClick();}
    public void onDeleteRecipeButtonClick()         {listener.onDeleteRecipeButtonClick();}
    public void onSeeAllRecipesButtonClick()        {listener.onSeeAllRecipesButtonClick();}
    public void onImportRecipeFromJSONButtonClick() {listener.onImportRecipeFromJSONButtonClick();}
    public void onBackToHomeRecipeButtonClick()     {listener.onBackToHomeRecipeButtonClick();}

    public void recipeSearchTextFieldError(boolean isError) {setNodeColor(recipeSearchTextField, isError);}

    /**
     * Permet d'aaficher le contenu de la recette
     * @param recipeName le nom de la recette
     * @param recipeProducts le nom des produits
     * @param recipePreparation le contenu de la preparation de sa recette
     */
    public void setRecipeTextArea(String recipeName, String recipeProducts, String recipePreparation) {
        final String nameHeader = "Nom de la recette :  ";
        final String productsHeader = "Ingrédients : \n";
        final String preparationHeader = "Préparation : \n";

        String recipeText = nameHeader        + recipeName     + "\n"
                          + productsHeader    + recipeProducts + "\n"
                          + preparationHeader + recipePreparation;

        recipeTextArea.setText(recipeText);
    }
    public void resetRecipeTextArea() {
        recipeTextArea.setText("Aucune recette sélectionnée");
    }

    public void setDisableRecipeButtons(boolean value) {
        deleteRecipeButton.setDisable(value);
        modifyRecipeButton.setDisable(value);
        favoriteCheckBox.setDisable(value);
    }

    public void logout() {
        listener.logout();
    }

    /**
     * Active seulement les éléments que l'utilisateur peut avoir pour le menu recette favorite
     */
    public void initReadOnlyMode() {
        readOnly = true;
        recipeSearchTextField.setVisible(false);
        seeAllRecipesButton.setVisible(false);
        importJSONButton.setVisible(false);
        modifyRecipeButton.setVisible(false);
        deleteRecipeButton.setVisible(false);
        cancelButton.setOnAction(event -> {
            listener.onEndViewFavoriteRecipeButton();
        });
        favoriteCheckBox.setVisible(false);
    }

    public void checkFavoriteCheckBox(Boolean isSelected) {
        favoriteCheckBox.setSelected(isSelected);
    }

    public void onFavoriteRecipeCheck() {
        Boolean isChecked = favoriteCheckBox.isSelected();
        listener.onFavoriteRecipeCheck(isChecked);
    }

    public void helpRecipe() {
        listener.helpModifyRecipeClick();
    }

    public void setVisibleHelpRecipe(boolean isVisible) {
        helpRecipeMenu.setVisible(isVisible);
    }


    public interface UserRecipesListener {
        void onRecipeSearchTextFieldSubmit(String recipeName);
        void onModifyRecipeButtonClick();
        void onDeleteRecipeButtonClick();
        void onSeeAllRecipesButtonClick();
        void onImportRecipeFromJSONButtonClick();
        void onBackToHomeRecipeButtonClick();
        void logout();
        void onEndViewFavoriteRecipeButton();
        void onFavoriteRecipeCheck(Boolean isChecked);
        void helpModifyRecipeClick();
    }
}
