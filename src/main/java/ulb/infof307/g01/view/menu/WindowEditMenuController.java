package ulb.infof307.g01.view.menu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.recipe.WindowSearchRecipeController;
import ulb.infof307.g01.view.tools.UtilisationContrat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant l'edition d'un menu mais qui ne fait rien quand à quoi faire de ce menu
 * Il laisse cela aux enfants
 *
 */
public class WindowEditMenuController extends Window implements UtilisationContrat<Recipe> {
    static Scene scene;
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
        this.primaryStage.setScene(scene);
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
        this.primaryStage.setScene(this.scene);
    }

    /**
     * Doi être override par les classe héritière.
     * @throws IOException
     */
    @FXML
    void returnMain(){}

    @FXML
    public void fillTableView(TableView table, List<Recipe> valueList) {
        for (int i = 0; i < valueList.size(); i++) {
            table.getItems().add(valueList.get(i));
        }
    }


    public void refreshTableView() {
        this.setNodeColor(menuTableView, false);
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        menuTableColumn.setText(daysName.get(dayIndex).toString());
        this.menuTableView.getItems().clear();
        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(dayIndex)));

    }

    /**
     * Permet de faire appelle à la classe SearchRecipeController
     */
    @FXML
    private void searchRecipe() {
        this.scene = this.primaryStage.getScene();
        WindowSearchRecipeController controller = (WindowSearchRecipeController) this.loadFXML("SearchRecipe.fxml");
        controller.setMainController(this);
    }


    @FXML
    public void removeRecipeAction() {
        Recipe recipeToRemove = (Recipe) menuTableView.getSelectionModel().getSelectedItem();
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        this.myMenu.removeRecipeFrom(daysName.get(dayIndex), recipeToRemove);
        refreshTableView();
        removeRecipeButton.setVisible(false);
    }

    /**
     * Rend visible le bouton supprimer
     */
    @FXML
    public void recipeSelectedEvent() {
        int idx = menuTableView.getSelectionModel().getSelectedIndex();
        if (idx > -1) this.removeRecipeButton.setVisible(true);
    }

    /**
     * Doit être géré par les enfants...Que faire quand l'utilisateur à terminer d'éditer un menu?
     */
    @FXML
    public void saveMenu(){}
}
