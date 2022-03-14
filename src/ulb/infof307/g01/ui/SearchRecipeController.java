package ulb.infof307.g01.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Recipe;
import  ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Permet de rechercher une recette sur base de filtre
 * @param <T> Le type de la window qui fait appelle à nous
 * Retourne la recette choisie par l'utilisateur sinon previent que
 * la recherche est annuler
 */
public class SearchRecipeController <T extends UtilisationContrat<Recipe>> implements Initializable {
    private Stage stage;
    private Parent root;
    private Database db ;
    private ArrayList<String> dietList ;
    private ArrayList<String> typeList ;
    private ArrayList<Recipe>  recipeName;
    private  T mainController; //l'objet appelant qui implement les methodes add et cancel

    @FXML
    ComboBox recipeDietComboBox, recipeTypeComboBox;
    @FXML
    Spinner numberOfPersonSpinner;
    @FXML
    TableView recipeTableView;
    @FXML
    TableColumn columnRecipeName;
    @FXML
    Button cancelSearchRecipeButton;
    @FXML
    CheckBox activateSpinnerCheckBox;

    public SearchRecipeController() throws SQLException {
        this.db = new Database("autochef.sqlite");
        this.dietList = db.getAllCategories();
        this.typeList = db.getAllTypes();
        this.recipeName = db.getRecipeWhere(null, null, 0);
        this.dietList.add(0, "Tout");
        this.typeList.add(0, "Tout");
    }
    @FXML
    public void fillComboBox(ComboBox box, ArrayList<String> valueList){
        for(String value: valueList)
            box.getItems().add(value);
    }

    @FXML
    public void  fillTableView(TableView table, ArrayList<Recipe> valueList){
        for(int i =0; i < valueList.size(); i++){
            table.getItems().add(valueList.get(i));
        }
    }

    //override par les enfant mais pas abstract car par défaut le bouton qui lui est connecté ne fait rien
    @FXML
    public void confirmRecipe(ActionEvent event) throws IOException {}

    @FXML
    private void onlyIntValue(){
        numberOfPersonSpinner.getEditor().textProperty().addListener(new ChangeListener<String>() { //Seulement écrire des nombres
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfPersonSpinner.getEditor().setText(newValue.replaceAll("[^\\d*]", ""));
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnRecipeName.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        fillComboBox(this.recipeDietComboBox, dietList);
        fillComboBox(this.recipeTypeComboBox, typeList);
        fillTableView(this.recipeTableView, recipeName);
        recipeDietComboBox.getSelectionModel().selectFirst();
        recipeTypeComboBox.getSelectionModel().selectFirst();
        recipeDietComboBox.setVisibleRowCount(10);
        numberOfPersonSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000)
        );
        onlyIntValue();
        this.numberOfPersonSpinner.setVisible(false);
    }

    /**
     * L'user à annuler la recherche d'une recette , on previent l'appelant que le contrat est fini
     * @param event bouton retour à été cliqué
     * @throws IOException
     */
    public void returnToCreateMenu(ActionEvent event) throws IOException {
        this.mainController.cancel();
    }

    /**
     * On previent l'appelant que le contrat est fini
     * @param event Une recette à été selection dans la tableView
     * @throws IOException
     * Donne l'objet demander et construit pas l'utilisateur
     */
    public void addRecipe(Event event) throws IOException{
        int idx = recipeTableView.getSelectionModel().getSelectedIndex();
        if(idx > -1) this.mainController.add(this.recipeName.get(idx));
    }


    public void refreshTableView( Event actionEvent) throws SQLException{
        int dietIndex = recipeDietComboBox.getSelectionModel().getSelectedIndex();
        int typeIndex = recipeTypeComboBox.getSelectionModel().getSelectedIndex();
        int nbPerson = (int) numberOfPersonSpinner.getValue();

        String dietCondition = null; //all diet
        String typeCondition = null; //all type

        // get new recipe's list from Database
        if (dietIndex > 0) dietCondition = dietList.get(dietIndex);
        if (typeIndex > 0) typeCondition = typeList.get(typeIndex);
        if(!this.activateSpinnerCheckBox.isSelected()) nbPerson =0;
        this.recipeName = db.getRecipeWhere(dietCondition, typeCondition, nbPerson);
        this.recipeTableView.getItems().clear();
        this.fillTableView(this.recipeTableView, recipeName);

    }

    public void checkBoxEvent(MouseEvent mouseEvent) throws SQLException{
        if (this.activateSpinnerCheckBox.isSelected()){
            this.numberOfPersonSpinner.setVisible(true);

        }
        else{
            this.numberOfPersonSpinner.setVisible(false);

        }
        this.refreshTableView(mouseEvent);
    }

    /**
     *
     * @param mainControler Un pointeur vers l'objet appelant qui s'est mis en attente
     */
    public void setMainController(T mainControler) {
        this.mainController = mainControler;
    }
}
