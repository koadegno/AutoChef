package com.example.liste_de_courses;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.cert.PolicyNode;
import java.util.Objects;

public class ListeDeCourse { //connexion
    @FXML
    private Label welcomeText;
    @FXML
    private Label welcomeTexte;
    @FXML
    private Parent root;

@FXML
    protected void onCreateListButtonClick() throws IOException {
        welcomeText.setText("Je veux créer une liste de courses");
        FXMLLoader fxmlLoader = new FXMLLoader(ListeDeCourseApplication.class.getResource("createList-view.fxml"));
        Scene scene2 = new Scene(fxmlLoader.load(), 720, 630);

        //ListeDeCourseApplication app = new ListeDeCourseApplication();
    }

    protected void pageCreateList(Stage primaryStage){

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        Button btn = new Button("Ajouter un produit");

        root.getChildren().addAll(btn);
        Scene scene = new Scene(root, 350, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Crée une liste de courses!");
        primaryStage.show();
    }

    @FXML //mettre en public?
    protected void onSecondButtonClick(){
        welcomeTexte.setText("heu my friend");
    }
}