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


/**
 * Permet de modifier un menu existant
 */
public class ModifyMenuController extends EditMenuController implements Initializable  {
    WindowShowMenuController mainController;

    public ModifyMenuController(Menu menuName) throws SQLException {
        this.db = new Database("autochef.sqlite");
        this.myMenu = menuName;
        this.daysName = new ArrayList<>();
        for (int i = 0; i < 7; i++) daysName.add(Day.values()[i]);
    }

    @FXML
    public void setScene(Scene scene){
        this.scene = scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 7; i++) daysComboBox.getItems().add(daysName.get(i).toString());
        daysComboBox.getSelectionModel().selectFirst();
        menuTableColumn.setText(daysName.get(0).toString());
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(0)));
        this.removeRecipeButton.setVisible(false);
        menuNameTextField.setVisible(false);
        menuNameLabel.setVisible(false);
        generateMenuButton.setVisible(false);
    }

    /**
     * Que faire quand l'utilisateur annule la modification?
     * @param event l'utilisateur a annulé l'édition
     * @throws IOException
     */
    @Override
    @FXML
    void returnMain(ActionEvent event) throws IOException {
        this.mainController.cancel();
    }

    /**
     * Enregistre les modifications dan la base de données
     * @param event Bouton enregistré à été cliqué
     */
    @Override
    @FXML
    public void saveMenu(ActionEvent event){
        try{
            this.db.saveModifyMenu(myMenu);
            this.mainController.add(myMenu);
        }catch(Exception e){System.out.println(e);
        }
    }


    public void setMainController(WindowShowMenuController mainController) {
        this.mainController = mainController;
    }
}
