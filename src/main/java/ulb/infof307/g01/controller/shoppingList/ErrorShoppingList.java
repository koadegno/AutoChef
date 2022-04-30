package ulb.infof307.g01.controller.shoppingList;

import javafx.scene.control.Alert;
import ulb.infof307.g01.view.shoppingList.ShoppingListViewController;

public class ErrorShoppingList {

    public static void showMessageCreateShoppingList(){
        String message = "La liste de course a été enregistrée";
        ShoppingListViewController.showAlert(Alert.AlertType.INFORMATION, "Message", message);
    }

    public static void showErrorQuantityProduct(int maxQuantityToNotProfessional){
        String message = "Il ne vous ait pas possible de rajouter plus de " + maxQuantityToNotProfessional + " quantité \n " +
                "si vous n'êtes pas un professionnel.";
        ShoppingListViewController.showAlert(Alert.AlertType.ERROR, "Erreur", message);
    }

    public static void showErrorDeleteShoppingList(){
        String messageError = "Vous avez enregistré une liste de course vide.\n" +
                " Elle est donc supprimée. ";
        ShoppingListViewController.showAlert(Alert.AlertType.ERROR, "Erreur", messageError);

    }
}
