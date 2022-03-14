package ulb.infof307.g01.ui;

import java.io.FileReader;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
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
 * @see ulb.infof307.g01.cuisine.Menu
 * @see WindowShowMenuController
 * */
public class WindowMyMenusController implements Initializable {

    private final ArrayList<Menu> menus = new ArrayList<>();
    private ArrayList<String> allMenusNames = new ArrayList<>();
    private static Database database = null;
    @FXML
    TextField menuName;
    @FXML
    TreeView<Menu> menuTreeView;


    /**
     * Intérroge la base de données passée par la page précedente
     * et récupère la liste de tous les menus
     * @throws SQLException
     * */
    public void initializeMenusFromDB() {
        try {
            allMenusNames = database.getAllMenuName();
            for (String name : allMenusNames){
                menus.add(database.getMenuFromName(name));
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
            menuName.setStyle(null);
        }
    }


    /**
     * Lit le contenu introduit par l'utilisateur dans le
     * TextField menuName et affiche dans le TreeView
     * que les éléments qui commencent par le texte introduit.
     * */
    @FXML
    public void keyTyped(KeyEvent keyEvent){
        menuName.setStyle(null);
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
     * @throws IOException : si le fichier FXMLMyMenus n'existe pas
     * */
    public void displayMyMenus(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLMyMenus.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Affiche la page principale des menus*/
    public void backToMainMenuController(ActionEvent event)throws IOException {
        WindowMainMenuController mainMenuController = new WindowMainMenuController();
        mainMenuController.displayMainMenuController(event);
    }

    /**
     * Affiche la page qui montre le menu selectionné. Il passe à la classe
     * l'objet Menu, et la database.
     * @throws IOException : si le fichier FXMLShowMenu*/
    public void redirectToShowMenuController(MouseEvent mousePressed)throws IOException{

        String name = menuName.getText();
        if (!(Objects.equals(name, ""))){
            try{
                Menu menu = database.getMenuFromName(name);
                FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
                Parent root = loader.load();

                WindowShowMenuController controller = loader.getController();
                controller.setMenu(menu);
                controller.setDatabase(database);

                Stage stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
                Scene scene =  new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (SQLException e){menuName.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");}
        }

    }


    public void setDatabase(Database db){database = db;}
}
