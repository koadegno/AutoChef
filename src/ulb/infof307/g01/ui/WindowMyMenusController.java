package ulb.infof307.g01.ui;

import java.io.FileReader;
import java.net.URL;

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
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * La classe WindowMyMenusController représente le controlleur
 * pour la page qui affiche la liste des menus existants dans
 * la base de données. Elle permet à l'utilisateur de selectioner
 * un menu pour l'afficher, ou de tapper manuellement le nom du
 * menu. Elle implémente la classe Initializable pour pouvoir
 * acceder aux composants FXML.
 * @see ulb.infof307.g01.cuisine.Menu
 * @see WindowShowMenuController
 * @author _
 * */
public class WindowMyMenusController implements Initializable {

    private final ArrayList<Menu> menus = new ArrayList<>();
    private static Database database = null;
    @FXML
    TextField menuName;
    @FXML
    TreeView<Menu> menuTreeView;


    public void initializeMenusFromTextFile(String filename){
        //Les catégories doivent être séparées par des virgules!
        ArrayList<String> menuNames = readFromFile(filename);
        menuNames.forEach(name -> {menus.add(new Menu(name));});
    }
    public ArrayList<String> readFromFile(String filename){
        ArrayList<String> result = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            StringBuffer buffer = new StringBuffer();
            while (reader.ready()) {
                char c = (char) reader.read();
                if (c == ',') {
                    result.add(buffer.toString());
                    buffer = new StringBuffer();
                } else {
                    buffer.append(c);
                }
            }
            if (buffer.length() > 0) {
                result.add(buffer.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public void addDummyMenus(){
        ArrayList<String> menuNames = new ArrayList<>();
        menuNames.add("menu vege");menuNames.add("weekend");menuNames.add("menu random");menuNames.add("repas noel");menuNames.add("anniversaire");
        menuNames.forEach(name -> {menus.add(new Menu(name));});
    }

    /**
     * La méthode intérroge la base de données passée par la page précedente
     * et récupère la liste de tous les menus dans l'attribut menus
     * @throws SQLException
     * */
    public void initializeMenusFromDB() {
        try {
            for (String name : database.getAllMenuName()){menus.add(database.getMenuFromName(name));}
            addDummyMenus();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**La méthode initialise le controlleur en initialisant la liste
     * des menus, et en remplissant le composant TreeView avec les
     * données récupérées.
     * @see Initializable
     * @see TreeView
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMenusFromDB();
        fillTreeView(this.menus);
    }

    /**La méthode crée l'élément parent du TreeView et lui
     * rajoute un élément fils pour chaque menu qu'il lit
     * de la liste de menus.*/
    public void fillTreeView(ArrayList<Menu> menus){
        TreeItem<Menu> rootItem =  new TreeItem<>();
        menus.forEach(menu -> {
            TreeItem<Menu> menuName = new TreeItem<>(menu);
            rootItem.getChildren().add(menuName);
        });
        menuTreeView.setRoot(rootItem);
    }

    /**La méthode prend l'élément du TreeView qui à été
     * seléctionné, affiche le nom du menu dans le TextField
     * menuName et retourne un objet de type Menu
     * La méthode est appelé lorsque les suivants événements
     * sont détectés dans le TreeView:
     * <ul>
     *     <li>onContextMenuRequested</li>
     *     <li>onMouseClicked</li>
     * </ul>
     */
    @FXML
    public Menu selectedMenu(){
        TreeItem<Menu> selectedItem = menuTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            menuName.setText(selectedItem.getValue().toString());
            menuName.setStyle(null);
            return selectedItem.getValue();
        }
        return null;
    }

    /**
     * La méthode lit le texte du TextField menuName introduit
     * par l'utilisateur et ne montre que les éléments du
     * TreeView qui commencent par le texte introduit. Si le
     * texte est remis à vide, alors la liste d'éléments est
     * remise à celle par défaut
     * */
    @FXML
    public void keyTyped(){
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

    /**La méthode */
    public void displayMyMenus(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLMyMenus.fxml")));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void backToMainMenuController(ActionEvent event)throws IOException {
        WindowMainMenuController mainMenuController = new WindowMainMenuController();
        mainMenuController.displayMainMenuController(event);
    }

    public void fill(Menu menu){
        Recipe recipe1 = new Recipe("recette 1");
        recipe1.add(new Product("Salade de thon et légumes, appertisée"));
        recipe1.add(new Product("Artichaut, cuit"));
        recipe1.add(new Product("Salade de thon et légumes, appertisée"));
        Recipe recipe2 = new Recipe("recette 2");
        recipe2.add(new Product("Artichaut, cuit"));
        recipe2.add(new Product("Aubergine, cuite"));
        recipe2.add(new Product("Artichaut, cuit"));
        Recipe recipe3 = new Recipe("recette 3");
        recipe3.add(new Product("Salade de thon et légumes, appertisée"));
        recipe3.add(new Product("Artichaut, cuit"));
        recipe3.add(new Product("Aubergine, cuite"));
        menu.addMealTo(Day.Monday, recipe1);
        menu.addMealTo(Day.Monday, recipe2);
        menu.addMealTo(Day.Thursday, recipe2);
        menu.addMealTo(Day.Friday, recipe3);
    }

    public void redirectToShowMenuController(MouseEvent mousePressed)throws IOException, SQLException{
        try{
            Menu menu = database.getMenuFromName(menuName.getText());
            fill(menu);
            FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
            Parent root = loader.load();

            WindowShowMenuController controller = loader.getController();
            controller.setMenu(menu);
            controller.setDatabase(database);

            Stage stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
            Scene scene =  new Scene(root);
            stage.setTitle("Menu "+menu.getName());
            stage.setScene(scene);
            stage.show();

        }catch (SQLException exception){
            System.out.println("Menu n'existe pas!");
            menuName.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");

        }
    }

    public void setDatabase(Database db){database = db;}
}
