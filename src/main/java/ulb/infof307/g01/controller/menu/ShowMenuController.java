package ulb.infof307.g01.controller.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.menu.ShowMenuViewController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowMenuController extends Controller implements ShowMenuViewController.Listener,ListenerBackPreviousWindow {

    private Menu menu;
    private ShowMenuViewController viewController;


    public ShowMenuController(Stage primaryStage, String menuName) { this(primaryStage, menuName,null); }

    public ShowMenuController(Stage primaryStage, String menuName, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
        this.menu = new Menu(menuName);
    }

    public void displayMenu(){
        FXMLLoader loader = this.loadFXML("Menu.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        startMenu();

    }

    private void startMenu() {

        try {
            this.menu = Configuration.getCurrent().getMenuDao().get(menu.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        MenuController menuController = new MenuController(currentStage,this);
        menuController.showModifyMenu(menu);
    }

    @Override
    public void onGenerateShoppingListClicked(){
        ShoppingList shoppingList = menu.generateShoppingList();
        CreateUserShoppingListViewController createUserShoppingListViewController = new CreateUserShoppingListViewController();
        loadFXML(createUserShoppingListViewController, "ShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(createUserShoppingListViewController);

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(true);
        shoppingListController.setStage(currentStage);
        shoppingListController.fillProductTable(shoppingList);
    }

    @Override
    public void onBackClicked(){
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void logout() {
        userLogout();
    }

    @Override
    public void onReturn() { displayMenu(); }
}
