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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class MapViewController extends ViewController<MapViewController.Listener> implements Initializable  {

    public static final double LATITUDE_BRUSSELS = 50.85045;
    public static final double LONGITUDE_BRUSSELS = 5.34878;
    public static final double MAP_SCALE = 4000000.638572;
    // TODO: CONTEXT MENU DANS FXML ?
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem addShopMenuItem = new MenuItem("Ajouter magasin");
    private final MenuItem deleteShopMenuItem = new MenuItem("Supprimer magasin");
    private final MenuItem deleteItineraryItem = new MenuItem("Supprimer itinéraire");
    private final MenuItem modifyShopMenuItem = new MenuItem("Modifier magasin");
    private final MenuItem itineraryShopMenuItem = new MenuItem("Itinéraire");
    private boolean ifSearchDeparture = false;
    private static final int ONCE_CLICKED = 1;
    private final GraphicsOverlay shopGraphicsCircleOverlay = new GraphicsOverlay();
    private final GraphicsOverlay shopGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay itineraryGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay itineraryGraphicsCircleOverlay = new GraphicsOverlay();
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
        return shopGraphicsCircleOverlay;
    }

    public GraphicsOverlay getShopGraphicsTextList() { return shopGraphicsTextOverlay;}

    public GraphicsOverlay getItineraryGraphicsTextList() {
        return itineraryGraphicsTextOverlay;
    }

    public GraphicsOverlay getItineraryGraphicsCircleList() { return itineraryGraphicsCircleOverlay;}

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
        mapView.setViewpoint(new Viewpoint(LATITUDE_BRUSSELS, LONGITUDE_BRUSSELS, MAP_SCALE));
        mapView.getGraphicsOverlays().add(shopGraphicsCircleOverlay);
        mapView.getGraphicsOverlays().add(shopGraphicsTextOverlay);
        mapView.getGraphicsOverlays().add(addressGraphicsOverlay);
        mapView.getGraphicsOverlays().add(itineraryGraphicsCircleOverlay);
        mapView.getGraphicsOverlays().add(itineraryGraphicsTextOverlay);
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
//        this.loadFXML("ShowMap.fxml");
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
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == ONCE_CLICKED) {
                shopGraphicsCircleOverlay.clearSelection();
                listener.highlightGraphicPoint(mouseEvent.getX(),mouseEvent.getY());
            }
        });
    }


    /**
     * Initialisation du Contexte menu et action possible sur celui ci
     */
    private void initializeContextMenu(){

        contextMenu.getItems().addAll(addShopMenuItem, modifyShopMenuItem, deleteShopMenuItem, itineraryShopMenuItem, deleteItineraryItem);
        mapView.setContextMenu(contextMenu);
        deleteItineraryItem.setVisible(false);

        // context menu pour l'ajout
        addShopMenuItem.setOnAction(event -> listener.onAddShopClicked());

        // context menu pour la suppression
        deleteShopMenuItem.setOnAction(event -> {
            try {
                listener.onDeleteShopClicked(); //
            } catch (SQLException e) {
                showErrorSQL();
            }
        });

        // contexte menu pour la modification
        modifyShopMenuItem.setOnAction(event -> {
            try {
                listener.onUpdateShopClicked();
            } catch (SQLException e) {
                showErrorSQL();
            }
        });

        // contexte menu pour le calcul d'itinéraire
        itineraryShopMenuItem.setOnAction(event -> {
            listener.onItineraryClicked();
        });

        // Supprime l'itinéraire
        deleteItineraryItem.setOnAction(event -> {
            listener.onDeleteItineraryClicked();
        });
    }

    @FXML
    public void returnMainMenu() {listener.onBackButtonClicked();}

    public MenuItem getAddShopMenuItem() {return addShopMenuItem;}

    public MenuItem getDeleteShopMenuItem() { return deleteShopMenuItem;}

    public MenuItem getModifyShopMenuItem() {return modifyShopMenuItem;}

    public MenuItem getItineraryShopMenuItem() { return itineraryShopMenuItem;}

    public boolean getIfSearchDeparture() {return ifSearchDeparture;}

    public void setIfSearchDeparture() {ifSearchDeparture = !ifSearchDeparture;}

    public GraphicsOverlay getAddressGraphicsOverlay() {return addressGraphicsOverlay;}

    public ListenableFuture<List<GeocodeResult>> getGeocodeAsync() {return locatorTask.geocodeAsync(searchBox.getText(), geocodeParameters);}

    public void modifyVisibilityAddShopMenuItem() {getAddShopMenuItem().setVisible(getIfSearchDeparture());}

    public void modifyVisibilityDeleteShopMenuItem() {getDeleteShopMenuItem().setVisible(getIfSearchDeparture());}

    public void modifyVisibilityModifyShopMenuItem() {getModifyShopMenuItem().setVisible(getIfSearchDeparture());}

    public void modifyVisibilityDeleteItinerary() { deleteItineraryItem.setVisible(true);}

    public void modifyItineraryShopMenuItemText() {
        if (getIfSearchDeparture()) {getItineraryShopMenuItem().setText("Point de départ");}
        else {getItineraryShopMenuItem().setText("Itinéraire");}
    }


    public interface Listener {
        void onInitializeMapShop() throws SQLException;
        void onAddShopClicked();
        void onDeleteShopClicked() throws SQLException;
        void onUpdateShopClicked() throws SQLException;
        void onSearchShop(String shopName);
        void highlightGraphicPoint(double mouseX, double mouseY);
        boolean onSearchAddress(String address);
        void onBackButtonClicked();
        void onItineraryClicked();
        void onDeleteItineraryClicked();
    }
}

