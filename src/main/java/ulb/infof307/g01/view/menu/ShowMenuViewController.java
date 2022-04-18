
package ulb.infof307.g01.view.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.tools.UtilisationContrat;


/**
 * La classe ShowMenuViewController représente le controleur
 * pour la page qui affiche le contenu du menu selectionné. Elle
 * permet aux utilisateurs de le modifier, et de générer une liste
 * de courses.
 * Elle implémente la classe Initializable pour pouvoir
 * acceder aux composants FXML.
 * @see ulb.infof307.g01.model.Menu
 * @see ShowMenuViewController
 * */
public class ShowMenuViewController extends ViewController<ShowMenuViewController.Listener> implements Initializable, UtilisationContrat<Menu> {

    private Menu menu;

    @FXML
    Label menuNameLabel, nbOfDayLabel;
    @FXML
    HBox menuHBox;


    public Label getMenuNameLabel() {
        return menuNameLabel;
    }

    public Label getNbOfDayLabel() {
        return nbOfDayLabel;
    }

    public HBox getMenuHBox() {
        return menuHBox;
    }

    /**
     * Récupère le menu envoyé par la page précédente, affiche son
     * nom, sa duration et son contenu dans un tableau*/
    public void setMenu(Menu menu){
        this.menu= menu;
        ArrayList<Day> days = new ArrayList<>(Arrays.asList(Day.values()).subList(0, menu.getNbOfdays()));
        displayMenuInfo(menu.toString(), menu.getNbOfdays());
        displayMenuTable(days);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.menuNameLabel.setText(" ");
    }


    public void displayMenuInfo(String name, int nbOfDays){
//        this.menuNameLabel.setText(name);
//        this.nbOfDayLabel.setText("Durée : "+ nbOfDays +"jours");
    }

    /**
     * Affiche le contenu du menu, avec la liste des recettes
     * par jour.*/
    @FXML
    public void displayMenuTable(ArrayList<Day> days){
//        for (Day day : days){
//            TableView<Recipe> dayTable = new TableView<>();
//            dayTable.getColumns().clear();
//            TableColumn<Recipe, String> dayCol = new TableColumn<>(day.toString());
//            dayCol.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
//            List<Recipe> mealForDay = menu.getRecipesfor(day);
//            dayTable.getColumns().add(dayCol);
//            dayTable.getItems().addAll(mealForDay);
//            dayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Column width = table width
//            menuHBox.getChildren().add(dayTable);
//        }
    }

    /**
     * Affiche la page pour modifier le menu.
     * @throws  SQLException : si le menu envoyé ne se trouve pas dans la base de données*/
    @FXML
    public void goToModifyMenu()  {
        listener.onModifyMenuClicked();
        //this.loadFXML(modifyMenu,"CreateDisplayMenu.fxml" );
    }

    /**
     * Redirige vers la page de creation d'une liste de courses
     * où le tableau sera rempli avec les ingrédients qui se trouvent
     * dans chaque recette du menu
     * @throws IOException : si le fichier CreateUserShoppingList.fxml n'existe pas
     * */
    @FXML
    public void generateShoppingList() {
        listener.onGenerateShoppingListClicked();
//        //TODO: regler ce probleme pour le MVC
//        MenuController menuController = new MenuController(this);
//        //menuController.setStage(primaryStage);
//        menuController.displayCreateUserShoppingList();

    }

    public ShoppingList fillShoppingList(CreateUserShoppingListViewController controller){
        //TODO: changer ça qd il y aura le MVC
        ShoppingList myShoppingList = menu.generateShoppingList();
        controller.fillTableViewWithExistentShoppingList(myShoppingList);
        return myShoppingList;
    }


    public void back(){
        listener.onBackClicked();
        //UserMenusViewController menu = new UserMenusViewController();
        //menu.displayMyMenus();
    }

    @Override
    public void add(Menu menu) {
//        ShowMenuViewController controller = (ShowMenuViewController) this.loadFXML("ShowMenu.fxml");
//        controller.setMenu(menu);
    }

    @Override
    public void cancel() {

        try{
            add(Configuration.getCurrent().getMenuDao().get(this.menu.getName()));
        }
        catch (SQLException e){System.out.println(e);}
    }

    public interface Listener{
        void onModifyMenuClicked();
        void onGenerateShoppingListClicked();
        void onBackClicked();
    }
}