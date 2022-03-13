//TODO: DOSSIER RESSOURCES!!!!
package ulb.infof307.g01.ui;

import com.sun.source.tree.Tree;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.util.Callback;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.db.Database;

/*class ObjectPointer {
    Object pointer;
    String name;
}*/


public class WindowShowMenuController implements Initializable {

    private Menu menu;

    @FXML
    Label menuName, nbOfdays;
    @FXML
    //TreeView<ObjectPointer> menuTreeView;
    TreeView<String> menuTreeView;

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
        this.nbOfdays.setText("Duration:"+ nbOfDays +"jours");
    }

    @FXML
    public void displayMenuTable(ArrayList<Day> days){
        /*
        for (Day day : days){
            TableColumn<Recipe, String> col = new TableColumn<>(day.name());
            col.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
            List<Recipe> recipes = menu.getMealsfor(day);
            menuTableView.getColumns().add(col);
            menuTableView.getItems().add(recipes);
        }*/

        TreeItem<String> rootItem =  new TreeItem<>();
        menuTreeView.setRoot(rootItem);

        for (Day day : days){
            TreeItem<String> menuDay = new TreeItem<String>(day.name());
            List<Recipe> mealForDay = menu.getMealsfor(day);
            for (Recipe recipe : mealForDay){
                TreeItem<String> recipeForMeal = new TreeItem<String>(recipe.getName());
                menuDay.getChildren().add(recipeForMeal);
            }
            rootItem.getChildren().add(menuDay);
        }
    }

    @FXML
    public void getSelectedItem(){
        TreeItem<String> selectedItem = menuTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            for (Day day : Day.values()) {
                if (day.name().equals(selectedItem.getValue())){
                    System.out.println("selected: " + selectedItem.getValue());
                }
            }
        }
    }

    @FXML
    public void goToSearchRecipe(ActionEvent event) throws IOException{
        SearchRecipeController search = new SearchRecipeController();
        search.displaySearchRecipe(event);
    }

    @FXML
    public void displayMenu(MouseEvent mousePressed, String name){
        try {
            System.out.println("Menu existe");

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));  //          Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
            stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
            scene =  new Scene(root);
            stage.setTitle("Menu "+name);
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event) throws IOException {
        WindowMyMenusController menu = new WindowMyMenusController();
        menu.displayMenuList(event);
    }
}
