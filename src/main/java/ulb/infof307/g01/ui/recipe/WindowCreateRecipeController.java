package ulb.infof307.g01.ui.recipe;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.shoppingList.WindowUserShoppingListsController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowCreateRecipeController extends Window implements Initializable {
    private ArrayList<String> dietList;
    private ArrayList<String> typeList;
    @FXML
    private TableView ingredientTableView;
    @FXML
    private TextArea preparationTextArea;
    @FXML
    private ComboBox typeComboBox;
    @FXML
    private ComboBox dietComboBox;
    @FXML
    private Spinner nbPersonSpinner;

    public WindowCreateRecipeController() {
        try {
            this.dietList = Configuration.getCurrent().getRecipeCategoryDao().getAllName();
            this.typeList = Configuration.getCurrent().getRecipeTypeDao().getAllName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayMain() {
        this.loadFXML("createRecipe.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dietComboBox.setItems(FXCollections.observableArrayList(dietList));
        typeComboBox.setItems(FXCollections.observableArrayList(typeList));
        nbPersonSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        this.onlyIntValue(nbPersonSpinner);



    }
    public void returnHomeRecipeWindow(ActionEvent actionEvent) {
        WindowHomeRecipeController myRecipeWindow = new WindowHomeRecipeController();
        myRecipeWindow.displayMain();
    }

    public void addIngredients() {
        WindowUserShoppingListsController windowsMyShoppingListsController = new WindowUserShoppingListsController();
        this.loadFXML(windowsMyShoppingListsController, "CreateUserShoppingList.fxml");

        //Initialise la page avec les informations de la bdd
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();
    }
}
