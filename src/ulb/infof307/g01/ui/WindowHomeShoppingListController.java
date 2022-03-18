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
    public void displayMenuShoppingListController() {
        this.loadFXML("interface/FXMLMainShoppingList.fxml");
    }

    @FXML
    public void displayMyShoppingListController(){
        WindowUserShoppingListsController windowsMyShoppingListsController = new WindowUserShoppingListsController();
        this.loadFXML(windowsMyShoppingListsController, "interface/FXMLCreateMyShoppingList.fxml");

        //Initialise la page avec les informations de la bdd
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();

    }

    @FXML
    public void displayCreateShoppingListController(){
        WindowCreateUserShoppingListController windowsCreateMyShoppingListController = new WindowCreateUserShoppingListController();
        this.loadFXML(windowsCreateMyShoppingListController, "interface/FXMLCreateMyShoppingList.fxml");
        //Initialise la page avec les informations de la bdd
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();
    }

    @FXML
    public void returnMainMenu() {
        this.loadFXML("interface/FXMLMainPage.fxml");
    }
}
