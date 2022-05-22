package ulb.infof307.g01.controller.shoppingList;

import javafx.scene.control.Alert;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.map.MapConstants;
import ulb.infof307.g01.controller.map.MapController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.dao.ProductDao;
import ulb.infof307.g01.model.database.dao.ProductUnityDao;
import ulb.infof307.g01.model.database.dao.ShopDao;
import ulb.infof307.g01.model.database.dao.ShoppingListDao;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shoppingList.CreateShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.ModifyShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.ShoppingListViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contrôleur abstrait de la liste de course
 */
public abstract class ShoppingListController extends Controller implements ShoppingListViewController.Listener {
    protected ModifyShoppingListViewController modifyShoppingListViewController;
    protected CreateShoppingListViewController createShoppingListViewController;
    protected ShoppingList shoppingListToSend;
    private final ShopDao shopDao;
    private final boolean isProfessional;
    private final ProductDao productDao;
    private final ProductUnityDao productUnityDao;
    protected final ShoppingListDao shoppingListDao;


    //-------------------------CONSTRUCTEUR

    public ShoppingListController(ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        this.shopDao = configuration.getShopDao();
        User currentUser = configuration.getCurrentUser();
        isProfessional = currentUser.isProfessional();
        productDao = configuration.getProductDao();
        productUnityDao = configuration.getProductUnityDao();
        shoppingListDao = configuration.getShoppingListDao();



    }

    //Methode Listener de ShoppingListController

    /**
     * Permet de rajouter les informations d'un produit dans le TableView
     * @param shoppingListViewController instance de la classe ShoppingListViewController
     * @param nameProductChoose nom du produit
     * @param quantityOrNumberChoose la quantité du produit
     * @param nameUnityChoose l'unité du produit
     */
    public void addElementOfList(ShoppingListViewController shoppingListViewController, Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose){
        shoppingListViewController.removeBorderColor();
        shoppingListViewController.showAddProductError(false);

        if(canAddQuantityOrNumberProduct(quantityOrNumberChoose)){
            Product userProduct;

            if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
                //Cree le produit pour le mettre dans le tableView
                userProduct = new Product.ProductBuilder().withName(nameProductChoose.toString()).withQuantity(quantityOrNumberChoose).withNameUnity(nameUnityChoose.toString()).build();

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
        boolean canAddProduct = true;
        if(!isProfessional){
            int maxQuantityToNotProfessional = 100;
            if(quantityOrNumberChoose > maxQuantityToNotProfessional){
                canAddProduct = false;
                this.showErrorQuantityProduct(maxQuantityToNotProfessional);
            }
        }
        return canAddProduct;
    }

    /**
     * Affiche une erreur quand la quantité choisie par l'utilisateur non professionnel n'est pas permise
     * @param maxQuantityToNotProfessional le nombre max de quantité possible
     */
    public void showErrorQuantityProduct(int maxQuantityToNotProfessional){
        String message = "Il ne vous ait pas possible de rajouter plus de " + maxQuantityToNotProfessional + " quantité \n " +
                "si vous n'êtes pas un professionnel.";
        ShoppingListViewController.showAlert(Alert.AlertType.ERROR, "Erreur", message);
    }

    /**
     * Met les informations (noms des produits, noms des unités) pour pouvoir créer/modifier une liste de courses
     * @param isCreateUserShoppingListController VRAI si c'est une instance de la classe CreateShoppingListController sinon c'est ModifyShoppingListController
     */
    public void initInformationShoppingList(boolean isCreateUserShoppingListController){
        try {
            List<String> allProduct = productDao.getAllName();
            List<String> allUnitName = productUnityDao.getAllName();
            String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"}; //supprime les unités pour une recette
            allUnitName.removeAll(List.of(unitToRemove));
            List<String> allShoppingListName = shoppingListDao.getAllName();

            if(isCreateUserShoppingListController) createShoppingListViewController.initComboBox(allProduct, allUnitName);
            else modifyShoppingListViewController.initComboBox(allProduct, allUnitName, allShoppingListName);

        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    public void returnToUserMenu(){
        listenerBackPreviousWindow.onReturn();
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

    @Override
    public void viewShoppingListOnMap(Object nameUserShoppingList){
        if (!Objects.equals(nameUserShoppingList, null)) { //Initialiser la MAP
            String currentShoppingListName = (String) nameUserShoppingList;
            try {
                ShoppingList shoppingList = shoppingListDao.get(currentShoppingListName);
                Boolean readOnlyMode = true;
                MapController mapController = new MapController(listenerBackPreviousWindow,readOnlyMode );
                mapController.setProductListToSearchInShops(shoppingList);
                mapController.displayShopMap(shopWithProductList(shoppingList));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Pair<Shop,Integer>> shopWithProductList(ShoppingList shoppingList) throws SQLException {
        List<Shop> shopListWithProducts = shopDao.getShopWithProductList(shoppingList);
        List<Shop> shopWithMinPriceForProductList =  shopDao.getShopWithMinPriceForProductList(shoppingList);
        List<Pair<Shop,Integer>> pairShopColor = new ArrayList<>();
        for(Shop shop: shopListWithProducts){
            int color = MapConstants.COLOR_BLACK;
            if(shopWithMinPriceForProductList.contains(shop)) color = MapConstants.COLOR_RED;
            pairShopColor.add(new Pair<>(shop,color));
        }
        return pairShopColor;
    }

    //Fin Methode Listener de ShoppingListViewController


    //------------------------Methode Listener de CreateShoppingListViewController : implementer dans CreateShoppingListController

    public void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){}

    @Override
    public void helpCreateShoppingList() {}

    // Fin Methode Listener de CreateShoppingListViewController

    //Methode Listener de ModifyShoppingListViewController : implementer dans ModifyShoppingListController

    @Override
    public void helpModifyShoppingList(){}

    @Override
    public void exportShoppingList(String currentShoppingListName) {}

    @Override
    public void sendShoppingListByMail(String currentShoppingListName) {}

    @Override
    public void seeUserShoppingList(Object nameUserShoppingList) {}

    @Override
    public void confirmUserModifyShoppingList(String currentShoppingListName) {}

    //Fin Listener de ModifyShoppingListViewController

    //Methode Listener pour Recipe : implémenter dans RecipeShoppingListController
    @Override
    public void returnAddedProducts() {}
    @Override
    public void cancelRecipeCreation() {}
    // Fin pour recipe
}
