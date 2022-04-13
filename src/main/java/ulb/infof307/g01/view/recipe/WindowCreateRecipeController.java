package ulb.infof307.g01.view.recipe;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

public class WindowCreateRecipeController extends Window implements Initializable {
    private WindowViewRecipeController mainController = null; //to modify a recipe from WindowviewRecipeController
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
    private TextField recipeNameTextField;
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

    public void  displayMain() {
        this.loadFXML(this,"createRecipe.fxml");
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
        recipeNameTextField.setPromptText("Nom de la recette"); //to activate its existence. If not, setMainController will cause nullPointerException
        preparationTextArea.setWrapText(true);
    }

    private void refreshTableView() {
        Vector<Product> productOfShoppingList = new Vector<>(recipeIngredients);
        ingredientTableView.setItems(FXCollections.observableArrayList(productOfShoppingList));

    }
    public void saveRecipe() {
        removeRedBorder();

        int dietIndex = dietComboBox.getSelectionModel().getSelectedIndex();
        int typeIndex = typeComboBox.getSelectionModel().getSelectedIndex();
        int nbPerson = (int) nbPersonSpinner.getValue();
        String preparation = preparationTextArea.getText();
        String recipeName;
        if(mainController==null)recipeName = recipeNameTextField.getText();
        else recipeName = myRecipe.getName();

        if(dietIndex < 0)setNodeColor(dietComboBox, true);
        else if (typeIndex <0) setNodeColor(typeComboBox, true);
        else if(recipeIngredients.size() ==0) setNodeColor(ingredientTableView, true);
        else if(Objects.equals(preparation, ""))setNodeColor(preparationTextArea, true);
        else if(Objects.equals(recipeName, "")) setNodeColor(recipeNameTextField, true);
        else{
            String diet= dietList.get(dietIndex);
            String type = typeList.get(typeIndex);
            if(mainController==null) this.myRecipe = new Recipe(recipeName, 0, diet, type, nbPerson, preparation );
            else{
                this.myRecipe.setCategory(diet);
                this.myRecipe.setPreparation(preparation);
                this.myRecipe.setType(type);
                this.myRecipe.setNbrPerson(nbPerson);
                this.myRecipe.removeAll(this.myRecipe);
            }
            this.myRecipe.addAll(recipeIngredients);
            try {
                if(mainController==null) Configuration.getCurrent().getRecipeDao().insert(myRecipe); //creating new recipe (not modifying )
                else mainController.updateRecipe(myRecipe);
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
        if(mainController==null)setNodeColor(recipeNameTextField, false);
    }


    public void returnHomeRecipeWindow() {
        if(mainController==null) {
            WindowHomeRecipeController myRecipeWindow = new WindowHomeRecipeController();
            myRecipeWindow.displayMain();
        }
        else{ //cancel modfying
            mainController.cancel();
        }
    }

    public void addIngredients() {
        //TODO: regler ici pour le MVC
        this.scene = this.primaryStage.getScene();
        UserShoppingListViewViewController windowsMyShoppingListsController = new UserShoppingListViewViewController();
        //this.loadFXML(windowsMyShoppingListsController, "CreateUserShoppingList.fxml");
        windowsMyShoppingListsController.setCallerClass(this);

        //Initialise la page avec les informations de la bdd
        //TODO regler ce probleme
        //windowsMyShoppingListsController.initShoppingListElement();
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

    public  void setMainController(WindowViewRecipeController viewRecipe, Recipe recipeToModify){
        mainController = viewRecipe;
        myRecipe = recipeToModify;
        initElementToModifyingRecipe();
    }
    @FXML
    public void initElementToModifyingRecipe() {
        recipeIngredients = new ShoppingList("modifying");
        recipeIngredients.addAll(myRecipe);
        refreshTableView();
        preparationTextArea.setText(myRecipe.getPreparation());
        recipeNameTextField.setVisible(false);
        dietComboBox.getSelectionModel().select(myRecipe.getCategory());
        typeComboBox.getSelectionModel().select(myRecipe.getType());
        nbPersonSpinner.getEditor().setText(String.valueOf(myRecipe.getNbrPerson()));

    }


}
