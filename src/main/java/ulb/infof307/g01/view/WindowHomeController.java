package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.MapController;
import ulb.infof307.g01.view.menu.WindowHomeMenuController;
import ulb.infof307.g01.view.recipe.WindowHomeRecipeController;
import ulb.infof307.g01.view.shoppingList.HomeShoppingListViewController;

/**
 * La classe WindowHomeController représente le controleur
 * pour la page principale de l'application qui s'affiche
 * lorsque le programme est lancé. Elle permet à l'utilisateur d'être redirigé vers
 * la page des Listes de Courses, Recettes ou Menus.
 * @see ulb.infof307.g01.model.ShoppingList
 * @see ulb.infof307.g01.model.Recipe
 * @see ulb.infof307.g01.model.Menu
 * */
public class WindowHomeController extends Window{
    public WindowHomeController() {}

    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * */
    @FXML
    public void displayMain(Stage primaryStage){
        try{
            setStage(primaryStage);
            this.loadFXML("HomePage.fxml");
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * Affiche la page principale des Listes de Courses.
     * @see HomeShoppingListViewController
     * */
    @FXML
    public void redirectToShoppingList(){
        //TODO: solution pas ouf ??? pour la primaryStage
        HomePageController homePageController = new HomePageController();
        homePageController.setStage(primaryStage);
        homePageController.onShoppingListButtonClick();

    }

    /**
     * Affiche la page principale des Menus.
     * @see WindowHomeMenuController
     * */
    @FXML
    public void redirectToMenu(){
        WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
        mainMenuController.displayMainMenuController();
    }

    @FXML
    public void redirectToMap(){
        // TODO appeller la bonne methode pour lancer la fenêtre
        MapController mapController = new MapController(primaryStage);
        mapController.show();
    }

    @FXML
    public void redirectToRecipe(){
        WindowHomeRecipeController recipeWindow = new WindowHomeRecipeController();
        recipeWindow.displayMain();
    }


    @FXML
    public void closeApplication(){
        primaryStage.close();
    }

}
