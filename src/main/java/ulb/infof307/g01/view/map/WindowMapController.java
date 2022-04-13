package ulb.infof307.g01.view.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WindowMapController extends ViewController<WindowMapController.Listener> implements Initializable  {

// TODO: CONTEXT MENU DANS FXML ?
    private MapServices mapServices;
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem addShopMenuItem = new MenuItem("Ajouter magasin");
    private final MenuItem deleteShopMenuItem = new MenuItem("Supprimer magasin");
    private final MenuItem modifyShopMenuItem = new MenuItem("Modifier magasin");

    private static final int ONCE_CLICKED = 1;

    private final GraphicsOverlay shopGraphicsCercleOverlay = new GraphicsOverlay();
    private final GraphicsOverlay shopGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay addressGraphicsOverlay = new GraphicsOverlay();
    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;
    private MapView mapView;

    @FXML
    private Pane mapViewStackPane;

    @FXML
    private TextField textFieldMenuBar;

    @FXML
    private TextField searchBox;

    public GraphicsOverlay getShopGraphicsCercleList() {
        return shopGraphicsCercleOverlay;
    }

    public GraphicsOverlay getShopGraphicsTextList() {
        return shopGraphicsTextOverlay;
    }

    public MapView getMapView() {
        return mapView;
    }

    private void initializeMapService(){
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

    @FXML
    void onShopSearchBoxAction() {
        String fieldText = textFieldMenuBar.getText();
        listener.onSearchShop(fieldText);
    }

    /**
     * Methode de recherche des adresses lié a la searchBox
     *
     */
    @FXML
    private void onAddressSearchBoxAction() {
        String address = searchBox.getText();
        boolean found = listener.onSearchAddress(address);
        setNodeColor(searchBox, found);

    }

    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * */
    @FXML
//    public void displayMain(){
//        this.loadFXML("DisplayMap.fxml");
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMapService();
        mapViewStackPane.getChildren().add(mapView);
    }

    public void start(){
        try {
            listener.onInitializeMapShop();
            initializeContextMenu();
            initializeMapEvent();
            createLocatorTaskAndDefaultParameters();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
     * Methode initialisant le clique droit sur la map
     */
    private void initializeMapEvent() {
        mapView.setOnMouseClicked(mouseEvent -> {
            mapView.setCursor(Cursor.DEFAULT);
            // selectionner un point avec un simple clique droit
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == ONCE_CLICKED) {

                    Point2D mapViewPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                    listener.highlightGraphicPoint(mapViewPoint);

                }
            }
        });
    }

    /**
     * Initialisation du Contexte menu et action possible sur celui ci
     */
    private void initializeContextMenu(){
        contextMenu.getItems().addAll(addShopMenuItem, modifyShopMenuItem, deleteShopMenuItem);
        mapView.setContextMenu(contextMenu);

        // context menu pour l'ajout
        addShopMenuItem.setOnAction(event -> {listener.onAddShopClicked();});

        // context menu pour la suppression
        deleteShopMenuItem.setOnAction(event -> {
            try {
                listener.onDeleteShopClicked(); //
            } catch (SQLException e) {
                showErrorSQL();
            }
        });

        //contexte menu pour la modification
        modifyShopMenuItem.setOnAction(event -> {
            try {
                listener.onUpdateShopClicked();
            } catch (SQLException e) {
                showErrorSQL();
            }

        });
    }

//    @FXML
//    public void returnMainMenu() {
//        WindowHomeController windowHomeController = new WindowHomeController();
//        windowHomeController.displayMain(primaryStage);
//    }

    public MenuItem getAddShopMenuItem() {
        return addShopMenuItem;
    }

    public GraphicsOverlay getAddressGraphicsOverlay() {
        return addressGraphicsOverlay;
    }

    public ListenableFuture<List<GeocodeResult>> getGeocodeAsync(){
        return locatorTask.geocodeAsync(searchBox.getText(), geocodeParameters);
    }

    public interface Listener {
        void onInitializeMapShop() throws SQLException;
        void onAddShopClicked();
        void onDeleteShopClicked() throws SQLException;
        void onUpdateShopClicked() throws SQLException;
        void onSearchShop(String shopName);
        void highlightGraphicPoint(Point2D mapViewPoint);
        boolean onSearchAddress(String address);
    }
}

