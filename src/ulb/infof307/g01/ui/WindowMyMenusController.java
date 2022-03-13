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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Recipe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class WindowMyMenusController implements Initializable {

    private ArrayList<Menu> menus = new ArrayList<>();

    public void displayMenuList(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLMyMenus.fxml")));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    Button backBtn;
    public void backToMain(MouseEvent mousePressed)throws IOException {
        WindowMainController main = new WindowMainController();
        main.displayMain((Stage) ((Node) mousePressed.getSource()).getScene().getWindow());
    }

    public void initializeMenusFromTextFile(String filename){
        //Les catégories doivent être séparées par des virgules!
        ArrayList<String> menuNames = readFromFile(filename);
        for (String name : menuNames){
            menus.add(new Menu(name));
        }
    }

    public void initializeMenusFromDB() {
        //TODO: Relier avec BDD --> requetes (nomMenu)
        //db = new Database("test.sqlite");
        //db.creationTableMenus();
        //String query = "SELECT * FROM Menus;"; --> get all the info of one menu
        //if (db.sendRequest(query)){
        //  fill this.menus with the query
        //}
    }


    @FXML
    TreeView<Menu> menuTreeView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMenusFromTextFile("src\\ulb\\infof307\\g01\\ui\\menus");

        TreeItem<Menu> rootItem =  new TreeItem<>();

        for (Menu menu : menus){
            TreeItem<Menu> menuName = new TreeItem<>(menu);
            rootItem.getChildren().add(menuName);
        }
        menuTreeView.setRoot(rootItem);

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

    @FXML
    public Menu selectedMenu(){
        TreeItem<Menu> selectedItem = menuTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            menuName.setText(selectedItem.getValue().toString());
            return selectedItem.getValue();
        }
        return null;
    }

    @FXML
    TextField menuName;

    @FXML
    Button btnDisplayMenu;

    public void handleDisplayMenu(MouseEvent mousePressed)throws IOException{

        String name = menuName.getText();
        Menu menu = selectedMenu();
        //TODO: Get from DB!
        ObservableList<Recipe> recipes = FXCollections.observableArrayList(
                new Recipe("recette 1"),
                new Recipe("recette 2"),
                new Recipe("recette 3"),
                new Recipe("recette 34")
        );
        for (int i = 0; i < 7; i++) {
            for (Recipe recipe : recipes){
                menu.addMealTo(Day.values()[i], recipe);
            }
        }

        if (menus.contains(menu)){
            FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
            Parent root = loader.load();

            WindowShowMenuController controller = loader.getController();
            controller.setMenu(menu);

            Stage stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
            Scene scene =  new Scene(root);
            stage.setTitle("Menu "+name);
            stage.setScene(scene);
            stage.show();

        }else {
            System.out.println("Menu n'existe pas!");
        }

    }
}
