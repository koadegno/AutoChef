package src;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuListController implements Initializable {

    private ArrayList<String> categories = new ArrayList<>();


    public void initializeCategories(String filename){
        //Les catégories doivent être séparées par des virgules
        this.categories = readFromFile(filename);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCategories("src/categories");


        TreeItem<String> rootItem =  new TreeItem<>("Tous les menus");

        for (String category : this.categories){
            System.out.println(category);
            TreeItem<String> categoryName = new TreeItem<>("Menus" + category);
            rootItem.getChildren().add(categoryName);
            for (int i = 0; i < 4; i++) {
                TreeItem<String> recipeName = new TreeItem<>("Menu "+ category+ " " + i);
                categoryName.getChildren().add(recipeName);
            }
        }

        menuTreeView.setRoot(rootItem);

    }

    @FXML
    TextField menuName;
    @FXML
    TreeView menuTreeView;

    private Stage stage;
    private Scene scene;

    private Parent root;

    public void display(ActionEvent event) throws IOException{
        String name = menuName.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("interface/Menu.fxml"));
        root = loader.load();

        MenuController menuController = loader.getController();
        menuController.displayMenuName(name);

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene =  new Scene(root);
        stage.setTitle("Menu "+name);
        stage.setScene(scene);
        stage.show();

    }

    public void selectMenu(){

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
}
