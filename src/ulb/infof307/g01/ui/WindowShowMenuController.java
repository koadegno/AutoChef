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


public class WindowShowMenuController implements Initializable, UtilisationContrat<Menu> {

    private Menu menu;
    private static Database dataBase;
    static Scene scene;
    static Parent root;
    private Stage stage;

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
        ModifyMenuController modifyMenu = new ModifyMenuController(this.menu);
        modifyMenu.setMainController(this);
        FXMLLoader loader = new FXMLLoader(ModifyMenuController.class.getResource("interface/CreateDisplayMenu.fxml"));
        loader.setController(modifyMenu);
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = loader.load();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    public void generateShoppingList(ActionEvent event) throws IOException{
        //WindowsCreateMyShoppingListController createShoppingList = new WindowsCreateMyShoppingListController();
        //createShoppingList.nameMyCreateShoppingList.setText("LC de "+menu.getName());
        //createShoppingList.

        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
        root = loader.load();
        WindowsCreateMyShoppingListController controller = loader.getController();
        controller.nameMyCreateShoppingList.setText("LC de " + menu.getName());
        controller.setDatabase(dataBase);
        controller.initShoppingListElement();
        controller.initComboBox();



        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        stage.setScene( this.scene);
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
        dataBase = db;
    }

    @Override
    public void add(Menu menu) {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
        try{
            Parent root = loader.load();
            Scene scene =  new Scene(root);
            stage.setTitle("Menu "+menu.getName());
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){}

        WindowShowMenuController controller = loader.getController();
        controller.setMenu(menu);
        controller.setDatabase(dataBase);
    }

    @Override
    public void cancel() {

        try{
            add(this.dataBase.getMenuFromName(this.menu.getName()));
        }
        catch (SQLException e){System.out.println(e);}
    }
}
