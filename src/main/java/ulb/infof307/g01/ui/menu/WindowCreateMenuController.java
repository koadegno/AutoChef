package ulb.infof307.g01.ui.menu;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.ui.tools.GenerateMenuDialog;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class WindowCreateMenuController extends WindowEditMenuController implements Initializable {

    public WindowCreateMenuController() throws SQLException {
        this.myMenu = new Menu();
        this.daysName = new ArrayList<>();
        for (int i = 0; i < 7; i++) daysName.add(Day.values()[i]);
    }

    @FXML
    public void displayEditMeal() {
        this.loadFXML(this, "CreateDisplayMenu.fxml");
    }

    /**
     * Permet d'initialiser les différents objets utilisées de la fenêtre
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        daysComboBox.setItems(FXCollections.observableArrayList(daysName));
        daysComboBox.getSelectionModel().selectFirst();
        menuTableColumn.setText(daysName.get(0).toString());
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(0)));
        this.removeRecipeButton.setVisible(false);
        this.generateMenuButton.setOnAction((event) -> {
                try{this.generateMenu();}
                catch (SQLException e){}
        });
    }

    /**
     * returnMain est la methode connectée au bouton qui permet de retourner
     *  au menu principal
     */
    @Override
    @FXML
    public void returnMain() {
        this.loadFXML("HomeMenu.fxml");
    }

    /**
     * Cette fonction permet la génération automatique de menu
     * @throws SQLException
     */
    @FXML
    public void generateMenu() throws SQLException {
        final GenerateMenuDialog dialog = new GenerateMenuDialog();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.primaryStage);
        dialog.initObject();
        dialog.getOkButton().setOnAction((event1)->{
            int nbVegetarian = (int) dialog.getVegetarianSpinner().getValue();
            int nbMeat = (int) dialog.getMeatSpinner().getValue();
            int nbFish = (int) dialog.getFishSpinner().getValue();
            dialog.close();
            try {
                myMenu.generateMenu(nbVegetarian, nbMeat, nbFish);
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
     */
    @Override
    @FXML
    public void saveMenu(){
        myMenu.setName(this.menuNameTextField.getText());
        try{
            if(myMenu.size() == 0) {
                this.setNodeColor(menuTableView, true);
             } else {
                Configuration.getCurrent().getMenuDao().insert(myMenu);
                WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
                mainMenuController.displayMainMenuController();
            }
        } catch(SQLException e) {
            this.setNodeColor(menuNameTextField,true);
        }
    }
}