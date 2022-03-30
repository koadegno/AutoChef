package ulb.infof307.g01.model;

import com.esri.arcgisruntime.geometry.Point;

public class Shop {

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
