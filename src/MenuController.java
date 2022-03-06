
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuController {

    @FXML
    Label menuName;
    @FXML
    TableView menuTable;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void displayMenuName(String name){
        menuName.setText(name);
    }

    /*
    @FXML
    TableColumn day0;
    @FXML
    TableColumn day1;
    @FXML
    TableColumn day2;*/

    public void displayMenuTable(Menu menu){
        TableColumn day0 = new TableColumn<>("Name");
        day0.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn day1 = new TableColumn("Qté");
        day1.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //String[] mardi =  new String[3];
        //mardi[0]="Recette0";
        //mardi[1]="Recette1";
        //mardi[2]="Recette2";
        
        //day0.setText("Lundi");
        //for (int i = 0; i < mardi.length; i++) {
           // menuTable.getItems().add(mardi[i]);
        //}
        //day1.setText("Mardi");
        //day2.setText("Mercredi");

        menuTable.getColumns().addAll(day0, day1);

        menuTable.getItems().add(new Product("Tomate", 100));
        menuTable.getItems().add(new Product("Pommes", 50));
    }


    public void displayMenu(MouseEvent mousePressed, String name)throws IOException{
        System.out.println("Menu existe");
        Menu menu = new Menu(name);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("interface/Menu.fxml"));
        root = loader.load();

        MenuController menuController = loader.getController();
        menuController.displayMenuName(name);
        menuController.displayMenuTable(menu);

        stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
        scene =  new Scene(root);
        stage.setTitle("Menu "+name);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        MenuListController menu = new MenuListController();
        menu.displayMenuList(event);
    }
}
