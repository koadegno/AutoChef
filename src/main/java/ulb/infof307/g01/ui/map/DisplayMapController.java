package ulb.infof307.g01.ui.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.cuisine.Shop;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.WindowHomeController;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class DisplayMapController extends Window implements Initializable {

    private static final int ONCE_CLICKED = 1;
    private static final int DOUBLE_CLICKED = 2;
    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;
    private MapView mapView;
    private final GraphicsOverlay shopGraphicsCercleOverlay = new GraphicsOverlay();
    private final GraphicsOverlay shopGraphicsTextOverlay = new GraphicsOverlay();
    private final GraphicsOverlay addressGraphicsOverlay = new GraphicsOverlay();

    @FXML
    private Pane mapViewStackPane;

    @FXML
    private TextField textFieldMenuBar;

    @FXML
    private TextField searchBox;
    private ArrayList<Shop> allShopList;

    @FXML
    void onShoppingSearchBoxAction(ActionEvent event) {
        //TODO faire la requete a la db et afficher sur la maps les magasins
        //TODO ou alors avoir un combo box et il selectionne un elem qui l'emene au bonne endroit sur la carte

        String fieldText = textFieldMenuBar.getText();
        System.out.println();

        for(int index = 0; index < shopGraphicsTextOverlay.getGraphics().size(); index++){

            Graphic textGraphicShop = shopGraphicsTextOverlay.getGraphics().get(index);
            Graphic cercleGraphicShop = shopGraphicsCercleOverlay.getGraphics().get(index);

            TextSymbol textSymbol = (TextSymbol) textGraphicShop.getSymbol();
            if(textSymbol.getText().equals(fieldText) || Objects.equals(fieldText, "")){
                textGraphicShop.setVisible(true);
                cercleGraphicShop.setVisible(true);
            }
            else{
                textGraphicShop.setVisible(false);
                cercleGraphicShop.setVisible(false);

            }


        }

        event.consume();
    }

    /**
     * Methode de recherche des adresses lié a la searchBox
     * @param keyEvent l'action recu par le textfield
     */
    @FXML
    private void onAddressSearchBoxAction(ActionEvent keyEvent) {

        String address = searchBox.getText();
        if (!address.isBlank()) {
            setNodeColor(searchBox,false);
            performGeocode(address);
        }
        else{
            setNodeColor(searchBox,true);
        }

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
        initializeMapShop();

        createLocatorTaskAndDefaultParameters();
        //TODO
        //TODO fonction pour charger les magasins sur l'overlay
        mapViewStackPane.getChildren().add(mapView);
        mapView.getGraphicsOverlays().add(shopGraphicsCercleOverlay);
        mapView.getGraphicsOverlays().add(shopGraphicsTextOverlay);
        mapView.getGraphicsOverlays().add(addressGraphicsOverlay);

    }

    /**
     * Initialisation de la carte pour correspondre au point de vue la Belgique
     * et mise en place de la clé d'API
     */
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

    /**
     * Initialisation des magasins sur la map
     */
    private void initializeMapShop() {
        allShopList = new ArrayList<>();
        // TODO Recuperer la liste de Magasin de la db
        allShopList.add(new Shop("Lidl 3", new Point( 3.503561,50.6224768, SpatialReferences.getWgs84())));
        allShopList.add(new Shop("Aldi 2", new Point(5.6257913, 50.9702834, SpatialReferences.getWgs84())));
        allShopList.add(new Shop("Lidl 1", new Point(4.3586407, 50.8424057,SpatialReferences.getWgs84())));

        for(Shop shop: allShopList){
            addPointToOverlay(shop);
            System.out.println(shop.getCoordinate());
        }
    }

    /**
     * Methode initialisant les evenements sur la map
     */
    private void initializeMapEvent() {
        mapView.setOnMouseClicked(mouseEvent -> {
            mapView.setCursor(Cursor.DEFAULT);
            // selectionner un point avec un simple clique droit
            if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == ONCE_CLICKED){
                /* TODO Popup avec les info et les produits du magasin
                */
                Point2D mapViewPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                try {
                    DisplayMapController.this.highlightGraphicPoint(mapViewPoint);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // ajouter un point sur la map ou suppression si on double-clique droit
            // sur un point deja sur la map
            else if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == DOUBLE_CLICKED) {
                Point2D cursorPoint2D = new Point2D( mouseEvent.getX(),mouseEvent.getY());
                Point mapPoint = mapView.screenToLocation(cursorPoint2D);
                Shop shopToAdd = new Shop("new Shop", mapPoint);
                boolean isPointFound = deleteGraphicPoint(); //
                if(!isPointFound){
                    //TODO fenetre pour mettre les infos du magasin
                    addPointToOverlay(shopToAdd);
                }
            }
            shopGraphicsCercleOverlay.clearSelection();
            shopGraphicsTextOverlay.clearSelection();

        });
    }

    /**
     * Supprime les points selectioner de l'overlay
     * @return un boolean indiquant si un objet graphic a ete double-cliquer
     */
    private boolean deleteGraphicPoint(){
        boolean isPointFound = false;
        for(int i = 0; i < shopGraphicsCercleOverlay.getGraphics().size(); i++){

            Graphic cerclePointOnMap = shopGraphicsCercleOverlay.getGraphics().get(i);
            Graphic textPointOnMap = shopGraphicsTextOverlay.getGraphics().get(i); // le symbole texte associer au point aussi

            if(cerclePointOnMap.isSelected()){
                isPointFound = true;
                // TODO POP up avant de del avec les info du magasin
                ButtonType alertResult = showAlert(Alert.AlertType.CONFIRMATION,"Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
                if(alertResult == ButtonType.OK){
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
     * @param mapViewPoint les coordonnées du point a mettre en evidence
     * @throws ExecutionException Erreur a l'execution
     * @throws InterruptedException Erreur a l'execution
     */
    private void highlightGraphicPoint(Point2D mapViewPoint) throws ExecutionException, InterruptedException {
        ListenableFuture<IdentifyGraphicsOverlayResult> identifyFuture = mapView.identifyGraphicsOverlayAsync(shopGraphicsCercleOverlay,
                mapViewPoint, 10, false,1);

        identifyFuture.addDoneListener(() -> {
            try {
                // get the list of graphics returned by identify
                List<Graphic> identifiedGraphics = identifyFuture.get().getGraphics();
                if(identifiedGraphics.size() == 1){

                    // Use identified graphics as required, for example access attributes or geometry, select, build a table, etc...
                    identifiedGraphics.get(0).setSelected(true);

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace(); //TODO gerer l'erreur ?
                showAlert(Alert.AlertType.ERROR,"ERREUR !", "Veillez rapporter l'erreur au pres des développeurs.");
            }
        });
    }

    /**
     * Ajout d'un point avec son texte sur la map
     * @param shopToAdd la ou doit se trouver le point
     */
    private void addPointToOverlay(Shop shopToAdd) {
        // TODO Attention nombre magique
        //cree un cercle rouge
        SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 10);

        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        10, shopToAdd.getName(), 0xFF000000,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);

        Graphic circlePoint = new Graphic(shopToAdd.getCoordinate(), redCircleSymbol);
        Graphic textPoint = new Graphic(shopToAdd.getCoordinate(), pierTextSymbol);


        // ajoute des graphique a l'overlay
        shopGraphicsCercleOverlay.getGraphics().add(circlePoint);
        shopGraphicsTextOverlay.getGraphics().add(textPoint);

    }

    /**
     * Utilisation du service de geocoding(coordonné GPS associer a un lieu des infos) de ArcGis
     * Pour parametrer le service de geocoding Locator
     * et Parametre par defaut du service de geocoding
     */
    private void createLocatorTaskAndDefaultParameters() {
        locatorTask = new LocatorTask("https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");

        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*"); // permet de retourne tt les attributs
        geocodeParameters.setMaxResults(1);
        // comment les coordonnées doivent correspondre a la location
        geocodeParameters.setOutputSpatialReference(mapView.getSpatialReference());
    }

    /**
     * Recherche les coordonnées et les infos complètes qui correspond le mieux à l'adresse
     * et affiche le resultat sur la map
     * @param address une vraie adresse ex : Avenue Franklin Roosevelt 50 - 1050 Bruxelles
     */
    private void performGeocode(String address) {
        ListenableFuture<List<GeocodeResult>> geocodeResults = locatorTask.geocodeAsync(address, geocodeParameters);

        geocodeResults.addDoneListener(() -> {  // rècuperer le résultat
            try {
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) {
                    GeocodeResult result = geocodes.get(0);
                    displayResult(result);

                } else {
                    // pas d'adresse trouvé
                    showAlert(Alert.AlertType.INFORMATION,"Information", address + " n'est pas une adresse valide");
                }
            } catch (InterruptedException | ExecutionException exception) {

                showAlert(Alert.AlertType.ERROR,"Erreur", "Erreur lors de la recupération du resultat\nContactez un responsable");
                exception.printStackTrace();
            }
        });
    }

    /**
     * Crée et afficher l'objet graphique associé à une recherche d'adresse sur la map
     * @param geocodeResult le resultat d'une recherche
     */
    private void displayResult(GeocodeResult geocodeResult) {
        addressGraphicsOverlay.getGraphics().clear();

        // creation de l'objet graphique avec l'adresse
        // TODO NOMBRE MAGIQUE
        String label = geocodeResult.getLabel();
        TextSymbol textSymbol = new TextSymbol(18, label, 0xFF000000, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);
        Graphic textGraphic = new Graphic(geocodeResult.getDisplayLocation(), textSymbol);
        System.out.println(geocodeResult.getDisplayLocation());
        addressGraphicsOverlay.getGraphics().add(textGraphic);

        // creation de l'objet graphique avec le carré rouge
        // TODO ATTENTION NOMBRE MAGIQUE
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 12.0f);
        Graphic markerGraphic = new Graphic(geocodeResult.getDisplayLocation(), geocodeResult.getAttributes(), markerSymbol);
        addressGraphicsOverlay.getGraphics().add(markerGraphic);

        mapView.setViewpointCenterAsync(geocodeResult.getDisplayLocation());
    }

    @FXML
    public void returnMainMenu() {
        WindowHomeController windowHomeController = new WindowHomeController();
        windowHomeController.displayMain(primaryStage);
    }


}

