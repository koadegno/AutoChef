package ulb.infof307.g01.model;

import com.esri.arcgisruntime.geometry.Point;

import java.util.HashSet;

public class Shop extends HashSet<Product> {

    private final String name;
    private final Point coordinate;

    public Shop(String name, Point coordinate){
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public Point getCoordinate() {
        return coordinate;
    }

}
