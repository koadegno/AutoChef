package ulb.infof307.g01.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.stream.Stream;


public class ComboBoxController {
    private static final String[] productList = {"Tomates", "Salade", "Poisson", "Pommes", "Pâtes", "Saucisse", "Tacos", "Kiwi"};
    private static ObservableList<String> originalItems = FXCollections.observableArrayList(productList);
    String filter = "";


    @FXML
    ComboBox<String> comboBox = new ComboBox<>(originalItems);
    //Voici le comboBox:
    //<ComboBox
    //      fx:id="comboBox"
    //      editable="true"
    //      layoutX="112.0" layoutY="55.0"
    //      onAction="#handleOnKeyPressed"
    //      onHidden="#handleOnHiding"
    //      onKeyPressed="#handleOnKeyPressed"
    //      onMouseClicked="#handleOnMouseClicked"
    //      prefHeight="26.0" prefWidth="348.0"
    // />



    //event: onMouseClicked
    //action: montrer la liste des produits
    public void handleOnMouseClicked(MouseEvent click) throws IOException {
        comboBox.getItems().setAll(originalItems);

    }

    //event: onKeyPressed & onAction
    //action: récuperer le caractère, filtrer les produits qui match et afficher la liste filtrée
    //TODO: Ce truc ne marche pas, il ne reconnait pas le event onKeyPressed à moins que ce soit ENTER.
    // Il ne marche que si on remplace onKeyPressed par onKeyReleased, mais dans ce cas il ne filtre que par un seul charactère
    // C'est nul, il y a rien qui marche, c'est du kk, on veut pleurer. Thanks.
    public void handleOnKeyPressed(KeyEvent keyEvent){

        KeyCode inputKey =  keyEvent.getCode();
        ObservableList<String> matchingList = FXCollections.observableArrayList();
        if (inputKey.isLetterKey()){
            filter += keyEvent.getText();
        }
        if (inputKey == KeyCode.BACK_SPACE && filter.length() > 0) {
            filter = filter.substring(0, filter.length() - 1);
            comboBox.getItems().setAll(originalItems);
        }
        if (inputKey == KeyCode.ESCAPE) {
            filter = "";
        }
        if (filter.length() == 0) {
            matchingList = originalItems;

        } else {
            Stream<String> products = originalItems.stream();
            String userInput = filter.toLowerCase();
            products.filter(product -> product.toLowerCase().startsWith(userInput)).forEach(matchingList::add);
            comboBox.show();
        }
        comboBox.getItems().setAll(matchingList);

    }

    //event: onHidden
    public void handleOnHiding(Event e) {
        filter = "";
        String s = comboBox.getSelectionModel().getSelectedItem();
    }



}
