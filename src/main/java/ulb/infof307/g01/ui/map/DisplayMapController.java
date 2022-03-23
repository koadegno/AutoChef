package ulb.infof307.g01.ui.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.ui.Window;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DisplayMapController extends Window implements Initializable {

    private static final int ONCE_CLICKED = 1;
    private static final int DOUBLE_CLICKED = 2;
    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;
    private MapView mapView;
    private GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    @FXML
    private Pane mapViewStackPane;

    @FXML
    private TextField textFieldMenuBar;

    @FXML
    private TextField searchBox;




    @FXML
    void showSearchResult(KeyEvent event) {
        //TODO faire la requete a la db et afficher sur la maps les magasins
        //TODO ou alors avoir un combo box et il selectionne un elem qui l'emene au bonne endroit sur la carte

        System.out.println(textFieldMenuBar.getText());
        event.consume();
    }

    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * */
    @FXML
    public void displayMain(){
        this.loadFXML("DisplayMap.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMap();
        initializeMapEvent();
        setupTextField();

        createLocatorTaskAndDefaultParameters();

        searchBox.setOnAction(event -> {
            String address = searchBox.getText();
            if (!address.isBlank()) {
                performGeocode(address);
            }
        });
        //TODO fonction pour charger les magasins sur l'overlay
        mapViewStackPane.getChildren().add(mapView);
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }

    /**
     * Methode initialisant les evenements sur la map
     */
    private void initializeMapEvent() {
        mapView.setOnMouseClicked(mouseEvent -> {
            mapView.setCursor(Cursor.DEFAULT);
            // selectionner un point
            if(mouseEvent.getClickCount() == ONCE_CLICKED){
                /* TODO Popup avec les info et les produits du magasin

                */
                Point2D mapViewPoint = getCursorPosition(mouseEvent);
                try {
                    DisplayMapController.this.highlightGraphicPoint(mapViewPoint);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // ajouter un point sur la map ou suppression si on double
            // clique sur un point deja sur la map
            else if(mouseEvent.getClickCount() == DOUBLE_CLICKED) {
                boolean isPointAdded = false;
                Point2D cursorPoint2D = new Point2D( mouseEvent.getX(),mouseEvent.getY());

                SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 10);
                Point mapPoint = mapView.screenToLocation(cursorPoint2D);
                Graphic pierGraphic = new Graphic(mapPoint, redCircleSymbol);

                for(int i =0; i < graphicsOverlay.getGraphics().size(); i++){

                    Graphic pointOnMap = graphicsOverlay.getGraphics().get(i);
                    System.out.println("sur la carte "+pointOnMap.getGeometry());

                    if(pointOnMap.getGeometry().equals(pierGraphic.getGeometry(),100)){
                        // TODO POP up avant de del
                        ButtonType alertResult = showAlert(Alert.AlertType.CONFIRMATION,"Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
                        if(alertResult == ButtonType.OK){
                            removePointFromOverlay(pointOnMap);
                            isPointAdded = true;
                        }
                        break;
                    }
                }
                if(!isPointAdded){
                    //TODO fenetre pour mettre les infos du magasin
                    addPointToOverlay(mapPoint);
                }
            }
            graphicsOverlay.clearSelection();

        });
    }

    /**
     * Pour un point donné la methode met en evidence ce point sur la carte
     * @param mapViewPoint les coordonnées du point a mettre en evidence
     * @throws ExecutionException Erreur a l'execution
     * @throws InterruptedException Erreur a l'execution
     */
    private void highlightGraphicPoint(Point2D mapViewPoint) throws ExecutionException, InterruptedException {
        ListenableFuture<IdentifyGraphicsOverlayResult> identifyFuture = mapView.identifyGraphicsOverlayAsync(graphicsOverlay,
                mapViewPoint, 10, false,1);

        identifyFuture.addDoneListener(() -> {
            try {
                // get the list of graphics returned by identify
                List<Graphic> identifiedGraphics = identifyFuture.get().getGraphics();
                if(!identifiedGraphics.isEmpty()){
                    // Use identified graphics as required, for example access attributes or geometry, select, build a table, etc...
                    identifiedGraphics.get(0).setSelected(true);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace(); //TODO gerer l'erreur ?
                showAlert(Alert.AlertType.ERROR,"ERREUR !", "Veillez rapporter l'erreur au pres des développeurs.");
            }
        });
    }

    private Point2D getCursorPosition(MouseEvent mouseEvent){
        return new Point2D(mouseEvent.getX(), mouseEvent.getY());
    }

    private void removePointFromOverlay(Graphic mapGraphicPoint){
        graphicsOverlay.getGraphics().remove(mapGraphicPoint);

    }

    private void addPointToOverlay(Point mapPoint) {
        // create a red (0xFFFF0000) circle simple marker symbol
        // TODO Attention nombre magique
        SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 10);

        // create a graphic from the point and  TExt symbol
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        10, "Santa Monica Pier", 1,
                        TextSymbol.HorizontalAlignment.LEFT, TextSymbol.VerticalAlignment.BOTTOM);

        Graphic pierGraphic = new Graphic(mapPoint, redCircleSymbol);

        // add the graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(pierGraphic);
    }

    private void initializeMap(){
        mapView = new MapView();
        //TODO trouver un meilleur moyen de mettre la clé
        String yourApiKey = "AAPK7d69dbea614548bdb8b6096b100ce4ddBX61AYZWAVLJ-RF_EEw68FrqS-y9ngET8KMzms5ZERiMTtShQeDALmWawO0LcM1S";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);
        mapView.setMap(map);
        //TODO changer ces nombres magique
        mapView.setViewpoint(new Viewpoint(50.85045,5.34878, 4000000.638572));

    }


    private void setupTextField() {
        searchBox.setMaxWidth(400);
        searchBox.setPromptText("Search for an address");
    }


    private void createLocatorTaskAndDefaultParameters() {
        locatorTask = new LocatorTask("https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");

        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*");
        geocodeParameters.setMaxResults(1);
        geocodeParameters.setOutputSpatialReference(mapView.getSpatialReference());
    }

    private void performGeocode(String address) {
        ListenableFuture<List<GeocodeResult>> geocodeResults = locatorTask.geocodeAsync(address, geocodeParameters);

        geocodeResults.addDoneListener(() -> {
            try {
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) {
                    GeocodeResult result = geocodes.get(0);

                    displayResult(result);

                } else {
                    new Alert(Alert.AlertType.INFORMATION, "No results found.").show();
                }
            } catch (InterruptedException | ExecutionException e) {
                new Alert(Alert.AlertType.ERROR, "Error getting result.").show();
                e.printStackTrace();
            }
        });
    }

    private void displayResult(GeocodeResult geocodeResult) {
        graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result

        // create a graphic to display the address text
        String label = geocodeResult.getLabel();
        TextSymbol textSymbol = new TextSymbol(18, label, 0xFF000000, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        Graphic textGraphic = new Graphic(geocodeResult.getDisplayLocation(), textSymbol);
        graphicsOverlay.getGraphics().add(textGraphic);

        // create a graphic to display the location as a red square
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 12.0f);
        Graphic markerGraphic = new Graphic(geocodeResult.getDisplayLocation(), geocodeResult.getAttributes(), markerSymbol);
        graphicsOverlay.getGraphics().add(markerGraphic);

        mapView.setViewpointCenterAsync(geocodeResult.getDisplayLocation());
    }
}
