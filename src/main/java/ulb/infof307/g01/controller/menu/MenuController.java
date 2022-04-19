package ulb.infof307.g01.controller.menu;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.recipe.RecipeController;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.menu.CreateMenuViewController;
import ulb.infof307.g01.view.menu.HomeMenuViewController;
import ulb.infof307.g01.view.menu.ShowMenuViewController;
import ulb.infof307.g01.view.menu.GenerateMenuViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuController extends Controller implements CreateMenuViewController.Listener, GenerateMenuViewController.GenerateMenuListener, RecipeController.SearchRecipeListener {
    public static final int NUMBERS_DAYS_IN_WEEK = 7;
    public static final int DAY_ONE = 0;
    private ShowMenuViewController windowShowMenuViewController;
    private CreateMenuViewController createMenuViewController;
    private Menu menu;
    protected ArrayList<Day> daysName;
    private Stage popup = null;
    private Scene currentScene;
    private boolean isModifying;

    public MenuController(ShowMenuViewController showMenuViewController){
        //TODO: changer ça avec le MVC of course
        this.windowShowMenuViewController = showMenuViewController;
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
        createMenuViewController.getDaysComboBox().getSelectionModel().selectFirst();

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
        isModifying = false;
    }

    public void showModifyMenu(Menu menu) {
        this.menu = menu;
        showCreateMenu();
        createMenuViewController.setModifyMode();
        isModifying = true;
    }

    @Override
    public void onGenerateMenu(){
        try {
            GenerateMenuViewController generateMenuViewController = new GenerateMenuViewController();
            popup = popupFXML("GenerateMenuViewController.fxml", generateMenuViewController);
            generateMenuViewController.setListener(this);
        } catch (IOException e) {
            ViewController.showErrorSQL();
        }
    }

    @Override
    public boolean onSaveMenu(String menuName){
        boolean isSaved = true;
        if((menuName.isBlank() || menuName.isEmpty() ) && !isModifying ) return !isSaved;
        try{
            if(menu.size() == 0) {
                return !isSaved;
            } else {
                if(!isModifying){
                    menu.setName(menuName);
                    Configuration.getCurrent().getMenuDao().insert(menu);
                }
                else Configuration.getCurrent().getMenuDao().update(menu);


                FXMLLoader loader = this.loadFXML("HomeMenu.fxml");
                HomeMenuViewController viewController = loader.getController();
                HomePageController homePageController = new HomePageController(currentStage);
                viewController.setListener(homePageController);
                return isSaved;
            }
        } catch(SQLException e) {
            ViewController.showErrorSQL();
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
    public void onReturnClicked(){
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.onMenuButtonClick();
    }

    @Override
    public void onRemoveRecipeClicked(int dayIndex){
        Recipe recipeToRemove = createMenuViewController.getMenuTableView().getSelectionModel().getSelectedItem();
        menu.removeRecipeFrom(daysName.get(dayIndex), recipeToRemove);

    }


    @Override
    public void addValuesToGenerateMenu(int nbVegetarianDishes, int nbMeatDishes, int nbFishDishes) throws SQLException {
        menu.generateMenu(nbVegetarianDishes, nbMeatDishes, nbFishDishes);
        createMenuViewController.refreshTableView();
        cancelGenerateMenu();
    }

    @Override
    public void cancelGenerateMenu() {
        popup.close();
    }

    @Override
    public void onAddRecipeClicked() {
        currentScene = currentStage.getScene();
        RecipeController recipeController = new RecipeController();
        recipeController.setStage(currentStage);
        recipeController.setListener(this);
        recipeController.onSeeAllRecipesButtonClick();
    }

    @Override
    public void onCancelButtonClicked() {
        currentStage.setScene(currentScene);
    }

    @Override
    public void onRecipeSelected(Recipe selectedRecipe) {
        currentStage.setScene(currentScene);
        int selectedIndex = createMenuViewController.getDaysComboBox().getSelectionModel().getSelectedIndex();
        Day day = daysName.get(selectedIndex);
        menu.addRecipeTo(day,selectedRecipe);
        createMenuViewController.refreshTableView();
    }
}
