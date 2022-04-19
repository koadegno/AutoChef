package ulb.infof307.g01.controller.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.menu.ShowMenuViewController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowMenuController extends Controller implements ShowMenuViewController.Listener {

    private final Menu menu;
    private ShowMenuViewController viewController;


    public ShowMenuController(Stage primaryStage, Menu menu) {
        setStage(primaryStage);
        this.menu = menu;
    }

    public void showMenu(){
        FXMLLoader loader = this.loadFXML("ShowMenu.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        startMenu();

    }

    private void startMenu() {
        ArrayList<Day> days = new ArrayList<>(Arrays.asList(Day.values()).subList(0, menu.getNbOfdays()));
        displayMenuInfo();
        displayMenuTable(days);
    }

    private void displayMenuInfo(){
        viewController.getMenuNameLabel().setText(menu.getName());
        viewController.getNbOfDayLabel().setText("Durée : "+ menu.getNbOfdays() +"jours");
    }

    /**
     * Affiche le contenu du menu, avec la liste des recettes
     * par jour.
     **/
    private void displayMenuTable(ArrayList<Day> days){
        for (Day day : days){
            TableView<Recipe> dayTable = new TableView<>();
            dayTable.getColumns().clear();
            TableColumn<Recipe, String> dayCol = new TableColumn<>(day.toString());
            dayCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            List<Recipe> mealForDay = menu.getRecipesfor(day);
            dayTable.getColumns().add(dayCol);
            dayTable.getItems().addAll(mealForDay);
            dayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Column width = table width
            viewController.getMenuHBox().getChildren().add(dayTable);
        }
    }

    @Override
    public void onModifyMenuClicked(){
        MenuController menuController = new MenuController(currentStage);
        menuController.showModifyMenu(menu);
    }

    @Override
    public void onGenerateShoppingListClicked(){
        ShoppingList shoppingList = menu.generateShoppingList();
        CreateUserShoppingListViewController createUserShoppingListViewController = new CreateUserShoppingListViewController();
        loadFXML(createUserShoppingListViewController, "UserShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(createUserShoppingListViewController);

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(true);
        shoppingListController.setStage(currentStage);
        shoppingListController.fillProductTable(shoppingList);
    }

    @Override
    public void onBackClicked(){
        UserMenusController userMenusController = new UserMenusController(currentStage);
        userMenusController.showAllMenus();
    }

}
