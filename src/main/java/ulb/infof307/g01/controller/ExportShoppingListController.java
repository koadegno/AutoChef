package ulb.infof307.g01.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ulb.infof307.g01.model.ODTCreator;
import ulb.infof307.g01.model.PDFCreator;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.shoppingList.WindowExportShoppingListController;

import java.io.IOException;
import java.sql.SQLException;

public class ExportShoppingListController extends Controller implements WindowExportShoppingListController.Listener {
    private WindowExportShoppingListController windowExportShoppingListController;
    private Stage popupExportShoppingList;
    private ShoppingList shoppingList;

    public ExportShoppingListController(String currentShoppingListName){
        this.windowExportShoppingListController = new WindowExportShoppingListController();
        windowExportShoppingListController.setListener(this);
        try {
            this.shoppingList =  Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
        displayExportShoppingList(currentShoppingListName);
    }

    public void displayExportShoppingList(String currentShoppingListName){
        try {
            this.popupExportShoppingList = popupFXML("exportShoppingList.fxml", windowExportShoppingListController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportToPDF(){
        PDFCreator pdfCreator = new PDFCreator();
        pdfCreator.createPDF(shoppingList);
        popupExportShoppingList.close();
    }

    public void exportToODT(){
        ODTCreator odtCreator = new ODTCreator();
        try {
            odtCreator.createODT(shoppingList);
        } catch (Exception e) {
            e.printStackTrace();
            Window.showAlert(Alert.AlertType.ERROR,"Erreur","Une erreur c'est produit avec le fichier ODT, contacté le service d'assistance.");
        }
        popupExportShoppingList.close();
    }
}
