package ulb.infof307.g01.view.help;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.view.ViewController;


import java.io.InputStream;

public class HelpViewController extends ViewController<HelpViewController.Listener> {
    public VBox imageInformationVBox;
    private double x = 10;
    private double y = 10;
    private double width = 600;
    private double height = 390;


    /**
     * Permet d'afficher une image dans la popup
     * @param filename string contenant le nom de l'image à afficher
     */
    public void displayImageInformation(String filename){

        InputStream stream = (ViewController.class.getResourceAsStream(filename));
        Image image = new Image(stream);

        //Crée l'image
        ImageView imageView = new ImageView();

        //Les param de l'image
        imageView.setImage(image);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
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
