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
            List<Recipe> mealForDay = menu.getRecipesfor(day);
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
        Scene myscene = new Scene(root);
        modifyMenu.setScene(myscene);
        this.stage.setScene(myscene);
        this.stage.show();
    }

    @FXML
    public void generateShoppingList(ActionEvent event) throws IOException {
        WindowsCreateMyShoppingListController windowsCreateMyShoppingListController = new WindowsCreateMyShoppingListController();
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
        loader.setController(windowsCreateMyShoppingListController);
        Parent root = loader.load();
        windowsCreateMyShoppingListController.setDatabase(dataBase);
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();
        fillShoppingList(windowsCreateMyShoppingListController);

        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void fillShoppingList(WindowsCreateMyShoppingListController controller){
        ShoppingList myShoppingList = menu.generateShoppingList();
        controller.fillTableViewWithExistentShoppingList(myShoppingList);


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
