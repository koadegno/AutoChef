package ulb.infof307.g01.controller.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.menu.HomeMenuViewController;
import ulb.infof307.g01.view.menu.UserMenusViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserMenusController extends Controller implements UserMenusViewController.Listener {
    private final ArrayList<Menu> menus ;
    private ArrayList<String> allMenusNames;
    private UserMenusViewController viewController;


    public UserMenusController(Stage primaryStage) {
        setStage(primaryStage);
        this.menus = new ArrayList<>();
        this.allMenusNames = new ArrayList<>();
    }

    public void showAllMenus(){
        FXMLLoader loader = this.loadFXML("UserMenus.fxml");
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
        FXMLLoader loader = this.loadFXML("HomeMenu.fxml");
        HomeMenuViewController viewController = loader.getController();
        viewController.setListener(new HomePageController(currentStage));
    }

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
                ShowMenuController showMenuController = new ShowMenuController(currentStage,menu);
                showMenuController.showMenu();
                //TODO appeler le controlleur de show menu
//                ShowMenuViewController controller = (ShowMenuViewController) this.loadFXML("ShowMenu.fxml");
//                controller.setMenu(menu);

            } catch (SQLException e) {
                //TODO gerer l'erreur
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


}
