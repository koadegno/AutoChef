package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.controller.shoppingList.RecipeShoppingListController;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Classe qui contrôle la création d'une recette
 */
public class CreateRecipeController extends Controller implements CreateRecipeViewController.CreateRecipeListener {

    private CreateRecipeViewController createRecipeViewController;

    boolean isWaitingModification = false;

    private Recipe currentRecipe;

    private ShoppingList currentShoppingList;
    private Scene sceneModifyRecipe;

    private ListenerGetRecipe listenerGetRecipe;

    public CreateRecipeController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow) {
        this(null, primaryStage, listenerBackPreviousWindow);
    }

    public CreateRecipeController(ShoppingList currentShoppingList, Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow) {
        super(listenerBackPreviousWindow);
        this.currentShoppingList = currentShoppingList;
        setStage(primaryStage);
    }

    public void setListenerGetRecipe(ListenerGetRecipe listenerGetRecipe) {
        this.listenerGetRecipe = listenerGetRecipe;
    }


    public void setWaitingModification(boolean waitingModification) {
        isWaitingModification = waitingModification;
    }

    public void displayCreateRecipe() {
        FXMLLoader loader = this.loadFXML("CreateRecipe.fxml");
        createRecipeViewController = loader.getController();
        createRecipeViewController.setListener(this);
        this.initElementToDataBaseForCombobox();

    }

    /**
     * Récupère les noms des catégories et des types des recettes de la base de donnée
     */
    public void initElementToDataBaseForCombobox(){
        try {
            List<String> recipeCategoriesList = Configuration.getCurrent().getRecipeCategoryDao().getAllName();
            List<String> recipeTypesList = Configuration.getCurrent().getRecipeTypeDao().getAllName();
            createRecipeViewController.initCombobox(recipeCategoriesList, recipeTypesList);

        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    /**
     * Vérifie que la recette est correcte et l'enregistre
     *
     * @param diet        Régime de la recette
     * @param type        Type de la recette
     * @param nbPerson    Nombre de personnes pour lesquelles la recette est faites
     * @param preparation Instructions pour préparer la recette
     * @param recipeName  Nom de la recette
     */
    @Override
    public void onSubmitButton(String diet, String type, int nbPerson, String preparation, String recipeName) {

        boolean isValid = isValidRecipe(diet, type, nbPerson, preparation, recipeName);

        if (isValid) {
            int idRecipe = 0;
            boolean favoris = false;

            //Récupérer les informations de l'ancienne recette : ID + Favorite
            if (isWaitingModification) {
                idRecipe = currentRecipe.getId();
                favoris = currentRecipe.getFavorite();
            }

            //Creation d'une nouvelle recette
            currentRecipe = new Recipe.RecipeBuilder().withName(recipeName).withNumberOfPerson(nbPerson).withType(type).withPreparation(preparation).withCategory(diet).build();
            currentRecipe.addAll(currentShoppingList);
            try {
                if (isWaitingModification) {
                    currentRecipe.setId(idRecipe);
                    currentRecipe.setFavorite(favoris);
                    Configuration.getCurrent().getRecipeDao().update(currentRecipe);
                    isWaitingModification = false;
                } else
                    Configuration.getCurrent().getRecipeDao().insert(currentRecipe);
            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }

            if (listenerGetRecipe != null ) {
                listenerGetRecipe.setRecipe(currentRecipe);
            }
            listenerBackPreviousWindow.onReturn();
        }
    }

    /**
     * Vérifie que les paramètres sont valides pour créer un objet {@link Recipe},
     * si un des attributs de la recette est invalide, affiche une erreur sur l'élément
     * de l'interface concernée
     *
     * @param diet        Régime de la recette : ne doit pas être {@code null}
     * @param type        Type de la recette : ne doit pas être {@code null}
     * @param nbPerson    Nombre de personnes pour lesquelles la recette est faites : doit être supérieur à {@code 0}
     * @param preparation Instructions pour préparer la recette : ne doit pas être vide (minimum 1 caractère visible)
     * @param recipeName  Nom de la recette : ne doit pas être vide (minimum 1 caractère visible)
     * @return {@code true} si les paramètres sont valides, {@code false} sinon
     */
    private boolean isValidRecipe(String diet, String type, int nbPerson, String preparation, String recipeName) {
        boolean isValid = true;
        createRecipeViewController.clearErrors();

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

        // Vérifie qu'il y a au moins un ingrédient
        if(createRecipeViewController.getSizeTableViewIngredient() <= 0){
            createRecipeViewController.listIngredientIsSizeZeroError();
            isValid = false;
        }

        // Vérifie que la préparation n'est pas vide
        if (preparation.isBlank()) {
            createRecipeViewController.preparationTextAreaError();
            isValid = false;
        }
        // Vérifie que le nom n'est pas vide
        if (recipeName.isBlank()) {
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
     *
     * @see RecipeShoppingListController#initForCreateRecipe(ShoppingList)
     */
    @Override
    public void onModifyProductsButton() {
        this.sceneModifyRecipe = currentStage.getScene();

        if (currentShoppingList == null) currentShoppingList = new ShoppingList("Liste d'ingrédient");
        RecipeShoppingListController recipeShoppingListController = new RecipeShoppingListController(this);

        recipeShoppingListController.initForCreateRecipe(currentShoppingList);
    }

    /**
     * Méthode appelée par {@link ShoppingListController} après la fin de la modification des produits d'une recette
     *
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

    @Override
    public void onHelpCreateRecipeClicked() {
        int numberOfImageHelp = 10;
        HelpController helpController = new HelpController("helpCreateRecipe/", numberOfImageHelp);
        helpController.displayHelpShop();
    }

    /**
     * Revient à l'écran d'accueil des recettes
     */
    @Override
    public void onCancelButton() {
        listenerBackPreviousWindow.onReturn();
    }

    /**
     * Revient à l'écran permettant de consulter une recette
     */
    @Override
    public void onCancelModifyButton() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void logout() {
        userLogout();
    }

    public void prefillFields(Recipe currentRecipe, List<Product> productList) {
        this.currentRecipe = currentRecipe;
        createRecipeViewController.prefillFields(currentRecipe.getName(), currentRecipe.getPreparation(),
                currentRecipe.getType(), currentRecipe.getCategory(),
                currentRecipe.getNbrPerson(), productList);

        createRecipeViewController.setCancelButtonToModifyRecipe();
    }

    @FunctionalInterface
    public interface ListenerGetRecipe{
        void setRecipe(Recipe recipe);
    }
}
