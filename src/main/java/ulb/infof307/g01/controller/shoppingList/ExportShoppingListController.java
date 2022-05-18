package ulb.infof307.g01.controller.shoppingList;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.exception.PDFException;
import ulb.infof307.g01.model.export.ODTCreator;
import ulb.infof307.g01.model.export.PDFCreator;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shoppingList.ExportShoppingListViewController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Contrôleur pour la fenêtre d'exportation de la liste de course
 */
public class ExportShoppingListController extends Controller implements ExportShoppingListViewController.Listener {
    private ExportShoppingListViewController exportShoppingListViewController;
    private Stage popupExportShoppingList;
    private ShoppingList shoppingList;

    /**
     * Constructeur qui fait appel à la base de donnée pour recuperer la liste de courses
     * @param currentShoppingListName nom de la liste de courses
     */
    public ExportShoppingListController(String currentShoppingListName){
        this.exportShoppingListViewController = new ExportShoppingListViewController();
        exportShoppingListViewController.setListener(this);

        try {
            this.shoppingList =  Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }

        this.displayExportShoppingList();
    }

    /**
     * Affiche la popup pour pouvoir exporter en PDF ou ODT une liste de courses
     */
    public void displayExportShoppingList(){
        String filename = "ExportShoppingList.fxml";
        try {
            this.popupExportShoppingList = popupFXML(filename, exportShoppingListViewController);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(filename);
        }
    }

    /**
     * Exporter la liste de courses en PDF puis fermer la popup
     */
    public void exportToPDF(){
        PDFCreator pdfCreator = new PDFCreator();
        try {
            pdfCreator.createPDF(shoppingList);
        } catch (PDFException e) {
            ViewController.showAlert(Alert.AlertType.ERROR, "ERREUR", "Une erreur s'est produite lors de l'exportation en PDF.");
        }
        popupExportShoppingList.close();
    }

    /**
     * Exporter la liste de courses en ODT puis fermer la popup
     */
    public void exportToODT(){
        ODTCreator odtCreator = new ODTCreator();
        try {
            odtCreator.createODT(shoppingList);
        } catch (Exception e) {
            ViewController.showAlert(Alert.AlertType.ERROR,"Erreur","Une erreur c'est produit avec le fichier ODT, contacté le service d'assistance.");
        }
        popupExportShoppingList.close();
    }
}
