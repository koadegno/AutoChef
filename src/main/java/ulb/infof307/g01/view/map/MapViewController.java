package ulb.infof307.g01.view.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
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
    public Label timeFeetLabel;
    public Label timeBikeLabel;
    public Label lengthLabel;
    private boolean ifSearchDeparture = false;
    private final GraphicsOverlay shopGraphicsCircleOverlay = new GraphicsOverlay();
    private final GraphicsOverlay shopGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay itineraryGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay itineraryGraphicsCircleOverlay = new GraphicsOverlay();
    private final GraphicsOverlay addressGraphicsOverlay = new GraphicsOverlay();
    private MapView mapView;

    @FXML
    private Pane mapViewStackPane;

    @FXML
    private TextField textFieldMenuBar;

    @FXML
    private TextField searchBox;
    @FXML
    private MenuBar appMenuBar;
    @FXML
    private Menu searchAddressMenu;
    @FXML
    private Menu searchShopNameMenu;

    private Double currentCursorPosX;
    private Double currentCursorPosY;

    public MapView getMapView() {
        return mapView;
    }

    private void initializeMap(){
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


    /**
     * Cherche les magasins sur la carte avec le bon nom entré
     */
    @FXML
    void onShopSearchBoxAction() {
        String fieldText = textFieldMenuBar.getText();
        listener.onSearchShop(fieldText,getShopGraphicsTextList(),getShopGraphicsCircleList());
    }

    /**
     * Methode de recherche des adresses lié a la searchBox
     */
    @FXML
    private void onAddressSearchBoxAction() {
        String address = searchBox.getText();
        boolean isFound = listener.onSearchAddress(address);
        setNodeColor(searchBox, !isFound);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMap();
        mapViewStackPane.getChildren().add(mapView);
    }

    public void start(){
        initializeContextMenu();
        initializeMapEvent();
    }

    /**
     * Methode initialisant le clique droit sur la map
     */
    private void initializeMapEvent() {
        mapView.setOnMouseClicked(mouseEvent -> {
            mapView.setCursor(Cursor.DEFAULT);
            currentCursorPosX = mouseEvent.getX();
            currentCursorPosY = mouseEvent.getY();


            // selectionner un point avec un simple clique droit
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                shopGraphicsCircleOverlay.clearSelection();
                listener.highlightGraphicPoint(currentCursorPosX,currentCursorPosY,mapView,shopGraphicsCircleOverlay);
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

        // contexte menu pour l'ajout
        addShopMenuItem.setOnAction(event -> listener.onAddShopClicked(mapView,currentCursorPosX ,currentCursorPosY ));

        // contexte menu pour la suppression
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
        itineraryShopMenuItem.setOnAction(event -> listener.onItineraryClicked(currentCursorPosX,currentCursorPosY ));

        // Supprime l'itinéraire
        deleteItineraryItem.setOnAction(event -> listener.onDeleteItineraryClicked());
    }

    public void itineraryInformation(double timeFeet, double timeBike, double length){
        timeFeetLabel.setText(timeFeet +  " min");
        timeBikeLabel.setText(timeBike + " min");
        lengthLabel.setText(length + " m");
    }

    public void deleteItineraryInformation(){
        timeBikeLabel.setText("");
        timeFeetLabel.setText("");
        lengthLabel.setText("");
    }

    @FXML
    public void returnMainMenu() {listener.onBackButtonClicked();}


    public MenuItem getDeleteShopMenuItem() { return deleteShopMenuItem;}

    public MenuItem getModifyShopMenuItem() {return modifyShopMenuItem;}

    public MenuItem getItineraryShopMenuItem() { return itineraryShopMenuItem;}

    public boolean getIfSearchDeparture() {return ifSearchDeparture;}

    public void setIfSearchDeparture() {ifSearchDeparture = !ifSearchDeparture;}

    public List<Graphic> getAddressGraphicsOverlay() {return addressGraphicsOverlay.getGraphics();}

    public void modifyVisibilityAddShopMenuItem() {addShopMenuItem.setVisible(getIfSearchDeparture());}

    public void modifyVisibilityDeleteShopMenuItem() {getDeleteShopMenuItem().setVisible(getIfSearchDeparture());}

    public void modifyVisibilityModifyShopMenuItem() {getModifyShopMenuItem().setVisible(getIfSearchDeparture());}

    public void modifyVisibilityDeleteItinerary() { deleteItineraryItem.setVisible(true);}

    public void modifyItineraryShopMenuItemText() {
        if (getIfSearchDeparture()) {getItineraryShopMenuItem().setText("Point de départ");}
        else {getItineraryShopMenuItem().setText("Itinéraire");}
    }

    public List<Graphic> getItineraryGraphicsCircleList(){
        return itineraryGraphicsCircleOverlay.getGraphics();
    }

    public List<Graphic> getItineraryGraphicsTextList(){
        return itineraryGraphicsTextOverlay.getGraphics();
    }

    public List<Graphic> getShopGraphicsCircleList(){
        return shopGraphicsCircleOverlay.getGraphics();
    }

    public List<Graphic> getShopGraphicsTextList(){
        return shopGraphicsTextOverlay.getGraphics();
    }

    @FXML
    public void initReadOnlyMode() {
        appMenuBar.setVisible(false);
        searchShopNameMenu.setVisible(false);
        searchAddressMenu.setVisible(false);
    }

    public void helpMap() {
        listener.helpMapClicked();
    }

    public void logout() { listener.logout(); }

    public SpatialReference getSpatialReference() {
        return mapView.getSpatialReference();
    }

    public void setViewPointCenter(Point displayLocation) {
        mapView.setViewpointCenterAsync(displayLocation);
    }

    public void switchVisibilityContextMenu() {
        // Rend invisible les boutons non nécessaires
        modifyVisibilityAddShopMenuItem();
        modifyVisibilityDeleteShopMenuItem();
        modifyVisibilityModifyShopMenuItem();
        modifyVisibilityDeleteItinerary();

        // Switch la variable ifSearchDeparture
        setIfSearchDeparture();

        // Modifie le texte du bouton itinéraire
        modifyItineraryShopMenuItemText();
    }

    public void addShopGraphics(Graphic circlePoint,Graphic textPoint) {
        shopGraphicsCircleOverlay.getGraphics().add(circlePoint);
        shopGraphicsTextOverlay.getGraphics().add(textPoint);
    }

    public void addItineraryGraphics(Graphic circlePoint, Graphic textPoint) {
        itineraryGraphicsCircleOverlay.getGraphics().add(circlePoint);
        itineraryGraphicsTextOverlay.getGraphics().add(textPoint);
    }

    public void removeShopGraphics(Graphic circlePoint, Graphic textPoint) {
        shopGraphicsCircleOverlay.getGraphics().remove(circlePoint);
        shopGraphicsTextOverlay.getGraphics().remove(textPoint);
    }

    public interface Listener {
        void onAddShopClicked(MapView mapView, Double posX, Double posY);
        void onDeleteShopClicked() throws SQLException;
        void onUpdateShopClicked() throws SQLException;
        void onSearchShop(String shopName, List<Graphic> mapTextGraphics, List<Graphic> mapCercleGraphics);
        boolean onSearchAddress(String address);
        void onBackButtonClicked();
        void onItineraryClicked(Double posX, Double posY);
        void onDeleteItineraryClicked();
        void helpMapClicked();
        void logout();
        void highlightGraphicPoint(double mouseX, double mouseY, MapView mapView, GraphicsOverlay shopGraphicOverlay);
    }



}

