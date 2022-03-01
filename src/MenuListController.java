import java.io.FileReader;
import java.net.URL;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuListController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ArrayList<String> menus = new ArrayList<>();



    public void initializeMenusFromTextFile(String filename){
        //Les catégories doivent être séparées par des virgules!
        this.menus = readFromFile(filename);

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
    TreeView<String> menuTreeView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMenusFromTextFile("src/menus");

        TreeItem<String> rootItem =  new TreeItem<>("Tous les menus");

        for (String category : this.menus){
            TreeItem<String> categoryName = new TreeItem<>(category);
            rootItem.getChildren().add(categoryName);
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

    public String selectedMenu(){
        TreeItem<String> selectedItem = menuTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            System.out.println("selected Item: " + selectedItem.getValue());
            if (!selectedItem.getValue().equals("Tous les menus")){
                menuName.setText(selectedItem.getValue());
            }
            return selectedItem.getValue();
        }
        return "";
    }

    @FXML
    TextField menuName;
    @FXML
    Button btnDisplayMenu;

    public void handleDisplayMenu(MouseEvent mousePressed)throws IOException{

        String name = menuName.getText();

        //TODO: Vérifier que name est dans menus

        if (menus.contains(name)){
            System.out.println("Menu existe");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("interface/Menu.fxml"));
            root = loader.load();

            MenuController menuController = loader.getController();
            menuController.displayMenuName(name);

            stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
            scene =  new Scene(root);
            stage.setTitle("Menu "+name);
            stage.setScene(scene);
            stage.show();
        }else {
            System.out.println("Menu n'existe pas!");
        }

    }
}
