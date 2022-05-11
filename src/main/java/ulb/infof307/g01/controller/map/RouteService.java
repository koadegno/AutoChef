package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.map.MapViewController;

import java.util.List;
import java.util.stream.Collectors;

public class RouteService {
    public static final String ROUTE_TASK_URL = "https://route-api.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World";
    private final MapViewController mapViewController;
    private final Listener listener;
    public static final int WALKING = 4;
    public static final int AVERAGE_TIME_PEDESTRIAN = 5;
    public static final int AVERAGE_TIME_BIKE = 15;
    public static final int TIME_BY_BIKE = AVERAGE_TIME_BIKE / AVERAGE_TIME_PEDESTRIAN;


    public RouteService(MapViewController mapViewController,Listener listener){
        this.mapViewController = mapViewController;
        this.listener = listener;
    }


    public void onItinerary(Pair<Graphic, Graphic> selectedShop) {

        listener.setOnItineraryMode(true);

        if(selectedShop == null && mapViewController.getItineraryGraphicsCircleList().isEmpty()) return;


        // Si un itinéraire est déjà calculé, demande à supprimé le précédent
        boolean isDelete = deleteOldRoute();

        // Affiche le texte en fonction de ce qui est recherché
        if(isDelete){
            String text;
            Point mapPoint;
            if (mapViewController.getIfSearchDeparture()) {
                text = "Départ";
                MapView mapView = mapViewController.getMapView();
                mapPoint = cursorPoint(mapView);
            }
            else {
                text = "";
                assert selectedShop != null;
                Graphic shop = selectedShop.getLeft();
                mapPoint = (Point) shop.getGeometry();
            }
            listener.addCircle(MapController.COLOR_BLUE, text, mapPoint, false);
            mapViewController.switchVisibilityContextMenu();

            int readyToCalculRoute = 2;
            if (mapViewController.getItineraryGraphicsCircleList().size() == readyToCalculRoute) {
                calculateRoute();}
        }
    }

    /**
     * recuperer le point associé au curseur
     * @param mapView la mapView contient la fonction de calcul
     * @return le point de la map
     */
    private Point cursorPoint(MapView mapView) {
        // Il y a une correction de la position

        Point mapPoint;
        MenuItem addShopMenuItem = mapViewController.getAddShopMenuItem();
        Point2D cursorPoint2D = new Point2D(addShopMenuItem.getParentPopup().getX() + MapController.CORRECTION_POSITION_X,
                addShopMenuItem.getParentPopup().getY() + MapController.CORRECTION_POSITION_Y);
        Point2D cursorCoordinate = mapView.screenToLocal(cursorPoint2D);
        mapPoint = mapView.screenToLocation(cursorCoordinate);
        return mapPoint;
    }

    /**
     * Supprime l'itinéraire
     */
    public boolean onDeleteItinerary() {

        List<Graphic> itineraryGraphicsCercleList = mapViewController.getItineraryGraphicsCircleList();
        List<Graphic> itineraryGraphicsTextList   = mapViewController.getItineraryGraphicsTextList();

        int vide = 0;
        int itineraryIndex = 2;
        int departureIndex = 1;
        int arrival = 0;

        if (itineraryGraphicsCercleList.size() == vide) {return true;}

        ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer itinéraire ?", "Etes vous sur de vouloir supprimer l'itinéraire actuel ? ");
        if (alertResult == ButtonType.OK) {

            if (itineraryGraphicsCercleList.size() > itineraryIndex) {
                itineraryGraphicsCercleList.remove(itineraryIndex);
            }

            if (itineraryGraphicsCercleList.size() > departureIndex) {
                itineraryGraphicsCercleList.remove(departureIndex);
                itineraryGraphicsTextList.remove(departureIndex);
            }

            else { mapViewController.switchVisibilityContextMenu();}

            itineraryGraphicsCercleList.remove(arrival);
            itineraryGraphicsTextList.remove(arrival);

            listener.setOnItineraryMode(false);
            mapViewController.deleteItineraryInformation();
            return true;
        }

        return false;



    }

    public boolean deleteOldRoute() {
        int itineraryAlreadyExist = 1;
        boolean isDelete = true;
        if (mapViewController.getItineraryGraphicsCircleList().size() > itineraryAlreadyExist) { isDelete = onDeleteItinerary();}
        return isDelete;
    }

    /**
     * Calcule et affiche l'itinéraire
     */
    private void calculateRoute(){
        Graphic routeGraphic = new Graphic();
        routeGraphic.setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, MapController.COLOR_BLUE, MapController.WIDTH));

        mapViewController.getItineraryGraphicsCircleList().add(routeGraphic);
        RouteTask routeTask = new RouteTask(ROUTE_TASK_URL);
        ListenableFuture<RouteParameters> routeParametersFuture = routeTask.createDefaultParametersAsync();

        // Récupère les positions de départ et d'arrivée
        List<Stop> stops = mapViewController.getItineraryGraphicsCircleList()
                .stream()
                .filter(graphic -> graphic.getGeometry() != null)
                .map(graphic -> new Stop((Point) graphic.getGeometry()))
                .collect(Collectors.toList());

        routeParametersFuture.addDoneListener(() -> {
            try {
                RouteParameters routeParameters = routeParametersFuture.get();
                routeParameters.setStops(stops);

                //routeParameters.setReturnDirections(true);
                routeParameters.setDirectionsLanguage("fr");

                // choisis le mode de voyage
                routeParameters.setTravelMode(routeTask.getRouteTaskInfo().getTravelModes().get(WALKING));

                // calcul l'itinéraire
                ListenableFuture<RouteResult> routeResultFuture = routeTask.solveRouteAsync(routeParameters);

                // dessine l'itinéraire
                routeResultFuture.addDoneListener(() -> {
                    try {
                        RouteResult routeResult = routeResultFuture.get();
                        int firstRoute= 0;
                        Route route = routeResult.getRoutes().get(firstRoute);
                        routeGraphic.setGeometry(route.getRouteGeometry());

                        // Affiche la durée et la distance de l'itinéraire calculé
                        double totalTimeBike = route.getTotalTime() / TIME_BY_BIKE; // calcul du temps en vélo
                        mapViewController.itineraryInformation(Math.ceil(route.getTotalTime()), Math.ceil(totalTimeBike),Math.ceil(route.getTotalLength()));

                    } catch (Exception e) {
                        ViewController.showAlert(Alert.AlertType.ERROR, "Error", "Itinéraire impossible");
                        onDeleteItinerary();
                    }
                });
            } catch (Exception e) { ViewController.showAlert(Alert.AlertType.ERROR, "Error", "Problème avec l'itinéraire"); }
        });
    }

    /**
     * Vitesse moyenne de vélo calculer à partir de donnée d'internet
     * @return la vitesse moyenne de vélo
     */
    private int getTimeBike() {
        return AVERAGE_TIME_BIKE / AVERAGE_TIME_PEDESTRIAN;
    }

    public interface Listener{
        void setOnItineraryMode(boolean isOnItineraryMode);
        void addCircle(int color, String textCircle, Point coordinate, Boolean shop);
    }
}
