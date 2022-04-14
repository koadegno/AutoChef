package ulb.infof307.g01.view.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateMenuViewController extends ViewController<CreateMenuViewController.Listener> implements Initializable{
    @FXML
    ComboBox<Day> daysComboBox;
    @FXML
    TableView<Recipe> menuTableView;
    @FXML
    TableColumn<Recipe, String> menuTableColumn;
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
//    public void cancel() {
//        this.primaryStage.setScene(scene);
//    }

    /**
     * Methode de l'interface Utilisation contract qu'on a promis d'implementer pour
     * pouvoir utiliser la window qui permet la recherche d'une recette sur base de filtre
     * Elle permet à la classe appler de nous prevenir que l'utilisateur à fini sa recherche et
     * nous remettre la recette sélectionnée
     */
    public void add(Recipe recipe) {
//        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
//        myMenu.addRecipeTo(daysName.get(dayIndex), recipe);
//        refreshTableView();
//        this.primaryStage.setScene(this.scene);
    }

//
//
//    public void fillTableView(TableView<Recipe> table, List<Recipe> valueList) {
//        for (int i = 0; i < valueList.size(); i++) {
//            table.getItems().add(valueList.get(i));
//        }
//    }

    /**
     * Action liée à la daysComboBox
     */
    @FXML
    public void refreshTableView() {
        setNodeColor(menuNameTextField, false);
        setNodeColor(menuTableView, false);
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        listener.onDaysComboBoxClicked(dayIndex);
//        menuTableColumn.setText(daysName.get(dayIndex).toString());
//        this.menuTableView.getItems().clear();
//        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(dayIndex)));

    }

    /**
     * Permet de faire appelle à la classe SearchRecipeController
     */
    @FXML
    private void searchRecipe() {
        listener.onAddRecipeClicked();
//        this.scene = this.primaryStage.getScene();
//        WindowSearchRecipeController<UtilisationContrat<Recipe>> controller = (WindowSearchRecipeController<UtilisationContrat<Recipe>>) this.loadFXML("SearchRecipe.fxml");
//        controller.setMainController((UtilisationContrat<Recipe>) this);
    }


    @FXML
    public void removeRecipeAction() {
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        listener.onRemoveRecipeClicked(dayIndex);
        refreshTableView();
        removeRecipeButton.setVisible(false);
    }

    /**
     * Rend visible le bouton supprimer
     */
    @FXML
    public void recipeSelectedEvent() {
        int idx = menuTableView.getSelectionModel().getSelectedIndex();
        if (idx > -1) removeRecipeButton.setVisible(true);
    }




    public CreateMenuViewController() throws SQLException {
//        this.myMenu = new Menu();
//        this.daysName = new ArrayList<>();
//        for (int i = 0; i < 7; i++) daysName.add(Day.values()[i]);
    }

    @FXML
    public void displayEditMeal() {

//        this.loadFXML(this, "CreateDisplayMenu.fxml");
    }

    /**
     * Permet d'initialiser les différents objets utilisées de la fenêtre
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        daysComboBox.setItems(FXCollections.observableArrayList(daysName));
//        menuTableColumn.setText(daysName.get(0).toString());
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
//        this.fillTableView(menuTableView, myMenu.getRecipesfor(daysName.get(0)));
        this.removeRecipeButton.setVisible(false);
        this.generateMenuButton.setOnAction((event) -> {
            listener.onGenerateMenu();
        });
    }

    public ComboBox<Day> getDaysComboBox() {
        return daysComboBox;
    }

    public TableView<Recipe> getMenuTableView() {
        return menuTableView;
    }

    public TableColumn<Recipe, String> getMenuTableColumn() {
        return menuTableColumn;
    }

    /**
     * returnMain est la methode connectée au bouton qui permet de retourner
     *  au menu principal
     */
    @FXML
    public void returnMain() {
        listener.onReturnClicked();
    }

//    /**
//     * Cette fonction permet la génération automatique de menu
//     * @throws SQLException
//     */
//    @FXML
//    public void generateMenu() throws SQLException {
//        try {
//            GenerateMenuDialog generateMenuDialog = new GenerateMenuDialog();
//            popup = popupFXML("GenerateMenuDialog.fxml", generateMenuDialog);
//            generateMenuDialog.setMainController(this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Cette méthode permet de sauvegarder un menu dans la base de données
     */
    @FXML
    public void saveMenu(){
        boolean isSaved = listener.onSaveMenu(menuNameTextField.getText());
        setNodeColor(menuNameTextField,!isSaved);
        setNodeColor(menuTableView,!isSaved);
    }

//    public void addValuesToGenerateMenu(int nbVegetarianDishes, int nbMeatDishes, int nbFishDishes) {
//        popup.close();
//        try {
//            myMenu.generateMenu(nbVegetarianDishes, nbMeatDishes, nbFishDishes);
//            this.refreshTableView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void cancelGeneratingMenu() {
//        popup.close();
//    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setModifyMode() {
        generateMenuButton.setVisible(false);
        menuNameTextField.setVisible(false);
        menuNameLabel.setVisible(false);
    }


    public interface Listener{
        void onGenerateMenu();
        boolean onSaveMenu(String menuName);
        void onDaysComboBoxClicked(int dayIndex);
        void onAddRecipeClicked();
        void onReturnClicked();
        void onRemoveRecipeClicked(int dayIndex);
    }
}