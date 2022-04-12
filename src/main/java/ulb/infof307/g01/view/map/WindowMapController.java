package ulb.infof307.g01.view.map;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.shop.ShowShopController;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WindowMapController extends ViewController<WindowMapController.Listener> implements Initializable  {

// TODO: CONTEXT MENU DANS FXML ?
    private final MapServices mapServices = new MapServices();
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem addShopMenuItem = new MenuItem("Ajouter magasin");
    private final MenuItem deleteShopMenuItem = new MenuItem("Supprimer magasin");
    private final MenuItem modifyShopMenuItem = new MenuItem("Modifier magasin");

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
    void onShopSearchBoxAction(ActionEvent event) {
        String fieldText = textFieldMenuBar.getText();

        for(int index = 0; index < mapServices.getShopGraphicsTextList().size(); index++){

            Graphic textGraphicShop = mapServices.getShopGraphicsTextList().get(index);
            Graphic cercleGraphicShop = mapServices.getShopGraphicsCercleList().get(index);

            TextSymbol textSymbol = (TextSymbol) textGraphicShop.getSymbol();
            if(textSymbol.getText().contains(fieldText) || Objects.equals(fieldText, "")){
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
     *
     */
    @FXML
    private void onAddressSearchBoxAction() {

        String address = searchBox.getText();
        if (!address.isBlank()) {
            setNodeColor(searchBox,false);
            mapServices.performGeocode(address);
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
//    public void displayMain(){
//        this.loadFXML("DisplayMap.fxml");
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMapService();
        try {
            listener.onInitializeMapShop();
        } catch (SQLException e) {
            showErrorSQL();
        }

        initializeContextMenu();

        mapServices.initializeMapEvent();
        mapServices.createLocatorTaskAndDefaultParameters();
        mapViewStackPane.getChildren().add(mapServices.getMapView());
    }

    /**
     * Initialisation des magasins sur la map
     */
    private void initializeMapShop() throws SQLException {
    }

    public MenuItem getAddShopMenuItem() {
        return addShopMenuItem;
    }

    /**
     * Initialisation du Contexte menu et action possible sur celui ci
     */
    private void initializeContextMenu(){
        mapServices.getMapView().setContextMenu(contextMenu);
        contextMenu.getItems().addAll(addShopMenuItem, modifyShopMenuItem, deleteShopMenuItem);

        // context menu pour l'ajout
        addShopMenuItem.setOnAction(event -> {
            listener.onAddShopClicked();
        });

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
            for(int index = 0; index < mapServices.getShopGraphicsCercleList().size(); index++) {
                Graphic cercleGraphic = mapServices.getShopGraphicsCercleList().get(index);
                Graphic textGraphic = mapServices.getShopGraphicsTextList().get(index);

                if(cercleGraphic.isSelected()){
                    Point mapPoint = (Point) cercleGraphic.getGeometry();
                    String shopName = ((TextSymbol) textGraphic.getSymbol()).getText();
                    try {
                        Shop shopToModify = Configuration.getCurrent().getShopDao().get(shopName,mapPoint);
                        ShowShopController showShopController = new ShowShopController();
                        showShopController.createPopup(shopToModify, mapServices,true);
                    } catch (SQLException e) {
                        Window.showAlert(Alert.AlertType.ERROR,"ERROR","Erreur au niveau de la basse de donnée veillez contactez le manager");
                        e.printStackTrace();
                    }
                    break;
                }
            }

        });
    }

//    @FXML
//    public void returnMainMenu() {
//        WindowHomeController windowHomeController = new WindowHomeController();
//        windowHomeController.displayMain(primaryStage);
//    }



    public interface Listener {
        void onInitializeMapShop() throws SQLException;
        void onAddShopClicked();
        void onDeleteShopClicked() throws SQLException;
    }
}

