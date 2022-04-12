package ulb.infof307.g01.controller;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.map.WindowMapController;

import java.sql.SQLException;
import java.util.List;

public class MapController extends Controller implements WindowMapController.Listener {

    public static final int COLOR_RED = 0xFFFF0000;
    public static final int TEXT_SIZE = 10;
    private static final int ONCE_CLICKED = 1;
    private static final int DOUBLE_CLICKED = 2;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int ADDRESS_SIZE = 18;
    public static final float ADDRESS_MARKER_SIZE = 12.0f;
    private WindowMapController viewController;

    public MapController() {
    }

    public void start(Stage primaryStage){
        this.setStage(primaryStage);
        FXMLLoader loader = this.loadFXML("DisplayMap.fxml");
        viewController = loader.getController();
        viewController.setListener(this);
        this.setNewScene(loader,"Carte");
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

        //TODO cree un controlleur pour le shop et l'appeler avec le point que tu veux ajouter
        ShopController shopController = new ShopController(new Shop(mapPoint),this,false);
    }

    /**
     * Ajout d'un point avec son texte sur la map
     *
     * @param shopToAdd le nouveau magasin a ajouter
     */
    public void addShop(Shop shopToAdd) {
        //crée un cercle rouge
        SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, COLOR_RED, TEXT_SIZE);
        // cree un texte attacher au point
        TextSymbol pierTextSymbol =
                new TextSymbol(
                        TEXT_SIZE, shopToAdd.getName(), COLOR_BLACK,
                        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);

        Graphic circlePoint = new Graphic(shopToAdd.getCoordinate(), redCircleSymbol);
        Graphic textPoint = new Graphic(shopToAdd.getCoordinate(), pierTextSymbol);

        // ajoute des graphiques à l'overlay
        viewController.getShopGraphicsCercleList().getGraphics().add(circlePoint);
        viewController.getShopGraphicsTextList().getGraphics().add(textPoint);
    }

    /**
     * Met a jour le shop afficher sur la carte
     * @param shop le magasin existant qu'il faut mettre a jour
     */
    public void updateShop(Shop shop){
        GraphicsOverlay shopGraphicsCercleList = viewController.getShopGraphicsCercleList();
        GraphicsOverlay shopGraphicsTextList = viewController.getShopGraphicsTextList();

        for(int index=0;index < shopGraphicsCercleList.getGraphics().size();index++ ) {
            Graphic cercleGraphic = shopGraphicsCercleList.getGraphics().get(index);
            Graphic textGraphic = shopGraphicsTextList.getGraphics().get(index);

            if (cercleGraphic.isSelected()) {
                ((TextSymbol) textGraphic.getSymbol()).setText(shop.getName());
            }
        }
    }

    /**
     * Supprime les points selectioner de l'overlay
     *
     */
    public void onDeleteShopClicked() throws SQLException {
        GraphicsOverlay shopGraphicsCercleList = viewController.getShopGraphicsCercleList();
        GraphicsOverlay shopGraphicsTextList = viewController.getShopGraphicsTextList();

        for (int i = 0; i < shopGraphicsCercleList.getGraphics().size(); i++) {
            Graphic cerclePointOnMap = shopGraphicsCercleList.getGraphics().get(i);
            Graphic textPointOnMap = shopGraphicsTextList.getGraphics().get(i); // le symbole texte associer au point aussi

            if (cerclePointOnMap.isSelected()) {
                ButtonType alertResult = Window.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
                if (alertResult == ButtonType.OK) {
                    TextSymbol textSymbol = (TextSymbol) textPointOnMap.getSymbol();
                    Point mapPoint = (Point) textPointOnMap.getGeometry();

                    Shop shopToDelete = Configuration.getCurrent().getShopDao().get(textSymbol.getText(), mapPoint);
                    Configuration.getCurrent().getShopDao().delete(shopToDelete);

                    shopGraphicsCercleList.getGraphics().remove(cerclePointOnMap);
                    shopGraphicsTextList.getGraphics().remove(textPointOnMap);
                }
                break; // tu as deja accomplie la tache que tu devais
            }
        }
    }
}
