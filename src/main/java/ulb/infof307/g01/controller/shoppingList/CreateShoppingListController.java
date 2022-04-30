package ulb.infof307.g01.controller.shoppingList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.shoppingList.CreateShoppingListViewController;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

public class CreateShoppingListController extends ShoppingListController {


    public CreateShoppingListController(CreateShoppingListViewController createShoppingListViewController, ListenerBackPreviousWindow listenerBackPreviousWindow) {
        super(createShoppingListViewController, listenerBackPreviousWindow);
    }

    public CreateShoppingListController(CreateShoppingListViewController createShoppingListViewController) {
        super(createShoppingListViewController, null);
    }

    /**
     * Crée une nouvelle liste de courses
     * @param shoppingListName le nom de la liste de course crée
     * @param sizeTableViewDisplayProductList la taille du tableView qui contient les informations des produits
     */
    public void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){
        createShoppingListViewController.removeBorderColor();

        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            createShoppingListViewController.showNameUserCreateShoppingListError();
        }
        else if(sizeTableViewDisplayProductList == 0){ // table view est vide
            createShoppingListViewController.showIsEmptyTableViewError();
        }
        else {
            this.shoppingListToSend = new ShoppingList(shoppingListName);
            createShoppingListViewController.fillShoppingListToSend();
            try {
                Configuration.getCurrent().getShoppingListDao().insert(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                createShoppingListViewController.showNameUserCreateShoppingListError();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // else tout ce passe bien
            createShoppingListViewController.returnToMenu.fire();
        }
    }

    /**
     * Remplis le tableView d'une liste de course existant
     * @param shoppingList liste de courses
     */
    public void fillProductTable(ShoppingList shoppingList){
        createShoppingListViewController.clearProductTableView();
        Vector<Product> temp = new Vector<>(shoppingList);
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        createShoppingListViewController.setProductTableView(data);
        //Retour menu precedent : MainShoppingList
        createShoppingListViewController.setReturnButtonAction();

    }

}
