package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.networkanalysis.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.map.MapViewController;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MapController extends Controller implements MapViewController.Listener, ShopController.ShopListener {

    public static final int COLOR_RED = 0xFFFF0000;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_BLUE   = 0xFF008DFF;
    public static final int ADDRESS_SIZE = 18;
    public static final float ADDRESS_MARKER_SIZE = 12.0f;
    public static final int CORRECTION_POSITION_X = 10, SIZE = 10;
    public static final int CORRECTION_POSITION_Y = 5;
    private MapViewController viewController;
    public MapController(Stage primaryStage){
        setStage(primaryStage);
    }

    /**
     * Lance l'affichage de la carte
     */
    public void show(){

        FXMLLoader loader = this.loadFXML("ShowMap.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        viewController.start();
    }

    /**
     * Ajout d'un point avec son texte sur la map
     *
     * @param color        Couleur du cercle
     * @param textCircle   Texte écrit à côté du cercle
     * @param coordinate   Coordonnée du cercle
     * @param shop         Si l'élément à ajouter est un magasin
     */
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
            viewController.getItineraryGraphicsCircleList().getGraphics().add(circlePoint);
            viewController.getItineraryGraphicsTextList().getGraphics().add(textPoint);
        }
    }


    /**
     * Initialise les magasins sur la carte
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
    public void onInitializeMapShop() throws SQLException {
        List<Shop> allShopList = Configuration.getCurrent().getShopDao().getShops();
        for(Shop shop: allShopList){
            addCircle(COLOR_RED, shop.getName(), shop.getCoordinate(), true);
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

        HomePageController homePageController = new HomePageController(currentStage);
        FXMLLoader loader = this.loadFXML("HomePage.fxml");
        HomePageViewController viewController = loader.getController();

        viewController.setListener(homePageController);
        homePageController.displayMain();
    }

    /**
     * Supprime l'itinéraire
     */
    @Override
    public void onDeleteItineraryClicked() {

        GraphicsOverlay itineraryGraphicsCercleList = viewController.getItineraryGraphicsCircleList();
        GraphicsOverlay itineraryGraphicsTextList   = viewController.getItineraryGraphicsTextList();

        int vide = 0;
        int itineraryIndex = 2;
        int departureIndex = 1;
        int arrival = 0;

        if (itineraryGraphicsCercleList.getGraphics().size() == vide) {return;}

        ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer itinéraire ?", "Etes vous sur de vouloir supprimer l'itinéraire actuel ? ");
        if (alertResult == ButtonType.OK) {

            if (itineraryGraphicsCercleList.getGraphics().size() > itineraryIndex) {
                itineraryGraphicsCercleList.getGraphics().remove(itineraryIndex);
            }

            if (itineraryGraphicsCercleList.getGraphics().size() > departureIndex) {
                itineraryGraphicsCercleList.getGraphics().remove(departureIndex);
                itineraryGraphicsTextList.getGraphics().remove(departureIndex);
            }

            else { switchVisibilityContextMenu();}

            itineraryGraphicsCercleList.getGraphics().remove(arrival);
            itineraryGraphicsTextList.getGraphics().remove(arrival);
        }
    }

    /**
     * Récupère la position départ ou d'arrivée de l'itinéraire et y dessine un cercle bleu
     */
    @Override
    public void onItineraryClicked() {

        MapView mapView = viewController.getMapView();
        MenuItem itineraryMenuItem = viewController.getItineraryShopMenuItem();
        mapView.setCursor(Cursor.DEFAULT);

        // Si un itinéraire est déjà calculé, demande à supprimé le précédent
        int itineraryAlreadyExist = 1;
        if (viewController.getItineraryGraphicsCircleList().getGraphics().size() > itineraryAlreadyExist) { onDeleteItineraryClicked();}

        // Affiche le texte en fonction de ce qui est recherché
        String text;
        if (viewController.getIfSearchDeparture()) {text = "Départ";}
        else {text = "Arrivée";}

        switchVisibilityContextMenu();

        // Il y a une correction de la position
        Point2D cursorPoint2D = new Point2D(itineraryMenuItem.getParentPopup().getX() + CORRECTION_POSITION_X,
                itineraryMenuItem.getParentPopup().getY() + CORRECTION_POSITION_Y);
        Point2D cursorCoordinate = mapView.screenToLocal(cursorPoint2D);
        Point mapPoint = mapView.screenToLocation(cursorCoordinate);

        // Dessine le cercle sur la carte
        addCircle(COLOR_BLUE, text, mapPoint, false);

        int readyToCalculRoute = 2;
        if (viewController.getItineraryGraphicsCircleList().getGraphics().size() == readyToCalculRoute) { calculRoute();}
    }

    /**
     * Calcule et affiche l'itinéraire
     */
    private void calculRoute() {

        Graphic routeGraphic = new Graphic();
        int width = 4;
        routeGraphic.setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, COLOR_BLUE, width));

        viewController.getItineraryGraphicsCircleList().getGraphics().add(routeGraphic);
        RouteTask routeTask = new RouteTask("https://route-api.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World");
        ListenableFuture<RouteParameters> routeParametersFuture = routeTask.createDefaultParametersAsync();

        // Récupère les positions de départ et d'arrivée
        List<Stop> stops = viewController.getItineraryGraphicsCircleList().getGraphics()
                .stream()
                .filter(graphic -> graphic.getGeometry() != null)
                .map(graphic -> new Stop((Point) graphic.getGeometry()))
                .collect(Collectors.toList());

        routeParametersFuture.addDoneListener(() -> {
            try {
                RouteParameters routeParameters = routeParametersFuture.get();
                routeParameters.setStops(stops);

                routeParameters.setReturnDirections(true);
                routeParameters.setDirectionsLanguage("fr");

                // choisis le mode de voyage
                int walking = 4;
                routeParameters.setTravelMode(routeTask.getRouteTaskInfo().getTravelModes().get(walking));

                // calcul l'itinéraire
                ListenableFuture<RouteResult> routeResultFuture = routeTask.solveRouteAsync(routeParameters);

                // dessine l'itinéraire
                routeResultFuture.addDoneListener(() -> {
                    try {
                        RouteResult routeResult = routeResultFuture.get();
                        int firstRoute= 0;
                        Route route = routeResult.getRoutes().get(firstRoute);
                        routeGraphic.setGeometry(route.getRouteGeometry());

                        // Affiche la durée et la distance de l'itinéraire calculé
                        itineraryInformationDisplay(route.getTotalTime(), route.getTotalLength());

                        route.getDirectionManeuvers().forEach(step -> System.out.println(step.getDirectionText()));
                    } catch (Exception e) { e.printStackTrace(); }
                });
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    private void itineraryInformationDisplay(double time, double length) {

        String information = Math.ceil(time) + " min " + Math.ceil(length) + " m ";

        TextSymbol pierTextSymbol =
                new TextSymbol(
                        SIZE*2, information, COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);

        MapView mapView = viewController.getMapView();
        MenuItem itineraryMenuItem = viewController.getItineraryShopMenuItem();

        Point2D cursorPoint2D = new Point2D(itineraryMenuItem.getParentPopup().getX() + CORRECTION_POSITION_X,
                itineraryMenuItem.getParentPopup().getY() + CORRECTION_POSITION_Y);
        Point2D cursorCoordinate = mapView.screenToLocal(cursorPoint2D);
        Point mapPoint = mapView.screenToLocation(cursorCoordinate);

        Graphic textPoint   = new Graphic(mapPoint, pierTextSymbol);
        viewController.getItineraryGraphicsTextList().getGraphics().add(textPoint);
    }


    /**
     * Rend inaccessible certains items du contexte menu
     */
    private void switchVisibilityContextMenu() {

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
     * Recherche avec une l'adresse d'un magasin
     * @param address l'adresse du magasin (Ville, rue, commune, numéro)
     * @return l'adresse a été trouver ou non
     */
    @Override
    public boolean onSearchAddress(String address) {
        return !address.isBlank() && performGeocode(address);
    }

    /**
     * Supprime les points selectioner de l'overlay
     *
     */
    public void onDeleteShopClicked() throws SQLException {

        GraphicsOverlay shopGraphicsCercleList = viewController.getShopGraphicsCercleList();
        GraphicsOverlay shopGraphicsTextList = viewController.getShopGraphicsTextList();
        Pair<Graphic, Graphic> shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;

        ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
        if (alertResult == ButtonType.OK) {
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

    /**
     * Cherche le magasin selectionner par l'utilisateur et
     * renvoie la paire d'objet graphique associé aux coordonnées cliquer
     * @return paire d'objet graphique
     */
    private Pair<Graphic, Graphic> getSelectedShop(){
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

        for(int index = 0; index < mapTextGraphics.size(); index++){
            Graphic textGraphic = mapTextGraphics.get(index);
            Graphic cercleGraphic = mapCercleGraphics.get(index);

            TextSymbol textSymbol = (TextSymbol) textGraphic.getSymbol();
            if(textSymbol.getText().contains(shopName) || Objects.equals(shopName, "")){
                textGraphic.setVisible(true);
                cercleGraphic.setVisible(true);
            }
            else{
                textGraphic.setVisible(false);
                cercleGraphic.setVisible(false);
            }
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
        ListenableFuture<IdentifyGraphicsOverlayResult> identifyFuture = viewController.getMapView().identifyGraphicsOverlayAsync(
                viewController.getShopGraphicsCercleList(),
                mapViewPoint, SIZE, false, 1);

        identifyFuture.addDoneListener(() -> {
            try {
                // recup la liste retourner par identify
                List<Graphic> identifiedGraphics = identifyFuture.get().getGraphics();
                if (identifiedGraphics.size() == 1) {
                    identifiedGraphics.get(0).setSelected(true);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                //TODO gerer l'erreur
            }
        });
    }

    /**
     * Recherche les coordonnées et les infos complètes qui correspond le mieux à l'adresse
     * et affiche le resultat sur la map
     *
     * @param address une vraie adresse ex : Avenue Franklin Roosevelt 50 - 1050 Bruxelles
     */
    private boolean performGeocode(String address) {
        final boolean[] found = {false};
        ListenableFuture<List<GeocodeResult>> geocodeResults = viewController.getGeocodeAsync();
        geocodeResults.addDoneListener(() -> {  // rècuperer le résultat
            try {
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) {
                    GeocodeResult result = geocodes.get(0);
                    displayResult(result);
                    found[0] = true;
                } else {
                    // pas d'adresse trouvé
                    //TODO gerer l'erreur
                    found[0] = false;
                }
            } catch (InterruptedException | ExecutionException exception) {
                //TODO gerer l'erreur
                ViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recupération du resultat\nContactez un responsable");
                exception.printStackTrace();
                found[0] = false;
            }
        });
        return found[0];
    }

    /**
     * Crée et afficher l'objet graphique associé à une recherche d'adresse sur la map
     *
     * @param geocodeResult le resultat d'une recherche
     */
    void displayResult(GeocodeResult geocodeResult) {
        GraphicsOverlay addressGraphicsOverlay = viewController.getAddressGraphicsOverlay();
        addressGraphicsOverlay.getGraphics().clear();

        // creation de l'objet graphique avec l'adresse
        String label = geocodeResult.getLabel();
        TextSymbol textSymbol = new TextSymbol(ADDRESS_SIZE, label, COLOR_BLACK, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        Graphic textGraphic = new Graphic(geocodeResult.getDisplayLocation(), textSymbol);
        addressGraphicsOverlay.getGraphics().add(textGraphic);

        // creation de l'objet graphique avec le carré rouge
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, COLOR_RED, ADDRESS_MARKER_SIZE);
        Graphic markerGraphic = new Graphic(geocodeResult.getDisplayLocation(), geocodeResult.getAttributes(), markerSymbol);
        addressGraphicsOverlay.getGraphics().add(markerGraphic);

        viewController.getMapView().setViewpointCenterAsync(geocodeResult.getDisplayLocation());
    }
}
