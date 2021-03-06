package ulb.infof307.g01.controller.menu;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.MenuDao;
import ulb.infof307.g01.view.menu.UserMenusViewController;
import ulb.infof307.g01.view.ViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui contrôle l'affichage de tous les menus existants de l'utilisateur
 * Permet de voir une liste de menus
 */
public class UserMenusController extends Controller implements UserMenusViewController.Listener,ListenerBackPreviousWindow {
    private ArrayList<Menu> menus ;
    private List<String> allMenusNames;
    private UserMenusViewController viewController;


    public UserMenusController(ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
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
            MenuDao menuDao = configuration.getMenuDao();
            allMenusNames = menuDao.getAllName();
            for (String name : allMenusNames){
                menus.add(menuDao.get(name));
            }
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }

        fillMenuTableView();
    }

    /**
     * Affiche la page principale des menus*/
    @Override
    public void onBackButtonClicked() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void onHelpUserMenusClicked() {
        int numberOfImageHelp = 3;
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
                MenuDao menuDao = configuration.getMenuDao();
                Menu menu = menuDao.get(menuName);
                ShowUserMenuController showUserMenuController = new ShowUserMenuController(menu.getName(),this);
                showUserMenuController.displayMenu();

            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }
        }
        return isNameBlank;
    }

    private void fillMenuTableView(){
        viewController.fillMenuTableView(menus);
    }


    @Override
    public void onReturn() { displayAllMenus(); }
}
