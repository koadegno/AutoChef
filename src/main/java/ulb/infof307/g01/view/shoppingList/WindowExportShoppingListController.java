package ulb.infof307.g01.view.shoppingList;

import javafx.scene.control.Label;
import ulb.infof307.g01.view.ViewController;


public class WindowExportShoppingListController extends ViewController<WindowExportShoppingListController.Listener> {

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
