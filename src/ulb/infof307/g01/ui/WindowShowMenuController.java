//TODO: DOSSIER RESSOURCES!!!!
package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import ulb.infof307.g01.cuisine.*;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.db.Database;

/*class ObjectPointer {
    Object pointer;
    String name;
}*/


public class WindowShowMenuController implements Initializable {

    private Menu menu;
    private static Database dataBase;

    @FXML
    Label menuName, nbOfdays;
    @FXML
    //TreeView<ObjectPointer> menuTreeView;
    TreeView<String> menuTreeView;
    @FXML
    HBox menuHBox;

    Database database = new Database("db");


    public void setMenu(Menu menu){
        this.menu= menu;
        //TODO: Get from DB!
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < menu.getNbOfdays(); i++) {
            days.add(Day.values()[i]);
        }

        displayMenuInfo(menu.toString(), menu.getNbOfdays());
        displayMenuTable(days);
    }

    private Stage stage;
    private Scene scene;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.menuName.setText(" ");
    }

    @FXML
    public void displayMenuInfo(String name, int nbOfDays){
        this.menuName.setText(name);
        this.nbOfdays.setText("Duration: "+ nbOfDays +"jours");
    }

    @FXML
    public void displayMenuTable(ArrayList<Day> days){
     /*
        TreeItem<String> rootItem =  new TreeItem<>();
        menuTreeView.setRoot(rootItem);

        for (Day day : days){
            TreeItem<String> menuDay = new TreeItem<String>(day.toString());
            List<Recipe> mealForDay = menu.getMealsfor(day);
            for (Recipe recipe : mealForDay){
                TreeItem<String> recipeForMeal = new TreeItem<String>(recipe.getName());
                menuDay.getChildren().add(recipeForMeal);
            }
            rootItem.getChildren().add(menuDay);
        }
         */
        for (Day day : days){
            TableView<Recipe> dayTable = new TableView<>();
            dayTable.getColumns().clear();
            TableColumn<Recipe, String> dayCol = new TableColumn<>(day.name());
            dayCol.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
            List<Recipe> mealForDay = menu.getMealsfor(day);
            dayTable.getColumns().add(dayCol);
            dayTable.getItems().addAll(mealForDay);
            dayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Column width = table width
            menuHBox.getChildren().add(dayTable);
        }
    }

    @FXML
    public void goToModifyMenu(ActionEvent event) throws IOException, SQLException {

        //TODO: Changer pour qu'il redirige vers la partie de modifier le menu
        SearchRecipeController search = new SearchRecipeController();
        search.displaySearchRecipe(event);
    }

    @FXML
    public void generateShoppingList(ActionEvent event) throws IOException{
        //WindowsCreateMyShoppingListController createShoppingList = new WindowsCreateMyShoppingListController();
        //createShoppingList.nameMyCreateShoppingList.setText("LC de "+menu.getName());
        //createShoppingList.

        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
        Parent root = loader.load();
        WindowsCreateMyShoppingListController controller = loader.getController();
        controller.nameMyCreateShoppingList.setText("LC de " + menu.getName());
        controller.setDatabase(dataBase);
        controller.initShoppingListElement();
        controller.initComboBox();



        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    public void fillShoppingList(WindowsCreateMyShoppingListController controller){
        //ShoppingList generatedShoppingList = menu.generateShoppingList();
        //controller.fillShoppingListToSend(generatedShoppingList);
        HashMap<Product, Integer> productsAndQuantity = new HashMap<>();
        for (int i = 0; i <menu.getNbOfdays(); i++) {
            for (Recipe meal : menu.getMealsfor(Day.values()[i])){
                for (Product product : meal){
                    if (productsAndQuantity.containsKey(product)){
                        productsAndQuantity.replace(product, productsAndQuantity.get(product) + 1);
                    }
                    else {
                        productsAndQuantity.put(product, 1);
                    }
                }
            }
        }

    }

    public void back(ActionEvent event) throws IOException {
        WindowMyMenusController menu = new WindowMyMenusController();
        menu.displayMyMenus(event);
    }

    public void setDatabase(Database db){
        dataBase = db;}
}
