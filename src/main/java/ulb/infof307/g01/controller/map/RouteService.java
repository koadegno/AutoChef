package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.map.MapViewController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Classe permettant d'utiliser le service de localisation de route de ARCGIS
 */
public class RouteService{
    public static final String ROUTE_TASK_URL = "https://route-api.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World";
    private final MapViewController mapViewController;
    private final Listener listener;
    public static final int WALKING = 4;
    public static final int AVERAGE_TIME_PEDESTRIAN = 5;
    public static final int AVERAGE_TIME_BIKE = 15;
    public static final int TIME_BY_BIKE = AVERAGE_TIME_BIKE / AVERAGE_TIME_PEDESTRIAN;
    private final RouteTask routeTask;
    
    private double totalTime;
    private double totalTimeBike;
    private long totalLength;


    public RouteService(MapViewController mapViewController,Listener listener){
        this.mapViewController = mapViewController;
        this.listener = listener;
        routeTask = new RouteTask(ROUTE_TASK_URL);
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getTotalTimeBike() {
        return totalTimeBike;
    }

    public long getTotalLength() {
        return totalLength;
    }

    /**
     * Dessine un point bleu pour chaque point de depart et lance le calcule de la route quand c'est
     * prêt
     * @param selectedShop le magasin sélectionné par l'utilisateur
     * @param mapPoint un point sur la map sélectionné par l'utilisateur
     * @param itineraryCircleList La liste des objets cercle graphique associés à l'itinéraire
     * @param itineraryTextList La liste des objets texte graphique associés à l'itinéraire
     * @param isDeparture Vrai si c'est le départ qui doit être fait
     */
    public void itinerary(Pair<Graphic, Graphic> selectedShop, Point mapPoint, List<Graphic> itineraryCircleList, List<Graphic> itineraryTextList, boolean isDeparture) {

        if(selectedShop == null && itineraryCircleList.isEmpty()) return;

        // Si un itinéraire est déjà calculé, demande à supprimé le précédent
        boolean isDelete = deleteOldRoute(itineraryCircleList,itineraryTextList);

        // Affiche le texte en fonction de ce qui est recherché
        if(isDelete){
            String text = "";
            if (isDeparture) {text = "Départ";}
            else {
                assert selectedShop != null;
                Graphic shop = selectedShop.getLeft();
                mapPoint = (Point) shop.getGeometry();
            }
            listener.addCircle(MapConstants.COLOR_BLUE, text, mapPoint, false);
        }
    }

    /**
     *  Supprime l'itinéraire
     * @return Vrai si l'itinéraire est supprimé
     * @param itineraryGraphicsCercleList La liste des objets cercle graphique associés à l'itinéraire
     * @param itineraryGraphicsTextList La liste des objets texte graphique associés à l'itinéraire
     */
    public boolean onDeleteItinerary(List<Graphic> itineraryGraphicsCercleList, List<Graphic> itineraryGraphicsTextList) {
        int itineraryIndex = 2;
        int departureIndex = 1;
        int arrival = 0;

        if (itineraryGraphicsCercleList.isEmpty()) {return true;}

        ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer itinéraire ?", "Etes vous sur de vouloir supprimer l'itinéraire actuel ? ");
        if (alertResult == ButtonType.OK) {

            if (itineraryGraphicsCercleList.size() > itineraryIndex) {
                itineraryGraphicsCercleList.remove(itineraryIndex);
            }

            if (itineraryGraphicsCercleList.size() > departureIndex) {
                itineraryGraphicsCercleList.remove(departureIndex);
                itineraryGraphicsTextList.remove(departureIndex);
            }

            else { mapViewController.switchVisibilityContextMenu();} //TODO ne devrait pas etre la

            itineraryGraphicsCercleList.remove(arrival);
            itineraryGraphicsTextList.remove(arrival);

            return true;
        }

        return false;



    }

    /**
     * supprime l'ancien itinéraire s'il existe
     * @return Vrai si l'itinéraire est supprimé
     * @param itineraryCircleList La liste des objets cercle graphique associés à l'itinéraire
     * @param itineraryTextList La liste des objets texte graphique associés à l'itinéraire
     */
    private boolean deleteOldRoute(List<Graphic> itineraryCircleList, List<Graphic> itineraryTextList) {
        int itineraryAlreadyExist = 1;
        boolean isDelete = true;
        if (itineraryCircleList.size() > itineraryAlreadyExist) { isDelete = onDeleteItinerary(itineraryCircleList,itineraryTextList);}
        return isDelete;
    }

    /**
     * Calcule et affiche l'itinéraire
     * @return Vrai si une route a été trouvé
     * @param itineraryCircleList La liste des objets graphique associés à l'itinéraire
     */
    public boolean calculateRoute(List<Graphic> itineraryCircleList){
        AtomicBoolean hasFoundRoute = new AtomicBoolean(false);

        Graphic routeGraphic = new Graphic();
        routeGraphic.setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, MapConstants.COLOR_BLUE, MapConstants.WIDTH));
        itineraryCircleList.add(routeGraphic);

        ListenableFuture<RouteParameters> routeParametersFuture = routeTask.createDefaultParametersAsync();

        // Récupère les positions de départ et d'arrivée
        List<Stop> stops = itineraryCircleList
                .stream()
                .filter(graphic -> graphic.getGeometry() != null)
                .map(graphic -> new Stop((Point) graphic.getGeometry()))
                .collect(Collectors.toList());

        // attendre que routeParametersFuture aie finie son travail
        try {
            routeParametersFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            return hasFoundRoute.get();
        }

        setRouteParametersListener(hasFoundRoute, routeGraphic, routeParametersFuture, stops);

        return hasFoundRoute.get();

    }

    /**
     * lancer le processus de recherche de route
     * @param hasFoundRoute valeur de retour : Vrai si une route a été trouvé
     * @param routeGraphic valeur de retour : Contient le chemin, la route entre les stops
     * @param routeParametersFuture les paramètres nécessaires pour calculer la route
     * @param stops la liste des stops (point de départ et d'arrivée)
     */
    private void setRouteParametersListener(AtomicBoolean hasFoundRoute, Graphic routeGraphic, ListenableFuture<RouteParameters> routeParametersFuture, List<Stop> stops) {
        routeParametersFuture.addDoneListener(()->{
            try {
                RouteParameters routeParameters = routeParametersFuture.get();
                routeParameters.setStops(stops);

                // choisis le mode de voyage
                routeParameters.setTravelMode(routeTask.getRouteTaskInfo().getTravelModes().get(WALKING));

                // calcul l'itinéraire
                ListenableFuture<RouteResult> routeResultFuture = routeTask.solveRouteAsync(routeParameters);

                // attendre que routeResultFuture aie finie son travail
                routeResultFuture.get();

                // dessine l'itinéraire
                routeResultFuture.addDoneListener(() -> {
                    try {
                        RouteResult routeResult = routeResultFuture.get();
                        int firstRoute= 0;
                        Route route = routeResult.getRoutes().get(firstRoute);
                        routeGraphic.setGeometry(route.getRouteGeometry());

                        // Affiche la durée et la distance de l'itinéraire calculé
                        // calcul du temps en vélo
                        totalTime = Math.round(route.getTotalTime());
                        totalTimeBike = Math.round(route.getTotalTime() / TIME_BY_BIKE);
                        totalLength = Math.round(route.getTotalLength());
                        System.out.println("moi j'ai fini");
                        hasFoundRoute.set(true);

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        hasFoundRoute.set(false);
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                hasFoundRoute.set(false);
            }

        });
    }



    public interface Listener{
        void addCircle(int color, String textCircle, Point coordinate, Boolean shop);
    }
}
