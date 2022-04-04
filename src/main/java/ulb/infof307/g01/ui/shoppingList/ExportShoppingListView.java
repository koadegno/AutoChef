package ulb.infof307.g01.ui.shoppingList;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.ui.Window;


public class ExportShoppingListView extends Window {

    public Label nameShoppingList;
    public VBox vBox;
    public ShoppingList shoppingList = null;

    public void exportToPDF(){
        //TODO:attendre que Salma ait push
        closePopup();
    }

    public void exportToODT(){
        //TODO: attendre que Elsbeth ait fini
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
