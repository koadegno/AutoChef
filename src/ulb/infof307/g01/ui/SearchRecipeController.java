package ulb.infof307.g01.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SearchRecipeController implements Initializable {
    private Stage stage;
    private Parent root;
    private ArrayList<String> dietList = new ArrayList<>(Arrays.asList("Végétarien","Viande", "Poisson", "Halal"));
    private ArrayList<String>  typeList = new ArrayList<>(Arrays.asList("Desserts", "Entrées", "Plats", "Boissons"));
    private ArrayList<Recipe>  recipeName = new ArrayList<>(Arrays.asList(new Recipe("Gateau"), new Recipe("Muffins"), new Recipe("Lasagne"), new  Recipe("Gratin dauphinois")));
    @FXML
    ComboBox recipeDietComboBox, recipeTypeComboBox;
    @FXML
    Spinner numberOfPersonSpinner;
    @FXML
    TableView recipeTableView;
    @FXML
    TableColumn columnRecipeName;
    @FXML
    Button addFindRecipeButton, cancelSearchRecipeButton;


    @FXML
    public void fillComboBox(ComboBox box, ArrayList<String> valueList){
        for(String value: valueList)
            box.getItems().add(value);
    }

    @FXML
    public void  fillTableView(TableView table, ArrayList<Recipe> valueList){
        for(int i =0; i < valueList.size(); i++){
            table.getItems().add(valueList.get(i));
        }
    }

    @FXML
    public void confirmRecipe(ActionEvent event) throws IOException {

    }

    @FXML
    private void onlyIntValue(){
        numberOfPersonSpinner.getEditor().textProperty().addListener(new ChangeListener<String>() { //Seulement écrire des nombres
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfPersonSpinner.getEditor().setText(newValue.replaceAll("[^\\d*]", ""));
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnRecipeName.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        fillComboBox(this.recipeDietComboBox, dietList);
        fillComboBox(this.recipeTypeComboBox, typeList);
        fillTableView(this.recipeTableView, recipeName);
        recipeDietComboBox.getSelectionModel().selectFirst();
        recipeTypeComboBox.getSelectionModel().selectFirst();
        recipeDietComboBox.setVisibleRowCount(10);
        numberOfPersonSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        onlyIntValue();
    }

    public void displaySearchRecipe(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("interface/searchRecipe.fxml"));
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    public void returnToCreateMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load((getClass().getResource("interface/FXMLMainPage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public Recipe addRecipe(ActionEvent event) throws IOException{
        int idx = recipeTableView.getSelectionModel().getSelectedIndex();
        System.out.println(recipeName.get(idx).getName());
        return this.recipeName.get(idx);
    }


    public void refreshTableView( Event actionEvent) {
        String diet = dietList.get(recipeDietComboBox.getSelectionModel().getSelectedIndex());
        String type = typeList.get(recipeTypeComboBox.getSelectionModel().getSelectedIndex());
        int nbPerson = (int) numberOfPersonSpinner.getValue();

        // TODO: get new recipe's list from Database
        System.out.println(diet + " " + type +" " + nbPerson);
        // TODO: refresh table
    }
}
