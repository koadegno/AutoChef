import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty produc;
    private SimpleStringProperty quantity;

    public Product(String produc, String quantity) {
        this.produc = new SimpleStringProperty(produc);
        this.quantity = new SimpleStringProperty(quantity);
    }

    public String getQuantity() {
        return this.quantity.get();
    }
    public String getProduc() {
        return this.produc.get();
    }

}
