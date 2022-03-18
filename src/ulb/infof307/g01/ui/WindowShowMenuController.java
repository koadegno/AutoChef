
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
     * @throws  SQLException : si le menu envoyé ne se trouve pas dans la base de données*/
    @FXML
    public void goToModifyMenu() throws SQLException {
        WindowModifyMenuController modifyMenu = new WindowModifyMenuController(this.menu);
        modifyMenu.setMainController(this);
        this.loadFXML(modifyMenu,"interface/CreateDisplayMenu.fxml" );
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
        this.loadFXML(windowsCreateMyShoppingListController, "interface/FXMLCreateMyShoppingList.fxml");
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();
        fillShoppingList(windowsCreateMyShoppingListController);

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
        menu.displayMyMenus();
    }

    @Override
    public void add(Menu menu) {
        WindowShowMenuController controller = (WindowShowMenuController) this.loadFXML("interface/FXMLShowMenu.fxml");
        controller.setMenu(menu);
    }

    @Override
    public void cancel() {

        try{
            add(this.applicationConfiguration.getCurrent().getDatabase().getMenuFromName(this.menu.getName()));
        }
        catch (SQLException e){System.out.println(e);}
    }
}
