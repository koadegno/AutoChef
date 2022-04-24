package ulb.infof307.g01.controller.map;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.shop.HelpShopViewController;

import java.io.IOException;


public class HelpShopController extends Controller implements HelpShopViewController.Listener {
    private HelpShopViewController helpShopViewController;
    private Stage helpShopPopup;
    private final String beginFilePathNameImageInformation = "helpShop/";
    private int numberImageInformation = 1;
    private final int lastNumberImageInformation = 14;

    public HelpShopController(){
        helpShopViewController = new HelpShopViewController();
    }

    public void displayHelpShop(){
        try {
            helpShopPopup = this.popupFXML("HelpShop.fxml", helpShopViewController);
            helpShopPopup.setResizable(false);
            helpShopViewController.setListener(this);
            createFilePathImageInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leftButtonClicked() {
        if((numberImageInformation - 1) > 0){
            numberImageInformation -= 1;
            createFilePathImageInformation();
        }
    }

    @Override
    public void rightButtonClicked() {
        if((numberImageInformation + 1) < lastNumberImageInformation){
            numberImageInformation += 1;
            createFilePathImageInformation();
        }
        else{
            helpShopPopup.close();
        }
    }

    public void createFilePathImageInformation(){
        String filepath = beginFilePathNameImageInformation + numberImageInformation + ".png";
        helpShopViewController.displayImageInformation(filepath);

    }
}
