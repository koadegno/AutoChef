package ulb.infof307.g01.model;

import com.esri.arcgisruntime.geometry.Point;
import ulb.infof307.g01.controller.map.LocatorService;

import java.util.HashSet;

/**
 * Classe représentant un magasin
 */
public class Shop extends HashSet<Product> {

    private String name;
    private Point coordinate;
    private String address;
    private int id = -1;
    private LocatorService locatorService;

    public Shop(){
        this.coordinate = null;
    }

    public Shop(int id,String name, Point coordinate){
        this(name,coordinate);
        this.id = id;
    }
    public Shop(String name, Point coordinate){
        this.name = name;
        this.coordinate = coordinate;
    }

    public Shop(Point shopPoint) {
        this(null,shopPoint);
    }

    public Shop(String name, String address) {
        this.name = name;
        this.address = address;
        locatorService = new LocatorService();
        coordinate = locatorService.convertAddressToPoint(address);
    }

    public void setName(String shopName){
        name = shopName;
    }

    public double getCoordinateX() { return coordinate.getX(); }

    public double getCoordinateY() {
        return coordinate.getY();
    }

    public String getName() {
        return name;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public int getID() {
        return id;
    }

    public String toString(){
        StringBuilder toPrint = new StringBuilder(coordinate + String.format("name : %s (id : %d)\n", name,id));
        for(Product product:this){
            toPrint.append(product);
        }
        return toPrint.toString();
    }

    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        Shop otherShop = (Shop)other;

        return this.name.equals(otherShop.name) && (this.coordinate.getX() == otherShop.coordinate.getX()) && this.coordinate.getY() == otherShop.coordinate.getY();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String shopAddress) {
        address = shopAddress;
        coordinate = locatorService.convertAddressToPoint(shopAddress);
    }
}
