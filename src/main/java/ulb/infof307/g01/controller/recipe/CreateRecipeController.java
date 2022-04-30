package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;
import ulb.infof307.g01.view.recipe.FavoriteRecipeViewController;
import ulb.infof307.g01.view.recipe.SearchRecipeViewController;
import ulb.infof307.g01.view.recipe.UserRecipesViewController;

import java.sql.SQLException;
import java.util.Vector;

public class CreateRecipeController extends Controller implements CreateRecipeViewController.CreateRecipeListener{

    Scene sceneViewRecipe = null;

    private CreateRecipeViewController createRecipeViewController;
    boolean isWaitingModification = false;

    private Recipe currentRecipe;
    private ShoppingList currentShoppingList;
    private Scene sceneModifyRecipe;

    public CreateRecipeController(Stage primaryStage) { this(primaryStage,null) ;}

    public CreateRecipeController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
    }

    public void showCreateRecipe(){
        FXMLLoader loader = this.loadFXML("CreateRecipe.fxml");
        createRecipeViewController = loader.getController();
        createRecipeViewController.setListener(this);
    }

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

            listenerBackPreviousWindow.onReturn();
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

    @Override
    public void onHelpCreateRecipeClicked(){
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

}
