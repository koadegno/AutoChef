package ulb.infof307.g01.view.recipe;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ulb.infof307.g01.view.ViewController;

public class UserRecipesViewController extends ViewController<UserRecipesViewController.UserRecipesListener> {

    @FXML
    private TextField recipeSearchTextField;

    @FXML
    private TextArea recipeTextArea;

    public void onRecipeSearchTextFieldSubmit(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER)
            listener.onRecipeSearchTextFieldSubmit(recipeSearchTextField.getText());
    }

    public void onModifyRecipeButtonClick()         {listener.onModifyRecipeButtonClick();}
    public void onDeleteRecipeButtonClick()         {listener.onDeleteRecipeButtonClick();}
    public void onSeeAllRecipesButtonClick()        {listener.onSeeAllRecipesButtonClick();}
    public void onImportRecipeFromJSONButtonClick() {listener.onImportRecipeFromJSONButtonClick();}

    public void recipeSearchTextFieldError(boolean isError) {setNodeColor(recipeSearchTextField, true);}

    public void setRecipeTextArea(String recipeName, String recipeProducts, String recipePreparation) {
        final String nameHeader = "Nom de la recette :  ";
        final String productsHeader = "Ingrédients : \n";
        final String preparationHeader = "Préparation : \n";

        String recipeText = nameHeader        + recipeName     + "\n"
                          + productsHeader    + recipeProducts + "\n"
                          + preparationHeader + recipePreparation;

        recipeTextArea.setText(recipeText);
    }
    public interface UserRecipesListener {
        void onRecipeSearchTextFieldSubmit(String recipeName);
        void onModifyRecipeButtonClick();
        void onDeleteRecipeButtonClick();
        void onSeeAllRecipesButtonClick();
        void onImportRecipeFromJSONButtonClick();
    }
}
