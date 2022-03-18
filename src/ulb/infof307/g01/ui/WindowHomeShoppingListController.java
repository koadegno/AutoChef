package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.db.*;

import java.io.IOException;
import java.util.Objects;

/**
 * Classe qui permet d'afficher la fenetre principal de la liste de courses avec deux boutons
 * qui dirige chacun vers une autre sous fenetre de liste de courses
 */

public class WindowHomeShoppingListController extends Window {
    private Stage stage;
    private Parent root;

    public void displayMenuShoppingListController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowUserShoppingListsController.class.getResource("interface/FXMLMainShoppingList.fxml"));
        root = loader.load();
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void displayMyShoppingListController(ActionEvent event) throws IOException{
        WindowUserShoppingListsController windowsMyShoppingListsController = new WindowUserShoppingListsController();
        FXMLLoader loader = new FXMLLoader(WindowUserShoppingListsControllerTools.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
        loader.setController(windowsMyShoppingListsController); //controler pour affichage de liste de courses
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = loader.load();

        //Initialise la page avec les informations de la bdd
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();

        //afficher la fenetre la liste de courses
        Scene myscene = new Scene(root);
        this.stage.setScene(myscene);
        this.stage.show();
    }

    @FXML
    public void displayCreateShoppingListController(ActionEvent event) throws IOException{
        WindowCreateUserShoppingListController windowsCreateMyShoppingListController = new WindowCreateUserShoppingListController();
        FXMLLoader loader = new FXMLLoader(WindowCreateUserShoppingListController.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
        loader.setController(windowsCreateMyShoppingListController); //controler pour affichage d'une creation de liste de courses
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = loader.load();

        //Initialise la page avec les informations de la bdd
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();

        //afficher la fenetre de la creation de liste de courses
        Scene myscene = new Scene(root);
        this.stage.setScene(myscene);
        this.stage.show();

    }

    @FXML
    public void returnMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("interface/FXMLMainPage.fxml"))));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
