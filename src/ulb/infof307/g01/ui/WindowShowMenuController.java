
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


/**
 * La classe WindowShowMenuController représente le controleur
 * pour la page qui affiche le contenu du menu selectionné. Elle
 * permet aux utilisateurs de le modifier, et de générer une liste
 * de courses.
 * Elle implémente la classe Initializable pour pouvoir
 * acceder aux composants FXML.
 * @see ulb.infof307.g01.cuisine.Menu
 * @see WindowShowMenuController
 * */
public class WindowShowMenuController extends Window implements Initializable, UtilisationContrat<Menu> {

    private Menu menu;
    private static Database dataBase;
    static Scene scene;
    static Parent root;
    private Stage stage;

    @FXML
    Label menuName, nbOfdays;
    @FXML
    HBox menuHBox;


    /**
     * Récupère le menu envoyé par la page précédente, affiche son
     * nom, sa duration et son contenu dans un tableau*/
    public void setMenu(Menu menu){
        this.menu= menu;
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < menu.getNbOfdays(); i++) {
            days.add(Day.values()[i]);
        }
        displayMenuInfo(menu.toString(), menu.getNbOfdays());
        displayMenuTable(days);
    }

    /**
     * Initialise le controleur
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.menuName.setText(" ");
    }


    @FXML
    public void displayMenuInfo(String name, int nbOfDays){
        this.menuName.setText(name);
        this.nbOfdays.setText("Durée : "+ nbOfDays +"jours");
    }

    /**
     * Affiche le contenu du menu, avec la liste des recettes
     * par jour.*/
    @FXML
    public void displayMenuTable(ArrayList<Day> days){
        for (Day day : days){
            TableView<Recipe> dayTable = new TableView<>();
            dayTable.getColumns().clear();
            TableColumn<Recipe, String> dayCol = new TableColumn<>(day.toString());
            dayCol.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
            List<Recipe> mealForDay = menu.getRecipesfor(day);
            dayTable.getColumns().add(dayCol);
            dayTable.getItems().addAll(mealForDay);
            dayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Column width = table width
            menuHBox.getChildren().add(dayTable);
        }
    }

    /**
     * Affiche la page pour modifier le menu.
     * @throws IOException : si le fichier CreateDisplayMenu.fxml n'existe pas
     * @throws  SQLException : si le menu envoyé ne se trouve pas dans la base de données*/
    @FXML
    public void goToModifyMenu(ActionEvent event) throws IOException, SQLException {
        WindowModifyMenuController modifyMenu = new WindowModifyMenuController(this.menu);
        modifyMenu.setMainController(this);
        FXMLLoader loader = new FXMLLoader(WindowModifyMenuController.class.getResource("interface/CreateDisplayMenu.fxml"));
        loader.setController(modifyMenu);
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = loader.load();
        Scene myscene = new Scene(root);
        modifyMenu.setScene(myscene);
        this.stage.setScene(myscene);
        this.stage.show();
    }

    /**
     * Redirige vers la page de creation d'une liste de courses
     * où le tableau sera rempli avec les ingrédients qui se trouvent
     * dans chaque recette du menu
     * @throws IOException : si le fichier FXMLCreateMyShoppingList.fxml n'existe pas
     * */
    @FXML
    public void generateShoppingList(ActionEvent event) throws IOException {
        WindowCreateUserShoppingListController windowsCreateMyShoppingListController = new WindowCreateUserShoppingListController();
        FXMLLoader loader = new FXMLLoader(WindowUserShoppingListsController.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
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


    public void fillShoppingList(WindowCreateUserShoppingListController controller){
        ShoppingList myShoppingList = menu.generateShoppingList();
        controller.fillTableViewWithExistentShoppingList(myShoppingList);
    }

    /**
     * Affiche la page avec la liste des menus
     * */
    public void back(ActionEvent event) throws IOException {
        WindowUserMenuListController menu = new WindowUserMenuListController();
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
