package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
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

public class MapController extends Controller implements MapViewController.Listener, ShopController.ShopListener, RouteService.Listener {

    public static final int COLOR_RED = 0xFFFF0000;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_BLUE   = 0xFF008DFF;
    public static final int ADDRESS_SIZE = 18;
    public static final float ADDRESS_MARKER_SIZE = 12.0f;
    public static final int CORRECTION_POSITION_X = 10, SIZE = 10;
    public static final int CORRECTION_POSITION_Y = 5;
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
        viewController.start();
        routeService = new RouteService(viewController,this);
        locatorService = new LocatorService(viewController);
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
     * @param shop         Si l'élément à ajouter est un magasin
     */
    @Override
    public void addCircle(int color, String textCircle, Point coordinate, Boolean shop) {

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

        if (shop) {
            viewController.getShopGraphicsCercleList().getGraphics().add(circlePoint);
            viewController.getShopGraphicsTextList().getGraphics().add(textPoint);
        }

        else {
            viewController.getItineraryGraphicsCircleList().add(circlePoint);
            viewController.getItineraryGraphicsTextList().add(textPoint);
        }
    }


    /**
     * Initialise les magasins sur la carte
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
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
        //List<Shop> nearestShopWithProductList =  Configuration.getCurrent().getShopDao().getNearestShopsWithProductList(); (Departure?)
        for(Shop shop: shopListWithProducts){
            String toDisplay = shop.getName() + ": " + Configuration.getCurrent().getShopDao().getShoppingListPriceInShop(shop, productListToSearchInShops) + " €";
            int color = COLOR_BLACK;
            if(shopWithMinPriceForProductList.contains(shop)) color = COLOR_RED;
            addCircle(color, toDisplay, shop.getCoordinate(), true);
        }
    }

    /**
     * Lance le popup pour choisir les informations concernant le magasin
     */
    @Override
    public void onAddShopClicked() {

        MapView mapView = viewController.getMapView();
        MenuItem addShopMenuItem = viewController.getAddShopMenuItem();
        mapView.setCursor(Cursor.DEFAULT);
        //il y a une correction de la position
        Point2D cursorPoint2D = new Point2D(addShopMenuItem.getParentPopup().getX() + CORRECTION_POSITION_X,
                addShopMenuItem.getParentPopup().getY() + CORRECTION_POSITION_Y);
        Point2D cursorCoordinate = mapView.screenToLocal(cursorPoint2D);
        Point mapPoint = mapView.screenToLocation(cursorCoordinate);

        ShopController shopController = new ShopController(new Shop(mapPoint),false,  this);
        shopController.show();
    }

    /**
     * Met a jour le shop afficher sur la carte
     * @param shop le magasin existant qu'il faut mettre a jour
     */
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

        Pair<Graphic, Graphic> shopOverlays = getSelectedShop();
        if(shopOverlays == null) return;
        Graphic cercleGraphic = shopOverlays.getLeft();
        Graphic textGraphic = shopOverlays.getRight();

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


    @Override
    public void onDeleteItineraryClicked() {
        routeService.onDeleteItinerary();
    }

    /**
     * Récupère la position départ ou d'arrivée de l'itinéraire et y dessine un cercle bleu
     */
    @Override
    public void onItineraryClicked() {
        routeService.onItinerary();
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
     * Rend inaccessible certains items du contexte menu
     */
    @Override
    public void switchVisibilityContextMenu() {

        // Rend invisible les boutons non nécessaires
        viewController.modifyVisibilityAddShopMenuItem();
        viewController.modifyVisibilityDeleteShopMenuItem();
        viewController.modifyVisibilityModifyShopMenuItem();
        viewController.modifyVisibilityDeleteItinerary();

        // Switch la variable ifSearchDeparture
        viewController.setIfSearchDeparture();

        // Modifie le texte du bouton itinéraire
        viewController.modifyItineraryShopMenuItemText();
    }


    /**
     * Recherche avec l'adresse d'un magasin
     * @param address l'adresse du magasin (Ville, rue, commune, numéro)
     * @return boolean est ce que l'adresse a été trouvé ou non
     */
    @Override
    public boolean onSearchAddress(String address) {
        return !address.isBlank() && performGeocode(address);
    }

    /**
     * Supprime le point sélectionné de l'overlay
     */
    public void onDeleteShopClicked() throws SQLException {
        GraphicsOverlay shopGraphicsCercleList = viewController.getShopGraphicsCercleList();
        GraphicsOverlay shopGraphicsTextList = viewController.getShopGraphicsTextList();
        Pair<Graphic, Graphic> shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;
        if(!isOnItineraryMode){

            ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
            if (alertResult == ButtonType.OK ) {
                Graphic cerclePointOnMap = shopOverlay.getLeft();
                Graphic textPointOnMap = shopOverlay.getRight();

                TextSymbol shopName = (TextSymbol) textPointOnMap.getSymbol();
                Point shopPoint = (Point) textPointOnMap.getGeometry();

                Shop shopToDelete = Configuration.getCurrent().getShopDao().get(shopName.getText(), shopPoint);
                Configuration.getCurrent().getShopDao().delete(shopToDelete);

                shopGraphicsCercleList.getGraphics().remove(cerclePointOnMap);
                shopGraphicsTextList.getGraphics().remove(textPointOnMap);
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
    public Pair<Graphic, Graphic> getSelectedShop(){
        GraphicsOverlay shopGraphicsCercleList = viewController.getShopGraphicsCercleList();
        GraphicsOverlay shopGraphicsTextList = viewController.getShopGraphicsTextList();

        for (int i = 0; i < shopGraphicsCercleList.getGraphics().size(); i++) {
            Graphic cerclePointOnMap = shopGraphicsCercleList.getGraphics().get(i);
            Graphic textPointOnMap = shopGraphicsTextList.getGraphics().get(i); // le symbole texte associer au point aussi

            if (cerclePointOnMap.isSelected()) {
                return new Pair<>(cerclePointOnMap, textPointOnMap);
            }
        }
        return null;
    }

    /**
     * Cherche un magasin parmi les magasins enregistrer dans la carte
     * @param shopName le nom du magasin
     */
    @Override
    public void onSearchShop(String shopName) {

        GraphicsOverlay mapTextOverlay = viewController.getShopGraphicsTextList();
        List<Graphic> mapTextGraphics = mapTextOverlay.getGraphics();

        GraphicsOverlay mapCercleOverlay = viewController.getShopGraphicsCercleList();
        List<Graphic> mapCercleGraphics = mapCercleOverlay.getGraphics();

        boolean isVisible;
        for(int index = 0; index < mapTextGraphics.size(); index++){
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
    public void highlightGraphicPoint(double mouseX, double mouseY) {
        Point2D mapViewPoint = new Point2D(mouseX, mouseY);
        ListenableFuture<IdentifyGraphicsOverlayResult> graphicsOverlayAsyncIdentified = viewController.getMapView().identifyGraphicsOverlayAsync(
                viewController.getShopGraphicsCercleList(),
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
    private boolean performGeocode(String address) {
        boolean isGeocodePerformed = locatorService.performGeocode(address);
        System.out.println("le res : " + isGeocodePerformed);
        if( !isGeocodePerformed){
            ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération du résultat\nContactez un responsable");
        }
        return isGeocodePerformed;

    }





}