package ulb.infof307.g01.view.menu;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.view.ViewController;

import java.util.ResourceBundle;

/**
 * La classe WindowMyMenusController représente le controleur
 * pour la page qui affiche la liste des menus existants dans
 * la base de données. Elle permet à l'utilisateur de selectioner
 * un menu pour l'afficher, ou de tapper manuellement le nom du
 * menu. Elle implémente la classe Initializable pour pouvoir
 * acceder aux composants FXML.
 * @see ulb.infof307.g01.model.Menu
 * @see WindowShowMenuController
 * */
public class UserMenusViewController extends ViewController<UserMenusViewController.Listener> implements Initializable {
    @FXML
    TextField menuName;
    @FXML
    TreeView<Menu> menuTreeView;

    public TreeView<Menu> getMenuTreeView() {
        return menuTreeView;
    }

    /**
     * Initialisation du controleur: initialise la liste de menus
     * et rempli le TreeView avec cette liste de menus
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /**
     * Retourne le menu selectionné sur le
     * TreeView menuTreeView et l'affiche dans le TextField menuName
     * */
    @FXML
    public void selectedMenu(){
        TreeItem<Menu> selectedItem = menuTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            menuName.setText(selectedItem.getValue().toString());
            this.setNodeColor(menuName, false);
        }
    }


    /**
     * Lit le contenu introduit par l'utilisateur dans le
     * TextField menuName et affiche dans le TreeView
     * que les éléments qui commencent par le texte introduit.
     * */
    @FXML
    public void keyTyped() {
        boolean isBlank = listener.onKeyTapped(menuName.getText());
        this.setNodeColor(menuName, isBlank);
    }

    /**
     * Affiche la page principale des menus*/
    public void backToMainMenuController() {
        listener.onBackButtonClicked();
    }

    /**
     * Affiche la page qui montre le menu selectionné. Il passe à la classe
     * l'objet Menu, et la database.
     * */
    public void redirectToShowMenuController(MouseEvent mousePressed){
        String name = menuName.getText();
        boolean isNameBlank = listener.onShowMenuClicked(name);
        setNodeColor(menuName, isNameBlank);

    }

    public interface Listener {
        boolean onShowMenuClicked(String menuName);
        boolean onKeyTapped(String menuName);
        void onBackButtonClicked();
    }
}
