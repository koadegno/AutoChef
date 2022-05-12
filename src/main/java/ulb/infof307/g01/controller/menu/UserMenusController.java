package ulb.infof307.g01.controller.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.menu.UserMenusViewController;
import ulb.infof307.g01.view.ViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe qui contrôle l'affichage de tous les menus existants de l'utilisateur
 * Permet de voir une liste de menus
 */
public class UserMenusController extends Controller implements UserMenusViewController.Listener,ListenerBackPreviousWindow {
    private ArrayList<Menu> menus ;
    private List<String> allMenusNames;
    private UserMenusViewController viewController;


    public UserMenusController(Stage primaryStage) { this(primaryStage,null); }

    public UserMenusController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
        this.menus = new ArrayList<>();
        this.allMenusNames = new ArrayList<>();
    }

    public void displayAllMenus(){
        FXMLLoader loader = this.loadFXML("Menus.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        start();
    }

    /**
     * Prend la base de données qui a été configurée et
     * récupère la liste de tous les menus et remplire le menu treeView
     * @see Configuration
     * */
    private void start(){
        this.menus = new ArrayList<>();
        try {
            allMenusNames = Configuration.getCurrent().getMenuDao().getAllName();
            for (String name : allMenusNames){
                menus.add(Configuration.getCurrent().getMenuDao().get(name));
            }
        } catch (SQLException e) {
            //TODO gerer l'erreur
            e.printStackTrace();
        }

        fillMenuTreeView();
    }

    /**
     * Affiche la page principale des menus*/
    @Override
    public void onBackButtonClicked() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void onHelpUserMenusClicked() {
        int numberOfImageHelp = 6;
        HelpController helpController = new HelpController("helpUserMenu/", numberOfImageHelp);
        helpController.displayHelpShop();
    }

    @Override
    public void logout() {userLogout();}

    /**
     * Affiche la page qui montre le menu selectionné. Il passe à la classe
     * l'objet Menu, et la database.
     * */
    @Override
    public boolean onShowMenuClicked(String menuName) {

        boolean isNameBlank = menuName.isBlank() || menuName.isEmpty();

        if (!isNameBlank) {
            try {
                Menu menu = Configuration.getCurrent().getMenuDao().get(menuName);
                ShowMenuController showMenuController = new ShowMenuController(currentStage,menu.getName(),this);
                showMenuController.displayMenu();

            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }
        }
        return isNameBlank;
    }

    /**
     * Lit le contenu introduit par l'utilisateur dans le
     * TextField menuName et affiche dans le TreeView
     * que les éléments qui commencent par le texte introduit.
     * */
    @Override
    public boolean onKeyTapped(String menuName) {
        if (Objects.equals(menuName, "")){
            viewController.getMenuTreeView().setRoot(null);
            fillMenuTreeView();
        }else {
            ArrayList<Menu> matchingMenus = new ArrayList<>();
            menus.forEach(menu -> {
                if (menu.getName().startsWith(menuName)){matchingMenus.add(menu);}
            });
            viewController.getMenuTreeView().setRoot(null);
            fillMenuTreeView(matchingMenus);
        }
        return menuName.isEmpty() || menuName.isBlank();
    }

    private void fillMenuTreeView(){
        fillMenuTreeView(menus);
    }

    private void fillMenuTreeView(List<Menu> menuList){

        TreeItem<Menu> rootItem =  new TreeItem<>();
        menuList.forEach(menu -> {
            TreeItem<Menu> menuName = new TreeItem<>(menu);
            rootItem.getChildren().add(menuName);
        });
        viewController.getMenuTreeView().setRoot(rootItem);
    }


    @Override
    public void onReturn() { displayAllMenus(); }
}
