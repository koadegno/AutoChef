package ulb.infof307.g01.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
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
     * Action liée à la daysComboBox
     */
    @FXML
    public void refreshTableView() {
        setNodeColor(menuNameTextField, false);
        setNodeColor(menuTableView, false);
        int dayIndex = daysComboBox.getSelectionModel().getSelectedIndex();
        listener.onDaysComboBoxClicked(dayIndex);
    }

    /**
     * Permet de faire appelle à la classe SearchRecipeController
     */
    @FXML
    private void searchRecipe() {
        listener.onAddRecipeClicked();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.removeRecipeButton.setVisible(false);
        this.generateMenuButton.setOnAction((event) -> listener.onGenerateMenu());
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

    /**
     * Cette méthode permet de sauvegarder un menu dans la base de données
     */
    @FXML
    public void saveMenu(){
        boolean isSaved = listener.onSaveMenu(menuNameTextField.getText());
        setNodeColor(menuNameTextField,!isSaved);
        setNodeColor(menuTableView,!isSaved);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setModifyMode() {
        generateMenuButton.setVisible(false);
        menuNameTextField.setVisible(false);
        menuNameLabel.setVisible(false);
    }

    public void helpCreateMenu() {
        listener.onHelpCreateMenuClicked();
    }

   
    public void logout() {
        listener.logout();
    }


    public interface Listener{
        void onGenerateMenu();
        boolean onSaveMenu(String menuName);
        void onDaysComboBoxClicked(int dayIndex);
        void onAddRecipeClicked();
        void onReturnClicked();
        void onRemoveRecipeClicked(int dayIndex);
        void onHelpCreateMenuClicked();

        void logout();
    }
}