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
public class MapController extends Controller implements MapViewController.Listener, ShopController.ShopListener, RouteService.Listener {

    public static final int COLOR_RED = 0xFFFF0000;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_BLUE   = 0xFF008DFF;
    public static final int ADDRESS_SIZE = 18;
    public static final float ADDRESS_MARKER_SIZE = 12.0f;
    public static final int SIZE = 10;
    public static final int LAST_NUMBER_IMAGE_HELP_PAGE = 12;
    public static final int WIDTH = 4;

    private MapViewController viewController;

    private boolean isOnItineraryMode;

    private boolean readOnlyMode;
    private ShoppingList productListToSearchInShops ;

    private RouteService routeService;
    private LocatorService locatorService;

    @Override
    public void setOnItineraryMode(boolean onItineraryMode) {
        isOnItineraryMode = onItineraryMode;
    }

    public MapController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow, Boolean readOnlyMode){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
        isOnItineraryMode = false;
        this.readOnlyMode = readOnlyMode;
    }

    /**
     * Lance l'affichage de la carte
     */
    public void displayMap(){
        FXMLLoader loader = this.loadFXML("Map.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        try {
            onInitializeMapShop();
            viewController.start();
            routeService = new RouteService(viewController,this);
            locatorService = new LocatorService(viewController);
        } catch (SQLException e) {
            ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Contactez un responsable");

        }
    }

    public void setProductListToSearchInShops(ShoppingList productListToSearchInShops){
        this.productListToSearchInShops = productListToSearchInShops;
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
                SIZE);

        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        SIZE, textCircle, COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        Graphic circlePoint = new Graphic(coordinate, circleSymbol);
        Graphic textPoint   = new Graphic(coordinate, pierTextSymbol);
        // rajoute les cercles créés au bon overlay
        System.out.println(coordinate);

        if (isShop) {
            viewController.addShopGraphics(circlePoint,textPoint);
        }

        else {
            viewController.addItineraryGraphics(circlePoint,textPoint);
        }
    }


    /**
     * Initialise les magasins sur la carte
     * @throws SQLException erreur au niveau de la base de donnée
     */
    public void onInitializeMapShop() throws SQLException {

        if(readOnlyMode){
            displayShopsWithProductList();
        }
        else {
            List<Shop> allShopList  = Configuration.getCurrent().getShopDao().getShops();
            for(Shop shop: allShopList){
                addCircle(COLOR_RED, shop.getName(), shop.getCoordinate(), true);
            }
        }
    }

    private void displayShopsWithProductList() throws SQLException {
        viewController.initReadOnlyMode();
        List<Shop> shopListWithProducts = Configuration.getCurrent().getShopDao().getShopWithProductList(productListToSearchInShops);
        List<Shop> shopWithMinPriceForProductList =  Configuration.getCurrent().getShopDao().getShopWithMinPriceForProductList(productListToSearchInShops);
        for(Shop shop: shopListWithProducts){
            String toDisplay = shop.getName() + ": " + Configuration.getCurrent().getShopDao().getShoppingListPriceInShop(shop, productListToSearchInShops) + " €";
            int color = COLOR_BLACK;
            if(shopWithMinPriceForProductList.contains(shop)) color = COLOR_RED;
            addCircle(color, toDisplay, shop.getCoordinate(), true);
        }
    }

    /**
     * Lance le popup pour choisir les informations concernant le magasin
     * @param mapView la mapView contient les methodes de conversion d'une coordonnée X,Y en un point sur la carte
     * @param cursorX la coordonnée X du curseur
     * @param cursorY la coordonnée Y du curseur
     */
    @Override
    public void onAddShopClicked(MapView mapView, Double cursorX, Double cursorY) {

        //il y a une correction de la position
        Point mapPoint = cursorPoint(mapView, cursorX, cursorY);

        ShopController shopController = new ShopController(new Shop(mapPoint),false,  this);
        shopController.show();
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
     * Met a jour le shop afficher sur la carte
     * @param shop le magasin existant qu'il faut mettre a jour
     */
    @Override
    public void updateShop(Shop shop){
        Pair<Graphic, Graphic> shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;

        Graphic textPointOnMap = shopOverlay.getRight();
        TextSymbol shopName = (TextSymbol) textPointOnMap.getSymbol();
        shopName.setText(shop.getName());
    }

    /**
     * Cherche le magasin correspondant à la position et lance le popup
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
    public void onUpdateShopClicked() throws SQLException {

        Pair<Graphic, Graphic> graphicPair = getSelectedShop();
        if(graphicPair == null) return;
        Graphic cercleGraphic = graphicPair.getLeft();
        Graphic textGraphic = graphicPair.getRight();

        Point mapPoint = (Point) cercleGraphic.getGeometry();
        String shopName = ((TextSymbol) textGraphic.getSymbol()).getText();

        Shop shopToModify = Configuration.getCurrent().getShopDao().get(shopName,mapPoint);
        ShopController showShopController = new ShopController(shopToModify,true, this);
        showShopController.show();
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
        HelpController helpController = new HelpController("helpMap/", LAST_NUMBER_IMAGE_HELP_PAGE);
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

        Point adressPosition = performGeocode(address, addressGraphicsOverlay);

        if(readOnlyMode && adressPosition != null){
            System.out.println(adressPosition.getX() +" " +adressPosition.getY());
            try {
                List<Shop> nearestShopWithProductList =  Configuration.getCurrent().getShopDao().getNearestShopsWithProductList(productListToSearchInShops,adressPosition);
                for(Shop shop: nearestShopWithProductList){
                    String toDisplay = shop.getName() + ": " + Configuration.getCurrent().getShopDao().getShoppingListPriceInShop(shop, productListToSearchInShops) + " €";
                    addCircle(COLOR_BLUE, toDisplay, shop.getCoordinate(), true);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return adressPosition != null;
    }

    /**
     * Supprime le point sélectionné de l'overlay
     */
    public void onDeleteShopClicked() throws SQLException {
        Pair<Graphic, Graphic> shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;
        if(!isOnItineraryMode){

            ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
            if (alertResult == ButtonType.OK ) {
                Graphic circlePointOnMap = shopOverlay.getLeft();
                Graphic textPointOnMap = shopOverlay.getRight();

                TextSymbol shopName = (TextSymbol) textPointOnMap.getSymbol();
                Point shopPoint = (Point) textPointOnMap.getGeometry();

                Shop shopToDelete = Configuration.getCurrent().getShopDao().get(shopName.getText(), shopPoint);
                Configuration.getCurrent().getShopDao().delete(shopToDelete);

                viewController.removeShopGraphics(circlePointOnMap,textPointOnMap);
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
                mapViewPoint, SIZE, false, 1);

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
        Point adressPosition = locatorService.performGeocode(address,addressGraphicsOverlay);
        if( adressPosition == null){
            ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Adresse non valide");
        }
        return adressPosition;

    }





}