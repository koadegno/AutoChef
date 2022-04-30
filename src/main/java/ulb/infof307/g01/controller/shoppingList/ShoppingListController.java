package ulb.infof307.g01.controller.shoppingList;

import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.ShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingListController extends Controller implements ShoppingListViewController.Listener {
    protected UserShoppingListViewController userShoppingListViewController;
    protected CreateUserShoppingListViewController createUserShoppingListViewController;
    protected ShoppingList shoppingListToSend;

    //-------------------------CONSTRUCTEUR

    public ShoppingListController(CreateUserShoppingListViewController createUserShoppingListViewController, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        this.createUserShoppingListViewController = createUserShoppingListViewController;
        this.createUserShoppingListViewController.setListener(this);
    }

    public ShoppingListController(UserShoppingListViewController userShoppingListViewController, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        this.userShoppingListViewController = userShoppingListViewController;
        this.userShoppingListViewController.setListener(this);
    }

    //Methode Listener de ShoppingListController

    public void addElementOfList(ShoppingListViewController shoppingListViewController, Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose){
        shoppingListViewController.removeBorderColor();
        shoppingListViewController.showAddProductError(false);

        if(canAddQuantityOrNumberProduct(quantityOrNumberChoose)){
            Product userProduct;

            if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
                //Cree le produit pour le mettre dans le tableView
                userProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());

                shoppingListViewController.addProductToTableView(userProduct);
                shoppingListViewController.clearElementAddProduct();
            } else {
                shoppingListViewController.showAddProductError(true);
            }
        }

    }

    /**
     * Permet de savoir si l'utilisateur peut rajouter un grand nombre de quantité d'un produit
     * @param quantityOrNumberChoose la quantité du produit
     * @return un boolean à Vrai si l'utilisateur peut rajouter le produit avec la quantité choisi
     */
    private boolean canAddQuantityOrNumberProduct(int quantityOrNumberChoose){
        boolean isProfessional = Configuration.getCurrent().getCurrentUser().isProfessional();
        boolean canAddProduct = true;
        if(!isProfessional){
            int maxQuantityToNotProfessional = 100;
            if(quantityOrNumberChoose > maxQuantityToNotProfessional){
                canAddProduct = false;
                ErrorShoppingList.showErrorQuantityProduct(maxQuantityToNotProfessional);
            }
        }
        return canAddProduct;
    }

    @Override
    public void helpShoppingList(boolean isCreateShoppingList) {
        int numberOfImageHelp;
        String directory;
        if(isCreateShoppingList){
            numberOfImageHelp = 8;
            directory = "helpCreateShoppingList/";
        }
        else{
            numberOfImageHelp = 12;
            directory = "helpUserShoppingList/";
        }
        HelpController helpController = new HelpController(directory, numberOfImageHelp);
        helpController.displayHelpShop();
    }

    @Override
    public void returnHomeShoppingList() {
        listenerBackPreviousWindow.onReturn();
    }

    public void addProductToShoppingListToSend(Product product){
        shoppingListToSend.add(product);
    }

    @Override
    public void logout(){userLogout();}

    //Fin Methode Listener de ShoppingListViewController


    //------------------------Methode Listener de CreateUserShoppingListViewController : implementer dans CreateShoppingListController

    public void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){}

    // Fin Methode Listener de CreateUserShoppingListViewController

    //Methode Listener de ModifyShoppingListViewController : implementer dans ModifyShoppingListController

    public void initInformationShoppingList(boolean isCreateUserShoppingListController){
        try {
            ArrayList<String> allProduct = Configuration.getCurrent().getProductDao().getAllName();
            ArrayList<String> allUnitName = Configuration.getCurrent().getProductUnityDao().getAllName();
            String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
            allUnitName.removeAll(List.of(unitToRemove));
            List<String> allShoppingListName = Configuration.getCurrent().getShoppingListDao().getAllName();

            if(isCreateUserShoppingListController) createUserShoppingListViewController.initComboBox(allProduct, allUnitName);
            else userShoppingListViewController.initComboBox(allProduct, allUnitName, allShoppingListName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportShoppingList(String currentShoppingListName) {}

    @Override
    public void sendShoppingListByMail(String currentShoppingListName) {}

    @Override
    public void seeUserShoppingList(Object nameUserShoppingList) {}

    @Override
    public void confirmUserModifyShoppingList(String currentShoppingListName) {}

    @Override
    public void returnToUserMenu() {}

    //Fin Listener de ModifyShoppingListViewController

    //Methode Listener pour Recipe : implémenter dans RecipeShoppingListController
    @Override
    public void returnAddedProducts() {}
    @Override
    public void cancelRecipeCreation() {}
    // Fin pour recipe
}
