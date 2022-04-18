package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;
import ulb.infof307.g01.view.recipe.HomeRecipeViewController;
import ulb.infof307.g01.view.recipe.SearchRecipeViewController;
import ulb.infof307.g01.view.recipe.UserRecipesViewController;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RecipeController extends Controller implements HomeRecipeViewController.HomeRecipeListener,
        CreateRecipeViewController.CreateRecipeListener,
        UserRecipesViewController.UserRecipesListener,
        SearchRecipeViewController.Listener {

    // private Controller parentController; //TODO

    Scene sceneViewRecipe = null;
    Scene sceneModifyRecipe = null;

    boolean isWaitingModification = false;

    private CreateRecipeViewController createRecipeViewController;
    private UserRecipesViewController userRecipesViewController;
    private SearchRecipeViewController searchRecipeViewController;

    private Recipe currentRecipe;
    private ShoppingList currentShoppingList;
    private SearchRecipeListener listener;

    public void displayMain() {
        this.currentShoppingList = null;
        this.currentRecipe = null;
        FXMLLoader loader = loadFXML("HomeRecipe.fxml");
        HomeRecipeViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    public void setListener(SearchRecipeListener searchRecipeListener){
        this.listener = searchRecipeListener;
    }
    // <-------------------------- Écran d'accueil des Recettes --------------------------> \\

    @Override
    public void onUserRecipesButtonClick() {
        FXMLLoader loader = this.loadFXML("viewRecipe.fxml");
        userRecipesViewController = loader.getController();
        userRecipesViewController.setListener(this);
    }

    @Override
    public void onNewRecipeButtonClick() {
        FXMLLoader loader = this.loadFXML("createRecipe.fxml");
        createRecipeViewController = loader.getController();
        createRecipeViewController.setListener(this);
    }

    @Override
    public void onBackButtonClick() {
        //parentController.displayMain(); TODO
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayMain();
    }

    // <-------------------------- Écran de Création des Recettes --------------------------> \\

    @Override
    public void onSubmitButton(String diet, String type, int nbPerson, String preparation, String recipeName) {

        boolean isValid = isValidRecipe(diet, type, nbPerson, preparation, recipeName);

        if (isValid) {
            currentRecipe = new Recipe(recipeName);
            currentRecipe.setCategory(diet);
            currentRecipe.setPreparation(preparation);
            currentRecipe.setType(type);
            currentRecipe.setNbrPerson(nbPerson);
            currentRecipe.addAll(currentShoppingList);
            try {
                if (isWaitingModification) {
                    Configuration.getCurrent().getRecipeDao().update(currentRecipe);
                    isWaitingModification = false;
                } else
                    Configuration.getCurrent().getRecipeDao().insert(currentRecipe);
            } catch (SQLException e) {
                e.printStackTrace();
                ViewController.showErrorSQL();
            }

            displayMain();
        }
    }

    /**
     * Vérifie que les paramètres sont valides pour créer un objet {@link Recipe}
     * @return {@code true} si les paramètres sont valides, {@code false} sinon
     */
    private boolean isValidRecipe(String diet, String type, int nbPerson, String preparation, String recipeName) {
        boolean isValid = true;
        // Vérifie qu'un Régime a été sélectionné
        if (diet == null) {
            createRecipeViewController.dietComboBoxError();
            isValid = false;
        }
        // Vérifie qu'un type a été sélectionné
        if (type == null) {
            createRecipeViewController.typeComboBoxError();
            isValid = false;
        }
        // TODO Vérifier que la liste d'ingrédients n'est pas vide
        // TODO: Reset erreur quand condition OK
        // Vérifie que la préparation n'est pas vide
        if (preparation.isBlank()) {
            createRecipeViewController.preparationTextAreaError();
            isValid = false;
        }
        // Vérifie que le nom n'est pas vide
        if(recipeName.isBlank()) {
            createRecipeViewController.recipeNameTextFieldError();
            isValid = false;
        }
        // Vérifie que le nombre de personnes est supérieur à 0
        if (nbPerson < 1) {
            createRecipeViewController.nbPersonSpinnerError();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onModifyProductsButton() {
        this.sceneModifyRecipe = currentStage.getScene();

        if (currentShoppingList == null) currentShoppingList = new ShoppingList("temporary");
        ShoppingListController shoppingListController = new ShoppingListController(this);

        shoppingListController.initForCreateRecipe(currentShoppingList);
    }

    public void modifyProductsCallback(@Nullable ShoppingList products) {
        currentStage.setScene(sceneModifyRecipe);

        if (products != null) {
            currentShoppingList = products;
            Vector<Product> productOfShoppingList = new Vector<>(products);
            createRecipeViewController.fillProductsTable(productOfShoppingList);
        }
    }


    @Override
    public void onCancelButton() {
        displayMain();
    }

    // <-------------------------- Écran de Liste des Recettes --------------------------> \\

    @Override
    public void onRecipeSearchTextFieldSubmit(String recipeName) {
        if (recipeName.isBlank())
            userRecipesViewController.recipeSearchTextFieldError(true);

        try {
            currentRecipe = Configuration.getCurrent().getRecipeDao().get(recipeName);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }

        if (currentRecipe == null)
            userRecipesViewController.recipeSearchTextFieldError(true);
        else {
            userRecipesViewController.recipeSearchTextFieldError(false);
            userRecipesViewController.setDisableRecipeButtons(false);
            userRecipesViewController.setRecipeTextArea(currentRecipe.getName(), productListToString(),
                    currentRecipe.getPreparation());
        }

    }

    private String productListToString() {
        StringBuilder res = new StringBuilder();
        for (Product p : currentRecipe) {
            res.append(p.getQuantity());
            res.append(p.getNameUnity()).append(" ");
            res.append(p.getName()).append("\n");
        }
        return res.toString();
    }

    @Override
    public void onModifyRecipeButtonClick() {
        isWaitingModification = true;

        this.sceneViewRecipe = currentStage.getScene();
        FXMLLoader loader = this.loadFXML("createRecipe.fxml");
        createRecipeViewController = loader.getController();
        createRecipeViewController.setListener(this);

        List<Product> productList = new ArrayList<>(currentRecipe);
        this.currentShoppingList = new ShoppingList(currentRecipe.getName());
        currentShoppingList.addAll(productList);
        createRecipeViewController.prefillFields(currentRecipe.getName(), currentRecipe.getPreparation(),
                currentRecipe.getType(), currentRecipe.getCategory(),
                currentRecipe.getNbrPerson(), productList);

        createRecipeViewController.setCancelButtonToModifyRecipe();
    }

    @Override
    public void onCancelModifyButton() {
        currentStage.setScene(sceneViewRecipe);
    }


    @Override
    public void onDeleteRecipeButtonClick() {
        try {
            Configuration.getCurrent().getRecipeDao().delete(currentRecipe);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
        userRecipesViewController.setDisableRecipeButtons(true);
        userRecipesViewController.resetRecipeTextArea();
        currentRecipe = null;
    }

    @Override
    public void onSeeAllRecipesButtonClick() {
        FXMLLoader loader = loadFXML("SearchRecipe.fxml");
        searchRecipeViewController = loader.getController();
        searchRecipeViewController.setListener(this);

        try {
            ArrayList<Recipe> recipesList = Configuration.getCurrent().getRecipeDao().getRecipeWhere(null, null, 0);
            ArrayList<String> typesList = Configuration.getCurrent().getRecipeTypeDao().getAllName();
            ArrayList<String> dietsList = Configuration.getCurrent().getRecipeCategoryDao().getAllName();
            searchRecipeViewController.initialize(dietsList, typesList, recipesList);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    @Override
    public void onImportRecipeFromJSONButtonClick() {
        final String windowTitle = "Importer une Recette depuis un fichier JSON";
        String extensionDescription = "Fichier JSON";
        File jsonFile = ViewController.showFileChooser(windowTitle, extensionDescription,
                "*.json", currentStage);

        if (jsonFile != null && jsonFile.getName().endsWith(".json")) {
            JSON json = new JSON();
            json.jsonReader(jsonFile.getAbsolutePath());
        }

    }

    @Override
    public void onBackToHomeRecipeButtonClick() {
        this.displayMain();
    }

    // <-------------------------- Écran de Recherche de Recette --------------------------> \\

    @Override
    public void onTypeComboBoxSelected(String recipeType) {
        String recipeDiet = searchRecipeViewController.getDietComboBoxSelectedItem();
        int recipeNbPerson = searchRecipeViewController.getNbPersonSpinnerValue();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    @Override
    public void onDietComboBoxSelected(String recipeDiet) {
        String recipeType = searchRecipeViewController.getTypeComboBoxSelectedItem();
        int recipeNbPerson = searchRecipeViewController.getNbPersonSpinnerValue();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    @Override
    public void onNbPersonCheckBoxChecked(boolean isChecked) {
        searchRecipeViewController.setDisableNbPersonSpinner(!isChecked);

        onNbPersonSpinnerClicked(searchRecipeViewController.getNbPersonSpinnerValue());
    }

    @Override
    public void onNbPersonSpinnerClicked(int recipeNbPerson) {
        String recipeType = searchRecipeViewController.getTypeComboBoxSelectedItem();
        String recipeDiet = searchRecipeViewController.getDietComboBoxSelectedItem();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    @Override
    public void onNbPersonSpinnerKeyPressed(int recipeNbPerson) {
        onNbPersonSpinnerClicked(recipeNbPerson);
    }

    private void refreshRecipeList(String recipeType, String recipeDiet, int nbPerson) {
        try {
            List<Recipe> recipesList = Configuration.getCurrent().getRecipeDao().getRecipeWhere(recipeDiet, recipeType, nbPerson);
            searchRecipeViewController.refreshRecipesTableView(recipesList);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    @Override
    public void onRecipesTableViewClicked(Recipe selectedRecipe) {
        if(listener == null) onUserRecipesButtonClick();

        currentRecipe = selectedRecipe;
        if (currentRecipe != null) {
            if(listener == null){
                userRecipesViewController.recipeSearchTextFieldError(false);
                userRecipesViewController.setDisableRecipeButtons(false);
                userRecipesViewController.setRecipeTextArea(currentRecipe.getName(), productListToString(), currentRecipe.getName());

            }else {
                listener.onRecipeSelected(selectedRecipe);

            }
        }
    }

    @Override
    public void onCancelSearchButton() {
        if(listener == null)onRecipesTableViewClicked(currentRecipe);
        else listener.onCancelButtonClicked();
    }

    public interface SearchRecipeListener{
        void onRecipeSelected(Recipe selectedRecipe);
        void onCancelButtonClicked();
    }
}
