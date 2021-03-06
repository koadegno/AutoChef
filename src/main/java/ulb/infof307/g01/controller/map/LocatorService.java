package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;


public class LocatorService {

    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;

    public static final String GEOCODE_URL_TASK = "https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";


    public LocatorService() {
        createLocatorTaskAndDefaultParameters();
    }

    public ListenableFuture<List<GeocodeResult>> getGeocodeAsync(String address) {return locatorTask.geocodeAsync(address, geocodeParameters);}


    /**
     * Utilisation du service de geocoding(coordonné GPS associer a un lieu des infos) de ArcGis
     * Pour paramètrer le service de geocoding Locator
     * et Paramètre par défaut du service de geocoding
     */
    void createLocatorTaskAndDefaultParameters() {
        locatorTask = new LocatorTask(GEOCODE_URL_TASK);

        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*"); // permet de retourner tous les attributs
        geocodeParameters.setMaxResults(1);
        // comment les coordonnées doivent correspondre a la location
        geocodeParameters.setOutputSpatialReference(SpatialReferences.getWebMercator());
    }

    public Point convertAddressToPoint(String address) throws NullPointerException{
        List<Graphic> fakeAddressOverlay = new ArrayList<>();
        Point addressPoint = performGeocode(address,fakeAddressOverlay);
        if(addressPoint == null) throw new NullPointerException("Il n'y a pas de coordonnée correspondant a cette adresse");
        return addressPoint;
    }

    /**
     * Recherche les coordonnées et les infos complètes qui correspond le mieux à l'adresse
     * et affiche le résultat sur la map
     *
     * @param address une vraie adresse ex : Avenue Franklin Roosevelt 50 - 1050 Bruxelles
     */
    public Point performGeocode(String address,List<Graphic> addressGraphicsOverlay) {
        ListenableFuture<List<GeocodeResult>> geocodeResults = getGeocodeAsync(address);

        // attendre que le geocode se fasse
        try {
            geocodeResults.get();
        } catch (InterruptedException | ExecutionException e) {
           return null;
        }

        return setGeocode(geocodeResults,addressGraphicsOverlay);
    }

    /**
     * ajoute le listener au geocodeResults
     * @param geocodeResults résultat de la recherche
     */
    private Point setGeocode(ListenableFuture<List<GeocodeResult>> geocodeResults,List<Graphic> addressGraphicsOverlay) {
        AtomicReference<Point> addressPosition = new AtomicReference<>(null);
        geocodeResults.addDoneListener(() -> {  // récupérer le résultat
            try {
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) { // trouver qlq chose
                    GeocodeResult result = geocodes.get(0);
                    addressPosition.set(displayResult(result, addressGraphicsOverlay));
                }
            } catch (InterruptedException | ExecutionException exception) {
                addressPosition.set(null);
            }
        });
        return addressPosition.get();
    }

    /**
     * Crée et afficher l'objet graphique associé à une recherche d'adresse sur la map
     *
     * @param geocodeResult le résultat d'une recherche
     */
    private Point displayResult(GeocodeResult geocodeResult,List<Graphic> addressGraphicsOverlay ) {
        addressGraphicsOverlay.clear();

        // creation de l'objet graphique avec l'adresse
        String label = geocodeResult.getLabel();
        TextSymbol textSymbol = new TextSymbol(MapConstants.ADDRESS_SIZE,
                label,
                MapConstants.COLOR_BLACK,
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.BOTTOM);
        Point displayLocation = geocodeResult.getDisplayLocation();
        Graphic textGraphic = new Graphic(displayLocation, textSymbol);
        addressGraphicsOverlay.add(textGraphic);

        // creation de l'objet graphique avec le carré rouge
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE,
                MapConstants.COLOR_RED,
                MapConstants.ADDRESS_MARKER_SIZE);

        Graphic markerGraphic = new Graphic(displayLocation, geocodeResult.getAttributes(), markerSymbol);
        addressGraphicsOverlay.add(markerGraphic);

        return displayLocation;
    }
}
