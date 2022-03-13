package ulb.infof307.g01.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.jar.Attributes;

public class GenerateMenuDialog extends Stage {
    private VBox dialogVbox;
    private HBox vegetarianBox,meatBox,fishBox,buttonBox;
    private Spinner<Integer> vegetarianSpinner,meatSpinner,fishSpinner;
    private Button okButton, cancelButton;
    final double spacing = 100;
    final int hboxHeight = 30;


    public void initObject(){
        this.initBoxes();
        this.initSetAlignement();
        this.initButton();
        this.initSpinners();
        this.fillBoxes();
        Scene dialogScene = new Scene(dialogVbox, 500, 300);
        this.setScene(dialogScene);
    }

    public Button getOkButton(){
        return okButton;
    }

    private void fillBoxes(){
        //labels
        Label vegetarianLabel = new Label("Nombre de plats végétariens : ");
        Label meatLabel = new Label("Nombre de plats de viande : ");
        Label fishLabel = new Label("Nombre de plats de poisson : ");
        buttonBox.getChildren().addAll(okButton,cancelButton);
        //add labels and spinners
        vegetarianBox.getChildren().addAll(vegetarianLabel, vegetarianSpinner);
        meatBox.getChildren().addAll(meatLabel, meatSpinner);
        fishBox.getChildren().addAll(fishLabel, fishSpinner);
        //add hbox
        dialogVbox.getChildren().addAll(vegetarianBox, meatBox,fishBox, buttonBox);
    }

    private void initBoxes(){
        dialogVbox = new VBox(this.spacing/2);
        vegetarianBox = new HBox(this.spacing);
        meatBox = new HBox(this.spacing);
        fishBox = new HBox(this.spacing);
        buttonBox = new HBox(this.spacing/2);

        vegetarianBox.setPrefHeight(hboxHeight);
        meatBox.setPrefHeight(hboxHeight);
        fishBox.setPrefHeight(hboxHeight);
    }

    private void initButton() {
        okButton = new Button("Valider");
        cancelButton = new Button("Annuler");
        //connected button
        cancelButton.setOnAction((event1) -> {
            this.close();
        });
    }

    private void initSpinners() {
        vegetarianSpinner = new Spinner<Integer>(0, 100,0);
        meatSpinner = new Spinner<>(0, 100,0);
        fishSpinner = new Spinner<>(0, 100,0);
    }

    private void initSetAlignement() {
        dialogVbox.setAlignment(Pos.CENTER);
        vegetarianBox.setAlignment(Pos.CENTER);
        meatBox.setAlignment(Pos.CENTER);
        fishBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
    }

    public Spinner<Integer> getVegetarianSpinner() {
        return this.vegetarianSpinner;
    }
    public Spinner<Integer> getMeatSpinner() {
        return this.meatSpinner;
    }
    public Spinner<Integer> getFishSpinner() {
        return this.fishSpinner;
    }
}
