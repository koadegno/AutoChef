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

public class CreateMenuController extends ulb.infof307.g01.ui.EditMenuController implements Initializable {

    public CreateMenuController() throws SQLException {
        this.db = new Database("autochef.sqlite");
        this.myMenu = new Menu();
        this.daysName = new ArrayList<>();
        for (int i = 0; i < 7; i++) daysName.add(Day.values()[i]);
    }

    @FXML
    public void displayEditMeal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(CreateMenuController.class.getResource("interface/CreateDisplayMenu.fxml"));
        loader.setController(this);
        root = loader.load();
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 7; i++) daysComboBox.getItems().add(daysName.get(i).toString());
        daysComboBox.getSelectionModel().selectFirst();
        menuTableColumn.setText(daysName.get(0).toString());
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        this.fillTableView(menuTableView, myMenu.getMealsfor(daysName.get(0)));
        this.removeRecipeButton.setVisible(false);
        this.generateMenuButton.setOnAction((event1) -> {
                try{this.generateMenu(event1);}
                catch (SQLException e){}
            });

    }
    @Override
    @FXML
    public void returnMain(ActionEvent event) throws IOException {
        //TODO:  return to Elsbeth's page
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    @Override
    @FXML
    public void saveMenu(ActionEvent event){
        myMenu.setName(this.menuNameTextField.getText());
        try{
            this.db.saveNewMenu(myMenu);
            WindowMainMenuController mainMenuController = new WindowMainMenuController();
            mainMenuController.setDataBase(db);
            mainMenuController.displayMainMenuController(event);
        }catch(SQLException e){System.out.println("non!");} catch (IOException e) {
            e.printStackTrace();
        }
    }


}