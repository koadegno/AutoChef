package ulb.infof307.g01.controller.shoppingList;

import javafx.scene.control.Alert;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.controller.mail.MailController;
import ulb.infof307.g01.controller.menu.UserMenusController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ShoppingListDao;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shoppingList.ModifyShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.ShoppingListViewController;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

/**
 * Contrôleur de la page de Modification de la liste de course
 */
public class ModifyShoppingListController extends ShoppingListController {

    public ModifyShoppingListController(ListenerBackPreviousWindow listenerBackPreviousWindow) {
        super(listenerBackPreviousWindow);
        displayModifyShoppingList();
    }

    public void displayModifyShoppingList(){
        modifyShoppingListViewController = new ModifyShoppingListViewController();
        modifyShoppingListViewController.setListener(this);
        loadFXML(modifyShoppingListViewController, "ShoppingList.fxml");
        initInformationShoppingList(false);
        modifyShoppingListViewController.isVisibleElementToModifyMyShoppingList(false);


    }

    /**
     * Affiche le contenu d'une liste de courses
     * @param nameUserShoppingList le nom d'une liste de courses
     */
    @Override
    public void seeUserShoppingList(Object nameUserShoppingList){
        if(Objects.equals(nameUserShoppingList, null)){ //nom est null
            modifyShoppingListViewController.isVisibleElementToModifyMyShoppingList(false);
        }
        else{
            String currentShoppingListName = (String) nameUserShoppingList;
            try {
                // afficher les produits de la liste de course dans la table
                ShoppingList shoppingList = shoppingListDao.get(currentShoppingListName);
                Vector<Product> productOfShoppingList =  new Vector<>(shoppingList);
                modifyShoppingListViewController.addProductListToTableView(productOfShoppingList);
                modifyShoppingListViewController.isVisibleElementToModifyMyShoppingList(true);
                modifyShoppingListViewController.setCurrentShoppingListName(currentShoppingListName);
            } catch (SQLException e) {
                ViewController.showErrorSQL();

            }
        }
    }

    /**
     * Enregistre le contenu d'une liste de courses qui a été modifier
     * @param currentShoppingListName nom d'une liste de courses
     */
    public void confirmUserModifyShoppingList(String currentShoppingListName){
        try {
            //Recupere liste de courses chez la bdd
            ShoppingList shoppingListInDataBase = shoppingListDao.get(currentShoppingListName);
            this.shoppingListToSend = new ShoppingList(shoppingListInDataBase.getName(), shoppingListInDataBase.getId());

            //Renvoie liste de courses chez la bdd
            modifyShoppingListViewController.fillShoppingListToSend();
            shoppingListDao.update(shoppingListToSend);

            //Popup : confirmer que la liste de courses est enregistrée
            displayPopupMessageInformation();


        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    /**
     * Afficher une popup qui permet à l'utilisateur de savoir que sa liste de course a bien été modifiée
     */
    public void displayPopupMessageInformation(){
        //Refresh si la page liste de courses n'existe plus
        if(shoppingListToSend.isEmpty()){
            this.refreshModifyShoppingList();
            this.showErrorDeleteShoppingList();
        }
        else{
            this.showMessageCreateShoppingList();
        }
    }

    public void showMessageCreateShoppingList(){
        String message = "La liste de course a été enregistrée";
        ShoppingListViewController.showAlert(Alert.AlertType.INFORMATION, "Message", message);
    }


    public void showErrorDeleteShoppingList(){
        String messageError = "Vous avez enregistré une liste de course vide.\n" +
                " Elle est donc supprimée. ";
        ShoppingListViewController.showAlert(Alert.AlertType.ERROR, "Erreur", messageError);

    }

    /**
     * Permet de refresh la fenêtre pour modifier une liste de courses si celle ci a été supprimée
     */
    public void refreshModifyShoppingList(){
        modifyShoppingListViewController.isVisibleElementToModifyMyShoppingList(false);
        this.initInformationShoppingList(false);
    }

    public void exportShoppingList(String currentShoppingListName){
        new ExportShoppingListController(currentShoppingListName);
    }

    public void sendShoppingListByMail(String currentShoppingListName){
        ShoppingList shoppingList = null;
        try {
            shoppingList = shoppingListDao.get(currentShoppingListName);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
        new MailController(shoppingList);

    }

    public void helpModifyShoppingList(){
    int numberOfImageHelp = 9;
    String directory = "helpModifyShoppingList/";

    HelpController helpController = new HelpController(directory, numberOfImageHelp);
        helpController.displayHelpShop();
    }
}
