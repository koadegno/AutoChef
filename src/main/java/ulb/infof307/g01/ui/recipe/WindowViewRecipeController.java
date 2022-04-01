package ulb.infof307.g01.ui.recipe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.tools.UtilisationContrat;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowViewRecipeController extends Window  implements UtilisationContrat<Recipe>, Initializable {

    @FXML
    public TextField recipeTextField;
    @FXML
    public TextArea displayRecipeTextArea;
    private static Scene scene;
    private String ingredientTitle = "Ingrédients : ";
    private String preparationTitle = "Préparation : ";
    public void returnHomeRecipeWindow() {
        WindowHomeRecipeController myRecipeWindow = new WindowHomeRecipeController();
        myRecipeWindow.displayMain();
    }

    public void displayMain() {
        this.loadFXML("viewRecipe.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayRecipeTextArea.setEditable(false);
        displayRecipeTextArea.setWrapText(true);
        recipeTextField.setPromptText("Entrer nom de la recette");
    }

    public void displayRecipe(Recipe recipe){
        String preparation = preparationTitle + "\n" + recipe.getPreparation() + "\n";
        String ingredient = ingredientTitle + "\n" + productListToString(recipe) + "\n";
        String toDisplay = ingredient + preparation;
        displayRecipeTextArea.setText(toDisplay);
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
        displayRecipe(recipe);
        this.primaryStage.setScene(this.scene);
    }

    @Override
    public void cancel() {
        this.primaryStage.setScene(this.scene);
    }


    public void verifyTextFieldContent(KeyEvent keyEvent) {
        if(keyEvent.getCode() != KeyCode.ENTER)return;
        String recipeName = recipeTextField.getText();
        System.out.println(recipeName);
        if(recipeName==null)return;
        try {
            //TODO: verifier l'existence de la recette dans le db
            //TODO: displayRecipe(recipe) si elle existe sinon rougir le textfield
        }catch (Exception e){
            //TODO: rougir le textField
        }
    }

}
