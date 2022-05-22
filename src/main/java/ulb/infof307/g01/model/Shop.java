package ulb.infof307.g01.model;

import com.esri.arcgisruntime.geometry.Point;
import ulb.infof307.g01.controller.map.LocatorService;



/**
 * Classe représentant un magasin
 */
public class Shop extends ProductHashSet{

    private String name;
    private Point coordinate;
    private String address;
    private int id = -1;
    private LocatorService locatorService;
    public static class ShopBuilder{
        private String name = "";
        private Point coordinate = null;
        private String address = "";
        private int id = -1;



        public ShopBuilder withName(String name){
            this.name = name;
            return this;
        }

        public ShopBuilder withAddress(String address){
            this.address = address;
            return this;
        }

        public ShopBuilder withCoordinate(Point coordinate){
            this.coordinate = coordinate;
            return this;
        }

        public ShopBuilder withID(int id){
            this.id = id;
            return this;
        }

        public Shop build(){
            Shop shop = new Shop();
            shop.locatorService = new LocatorService();
            shop.id = id;
            shop.address = address;
            shop.name =name;
            if((!address.isBlank() || !address.isEmpty()) && coordinate == null){
                shop.coordinate = shop.locatorService.convertAddressToPoint(address);
            }
            else{
                shop.coordinate = coordinate;
            }
            return shop;
        }
    }

    public Shop(){}

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

    public void setAddress(String shopAddress) throws NullPointerException {
        address = shopAddress;
        coordinate = locatorService.convertAddressToPoint(shopAddress);
    }


}
