package ulb.infof307.g01.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Recipe;
import  ulb.infof307.g01.db.Database;
import ulb.infof307.g01.ui.GenerateMenuDialog;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileStore;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateMenuController implements Initializable, ulb.infof307.g01.ui.SearchRecipeInterface {
    private Stage stage;
    private static Scene scene;
    private static Parent root;
    private Database db ;
    private Menu myMenu;
    private ArrayList<Day> daysName;
    private ArrayList<Recipe> recipe;

    @FXML
    ComboBox daysComboBox ;
    @FXML
    TableView menuTableView;
    @FXML
    TableColumn menuTableColumn;
    @FXML
    Button removeRecipeButton;
    @FXML
    TextField menuNameTextField;

    public CreateMenuController() throws SQLException {
        this.db = new Database("autochef.sqlite");
        this.myMenu = new Menu();
        this.daysName = new ArrayList<>();
        for (int i = 0; i < 7; i++) daysName.add(Day.values()[i]);
        recipe = new ArrayList<>();

    }

    @Override
    public void cancelSearchRecipe(){
        stage.setScene(this.scene);
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

    @FXML
    public void  fillTableView(TableView table, List<Recipe> valueList){
        for(int i =0; i < valueList.size(); i++){
            table.getItems().add(valueList.get(i));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for( int i= 0; i < 7 ; i++) daysComboBox.getItems().add(daysName.get(i).toString());
        daysComboBox.getSelectionModel().selectFirst();
        menuTableColumn.setText(daysName.get(0).toString());
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        this.fillTableView(menuTableView, myMenu.getMealsfor(daysName.get(0)));
        this.removeRecipeButton.setVisible(false);
    }

    public void refreshTableView(){
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        menuTableColumn.setText(daysName.get(dayIndex).toString());
        this.menuTableView.getItems().clear();
        this.fillTableView(menuTableView, myMenu.getMealsfor(daysName.get(dayIndex)));

    }
    @FXML
    private void searchRecipe(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(SearchRecipeController.class.getResource("interface/searchRecipe.fxml"));
        Parent root = loader.load();
        SearchRecipeController controller = loader.getController();
        controller.setMainController(this);
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.stage.setScene( new Scene(root));
        this.stage.show();
    }
    @Override
    public void addRecipe(Recipe recipe) {
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        myMenu.addMealTo(daysName.get(dayIndex), recipe);
        this.refreshTableView();
        this.stage.setScene(this.scene);
    }

    @FXML
    public void generateMenu(ActionEvent event) throws SQLException {
        final GenerateMenuDialog dialog = new GenerateMenuDialog();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.stage);
        dialog.initObject();
        dialog.getOkButton().setOnAction((event1)->{
            int nbVegetarian = (int) dialog.getVegetarianSpinner().getValue();
            int nbMeat = (int) dialog.getMeatSpinner().getValue();
            int nbFish = (int) dialog.getFishSpinner().getValue();
            dialog.close();
            try {
                myMenu.generateMenu(db, nbVegetarian, nbMeat, nbFish);
                dialog.close();
                this.refreshTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        dialog.show();
        //myMenu.generateMenu(db);
        //refreshTableView();
    }


    @FXML
    public void removeRecipeAction(ActionEvent event){
        Recipe recipeToRemove = (Recipe) menuTableView.getSelectionModel().getSelectedItem();
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        this.myMenu.removeMealFrom(daysName.get(dayIndex), recipeToRemove);
        refreshTableView();
        removeRecipeButton.setVisible(false);
    }

    @FXML
    public void recipeSelectedEvent(Event event){
       int idx =  menuTableView.getSelectionModel().getSelectedIndex();
       if(idx>-1) this.removeRecipeButton.setVisible(true);
    }

    @FXML
    public void saveMenu(){
        myMenu.setName(this.menuNameTextField.getText());
        try{
            this.db.saveNewMenu(myMenu);
        }catch(SQLException e){System.out.println("non!");}
    }


}