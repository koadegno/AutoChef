package ulb.infof307.g01.ui.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.shop.ShowShopController;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapTools {
    public static final int COLOR_RED = 0xFFFF0000;
    private static final int ONCE_CLICKED = 1;
    private static final int DOUBLE_CLICKED = 2;
    public static final int COLOR_BLACK = 0xFF000000;
    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;
    private MapView mapView;

    private final GraphicsOverlay shopGraphicsCercleOverlay = new GraphicsOverlay();
    private final GraphicsOverlay shopGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay addressGraphicsOverlay = new GraphicsOverlay();

    public MapView getMapView() {
        return mapView;
    }

    public List<Graphic> getShopGraphicsCercleList(){
        return shopGraphicsCercleOverlay.getGraphics();
    }

    public List<Graphic> getShopGraphicsTextList(){
        return shopGraphicsTextOverlay.getGraphics();
    }

    public MapTools() {

//         Initialisation de la carte pour correspondre au point de vue la Belgique
//         et mise en place de la clé d'API
        mapView = new MapView();
        //TODO trouver un meilleur moyen de mettre la clé
        String yourApiKey = "AAPK7d69dbea614548bdb8b6096b100ce4ddBX61AYZWAVLJ-RF_EEw68FrqS-y9ngET8KMzms5ZERiMTtShQeDALmWawO0LcM1S";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);
        mapView.setMap(map);
        //TODO changer ces nombres magique
        mapView.setViewpoint(new Viewpoint(50.85045,5.34878, 4000000.638572));
        mapView.getGraphicsOverlays().add(shopGraphicsCercleOverlay);
        mapView.getGraphicsOverlays().add(shopGraphicsTextOverlay);
        mapView.getGraphicsOverlays().add(addressGraphicsOverlay);
    }

    /**
     * lance le popup pour ajouter un magasin sur la map
     * @param cursorPoint2D La position ou se trouve le curseur
     */
    void setShopOnMap(Point2D cursorPoint2D) {
        Point mapPoint = mapView.screenToLocation(cursorPoint2D);
        //TODO Recupe le nom du shop
        //POPUP SHOP
        ShowShopController showShopController = new ShowShopController();
        int id = -1;
        showShopController.createPopup(id,mapPoint,this);
    }

    /**
     * Methode initialisant les evenements sur la map
     */
    void initializeMapEvent() {
        mapView.setOnMouseClicked(mouseEvent -> {
            mapView.setCursor(Cursor.DEFAULT);
            // selectionner un point avec un simple clique droit
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == ONCE_CLICKED) {

                    Point2D mapViewPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                    highlightGraphicPoint(mapViewPoint);

                }
                // ajouter un point sur la map ou suppression
                else if (mouseEvent.getClickCount() == DOUBLE_CLICKED) {
                    Point2D cursorPoint2D = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                    setShopOnMap(cursorPoint2D);

                }
                shopGraphicsCercleOverlay.clearSelection();
            }
        });
    }

    /**
     * Supprime les points selectioner de l'overlay
     *
     * @return un boolean indiquant si un objet graphic a ete double-cliquer
     */
    boolean deleteGraphicPoint() {
        boolean isPointFound = false;
        for (int i = 0; i < shopGraphicsCercleOverlay.getGraphics().size(); i++) {

            Graphic cerclePointOnMap = shopGraphicsCercleOverlay.getGraphics().get(i);
            Graphic textPointOnMap = shopGraphicsTextOverlay.getGraphics().get(i); // le symbole texte associer au point aussi

            if (cerclePointOnMap.isSelected()) {
                isPointFound = true;
                // TODO POP up avant de del avec les info du magasin
                ButtonType alertResult = Window.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
                if (alertResult == ButtonType.OK) {
                    shopGraphicsCercleOverlay.getGraphics().remove(cerclePointOnMap);
                    shopGraphicsTextOverlay.getGraphics().remove(textPointOnMap);
                }
                break; // tu as deja accomplie la tache que tu devais
            }
        }
        return isPointFound;
    }

    /**
     * Pour un point donné la methode met en evidence ce point sur la carte
     *
     * @param mapViewPoint les coordonnées du point a mettre en evidence
     */
    void highlightGraphicPoint(Point2D mapViewPoint) {
        ListenableFuture<IdentifyGraphicsOverlayResult> identifyFuture = mapView.identifyGraphicsOverlayAsync(shopGraphicsCercleOverlay,
                mapViewPoint, 10, false, 1);

        identifyFuture.addDoneListener(() -> {
            try {
                // get the list of graphics returned by identify
                List<Graphic> identifiedGraphics = identifyFuture.get().getGraphics();
                if (identifiedGraphics.size() == 1) {

                    // Use identified graphics as required, for example access attributes or geometry, select, build a table, etc...
                    identifiedGraphics.get(0).setSelected(true);

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace(); //TODO gerer l'erreur ?
                Window.showAlert(Alert.AlertType.ERROR, "ERREUR !", "Veillez rapporter l'erreur au pres des développeurs.");
            }
        });
    }

    /**
     * Ajout d'un point avec son texte sur la map
     *
     * @param shopToAdd la ou doit se trouver le point
     */
    public void addPointToOverlay(Shop shopToAdd) {
        // TODO Attention nombre magique
        //cree un cercle rouge
        SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, COLOR_RED, 10);

        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        10, shopToAdd.getName(), COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);

        Graphic circlePoint = new Graphic(shopToAdd.getCoordinate(), redCircleSymbol);
        Graphic textPoint = new Graphic(shopToAdd.getCoordinate(), pierTextSymbol);
        System.out.println(shopToAdd.getCoordinate());

        // ajoute des graphique a l'overlay
        shopGraphicsCercleOverlay.getGraphics().add(circlePoint);
        shopGraphicsTextOverlay.getGraphics().add(textPoint);

    }

    /**
     * Utilisation du service de geocoding(coordonné GPS associer a un lieu des infos) de ArcGis
     * Pour parametrer le service de geocoding Locator
     * et Parametre par defaut du service de geocoding
     */
    void createLocatorTaskAndDefaultParameters() {
        locatorTask = new LocatorTask("https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");

        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*"); // permet de retourner tous les attributs
        geocodeParameters.setMaxResults(1);
        // comment les coordonnées doivent correspondre a la location
        geocodeParameters.setOutputSpatialReference(mapView.getSpatialReference());
    }

    /**
     * Recherche les coordonnées et les infos complètes qui correspond le mieux à l'adresse
     * et affiche le resultat sur la map
     *
     * @param address une vraie adresse ex : Avenue Franklin Roosevelt 50 - 1050 Bruxelles
     */
    void performGeocode(String address) {
        ListenableFuture<List<GeocodeResult>> geocodeResults = locatorTask.geocodeAsync(address, geocodeParameters);

        geocodeResults.addDoneListener(() -> {  // rècuperer le résultat
            try {
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) {
                    GeocodeResult result = geocodes.get(0);
                    displayResult(result);

                } else {
                    // pas d'adresse trouvé
                    Window.showAlert(Alert.AlertType.INFORMATION, "Information", address + " n'est pas une adresse valide");
                }
            } catch (InterruptedException | ExecutionException exception) {

                Window.showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recupération du resultat\nContactez un responsable");
                exception.printStackTrace();//TODO gerer l'erreur
            }
        });
    }

    /**
     * Crée et afficher l'objet graphique associé à une recherche d'adresse sur la map
     *
     * @param geocodeResult le resultat d'une recherche
     */
    void displayResult(GeocodeResult geocodeResult) {
        addressGraphicsOverlay.getGraphics().clear();

        // creation de l'objet graphique avec l'adresse
        // TODO NOMBRE MAGIQUE
        String label = geocodeResult.getLabel();
        TextSymbol textSymbol = new TextSymbol(18, label, COLOR_BLACK, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        Graphic textGraphic = new Graphic(geocodeResult.getDisplayLocation(), textSymbol);
        addressGraphicsOverlay.getGraphics().add(textGraphic);

        // creation de l'objet graphique avec le carré rouge
        // TODO ATTENTION NOMBRE MAGIQUE
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 12.0f);
        Graphic markerGraphic = new Graphic(geocodeResult.getDisplayLocation(), geocodeResult.getAttributes(), markerSymbol);
        addressGraphicsOverlay.getGraphics().add(markerGraphic);

        mapView.setViewpointCenterAsync(geocodeResult.getDisplayLocation());
    }
}