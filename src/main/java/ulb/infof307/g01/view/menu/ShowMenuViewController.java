
package ulb.infof307.g01.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

import ulb.infof307.g01.view.ViewController;


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
public class ShowMenuViewController extends ViewController<ShowMenuViewController.Listener> implements Initializable{

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.menuNameLabel.setText(" ");
    }


    /**
     * Affiche la page pour modifier le menu.*/
    @FXML
    public void goToModifyMenu()  {
        listener.onModifyMenuClicked();
    }

    /**
     * Redirige vers la page de creation d'une liste de courses
     * où le tableau sera rempli avec les ingrédients qui se trouvent
     * dans chaque recette du menu
     * */
    @FXML
    public void generateShoppingList() {
        listener.onGenerateShoppingListClicked();

    }

    public void back(){
        listener.onBackClicked();
    }

    public void logout() {
        listener.logout();
    }

    public interface Listener{
        void onModifyMenuClicked();
        void onGenerateShoppingListClicked();
        void onBackClicked();

        void logout();
    }
}
