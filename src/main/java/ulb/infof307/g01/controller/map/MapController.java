package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.controller.shop.ShopController;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ShopDao;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.map.MapViewController;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Classe qui contrôle les fonctionnalités de la map
 * Permet d'afficher une carte avec des magasins créés par l'utilisateur. + créé le plus cours
 * chemin d'un point A à un point B
 */
public class MapController extends Controller implements MapViewController.Listener, RouteService.Listener, MapShop.MapShopListener {

    private MapViewController viewController;

    private boolean isOnItineraryMode;

    private boolean readOnlyMode;
    private ShoppingList productListToSearchInShops ;

    private RouteService routeService;
    private LocatorService locatorService;
    private ShopDao shopDao;
    private MapShop mapShop;

    @Override
    public void setOnItineraryMode(boolean onItineraryMode) {
        isOnItineraryMode = onItineraryMode;
    }

    public MapController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow, Boolean readOnlyMode){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
        isOnItineraryMode = false;
        this.readOnlyMode = readOnlyMode;
        Configuration configuration = Configuration.getCurrent();
        shopDao = configuration.getShopDao();
        locatorService = new LocatorService();
    }

    /**
     * Lance l'affichage de la carte
     */
    public void displayMap(){
        FXMLLoader loader = this.loadFXML("Map.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        mapShop = new MapShop(this);
        onInitializeMapShop();
        viewController.start();
        routeService = new RouteService(viewController,this);
    }

    public void setProductListToSearchInShops(ShoppingList productListToSearchInShops){
        mapShop.setShoppingList(productListToSearchInShops);
    }

    /**
     * Ajout d'un point avec son texte sur la map
     *
     * @param color        Couleur du cercle
     * @param textCircle   Texte écrit à côté du cercle
     * @param coordinate   Coordonnée du cercle
     * @param isShop       Vrai si l'élément à ajouter est un magasin
     */
    @Override
    public void addCircle(int color, String textCircle, Point coordinate, Boolean isShop) {

        //crée un cercle
        SimpleMarkerSymbol circleSymbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                color,
                MapConstants.SIZE);
        Graphic circlePoint = new Graphic(coordinate, circleSymbol);

        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        MapConstants.SIZE, textCircle, MapConstants.COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        Graphic textPoint   = new Graphic(coordinate, pierTextSymbol);
        // rajoute les cercles créés au bon overlay

        if (isShop) {
            viewController.addShopGraphics(circlePoint,textPoint);
        }
        else {
            viewController.addItineraryGraphics(circlePoint,textPoint);
        }
    }

    /**
     * Initialise les magasins sur la carte
     */
    public void onInitializeMapShop() {

        try {
            if(readOnlyMode){
                viewController.initReadOnlyMode();
                mapShop.displayShopsWithProductList();
            }
            else{
                mapShop.initAllShops();
            }
        } catch (SQLException e) {
            ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Contactez un responsable");
        }
    }

    /**
     * récupère la coordonnée géographique associé au curseur
     * @param mapView la mapView contient la fonction de calcul
     * @return une coordonnée géographique correspondant a la position du curseur et la position de la map
     */
    private Point cursorPoint(MapView mapView, Double cursorX, Double cursorY) {
        Point2D cursorPoint2D = new Point2D(cursorX, cursorY);
        return mapView.screenToLocation(cursorPoint2D);
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
        routeService.onDeleteItinerary(itineraryGraphicsCercleList, itineraryGraphicsTextList);
    }

    /**
     * Lance l'itinéraire quand la position de départ et d'arrivée sont disponibles
     * @param posX position du curseur en X
     * @param posY position du curseur en Y
     * @param itineraryCircleList la liste des objets graphique present sur l'écran pour l'itinéraire (cercle)
     * @param itineraryTextList la liste des objets graphique present sur l'écran pour l'itinéraire (texte)
     * @param isDeparture Vrai si on place le point associer au depart
     */
    @Override
    public void onItineraryClicked(Double posX, Double posY, MapView mapView, List<Graphic> itineraryCircleList, List<Graphic> itineraryTextList, boolean isDeparture) {
        Pair<Graphic, Graphic> selectedShop= getSelectedShop();
        if(selectedShop != null){
            int readyToCalculRoute = 2;

            routeService.itinerary(selectedShop,cursorPoint(mapView,posX,posY), itineraryCircleList,itineraryTextList,isDeparture );
            viewController.switchVisibilityContextMenu();

            if (itineraryCircleList.size() == readyToCalculRoute) {
                boolean isRouteFound = routeService.calculateRoute(itineraryCircleList);
                System.out.println("la route a ete trouvé : " + isRouteFound);
                if(! isRouteFound) ViewController.showAlert(Alert.AlertType.ERROR, "Error", "Itinéraire impossible");
                else{
                    viewController.itineraryInformation(routeService.getTotalTime(),
                            routeService.getTotalTimeBike(),
                            routeService.getTotalLength());
                }
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
                    addCircle(MapConstants.COLOR_BLUE, toDisplay, shop.getCoordinate(), true);

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
     * Supprime le point sélectionné de l'overlay
     */
    @Override
    public void onDeleteShopClicked() {
        Pair<Graphic, Graphic> shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;
        mapShop.setSelectedShop(shopOverlay.getLeft(),shopOverlay.getRight());
        if(!isOnItineraryMode){

            ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
            if (alertResult == ButtonType.OK ) {
                Graphic circlePointOnMap = shopOverlay.getLeft();
                Graphic textPointOnMap = shopOverlay.getRight();
                try {
                    mapShop.deleteShop();
                    viewController.removeShopGraphics(circlePointOnMap,textPointOnMap);
                } catch (SQLException e) {
                    ViewController.showErrorSQL();
                }
            }
        }
        else{
            ViewController.showAlert(Alert.AlertType.INFORMATION, "Tu ne peux pas supprimer un magasin.\nSupprime d'abord l'itinéraire", "");
        }
    }

    /**
     * Cherche le magasin sélectionné par l'utilisateur et
     * renvoie la paire d'objet graphique associé aux coordonnées cliquer
     * @return paire d'objet graphique
     */
    private Pair<Graphic, Graphic> getSelectedShop(){
        List<Graphic> shopGraphicsCircleList = viewController.getShopGraphicsCircleList();//TODO changer ca
        List<Graphic> shopGraphicsTextList = viewController.getShopGraphicsTextList();

        for (int i = 0; i < shopGraphicsCircleList.size(); i++) {
            Graphic circlePointOnMap = shopGraphicsCircleList.get(i);
            Graphic textPointOnMap = shopGraphicsTextList.get(i); // le symbole texte associer au point aussi

            if (circlePointOnMap.isSelected()) {
                return new Pair<>(circlePointOnMap, textPointOnMap);
            }
        }
        return null;
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
     * Met en evidence un point sur la carte
     *
     * @param mouseX La position en X de la souris
     * @param mouseY La position en Y de la souris
     */
    @Override
    public void highlightGraphicPoint(double mouseX, double mouseY, MapView mapView, GraphicsOverlay shopGraphicOverlay) {
        Point2D mapViewPoint = new Point2D(mouseX, mouseY);
        ListenableFuture<IdentifyGraphicsOverlayResult> graphicsOverlayAsyncIdentified = mapView.identifyGraphicsOverlayAsync(
                shopGraphicOverlay,
                mapViewPoint, MapConstants.SIZE, false, 1);

        graphicsOverlayAsyncIdentified.addDoneListener(() -> {
            try {
                // récupère la liste d'objet graphic retournée par graphicsOverlayAsyncIdentified
                List<Graphic> identifiedGraphics = graphicsOverlayAsyncIdentified.get().getGraphics();
                if (!identifiedGraphics.isEmpty()) {
                    identifiedGraphics.get(0).setSelected(true);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Contactez un responsable");
            }
        });
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