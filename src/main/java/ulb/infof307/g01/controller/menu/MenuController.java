package ulb.infof307.g01.controller.menu;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.controller.recipe.SearchRecipeController;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.menu.CreateMenuViewController;
import ulb.infof307.g01.view.menu.GenerateMenuViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuController extends Controller implements CreateMenuViewController.Listener, GenerateMenuViewController.GenerateMenuListener, SearchRecipeController.SearchRecipeListener,ListenerBackPreviousWindow {
    public static final int NUMBERS_DAYS_IN_WEEK = 7;
    public static final int DAY_ONE = 0;
    private CreateMenuViewController createMenuViewController;
    private Menu menu;
    protected ArrayList<Day> daysName;
    private Stage popup = null;
    private Scene currentScene;
    private boolean isModifying;


    public MenuController(Stage primaryStage){ this(primaryStage,null); }

    public MenuController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
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

    public void displayCreateMenu(){
        FXMLLoader loader = this.loadFXML("CreateDisplayMenu.fxml");
        createMenuViewController = loader.getController();
        createMenuViewController.setListener(this);
        start();
        isModifying = false;
    }

    public void showModifyMenu(Menu menu) {
        this.menu = menu;
        displayCreateMenu();
        createMenuViewController.setModifyMode(menu.getName());
        isModifying = true;
    }

    @Override
    public void onGenerateMenu(){
        try {
            GenerateMenuViewController generateMenuViewController = new GenerateMenuViewController();
            popup = popupFXML("GenerateMenu.fxml", generateMenuViewController);
            generateMenuViewController.setListener(this);
        } catch (IOException e) {
            ViewController.showErrorSQL();
        }
    }

    @Override
    public void onSaveMenu(String menuName){
        boolean isError = false;
        if((menuName.isBlank() || menuName.isEmpty() ) && !isModifying )
        {   createMenuViewController.showNameMenuError(!isError);
            return;
        }
        try{
            if(menu.size() == 0) {
                createMenuViewController.showTableViewMenuError(!isError);
                return;
            } else {
                if(!isModifying){
                    menu.setName(menuName);
                    Configuration.getCurrent().getMenuDao().insert(menu);
                }
                else Configuration.getCurrent().getMenuDao().update(menu);

                createMenuViewController.showTableViewMenuError(isError);
                createMenuViewController.showNameMenuError(isError);

                listenerBackPreviousWindow.onReturn();

            }
        } catch(SQLException e) {
            ViewController.showErrorSQL();
        }
        createMenuViewController.showTableViewMenuError(isError);
        createMenuViewController.showNameMenuError(isError);
    }

    @Override
    public void onDaysComboBoxClicked(int dayIndex) {
        createMenuViewController.getMenuTableColumn().setText(daysName.get(dayIndex).toString());
        createMenuViewController.getMenuTableView().getItems().clear();
        fillMenuTableView(daysName.get(dayIndex));

    }

    @Override
    public void onReturnClicked(){
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void onRemoveRecipeClicked(int dayIndex){
        Recipe recipeToRemove = createMenuViewController.getMenuTableView().getSelectionModel().getSelectedItem();
        menu.removeRecipeFrom(daysName.get(dayIndex), recipeToRemove);

    }

    @Override
    public void onHelpCreateMenuClicked() {
        int numberOfImageHelp = 8;
        HelpController helpController = new HelpController("helpCreateMenu/", numberOfImageHelp);
        helpController.displayHelpShop();
    }

    @Override
    public void logout() {
        userLogout();
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
        SearchRecipeController searchRecipeController = new SearchRecipeController(currentStage,this);
        searchRecipeController.setListener(this);
        searchRecipeController.displaySearchRecipe();
    }

    @Override
    public void onRecipeSelected(Recipe selectedRecipe) {
        int selectedIndex = createMenuViewController.getDaysComboBox().getSelectionModel().getSelectedIndex();
        Day day = daysName.get(selectedIndex);
        menu.addRecipeTo(day,selectedRecipe);
        createMenuViewController.refreshTableView();
    }

    @Override
    public void onReturn() { displayCreateMenu();}
}
