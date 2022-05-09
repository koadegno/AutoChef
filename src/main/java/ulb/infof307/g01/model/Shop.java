package ulb.infof307.g01.model;

import com.esri.arcgisruntime.geometry.Point;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Shop  {

    private String name;
    private final Point coordinate;
    private int id = -1;
    private final HashSet<Product> productHashSet;

    public Shop(int id,String name, Point coordinate){
        this(name,coordinate);
        this.id = id;
    }
    public Shop(String name, Point coordinate){
        this.name = name;
        this.coordinate = coordinate;
        this.productHashSet = new HashSet<>();
    }

    public Shop(Point shopPoint) {
        this(null,shopPoint);
    }

    public void setName(String shopName){
        name = shopName;
    }

    public int size(){
        return productHashSet.size();
    }

    /**
     * supprime un produit du magasin
     * @param product le produit a supprimer
     * @return Vrai si le produit est dans le magasin
     */
    public boolean remove(Product product){
        return productHashSet.remove(product);
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
        for(Product product:productHashSet){
            toPrint.append(product);
        }
        return toPrint.toString();
    }

    public boolean addAll(List<Product> productList){
        return productHashSet.addAll(productList);
    }

    /**
     * Ajoute le produit si n'est pas deja dedans
     * @param product le produit
     * @return Vrai si le produit n'est pas deja dans le magasin
     */
    public boolean add(Product product){
        return productHashSet.add(product);
    }

    public HashSet<Product> getProduct() {
        return productHashSet;
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
