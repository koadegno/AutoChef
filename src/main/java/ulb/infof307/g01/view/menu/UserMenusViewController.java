package ulb.infof307.g01.view.menu;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.view.ViewController;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * La classe WindowMyMenusController représente le controleur
 * pour la page qui affiche la liste des menus existants dans
 * la base de données. Elle permet à l'utilisateur de selectioner
 * un menu pour l'afficher, ou de tapper manuellement le nom du
 * menu. Elle implémente la classe Initializable pour pouvoir
 * acceder aux composants FXML.
 * @see ulb.infof307.g01.model.Menu
 * @see ShowMenuViewController
 * */
public class UserMenusViewController extends ViewController<UserMenusViewController.Listener> implements Initializable {
    @FXML
    TableColumn<Menu, String> menuTableColumn;
    @FXML
    TableView<Menu> menuTableView;


    /**
     * Initialisation du controleur: initialise la liste de menus
     * et rempli le TreeView avec cette liste de menus
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /**
     * Affiche la page principale des menus*/
    public void backToMainMenuController() {
        listener.onBackButtonClicked();
    }


    public void helpUserMenus() {
        listener.onHelpUserMenusClicked();
    }

    public void logout() {
        listener.logout();
    }

    public void onMenuTableViewClicked() {
        Menu menuToDisplay = menuTableView.getSelectionModel().getSelectedItem();
        if(menuToDisplay != null) listener.onShowMenuClicked(menuToDisplay.getName());
    }

    public void fillMenuTableView(ArrayList<Menu> menus) {
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        menuTableView.getItems().addAll(menus);
    }

    public interface Listener {
        boolean onShowMenuClicked(String menuName);
        void onBackButtonClicked();
        void onHelpUserMenusClicked();

        void logout();
    }
}
