package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Classe permettant l'edition d'un menu mais qui ne fait rien quand à quoi faire de ce menu
 * Il laisse cela aux enfants
 *
 */
public class EditMenuController implements UtilisationContrat<Recipe>{
    static Scene scene;
    static Parent root;
    protected Stage stage;
    protected Database db;
    protected Menu myMenu;
    protected ArrayList<Day> daysName;
    @FXML
    ComboBox daysComboBox;
    @FXML
    TableView menuTableView;
    @FXML
    TableColumn menuTableColumn;
    @FXML
    Button removeRecipeButton, generateMenuButton;
    @FXML
    TextField menuNameTextField;
    @FXML
    Label menuNameLabel;


    /**
     * Methode de l'interface Utilisation contract qu'on a promis d'implementer pour
     * pouvoir utiliser la window qui permet la recherche d'une recette sur base de filtre
     * Elle permet à la classe appler de nous prevenir que l'utilisateur à annuller la recherche
     */
    @Override
    public void cancel() {
        stage.setScene(scene);
    }

    /**
     * Methode de l'interface Utilisation contract qu'on a promis d'implementer pour
     * pouvoir utiliser la window qui permet la recherche d'une recette sur base de filtre
     * Elle permet à la classe appler de nous prevenir que l'utilisateur à fini sa recherche et
     * nous remettre la recette sélectionnée
     */
    @Override
    public void add(Recipe recipe) {
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        myMenu.addRecipeTo(daysName.get(dayIndex), recipe);
        this.refreshTableView();
        this.stage.setScene(this.scene);
    }

    /**
     * Doi être override par les classe héritière.
     * @param event l'utilisateur a annulé l'édition
     * @throws IOException
     */
    @FXML
    void returnMain(ActionEvent event) throws IOException{}

    @FXML
    public void fillTableView(TableView table, List<Recipe> valueList) {
        for (int i = 0; i < valueList.size(); i++) {
            table.getItems().add(valueList.get(i));
        }
    }


    public void refreshTableView() {
        menuTableView.setStyle("");
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        menuTableColumn.setText(daysName.get(dayIndex).toString());
        this.menuTableView.getItems().clear();
        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(dayIndex)));

    }

    /**
     * Permet de faire appelle à la classe SearchRecipeController
     * @param event L'
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    private void searchRecipe(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(SearchRecipeController.class.getResource("interface/searchRecipe.fxml"));
        Parent root = loader.load();
        SearchRecipeController controller = loader.getController();
        controller.setMainController(this);
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    /**
     *
     * @param event bouton supprimer recette de la liste
     */
    @FXML
    public void removeRecipeAction(ActionEvent event) {
        Recipe recipeToRemove = (Recipe) menuTableView.getSelectionModel().getSelectedItem();
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        this.myMenu.removeRecipeFrom(daysName.get(dayIndex), recipeToRemove);
        refreshTableView();
        removeRecipeButton.setVisible(false);
    }

    /**
     * Rend visible le bouton supprimer
     * @param event recette à été selectionner dans la tableview
     */
    @FXML
    public void recipeSelectedEvent(Event event) {
        int idx = menuTableView.getSelectionModel().getSelectedIndex();
        if (idx > -1) this.removeRecipeButton.setVisible(true);
    }

    /**
     * Doit être géré par les enfants...Que faire quand l'utilisateur à terminer d'éditer un menu?
     * @param event Bouton enregistré à été cliqué
     */
    @FXML
    public void saveMenu(ActionEvent event){}
}
