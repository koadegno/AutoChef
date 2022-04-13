package ulb.infof307.g01.controller;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.map.WindowMapController;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MapController extends Controller implements WindowMapController.Listener, ShopController.MapListener{

    public static final int COLOR_RED = 0xFFFF0000;
    public static final int SIZE = 10;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int ADDRESS_SIZE = 18;
    public static final float ADDRESS_MARKER_SIZE = 12.0f;
    private WindowMapController viewController;

    public MapController(Stage primaryStage){
        setStage(primaryStage);
    }

    public void show(){
        FXMLLoader loader = this.loadFXML("DisplayMap.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        viewController.start();
//        this.setNewScene(loader,"Carte");

    }

    /**
     * Ajout d'un point avec son texte sur la map
     *
     * @param shopToAdd le nouveau magasin a ajouter
     */
    public void addShop(Shop shopToAdd) {
        //crée un cercle rouge
        SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                COLOR_RED,
                SIZE);
        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        SIZE, shopToAdd.getName(), COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);

        Graphic circlePoint = new Graphic(shopToAdd.getCoordinate(), redCircleSymbol);
        Graphic textPoint = new Graphic(shopToAdd.getCoordinate(), pierTextSymbol);

        // ajoute des graphiques à l'overlay
        viewController.getShopGraphicsCercleList().getGraphics().add(circlePoint);
        viewController.getShopGraphicsTextList().getGraphics().add(textPoint);
    }

    @Override
    public void onInitializeMapShop() throws SQLException {
        List<Shop> allShopList = Configuration.getCurrent().getShopDao().getShops();
        for(Shop shop: allShopList){
            addShop(shop);
        }
    }

    @Override
    public void onAddShopClicked() {
        MapView mapView = viewController.getMapView();
        MenuItem addShopMenuItem = viewController.getAddShopMenuItem();
        mapView.setCursor(Cursor.DEFAULT);
        //TODO les nombres sont la pour corriger la position du curseur
        //il y a une correction fait attention ca peut faire des erreurs tu penses ?
        Point2D cursorPoint2D = new Point2D(addShopMenuItem.getParentPopup().getX() + 10, addShopMenuItem.getParentPopup().getY() + 5);
        Point2D cursorCoordinate = mapView.screenToLocal(cursorPoint2D);
        Point mapPoint = mapView.screenToLocation(cursorPoint2D);

        Stage popUpStage = new Stage();
        ShopController shopController = new ShopController(popUpStage,new Shop(mapPoint),false,  this);
        shopController.show();
    }

    /**
     * Met a jour le shop afficher sur la carte
     * @param shop le magasin existant qu'il faut mettre a jour
     */
    public void updateShop(Shop shop){
        Pair shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;

        Graphic textPointOnMap = (Graphic) shopOverlay.getRight();
        TextSymbol shopName = (TextSymbol) textPointOnMap.getSymbol();
        shopName.setText(shop.getName());
        }

    @Override
    public void onUpdateShopClicked() throws SQLException {
        Pair shopOverlays = getSelectedShop();
        if(shopOverlays == null) return;
        Graphic cercleGraphic = (Graphic) shopOverlays.getLeft();
        Graphic textGraphic = (Graphic) shopOverlays.getLeft();

        Point mapPoint = (Point) cercleGraphic.getGeometry();
        String shopName = ((TextSymbol) textGraphic.getSymbol()).getText();

        Shop shopToModify = Configuration.getCurrent().getShopDao().get(shopName,mapPoint);
        ShopController showShopController = new ShopController(currentStage,shopToModify,true, this);
        showShopController.show();
    }

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
        Pair shopOverlay = getSelectedShop();
        if(shopOverlay == null) return;

        ButtonType alertResult = Window.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
        if (alertResult == ButtonType.OK) {
            Graphic cerclePointOnMap = (Graphic) shopOverlay.getLeft();
            Graphic textPointOnMap = (Graphic) shopOverlay.getRight();

            TextSymbol shopName = (TextSymbol) textPointOnMap.getSymbol();
            Point shopPoint = (Point) textPointOnMap.getGeometry();

            Shop shopToDelete = Configuration.getCurrent().getShopDao().get(shopName.getText(), shopPoint);
            Configuration.getCurrent().getShopDao().delete(shopToDelete);

            shopGraphicsCercleList.getGraphics().remove(cerclePointOnMap);
            shopGraphicsTextList.getGraphics().remove(textPointOnMap);
        }
    }

    private Pair getSelectedShop(){
        GraphicsOverlay shopGraphicsCercleList = viewController.getShopGraphicsCercleList();
        GraphicsOverlay shopGraphicsTextList = viewController.getShopGraphicsTextList();

        for (int i = 0; i < shopGraphicsCercleList.getGraphics().size(); i++) {
            Graphic cerclePointOnMap = shopGraphicsCercleList.getGraphics().get(i);
            Graphic textPointOnMap = shopGraphicsTextList.getGraphics().get(i); // le symbole texte associer au point aussi

            if (cerclePointOnMap.isSelected()) {
                return new Pair(cerclePointOnMap, textPointOnMap);
            }
        }
        return null;
    }

    @Override
    public void onSearchShop(String shopName) {
        GraphicsOverlay mapTextOverlay = viewController.getShopGraphicsTextList();
        List<Graphic> mapTextGraphics = mapTextOverlay.getGraphics();

        GraphicsOverlay mapCercleOverlay = viewController.getShopGraphicsTextList();
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
     * @param mapViewPoint les coordonnées du point à mettre en evidence
     */
    public void highlightGraphicPoint(Point2D mapViewPoint) {
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
                Window.showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recupération du resultat\nContactez un responsable");
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
