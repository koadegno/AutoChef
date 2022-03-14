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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class WindowMyMenusController implements Initializable {

    private final ArrayList<Menu> menus = new ArrayList<>();
    private ArrayList<String> allMenusNames = new ArrayList<>();
    private static Database database = null;
    @FXML
    TextField menuName;
    @FXML
    TreeView<Menu> menuTreeView;


    public void initializeMenusFromTextFile(String filename){
        //Les catégories doivent être séparées par des virgules!
        ArrayList<String> menuNames = readFromFile(filename);
        for (String name : menuNames){
            menus.add(new Menu(name));
        }
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializeMenusFromTextFile("src\\ulb\\infof307\\g01\\ui\\menus");
        initializeMenusFromDB();
        TreeItem<Menu> rootItem =  new TreeItem<>();
        for (Menu menu : menus){
            TreeItem<Menu> menuName = new TreeItem<>(menu);
            rootItem.getChildren().add(menuName);
        }
        menuTreeView.setRoot(rootItem);
    }

    @FXML
    public Menu selectedMenu(){
        TreeItem<Menu> selectedItem = menuTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            menuName.setText(selectedItem.getValue().toString());
            return selectedItem.getValue();
        }
        return null;
    }

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

    public void redirectToShowMenuController(MouseEvent mousePressed)throws IOException{

        String name = menuName.getText();
        Menu menu = selectedMenu();
        //TODO: Get from DB!
        ObservableList<Recipe> recipes = FXCollections.observableArrayList(
                new Recipe("recette 1"),
                new Recipe("recette 2"),
                new Recipe("recette 3"),
                new Recipe("recette 4")
        );
        for (int i = 0; i < 7; i++) {
            for (Recipe recipe : recipes){
                menu.addMealTo(Day.values()[i], recipe);
            }
        }
        menu.addMealTo(Day.Monday, new Recipe("recette 5"));
        menu.addMealTo(Day.Thursday, new Recipe("recette 9"));

        if (menus.contains(menu)){
            FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
            Parent root = loader.load();

            WindowShowMenuController controller = loader.getController();
            controller.setMenu(menu);
            controller.setDatabase(database);

            Stage stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
            Scene scene =  new Scene(root);
            stage.setTitle("Menu "+name);
            stage.setScene(scene);
            stage.show();

        }else {
            System.out.println("Menu n'existe pas!");
        }

    }

    public void setDatabase(Database db){database = db;}
}
