package ulb.infof307.g01.view.shoppingList;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.model.ODTCreator;
import ulb.infof307.g01.model.PDFCreator;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.Window;


public class ExportShoppingListView extends Window {

    public Label nameShoppingList;
    public VBox vBox;
    public ShoppingList shoppingList = null;

    public void exportToPDF(){
        PDFCreator pdfCreator = new PDFCreator();
        pdfCreator.createPDF(shoppingList);
        closePopup();
    }

    public void exportToODT(){
        ODTCreator odtCreator = new ODTCreator();
        try {
            odtCreator.createODT(shoppingList);
        } catch (Exception e) {
            e.printStackTrace();
            Window.showAlert(Alert.AlertType.ERROR,"Erreur","Une erreur c'est produit avec le fichier ODT, contacté le service d'assistance.");
        }
        closePopup();
    }

    public void setShoppingList(ShoppingList _shoppingList){
        this.shoppingList = _shoppingList;
        nameShoppingList.setText(shoppingList.getName());
    }

    private void closePopup(){
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }
}
