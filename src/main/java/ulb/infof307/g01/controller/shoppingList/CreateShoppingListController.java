package ulb.infof307.g01.controller.shoppingList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

public class CreateShoppingListController extends ShoppingListController {


    public CreateShoppingListController(CreateUserShoppingListViewController createUserShoppingListViewController, ListenerBackPreviousWindow listenerBackPreviousWindow) {
        super(createUserShoppingListViewController, listenerBackPreviousWindow);
    }

    public CreateShoppingListController(CreateUserShoppingListViewController createUserShoppingListViewController) {
        super(createUserShoppingListViewController, null);
    }

    public void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){
        createUserShoppingListViewController.removeBorderColor();

        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            createUserShoppingListViewController.showNameUserCreateShoppingListError();
        }
        else if(sizeTableViewDisplayProductList == 0){ // table view est vide
            createUserShoppingListViewController.showIsEmptyTableViewError();
        }
        else {
            this.shoppingListToSend = new ShoppingList(shoppingListName);
            createUserShoppingListViewController.fillShoppingListToSend();
            try {
                Configuration.getCurrent().getShoppingListDao().insert(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                createUserShoppingListViewController.showNameUserCreateShoppingListError();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // else tout ce passe bien
            createUserShoppingListViewController.returnToMenu.fire();
        }
    }

    public void fillProductTable(ShoppingList shoppingList){
        createUserShoppingListViewController.clearProductTableView();
        Vector<Product> temp = new Vector<>(shoppingList);
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        createUserShoppingListViewController.setProductTableView(data);
        //Retour menu precedent : MainShoppingList
        createUserShoppingListViewController.setReturnButtonAction();

    }

}
