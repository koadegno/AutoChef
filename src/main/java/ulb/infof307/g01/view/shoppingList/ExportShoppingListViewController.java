package ulb.infof307.g01.view.shoppingList;

import javafx.scene.control.Label;
import ulb.infof307.g01.view.ViewController;

/**
 * La classe gère la vue pour l'export d'une liste de courses en PDF ou en ODT
 */

public class ExportShoppingListViewController extends ViewController<ExportShoppingListViewController.Listener> {

    public Label nameShoppingList;

    public void exportToPDF(){
        listener.exportToPDF();
    }

    public void exportToODT(){
        listener.exportToODT();
    }

    public interface Listener{
        void displayExportShoppingList();
        void exportToPDF();
        void exportToODT();
    }
}
