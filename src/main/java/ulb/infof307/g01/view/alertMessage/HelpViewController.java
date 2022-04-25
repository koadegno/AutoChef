package ulb.infof307.g01.view.alertMessage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.view.ViewController;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class HelpViewController extends ViewController<HelpViewController.Listener> {
    public VBox imageInformationVBox;

    /**
     * Permet d'afficher une image dans la popup
     * @param filename string contenant le nom de l'image à afficher
     */
    public void displayImageInformation(String filename){

        String filePath = Objects.requireNonNull(ViewController.class.getResource(filename)).getPath();
        InputStream stream = null;

        try {
            stream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Image image = new Image(stream);

        //Crée l'image
        ImageView imageView = new ImageView();

        //Les param de l'image
        imageView.setImage(image);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        //Rajoute l'image dans le VBOX pour qu'elle puisse s'afficher
        imageInformationVBox.getChildren().clear(); //efface la VBOX au cas où il y avait une ancienne image
        imageInformationVBox.getChildren().add(imageView);
    }

    public void leftButton(){
        listener.leftButtonClicked();

    }

    public void rightButton(){
        listener.rightButtonClicked();
    }

    public interface Listener{
        void leftButtonClicked();
        void rightButtonClicked();
    }
}
