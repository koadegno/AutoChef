package ulb.infof307.g01.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import ulb.infof307.g01.cuisine.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateMenuController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ArrayList<Recipe> mondayRecipe;
    private ArrayList<Recipe> tuesdayRecipe;
    private ArrayList<Recipe> fridayRecipe;
    private ArrayList<Recipe> wednesdayRecipe;
    private ArrayList<Recipe> thursdayRecipe;
    private ArrayList<Recipe> sundayRecipe;
    private ArrayList<Recipe> saturdayRecipe;
    private ArrayList<String> daysname = new ArrayList<>();

    @FXML
    ComboBox daysComboBox ;
    @FXML
    TableView menuTableView;
    @FXML
    TableColumn menuTableColumn;

    public CreateMenuController(){
        daysname.add("Lundi");
        daysname.add("Mardi");
        daysname.add("Mercredi");
        daysname.add("Jeudi");
        daysname.add("Vendredi");
        daysname.add("Samedi");
        daysname.add("Dimanche");
    }

    @FXML
    public void displayEditMeal(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/CreateDisplayMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void returnMain(ActionEvent event) throws IOException {
        //TODO:  return to Elsbeth's page
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(String value: daysname) daysComboBox.getItems().add(value);
        daysComboBox.getSelectionModel().selectFirst();
        menuTableColumn.setText(daysname.get(0));
    }

    public void refreshTableView(Event event) throws IOException{
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        menuTableColumn.setText(daysname.get(dayIndex));
    }
    @FXML
    public void generateMenu(ActionEvent event){

    }
}