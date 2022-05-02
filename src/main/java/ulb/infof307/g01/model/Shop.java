package ulb.infof307.g01.model;

import com.esri.arcgisruntime.geometry.Point;

import java.util.HashSet;

/**
 * Classe représentant un magasin
 */
public class Shop extends HashSet<Product> {

    private String name;
    private final Point coordinate;
    private int id = -1;

    public Shop(int id,String name, Point coordinate){
        this(name,coordinate);
        this.id = id;
    }
    public Shop(String name, Point coordinate){
        this.name = name;
        this.coordinate = coordinate;
    }

    public Shop(Point shopPoint) {
        this.coordinate = shopPoint;
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

}
