package ulb.infof307.g01.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.menu.CreateMenuViewController;
import ulb.infof307.g01.view.menu.WindowShowMenuController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.tools.GenerateMenuDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuController extends Controller implements CreateMenuViewController.Listener, GenerateMenuDialog.GenerateMenuListener {
    public static final int NUMBERS_DAYS_IN_WEEK = 7;
    public static final int DAY_ONE = 0;
    private WindowShowMenuController windowShowMenuController;
    private CreateMenuViewController createMenuViewController;
    private Menu menu;
    protected ArrayList<Day> daysName;
    private Stage popup = null;




    public MenuController(WindowShowMenuController windowShowMenuController){
        //TODO: changer ça avec le MVC of course
        this.windowShowMenuController = windowShowMenuController;
    }

    public MenuController(Stage primaryStage){
        setStage(primaryStage);
        menu = new Menu();
        daysName = new ArrayList<>();
        daysName.addAll(Arrays.asList(Day.values()).subList(DAY_ONE, NUMBERS_DAYS_IN_WEEK));
    }

    public void start(){
        createMenuViewController.getDaysComboBox().setItems(FXCollections.observableArrayList(daysName));
        createMenuViewController.getMenuTableColumn().setText(daysName.get(DAY_ONE).toString());
        fillMenuTableView(daysName.get(DAY_ONE));
    }

    public void fillMenuTableView(Day day) {
        List<Recipe> valueList = menu.getRecipesfor(day);
        for (Recipe products : valueList) {
            createMenuViewController.getMenuTableView().getItems().add(products);
        }
    }

    public void showCreateMenu(){
        FXMLLoader loader = this.loadFXML("CreateDisplayMenu.fxml");
        createMenuViewController = loader.getController();
        createMenuViewController.setListener(this);
        start();
    }

    @Override
    public void onGenerateMenu(){
        try {
            GenerateMenuDialog generateMenuDialog = new GenerateMenuDialog();
            popup = popupFXML("GenerateMenuDialog.fxml", generateMenuDialog);
            generateMenuDialog.setListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSaveMenu(String menuName){
        boolean isSaved = true;
        if(menuName.isBlank()) return !isSaved;
        try{
            if(menu.size() == 0) {
                return !isSaved;
            } else {
                Configuration.getCurrent().getMenuDao().insert(menu);
                //TODO Changer de fenetre
//                WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
//                mainMenuController.displayMainMenuController();
                return isSaved;
            }
        } catch(SQLException e) {
            //TODO Erreur sql a géré
        }
        return isSaved;
    }

    @Override
    public void onDaysComboBoxClicked(int dayIndex) {
        createMenuViewController.getMenuTableColumn().setText(daysName.get(dayIndex).toString());
        createMenuViewController.getMenuTableView().getItems().clear();
        fillMenuTableView(daysName.get(dayIndex));

    }

    @Override
    public void onAddRecipeClicked() {
        //TODO Appeler le controlleur pour l'ajout de recette dans le menu
        FXMLLoader loader = this.loadFXML("CreateDisplayMenu.fxml");
        createMenuViewController = loader.getController();
        createMenuViewController.setListener(this);
    }

    @Override
    public void onReturnClicked(){
        HomePageController homePageController = new HomePageController();
        FXMLLoader loader = this.loadFXML("HomePage.fxml");
        HomePageViewController viewController = loader.getController();

        viewController.setListener(homePageController);
        homePageController.displayMain(currentStage);

    }

    @Override
    public void onRemoveRecipeClicked(int dayIndex){
        Recipe recipeToRemove = createMenuViewController.getMenuTableView().getSelectionModel().getSelectedItem();
        menu.removeRecipeFrom(daysName.get(dayIndex), recipeToRemove);

    }

    public void displayCreateUserShoppingList(){
        CreateUserShoppingListViewController createUserShoppingListViewController = new CreateUserShoppingListViewController();
        loadFXML(createUserShoppingListViewController, "CreateUserShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(createUserShoppingListViewController);

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(true);
        fillShoppingList(createUserShoppingListViewController);
    }

    public void fillShoppingList(CreateUserShoppingListViewController viewController){
        ShoppingList shoppingList =  windowShowMenuController.fillShoppingList(viewController);
        viewController.fillTableViewWithExistentShoppingList(shoppingList);
    }


    @Override
    public void addValuesToGenerateMenu(int nbVegetarianDishes, int nbMeatDishes, int nbFishDishes) throws SQLException {
        menu.generateMenu(nbVegetarianDishes, nbMeatDishes, nbFishDishes);
        createMenuViewController.refreshTableView();
    }

    @Override
    public void cancelGenerateMenu() {
        popup.close();
    }
}
