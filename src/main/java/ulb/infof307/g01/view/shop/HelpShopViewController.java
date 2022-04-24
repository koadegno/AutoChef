package ulb.infof307.g01.view.shop;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.view.ViewController;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class HelpShopViewController extends ViewController<HelpShopViewController.Listener> {
    public VBox imageInformationShopVBox;

    public void displayImageInformation(String filename){

        String filePath = Objects.requireNonNull(ViewController.class.getResource(filename)).getPath();
        InputStream stream = null;

        try {
            stream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Image image = new Image(stream);

        //Creating the image view
        ImageView imageView = new ImageView();

        //Setting image to the image view
        imageView.setImage(image);

        //Setting the image view parameters
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(450);
        imageView.setPreserveRatio(true);

        //Setting the Scene object
        imageInformationShopVBox.getChildren().clear();
        imageInformationShopVBox.getChildren().add(imageView);
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
