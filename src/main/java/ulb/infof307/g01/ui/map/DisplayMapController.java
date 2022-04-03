package ulb.infof307.g01.ui.map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.WindowHomeController;
import ulb.infof307.g01.ui.shop.ShowShopController;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class DisplayMapController extends Window implements Initializable {


    private final MapTools mapTools = new MapTools();
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem addShopMenuItem = new MenuItem("Add Shop");
    private final MenuItem deleteShopMenuItem = new MenuItem("Delete Shop");
    private final MenuItem modifieShopMenuItem = new MenuItem("Modifie Shop");

    @FXML
    private Pane mapViewStackPane;

    @FXML
    private TextField textFieldMenuBar;

    @FXML
    private TextField searchBox;
    private List<Shop> allShopList;

    @FXML
    void onShopSearchBoxAction(ActionEvent event) {
        //TODO faire la requete a la db et afficher sur la maps les magasins
        //TODO ou alors avoir un combo box et il selectionne un elem qui l'emmene au bonne endroit sur la carte

        String fieldText = textFieldMenuBar.getText();

        for(int index = 0; index < mapTools.getShopGraphicsTextList().size(); index++){

            Graphic textGraphicShop = mapTools.getShopGraphicsTextList().get(index);
            Graphic cercleGraphicShop = mapTools.getShopGraphicsCercleList().get(index);

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
            mapTools.performGeocode(address);
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
        try {
            initializeMapShop();
        } catch (SQLException e) {
            Window.showAlert(Alert.AlertType.ERROR,"ERROR","Erreur au niveau de la basse de donnée veillez contactez le manager");
            e.printStackTrace();
        }
        initializeContextMenu();

        mapTools.initializeMapEvent();
        mapTools.createLocatorTaskAndDefaultParameters();
        mapViewStackPane.getChildren().add(mapTools.getMapView());

    }

    /**
     * Initialisation des magasins sur la map
     */
    private void initializeMapShop() throws SQLException {
        // TODO Recuperer la liste de Magasin de la db
        allShopList = Configuration.getCurrent().getShopDao().getShops();
        for(Shop shop: allShopList){
            mapTools.addPointToOverlay(shop);
        }
    }

    /**
     * Initialisation du Contexte menu et action possible sur celui ci
     */
    private void initializeContextMenu(){
        mapTools.getMapView().setContextMenu(contextMenu);
        contextMenu.getItems().addAll(addShopMenuItem, modifieShopMenuItem, deleteShopMenuItem);
        addShopMenuItem.setOnAction(event -> {
            mapTools.getMapView().setCursor(Cursor.DEFAULT);
            // TODO NOMBRE MAGIQUE
            // les nombres sont la pour corriger la position du curseur
            Point2D cursorPoint2D = new Point2D(addShopMenuItem.getParentPopup().getX() + 10, addShopMenuItem.getParentPopup().getY() + 5);
            Point2D cursorPoint2D2 = mapTools.getMapView().screenToLocal(cursorPoint2D);

            mapTools.setShopOnMap(cursorPoint2D2);
        });

        deleteShopMenuItem.setOnAction(event -> {
            mapTools.deleteGraphicPoint(); //

        });

        modifieShopMenuItem.setOnAction(event -> {
            for(int i = 0; i < mapTools.getShopGraphicsCercleList().size(); i++) {
                Graphic cercleGraphic = mapTools.getShopGraphicsCercleList().get(i);
                if(cercleGraphic.isSelected()){
                    // TODO recup l'id et lancer la pop ip avec le bonne id
                    //POPUP SHOP
                    ShowShopController showShopController = new ShowShopController();
                    int id = 1; //TODO: seulement pour tester
                    showShopController.createPopup(id);
                    break;
                }
            }

        });
    }

    @FXML
    public void returnMainMenu() {
        WindowHomeController windowHomeController = new WindowHomeController();
        windowHomeController.displayMain(primaryStage);
    }


}

