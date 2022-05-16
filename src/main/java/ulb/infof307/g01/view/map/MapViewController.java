package ulb.infof307.g01.view.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import org.apache.jena.atlas.lib.Pair;
import org.jetbrains.annotations.NotNull;
import ulb.infof307.g01.controller.map.MapConstants;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.util.*;

/**
 * La classe gère la vue pour l'affichage de la carte
 */

public class MapViewController extends ViewController<MapViewController.Listener> implements Initializable  {

    public static final double LATITUDE_BRUSSELS = 50.85045;
    public static final double LONGITUDE_BRUSSELS = 5.34878;
    public static final double MAP_SCALE = 4000000.638572;
    // TODO: CONTEXT MENU DANS FXML ?
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem deleteItineraryItem = new MenuItem("Supprimer itinéraire");
    private final MenuItem itineraryShopMenuItem = new MenuItem("Itinéraire");
    public Label timeFeetLabel;
    public Label timeBikeLabel;
    public Label lengthLabel;
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
    private Menu searchShopNameMenu;
    private Double currentCursorPosX;
    private Double currentCursorPosY;
    static final int SIZE = 10;



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
        boolean isFound = listener.onSearchAddress(address, getItineraryGraphicsCircleList());
        setNodeColor(searchBox, !isFound);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMap();
        mapViewStackPane.getChildren().add(mapView);
    }

    /**
     * Ajout d'un point avec son texte sur la map
     *
     * @param color        Couleur du cercle
     * @param textCircle   Texte écrit à côté du cercle
     * @param coordinate   Coordonnée du cercle
     * @param isShop       Vrai si l'élément à ajouter est un magasin
     */
    public void addCircle(int color, String textCircle, Point coordinate, Boolean isShop) {

        Graphic circlePoint = getCircleGraphic(color, coordinate);
        Graphic textPoint = getTextGraphic(textCircle, coordinate);
        // rajoute les cercles créés au bon overlay

        if (isShop) {
            addShopGraphics(circlePoint,textPoint);
        }
        else {
            addItineraryGraphics(circlePoint,textPoint);
        }
    }

    @NotNull
    private Graphic getTextGraphic(String textCircle, Point coordinate) {
        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        MapConstants.SIZE, textCircle, MapConstants.COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        return new Graphic(coordinate, pierTextSymbol);
    }

    @NotNull
    private Graphic getCircleGraphic(int color, Point coordinate) {
        //crée un cercle
        SimpleMarkerSymbol circleSymbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                color,
                MapConstants.SIZE);
        return new Graphic(coordinate, circleSymbol);
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

            switchVisibilityContextMenu();

            // sélectionne un point avec un simple clique droit ? je suis sûre que tu as voulu dire "gauche " TODO:
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                shopGraphicsCircleOverlay.clearSelection();
                listener.highlightGraphicPoint(currentCursorPosX,currentCursorPosY,mapView,shopGraphicsCircleOverlay);
            }
        });
    }

    /**
     * Modifie la visibilité du contextMenu
     */
    private void switchVisibilityContextMenu() {

        List<Graphic> shopGraphicsCircleList = getShopGraphicsCircleList();

        for (Graphic circlePointOnMap : shopGraphicsCircleList) {
            // Affiche le bouton permettant de calculer un itinéraire seulement si le point d'arrivée est un magasin
            if (circlePointOnMap.isSelected()) {
                itineraryShopMenuItem.setVisible(true);
                break;
            } else {
                // Si on choisit un point de départ, affiche quand même le bouton
                itineraryShopMenuItem.setVisible(getItineraryGraphicsCircleList().size() == 1);
            }
        }

        modifyItineraryShopMenuItemText();

        // Ne rends pas visible le bouton delete, si aucun itinéraire n'est calculé
        deleteItineraryItem.setVisible(!getItineraryGraphicsCircleList().isEmpty());

        // Ne permets pas de calculer un itinéraire si un est déjà calculé
        if (getItineraryGraphicsCircleList().size() > 2) itineraryShopMenuItem.setVisible(false);

    }

    /**
     * Modifie le texte écrit sur le bouton
     */
    private void modifyItineraryShopMenuItemText() {
        if (getItineraryGraphicsCircleList().size() == 1) itineraryShopMenuItem.setText("Point de départ");
        else itineraryShopMenuItem.setText("Itinéraire");
    }


    /**
     * Initialisation du Contexte menu et action possible sur celui ci
     */
    private void initializeContextMenu(){

        contextMenu.getItems().addAll(itineraryShopMenuItem, deleteItineraryItem);
        mapView.setContextMenu(contextMenu);
        deleteItineraryItem.setVisible(false);

        // contexte menu pour le calcul d'itinéraire
        itineraryShopMenuItem.setOnAction(event -> {
            setItinerary();
            listener.onItineraryClicked(getItineraryGraphicsCircleList());
        });

        // Supprime l'itinéraire
        deleteItineraryItem.setOnAction(event -> {

            listener.onDeleteItineraryClicked(getItineraryGraphicsCircleList(),getItineraryGraphicsTextList());
            if(getItineraryGraphicsCircleList().isEmpty()){
                deleteItineraryInformation();

            }
        });
    }

    /**
     * Ajoute sur la carte un point associé à l'itinéraire
     */
    private void setItinerary() {
        boolean isDeparture = getItineraryGraphicsCircleList().size() == 1;
        Point mapPoint = cursorPoint(mapView,currentCursorPosX,currentCursorPosY);
        String text = "";
        if (isDeparture) {text = "Départ";}
        else { mapPoint = (Point) Objects.requireNonNull(getSelectedShop()).getGeometry();}
        addCircle(MapConstants.COLOR_BLUE, text, mapPoint, false);
    }

    /**
     * Cherche le magasin sélectionné par l'utilisateur et
     * renvoie la paire d'objet graphique associé aux coordonnées cliquer
     * @return le cercle sélectionné
     */
    private Graphic getSelectedShop(){
        List<Graphic> shopGraphicsCircleList = getShopGraphicsCircleList();
        for (Graphic circlePointOnMap : shopGraphicsCircleList) {
            if (circlePointOnMap.isSelected()) {
                return circlePointOnMap;
            }
        }
        return null;
    }

    public void itineraryInformation(double timeFeet, double timeBike, double length){
        timeFeetLabel.setText(timeFeet +  " min");
        timeBikeLabel.setText(timeBike + " min");
        lengthLabel.setText(length + " m");
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

    private void deleteItineraryInformation(){
        timeBikeLabel.setText("");
        timeFeetLabel.setText("");
        lengthLabel.setText("");
    }


    @FXML
    public void returnMainMenu() {listener.onBackButtonClicked();}

    private List<Graphic> getItineraryGraphicsCircleList(){
        return itineraryGraphicsCircleOverlay.getGraphics();
    }

    private List<Graphic> getItineraryGraphicsTextList(){
        return itineraryGraphicsTextOverlay.getGraphics();
    }

    private List<Graphic> getShopGraphicsCircleList(){
        return shopGraphicsCircleOverlay.getGraphics();
    }

    private List<Graphic> getShopGraphicsTextList(){
        return shopGraphicsTextOverlay.getGraphics();
    }

    @FXML
    public void initReadOnlyMode() {
        appMenuBar.setVisible(false);
        searchShopNameMenu.setVisible(false);
    }

    public void helpMap() {
        listener.helpMapClicked();
    }

    public void logout() { listener.logout(); }

    public void setViewPointCenter(Point displayLocation) {
        mapView.setViewpointCenterAsync(displayLocation);
    }

    public void addShopGraphics(Graphic circlePoint,Graphic textPoint) {
        shopGraphicsCircleOverlay.getGraphics().add(circlePoint);
        shopGraphicsTextOverlay.getGraphics().add(textPoint);
    }

    public void addItineraryGraphics(Graphic circlePoint, Graphic textPoint) {
        getItineraryGraphicsCircleList().add(circlePoint);
        getItineraryGraphicsTextList().add(textPoint);
    }

    public interface Listener {
        void onSearchShop(String shopName, List<Graphic> mapTextGraphics, List<Graphic> mapCercleGraphics);
        boolean onSearchAddress(String address, List<Graphic> addressGraphicsOverlay);
        void onBackButtonClicked();
        void onItineraryClicked(List<Graphic> itineraryCircleList);
        void onDeleteItineraryClicked(List<Graphic> itineraryGraphicsCercleList, List<Graphic> itineraryGraphicsTextList);
        void helpMapClicked();
        void logout();
        void highlightGraphicPoint(double mouseX, double mouseY, MapView mapView, GraphicsOverlay shopGraphicOverlay);
    }
}

