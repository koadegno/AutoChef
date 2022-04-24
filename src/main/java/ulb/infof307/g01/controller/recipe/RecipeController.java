package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.export.JSON;
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


/**
 * Contrôleur responsable de tous les écrans en lien avec les recettes
 */
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

    /**
     * Affiche l'écran d'accueil des recettes
     */
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

    /**
     * Affiche l'écran permettant à l'utilisateur de voir ses recettes
     */
    @Override
    public void onUserRecipesButtonClick() {
        FXMLLoader loader = this.loadFXML("VewRecipe.fxml");
        userRecipesViewController = loader.getController();
        userRecipesViewController.setListener(this);
    }

    /**
     * Affiche l'écran permettant de créer une nouvelle recette
     */
    @Override
    public void onNewRecipeButtonClick() {
        FXMLLoader loader = this.loadFXML("CreateRecipe.fxml");
        createRecipeViewController = loader.getController();
        createRecipeViewController.setListener(this);
    }

    /**
     * Retourne à l'écran principal
     */
    @Override
    public void onBackButtonClick() {
        //parentController.displayMain(); TODO
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayMain();
    }

    // <-------------------------- Écran de Création des Recettes --------------------------> \\

    /**
     * Vérifie que la recette est correcte et l'enregistre
     * @param diet Régime de la recette
     * @param type Type de la recette
     * @param nbPerson Nombre de personnes pour lesquelles la recette est faites
     * @param preparation Instructions pour préparer la recette
     * @param recipeName Nom de la recette
     */
    @Override
    public void onSubmitButton(String diet, String type, int nbPerson, String preparation, String recipeName) {

        boolean isValid = isValidRecipe(diet, type, nbPerson, preparation, recipeName);

        if (isValid) {
            int idRecipe = 0;
            if (isWaitingModification) {
                idRecipe = currentRecipe.getId();
            }
            currentRecipe = new Recipe(recipeName);
            currentRecipe.setCategory(diet);
            currentRecipe.setPreparation(preparation);
            currentRecipe.setType(type);
            currentRecipe.setNbrPerson(nbPerson);
            currentRecipe.addAll(currentShoppingList);
            try {
                if (isWaitingModification) {
                    currentRecipe.setId(idRecipe);
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
     * Vérifie que les paramètres sont valides pour créer un objet {@link Recipe},
     * si un des attributs de la recette est invalide, affiche une erreur sur l'élément
     * de l'interface concernée
     * @param diet Régime de la recette : ne doit pas être {@code null}
     * @param type Type de la recette : ne doit pas être {@code null}
     * @param nbPerson Nombre de personnes pour lesquelles la recette est faites : doit être supérieur à {@code 0}
     * @param preparation Instructions pour préparer la recette : ne doit pas être vide (minimum 1 caractère visible)
     * @param recipeName Nom de la recette : ne doit pas être vide (minimum 1 caractère visible)
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

    /**
     * Affiche l'écran permettant de modifier les produits contenus dans une recette
     * @see ShoppingListController#initForCreateRecipe(ShoppingList)
     */
    @Override
    public void onModifyProductsButton() {
        this.sceneModifyRecipe = currentStage.getScene();

        if (currentShoppingList == null) currentShoppingList = new ShoppingList("temporary");
        ShoppingListController shoppingListController = new ShoppingListController(this);

        shoppingListController.initForCreateRecipe(currentShoppingList);
    }

    /**
     * Méthode appelée par {@link ShoppingListController} après la fin de la modification des produits d'une recette
     * @param products La liste des {@link Product} ajoutés à la recette
     */
    public void modifyProductsCallback(@Nullable ShoppingList products) {
        currentStage.setScene(sceneModifyRecipe);

        if (products != null) {
            currentShoppingList = products;
            Vector<Product> productOfShoppingList = new Vector<>(products);
            createRecipeViewController.fillProductsTable(productOfShoppingList);
        }
    }

    /**
     * Revient à l'écran d'accueil des recettes
     */
    @Override
    public void onCancelButton() {
        displayMain();
    }

    // <-------------------------- Écran de Liste des Recettes --------------------------> \\

    /**
     * Cherche si une recette existe dans la base de données et l'affiche si possible, sinon affiche une erreur
     * sur le champ de recherche
     * @param recipeName Nom de la recette à rechercher
     */
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

    /**
     * Convertit une Liste de {@link Product} en un {@link String} lisible par des humains
     * @return le String construit
     */
    private String productListToString() {
        StringBuilder res = new StringBuilder();
        for (Product p : currentRecipe) {
            res.append(p.getQuantity());
            res.append(p.getNameUnity()).append(" ");
            res.append(p.getName()).append("\n");
        }
        return res.toString();
    }

    /**
     * Afficher l'écran permettant de modifier une recette déjà existante
     */
    @Override
    public void onModifyRecipeButtonClick() {
        isWaitingModification = true;

        this.sceneViewRecipe = currentStage.getScene();
        FXMLLoader loader = this.loadFXML("CreateRecipe.fxml");
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

    /**
     * Revient à l'écran permettant de consulter une recette
     */
    @Override
    public void onCancelModifyButton() {
        currentStage.setScene(sceneViewRecipe);
    }


    /**
     * Supprime une recette de la Base de donnée
     */
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

    /**
     * Affiche l'écran permettant de sélectionner une recette parmis toutes celles existantes
     */
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

    /**
     * Affiche une {@code pop-up} permettant d'importer une recette depuis un fichier {@code JSON}
     * @see JSON
     */
    @Override
    public void onImportRecipeFromJSONButtonClick() {
        final String windowTitle = "Importer une Recette depuis un fichier JSON";
        String extensionDescription = "Fichier JSON";
        File jsonFile = ViewController.showFileChooser(windowTitle, extensionDescription,
                "*.json", currentStage);

        if (jsonFile != null && jsonFile.getName().endsWith(".json")) {
            JSON json = new JSON();
            json.importRecipe(jsonFile.getAbsolutePath());
            onRecipeSearchTextFieldSubmit(json.getName());

        }

    }

    /**
     * Revient à la page d'accueil des recettes
     */
    @Override
    public void onBackToHomeRecipeButtonClick() {
        this.displayMain();
    }

    // <-------------------------- Écran de Recherche de Recette --------------------------> \\

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onTypeComboBoxSelected(String recipeType) {
        String recipeDiet = searchRecipeViewController.getDietComboBoxSelectedItem();
        int recipeNbPerson = searchRecipeViewController.getNbPersonSpinnerValue();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onDietComboBoxSelected(String recipeDiet) {
        String recipeType = searchRecipeViewController.getTypeComboBoxSelectedItem();
        int recipeNbPerson = searchRecipeViewController.getNbPersonSpinnerValue();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    /**
     * Active le {@link javafx.scene.control.Spinner} permettant de filtrer en fonction
     * d'un nombre de personnes
     */
    @Override
    public void onNbPersonCheckBoxChecked(boolean isChecked) {
        searchRecipeViewController.setDisableNbPersonSpinner(!isChecked);

        onNbPersonSpinnerClicked(searchRecipeViewController.getNbPersonSpinnerValue());
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onNbPersonSpinnerClicked(int recipeNbPerson) {
        String recipeType = searchRecipeViewController.getTypeComboBoxSelectedItem();
        String recipeDiet = searchRecipeViewController.getDietComboBoxSelectedItem();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onNbPersonSpinnerKeyPressed(int recipeNbPerson) {
        onNbPersonSpinnerClicked(recipeNbPerson);
    }

    /**
     * Rafraichit la liste des recettes affichées en fonction du régime, de la catégorie du plat et du nombre
     * de personnes sélectionnées
     * @param recipeType le type de {@link Recipe} selon lequelle filtrer
     * @param recipeDiet le régime de {@link Recipe} selon lequelle filtrer
     * @param nbPerson le nombre de personnes de {@link Recipe} selon lequelle filtrer
     */
    private void refreshRecipeList(String recipeType, String recipeDiet, int nbPerson) {
        try {
            List<Recipe> recipesList = Configuration.getCurrent().getRecipeDao().getRecipeWhere(recipeDiet, recipeType, nbPerson);
            searchRecipeViewController.refreshRecipesTableView(recipesList);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    /**
     * Affiche une recette sélectionnée
     * @see UserRecipesViewController
     */
    @Override
    public void onRecipesTableViewClicked(Recipe selectedRecipe) {
        if(listener == null) onUserRecipesButtonClick();

        currentRecipe = selectedRecipe;
        if (currentRecipe != null) {
            if(listener == null){
                userRecipesViewController.recipeSearchTextFieldError(false);
                userRecipesViewController.setDisableRecipeButtons(false);
                userRecipesViewController.setRecipeTextArea(currentRecipe.getName(), productListToString(), currentRecipe.getPreparation());

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
