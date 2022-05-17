package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ShopDao;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.map.MapViewController;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe qui contrôle les fonctionnalités de la map
 * Permet d'afficher une carte avec des magasins créés par l'utilisateur. + créé le plus cours
 * chemin d'un point A à un point B
 */
public class MapController extends Controller implements MapViewController.Listener {

    private MapViewController viewController;
    private boolean readOnlyMode;
    private ShoppingList productListToSearchInShops;
    private final RouteService routeService;
    private final LocatorService locatorService;
    private final ShopDao shopDao;


    public MapController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow, Boolean readOnlyMode){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
        this.readOnlyMode = readOnlyMode;
        Configuration configuration = Configuration.getCurrent();
        shopDao = configuration.getShopDao();
        locatorService = new LocatorService();
        routeService = new RouteService();
    }

    /**
     * Lance l'affichage de la carte
     */
    public void displayMap(){
        launchFXML();
        onInitializeMapShop();
        viewController.start();
    }

    public void displayShopMap( List<Pair<Shop,Integer>> pairList){
        displayMap();
        for(Pair<Shop,Integer> pairShopColor: pairList){
            Shop shop = pairShopColor.getLeft();
            int color = pairShopColor.getRight();
            try {
                String toDisplay = shop.getName() + " : " + shopDao.getShoppingListPriceInShop(shop, productListToSearchInShops) + " €";
                viewController.addCircle(color,toDisplay,shop.getCoordinate(),true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void launchFXML() {
        FXMLLoader loader = this.loadFXML("Map.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
    }

    /**
     * Initialise les magasins sur la carte
     */
    private void onInitializeMapShop() {

        try {
            if(readOnlyMode){
                viewController.initReadOnlyMode();
            }
            else{
                List<Shop> allShopList  = shopDao.getAllShops();
                for(Shop shop: allShopList){
                    viewController.addCircle(MapConstants.COLOR_RED, shop.getName(), shop.getCoordinate(), true);
                }
            }
        } catch (SQLException e) {
            ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Contactez un responsable");
        }
    }

    /**
     * Retour à la page d'accueil
     */
    @Override
    public void onBackButtonClicked() {
        listenerBackPreviousWindow.onReturn();
    }

    /**
     * suppression de l'itinéraire
     * @param itineraryGraphicsCercleList La liste des objets cercle graphique associés à l'itinéraire
     * @param itineraryGraphicsTextList La liste des objets texte graphique associés à l'itinéraire
     */
    @Override
    public void onDeleteItineraryClicked(List<Graphic> itineraryGraphicsCercleList, List<Graphic> itineraryGraphicsTextList) {
        if(!itineraryGraphicsCercleList.isEmpty()){
            routeService.onDeleteItinerary(itineraryGraphicsCercleList, itineraryGraphicsTextList);
        }
    }

    /**
     * Lance l'itinéraire dès qu'un point de départ et d'arrivée se trouve dans itineraryCircleList
     * @param itineraryCircleList la liste des objets graphique present sur l'écran pour l'itinéraire (cercle)
     */
    @Override
    public void onItineraryClicked(  List<Graphic> itineraryCircleList ) {

        if (itineraryCircleList.size() == MapConstants.READY_TO_CALCUL_ROUTE) {
            boolean isRouteFound = routeService.calculateRoute(itineraryCircleList);
            if(!isRouteFound) ViewController.showAlert(Alert.AlertType.ERROR, "Error", "Itinéraire impossible");
            else{
                viewController.itineraryInformation(routeService.getTotalTime(),
                        routeService.getTotalTimeBike(),
                        routeService.getTotalLength());
            }
        }
    }

    /**
     * Lance la page d'aide
     */
    @Override
    public void helpMapClicked() {
        HelpController helpController = new HelpController("helpMap/", MapConstants.LAST_NUMBER_IMAGE_HELP_PAGE);
        helpController.displayHelpShop();
    }

    @Override
    public void logout() {
        userLogout();
    }

    public void setProductListToSearchInShops(ShoppingList productListToSearchInShops) {
        this.productListToSearchInShops = productListToSearchInShops;
    }

    /**
     * Recherche avec l'adresse d'un magasin
     * @param address l'adresse du magasin (Ville, rue, commune, numéro)
     * @return boolean est ce que l'adresse a été trouvé ou non
     */
    @Override
    public boolean onSearchAddress(String address, List<Graphic> addressGraphicsOverlay) {
        if(address.isBlank()) return false;

        Point addressPosition = performGeocode(address, addressGraphicsOverlay);

        if(readOnlyMode && addressPosition != null){
            try {
                List<Shop> nearestShopWithProductList =  shopDao.getNearestShopsWithProductList(productListToSearchInShops,addressPosition);
                for(Shop shop: nearestShopWithProductList){
                    String toDisplay = shop.getName() + ": " + shopDao.getShoppingListPriceInShop(shop, productListToSearchInShops) + " €";
                    viewController.addCircle(MapConstants.COLOR_BLUE, toDisplay, shop.getCoordinate(), true);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (addressPosition != null){
            viewController.setViewPointCenter(addressPosition);
        }
        return addressPosition != null;
    }



    /**
     * Cherche un magasin parmi les magasins enregistrer dans la carte
     * @param shopName le nom du magasin
     */
    @Override
    public void onSearchShop(String shopName, List<Graphic> mapTextGraphics, List<Graphic> mapCercleGraphics) {
        boolean isVisible;
        for(int index = 0; index < mapCercleGraphics.size(); index++){
            Graphic textGraphic = mapTextGraphics.get(index);
            Graphic cercleGraphic = mapCercleGraphics.get(index);
            TextSymbol textSymbol = (TextSymbol) textGraphic.getSymbol();

            isVisible = textSymbol.getText().contains(shopName) ;
            textGraphic.setVisible(isVisible);
            cercleGraphic.setVisible(isVisible);
        }
    }



    /**
     * Recherche les coordonnées et les infos complètes qui correspond le mieux à l'adresse
     * et affiche le résultat sur la map
     *
     * @param address une vraie adresse ex : Avenue Franklin Roosevelt 50 - 1050 Bruxelles
     */
    private Point performGeocode(String address, List<Graphic> addressGraphicsOverlay) {
        Point addressPosition = locatorService.performGeocode(address,addressGraphicsOverlay);
        if( addressPosition == null){
            ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Adresse non valide");
        }
        return addressPosition;

    }

}