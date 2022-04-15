package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import ulb.infof307.g01.model.JSON;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;
import ulb.infof307.g01.view.recipe.HomeRecipeViewController;
import ulb.infof307.g01.view.recipe.UserRecipesViewController;

import java.io.File;
import java.sql.SQLException;

public class RecipeController extends Controller implements HomeRecipeViewController.HomeRecipeListener,
                                                            CreateRecipeViewController.CreateRecipeListener,
                                                            UserRecipesViewController.UserRecipesListener {

    private Controller parentController;

    private CreateRecipeViewController createRecipeViewController; //TODO
    private UserRecipesViewController userRecipesViewController;

    private Recipe currentRecipe;

    public void displayMain() {
        FXMLLoader loader = loadFXML("HomeRecipe.fxml");
        HomeRecipeViewController viewController = loader.getController();
        viewController.setListener(this);
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
        parentController.displayMain();
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

            try {
                Configuration.getCurrent().getRecipeDao().insert(currentRecipe);
            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }

            parentController.displayMain();

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
        // TODO: Connecter au nouveau WindowUserShoppingListsController
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
}
