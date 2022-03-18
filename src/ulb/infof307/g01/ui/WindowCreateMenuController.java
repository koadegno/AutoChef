package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowCreateMenuController extends WindowEditMenuController implements Initializable {

    public WindowCreateMenuController() throws SQLException {
        this.myMenu = new Menu();
        this.daysName = new ArrayList<>();
        for (int i = 0; i < 7; i++) daysName.add(Day.values()[i]);
    }

    @FXML
    public void displayEditMeal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowCreateMenuController.class.getResource("interface/CreateDisplayMenu.fxml"));
        loader.setController(this);
        root = loader.load();
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Permet d'initialiser les différents objets utilisées de la fenêtre
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 7; i++) daysComboBox.getItems().add(daysName.get(i).toString());
        daysComboBox.getSelectionModel().selectFirst();
        menuTableColumn.setText(daysName.get(0).toString());
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(0)));
        this.removeRecipeButton.setVisible(false);
        this.generateMenuButton.setOnAction((event1) -> {
                try{this.generateMenu(event1);}
                catch (SQLException e){}
        });
    }

    /**
     * returnMain est la methode connectée au bouton qui permet de retourner
     *  au menu principal
     * @param event l'utilisateur a annulé l'édition
     * @throws IOException
     */
    @Override
    @FXML
    public void returnMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Cette fonction permet la génération automatique de menu
     * @param event
     * @throws SQLException
     */
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
                myMenu.generateMenu(this.applicationConfiguration.getCurrent().getDatabase(), nbVegetarian, nbMeat, nbFish);
                dialog.close();
                this.refreshTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        dialog.show();
    }

    /**
     * Cette méthode permet de sauvegarder un menu dans la base de données
     * @param event Bouton enregistré à été cliqué
     */
    @Override
    @FXML
    public void saveMenu(ActionEvent event){
        myMenu.setName(this.menuNameTextField.getText());
        try{
            if(myMenu.size() == 0) {
                menuTableView.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
            } else {
                this.applicationConfiguration.getCurrent().getDatabase().saveNewMenu(myMenu);
                WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
                mainMenuController.displayMainMenuController(event);
            }
        } catch(SQLException e) {
            menuNameTextField.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}