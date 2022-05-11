package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.scene.control.Alert;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.map.MapViewController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;



public class LocatorService {

    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;

    public static final String GEOCODE_URL_TASK = "https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";
    public MapViewController mapViewController;


    public LocatorService( MapViewController mapViewController) {
        this.mapViewController = mapViewController;
        createLocatorTaskAndDefaultParameters();
    }

    public ListenableFuture<List<GeocodeResult>> getGeocodeAsync(String address) {return locatorTask.geocodeAsync(address, geocodeParameters);}


    /**
     * Utilisation du service de geocoding(coordonné GPS associer a un lieu des infos) de ArcGis
     * Pour parametrer le service de geocoding Locator
     * et Parametre par defaut du service de geocoding
     */
    void createLocatorTaskAndDefaultParameters() {
        locatorTask = new LocatorTask(GEOCODE_URL_TASK);

        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*"); // permet de retourner tous les attributs
        geocodeParameters.setMaxResults(1);
        // comment les coordonnées doivent correspondre a la location
        geocodeParameters.setOutputSpatialReference(mapViewController.getSpatialReference());
    }

    /**
     * Recherche les coordonnées et les infos complètes qui correspond le mieux à l'adresse
     * et affiche le résultat sur la map
     *
     * @param address une vraie adresse ex : Avenue Franklin Roosevelt 50 - 1050 Bruxelles
     */
    public boolean performGeocode(String address) {
        AtomicBoolean isFound= new AtomicBoolean(false); // tu utilises ça, car le geocodeResults est exécuté de manière asynchrone il te faut donc un type atomique
        ListenableFuture<List<GeocodeResult>> geocodeResults = getGeocodeAsync(address);

        try {
            geocodeResults.get();
        } catch (InterruptedException | ExecutionException e) {
           return isFound.get();
        }
        setGeocode(isFound, geocodeResults);

        return isFound.get();
    }

    /**
     * ajoute le listener au geocodeResults
     * @param isGeocodeFound stock si la recherche est fructueuse ou non
     * @param geocodeResults résultat de la recherche
     */
    private void setGeocode(AtomicBoolean isGeocodeFound, ListenableFuture<List<GeocodeResult>> geocodeResults) {
        geocodeResults.addDoneListener(() -> {  // récupérer le résultat
            try {
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) { // trouver qlq chose
                    GeocodeResult result = geocodes.get(0);
                    displayResult(result);
                    isGeocodeFound.set(true);

                } else {
                    // pas d'adresse trouvée
                    isGeocodeFound.set(false);

                }
            } catch (InterruptedException | ExecutionException exception) {
                isGeocodeFound.set(false);

            }
        });
    }

    /**
     * Crée et afficher l'objet graphique associé à une recherche d'adresse sur la map
     *
     * @param geocodeResult le résultat d'une recherche
     */
    private void displayResult(GeocodeResult geocodeResult) {
        List<Graphic> addressGraphicsOverlay = mapViewController.getAddressGraphicsOverlay();
        addressGraphicsOverlay.clear();

        // creation de l'objet graphique avec l'adresse
        String label = geocodeResult.getLabel();
        TextSymbol textSymbol = new TextSymbol(MapController.ADDRESS_SIZE,
                label,
                MapController.COLOR_BLACK,
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.BOTTOM);
        Point displayLocation = geocodeResult.getDisplayLocation();
        Graphic textGraphic = new Graphic(displayLocation, textSymbol);
        addressGraphicsOverlay.add(textGraphic);

        // creation de l'objet graphique avec le carré rouge
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE,
                MapController.COLOR_RED,
                MapController.ADDRESS_MARKER_SIZE);

        Graphic markerGraphic = new Graphic(displayLocation, geocodeResult.getAttributes(), markerSymbol);
        addressGraphicsOverlay.add(markerGraphic);

        mapViewController.setViewPointCenter(displayLocation);
    }
}
