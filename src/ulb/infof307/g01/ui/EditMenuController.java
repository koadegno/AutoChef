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

public class EditMenuController implements ulb.infof307.g01.ui.SearchRecipeInterface {
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


    public void cancelSearchRecipe() {
        stage.setScene(ulb.infof307.g01.ui.EditMenuController.scene);
    }

    @FXML
    void returnMain(ActionEvent event) throws IOException{}

    @FXML
    public void fillTableView(TableView table, List<Recipe> valueList) {
        for (int i = 0; i < valueList.size(); i++) {
            table.getItems().add(valueList.get(i));
        }
    }


    public void refreshTableView() {
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
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    public void addRecipe(Recipe recipe) {
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        myMenu.addMealTo(daysName.get(dayIndex), recipe);
        this.refreshTableView();
        this.stage.setScene(ulb.infof307.g01.ui.EditMenuController.scene);
    }

    @FXML
    public void removeRecipeAction(ActionEvent event) {
        Recipe recipeToRemove = (Recipe) menuTableView.getSelectionModel().getSelectedItem();
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        this.myMenu.removeMealFrom(daysName.get(dayIndex), recipeToRemove);
        refreshTableView();
        removeRecipeButton.setVisible(false);
    }

    @FXML
    public void recipeSelectedEvent(Event event) {
        int idx = menuTableView.getSelectionModel().getSelectedIndex();
        if (idx > -1) this.removeRecipeButton.setVisible(true);
    }

    @FXML
    public void saveMenu(){}
}
