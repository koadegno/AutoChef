package ulb.infof307.g01.view.recipe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.model.JSON;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.tools.UtilisationContrat;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Deprecated
public class WindowViewRecipeController extends Window  implements UtilisationContrat<Recipe>, Initializable {

    @FXML
    public TextField recipeSearchTextField;
    @FXML
    public TextArea recipeTextArea;
    @FXML
    public Button deleteButton, modifyRecipeButton;

    private static Scene scene;
    private String ingredientTitle = "Ingrédients : ";
    private String preparationTitle = "Préparation : ";
    private Recipe displayedRecipe = null;


    public void returnHomeRecipeWindow() {
        WindowHomeRecipeController myRecipeWindow = new WindowHomeRecipeController();
        myRecipeWindow.displayMain();
    }

    public void displayMain() {
        this.loadFXML("viewRecipe.fxml");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipeTextArea.setEditable(false);
        recipeTextArea.setWrapText(true);
        recipeSearchTextField.setPromptText("Entrer nom de la recette");
        deleteButton.setVisible(false);
        modifyRecipeButton.setVisible(false);
        refreshTextArea();
    }

    public void refreshTextArea() {
        if (displayedRecipe != null) {
            String preparation = preparationTitle + "\n" + displayedRecipe.getPreparation() + "\n";
            String ingredient = ingredientTitle + "\n" + productListToString(displayedRecipe) + "\n";
            String toDisplay = "Nom de la recette :  " + displayedRecipe.getName()  + "\n" + ingredient + preparation;
            recipeTextArea.setText(toDisplay);
            deleteButton.setVisible(true);
            modifyRecipeButton.setVisible(true);
        }
        else{
            recipeTextArea.setText("Aucune recette sélectionnée");
        }
    }

    public String productListToString(Recipe recipe){
        StringBuilder res = new StringBuilder();
        for (Product product: recipe) {
            String name = product.getName();
            String quantity= String.valueOf(product.getQuantity());
            String unity = product.getNameUnity();
            res.append(quantity).append(" ").append(unity).append(" ").append(name).append(" \n");
        }
        return res.toString();
    }


    public void searchRecipe() {
        this.scene = this.primaryStage.getScene();
        WindowSearchRecipeController controller = (WindowSearchRecipeController) this.loadFXML("SearchRecipe.fxml");
        controller.setMainController(this);
    }

    @Override
    public void add(Recipe recipe) {
        displayedRecipe = recipe;
        refreshTextArea();
        this.primaryStage.setScene(this.scene);
    }

    @Override
    public void cancel() {
        this.primaryStage.setScene(this.scene);
    }


    public void onRecipeSearchTextFieldSubmit(KeyEvent keyEvent) {
        if(keyEvent.getCode() != KeyCode.ENTER)return;
        String recipeName = recipeSearchTextField.getText();
        if(recipeName==null)return;
        try {
            displayedRecipe = Configuration.getCurrent().getRecipeDao().get(recipeName);
            if(displayedRecipe == null) setNodeColor(recipeSearchTextField,true);
            else setNodeColor(recipeSearchTextField,false);
            refreshTextArea();
        } catch (SQLException e) {
            e.printStackTrace();
            Window.showAlert(Alert.AlertType.ERROR,"Erreur", "Erreur lié a la base de donnée contacter le service d'assistance ");
        }
    }

    public void deleteRecipe() {
        try {
            Configuration.getCurrent().getRecipeDao().delete(displayedRecipe);
        } catch (SQLException e) {
            e.printStackTrace();
            e.printStackTrace();
            Window.showAlert(Alert.AlertType.ERROR, "ERROR", "MESSAGE D'ERREUR");
        }
        deleteButton.setVisible(false);
        modifyRecipeButton.setVisible(false);
        displayedRecipe = null;
        refreshTextArea();
    }

    public void modifyRecipe(){
        this.scene = this.primaryStage.getScene();
        WindowCreateRecipeController createRecipeWindow = new WindowCreateRecipeController();
        createRecipeWindow.displayMain();
        createRecipeWindow.setMainController(this, displayedRecipe);
    }

    public void importJSONRecipe() {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Ouvrir un fichier");
        dialog.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Fichier JSON", "*.json"));
        File file = dialog.showOpenDialog(primaryStage);
        if (file != null && file.getName().endsWith(".json")) {
            JSON json = new JSON();
            json.jsonReader(file.getAbsolutePath());
        }

    }

    public void updateRecipe(Recipe myRecipe) {
        this.primaryStage.setScene(this.scene);
        try {
            Configuration.getCurrent().getRecipeDao().update(myRecipe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        displayedRecipe = myRecipe;
        refreshTextArea();
    }
}
