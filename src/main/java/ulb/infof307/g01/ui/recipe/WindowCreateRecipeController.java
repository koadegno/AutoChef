package ulb.infof307.g01.ui.recipe;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.db.ShoppingListDao;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.shoppingList.WindowUserShoppingListsController;

import javax.sound.midi.Receiver;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

public class WindowCreateRecipeController extends Window implements Initializable {


    private ArrayList<String> dietList;
    private ArrayList<String> typeList;
    @FXML
    private TableView ingredientTableView;
    @FXML
    private TableColumn tableColumnProduct, tableColumnQuantityOrNumber, tableColumnUnity;
    @FXML
    private TextArea preparationTextArea;
    @FXML
    private ComboBox typeComboBox;
    @FXML
    private ComboBox dietComboBox;
    @FXML
    private Spinner nbPersonSpinner;
    @FXML
    public TextField recipeNameTextField;
    private Scene scene;
    private ShoppingList recipeIngredients = null;
    private Recipe myRecipe = null;

    public WindowCreateRecipeController() {
        try {
            this.dietList = Configuration.getCurrent().getRecipeCategoryDao().getAllName();
            this.typeList = Configuration.getCurrent().getRecipeTypeDao().getAllName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.recipeIngredients = new ShoppingList("ingredients");
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
        tableColumnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        tableColumnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        tableColumnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
    }

    private void refreshTableView() {
        Vector<Product> productOfShoppingList =  (Vector<Product>) recipeIngredients;
        ingredientTableView.setItems(FXCollections.observableArrayList(productOfShoppingList));

    }
    public void saveRecipe() {
        removeRedBorder();

        int dietIndex = dietComboBox.getSelectionModel().getSelectedIndex();
        int typeIndex = typeComboBox.getSelectionModel().getSelectedIndex();
        int nbPerson = (int) nbPersonSpinner.getValue();
        String recipeName = recipeNameTextField.getText();
        String preparation = preparationTextArea.getText();

        if(dietIndex < 0)setNodeColor(dietComboBox, true);
        else if (typeIndex <0) setNodeColor(typeComboBox, true);
        else if(recipeIngredients.size() ==0) setNodeColor(ingredientTableView, true);
        else if(Objects.equals(preparation, ""))setNodeColor(preparationTextArea, true);
        else if(Objects.equals(recipeName, "")) setNodeColor(recipeNameTextField, true);
        else{
            String diet= dietList.get(dietIndex);
            String type = typeList.get(typeIndex);
            this.myRecipe = new Recipe(recipeName, 0, diet, type, nbPerson, preparation );
            for (Product product : recipeIngredients) {
                this.myRecipe.add(product);
            }
            try {
                Configuration.getCurrent().getRecipeDao().insert(myRecipe);
            } catch (SQLException e) {
                e.printStackTrace();
                Window.showAlert(Alert.AlertType.ERROR, "ERROR", "MESSAGE D'ERREUR");
            }
            returnHomeRecipeWindow();
        }
    }

    private void removeRedBorder() {
        setNodeColor(dietComboBox, false);
        setNodeColor(typeComboBox, false);
        setNodeColor(ingredientTableView, false);
        setNodeColor(preparationTextArea, false);
        setNodeColor(recipeNameTextField, false);
    }


    public void returnHomeRecipeWindow() {
        WindowHomeRecipeController myRecipeWindow = new WindowHomeRecipeController();
        myRecipeWindow.displayMain();
    }

    public void addIngredients() {
        this.scene = this.primaryStage.getScene();
        WindowUserShoppingListsController windowsMyShoppingListsController = new WindowUserShoppingListsController();
        this.loadFXML(windowsMyShoppingListsController, "CreateUserShoppingList.fxml");
        windowsMyShoppingListsController.setCallerClass(this);

        //Initialise la page avec les informations de la bdd
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initForCreateRecipe(recipeIngredients);
    }


    public void cancel() {
        this.primaryStage.setScene(this.scene);
    }

    public void add(ShoppingList shoppingListToReturn) {
        this.recipeIngredients = shoppingListToReturn;
        this.primaryStage.setScene(this.scene);
        refreshTableView();
    }


}
