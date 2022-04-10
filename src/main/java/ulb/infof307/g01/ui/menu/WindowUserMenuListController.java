package ulb.infof307.g01.ui.menu;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.ui.Window;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
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
public class WindowUserMenuListController extends Window implements Initializable {

    private ArrayList<Menu> menus ;
    private ArrayList<String> allMenusNames;
    @FXML
    TextField menuName;
    @FXML
    TreeView<Menu> menuTreeView;


    /**
     * Prend la base de données qui a été configurée et
     * récupère la liste de tous les menus
     * @see Configuration
     * */
    public void initializeMenusFromDB() {
        menus = new ArrayList<>();
        try {
            allMenusNames = Configuration.getCurrent().getMenuDao().getAllName();
            for (String name : allMenusNames){
                menus.add(Configuration.getCurrent().getMenuDao().get(name));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * Initialisation du controleur: initialise la liste de menus
     * et rempli le TreeView avec cette liste de menus
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMenusFromDB();
        fillTreeView(this.menus);
    }

    public void fillTreeView(ArrayList<Menu> menus) {
        TreeItem<Menu> rootItem =  new TreeItem<>();
        menus.forEach(menu -> {
            TreeItem<Menu> menuName = new TreeItem<>(menu);
            rootItem.getChildren().add(menuName);
        });
        menuTreeView.setRoot(rootItem);
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
    public void keyTyped(KeyEvent keyEvent){
        this.setNodeColor(menuName, false);
        if (Objects.equals(menuName.getText(), "")){
            menuTreeView.setRoot(null);
            fillTreeView(menus);
        }else {
            ArrayList<Menu> matchingMenus = new ArrayList<>();
            menus.forEach(menu -> {
                if (menu.getName().startsWith(menuName.getText())){matchingMenus.add(menu);}
            });
            menuTreeView.setRoot(null);
            fillTreeView(matchingMenus);
        }
    }

    /**
     * Affiche la page avec la liste des menus
     * */
    public void displayMyMenus() {
        this.loadFXML("UserMenuList.fxml");
    }

    /**
     * Affiche la page principale des menus*/
    public void backToMainMenuController() {
        WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
        mainMenuController.displayMainMenuController();
    }

    /**
     * Affiche la page qui montre le menu selectionné. Il passe à la classe
     * l'objet Menu, et la database.
     * */
    public void redirectToShowMenuController(MouseEvent mousePressed){

        String name = menuName.getText();
        if (!(Objects.equals(name, ""))) {
            try {
                Menu menu = Configuration.getCurrent().getMenuDao().get(name);

                WindowShowMenuController controller = (WindowShowMenuController) this.loadFXML("ShowMenu.fxml");
                controller.setMenu(menu);

            } catch (SQLException e) {
                this.setNodeColor(menuName,true);
            }
        }
        else {
            setNodeColor(menuName, true);
        }
    }
}
