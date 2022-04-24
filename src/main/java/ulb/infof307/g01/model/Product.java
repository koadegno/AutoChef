package ulb.infof307.g01.model;

/**
 * Classe représentant un Produit Alimentaire/Ingrédient
 */
public class Product {

    private String name;
    private int quantity = 1;
    private double price = 0;
    private String nameUnity = "Unité";
    private String famillyProduct = "Famille";

    public Product(String productName) {
        name = productName;
    }

    public Product(String productName, int quantity) {
        name = productName;
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @param productName Nom du produit
     * @param quantity Quantité du produit
     * @param nameUnity Unité de la quantité du produit
     */
    public Product(String productName, int quantity, String nameUnity) {
        name = productName;
        this.quantity = quantity;
        this.nameUnity = nameUnity;
    }

    public Product(String productName, double price) {
        name = productName;
        this.price = price;
    }

    public Product(String productName, int quantity, String nameUnity,String famillyProduct, double price) {
        name = productName;
        this.quantity = quantity;
        this.nameUnity = nameUnity;
        this.famillyProduct = famillyProduct;
        this.price = price;
    }

    public Product(String productName, int quantity, String nameUnity,String famillyProduct) {
        name = productName;
        this.quantity = quantity;
        this.nameUnity = nameUnity;
        this.famillyProduct = famillyProduct;
    }

    public Product(String productName,  String nameUnity,String famillyProduct) {
        name = productName;
        this.nameUnity = nameUnity;
        this.famillyProduct = famillyProduct;
    }

    public Product(Product other) {
        this.name = other.name;
        this.quantity = other.quantity;
        this.nameUnity = other.nameUnity;
        this.famillyProduct = other.famillyProduct;
        this.price = other.price;
    }

    public void rename(String newName) {
        name = newName;
    }

    /**
     * Décrémente la quantité du Produit de 1, sauf si la quantité est déjà égale à 1
     * @return True si la quantité à effectivement été décrémenté, False sinon
     */
    public boolean decrease() {
        if (quantity == 1)
            return false;
        quantity--;
        return true;
    }

    /**
     * Augmente la quantité du Produit de 1
     */
    public void increase(int quantity) {
        this.quantity+=quantity;
    }

    public void increase(){
        quantity++;
    }

    /**
     * Modifie la quantité avec {@code newQuantity}, si la valeur est inférieure à 1, la quantité est mise à 1
     */
    public void setQuantity(int newQuantity) {
        if (newQuantity > 0)
            quantity = newQuantity;
        else
            quantity = 1;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getFamillyProduct() { return famillyProduct; }

    public String getNameUnity(){return nameUnity;}

    public double getPrice(){return price;}

    /**
     * Compare 2 objets
     */
    @Override
    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        Product product = (Product)other;

        return this.getName().equals(product.getName());
    }

    public Product clone() {
        return new Product(name, quantity, nameUnity, famillyProduct, price);
    }
    public void setNameUnity(String nameUnity) {
        this.nameUnity = nameUnity;
    }

    @Override
    public String toString(){
        return name + " "+price+"€/$";
    }
}

