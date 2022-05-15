package ulb.infof307.g01.model;

/**
 * Classe représentant un Produit Alimentaire/Ingrédient
 */
public class Product {

    private String name;
    private int quantity;
    private double price;
    private String nameUnity;
    private String familyProduct;

    /**
     * Builder pour construire des produits
     */
    public static class ProductBuilder {
        private String name;
        private int quantity = 1;
        private double price = 0;
        private String nameUnity = "Unité";
        private String familyProduct = "Famille";

        public ProductBuilder withName(String name){
            this.name = name;
            return this;
        }

        public ProductBuilder withQuantity(int quantity){
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder withNameUnity(String nameUnity){
            this.nameUnity = nameUnity;
            return this;
        }

        public ProductBuilder withFamilyProduct(String familyProduct){
            this.familyProduct = familyProduct;
            return this;
        }

        public ProductBuilder withPrice(double price){
            this.price = price;
            return this;
        }

        public Product clone(Product other){
            return new Product.ProductBuilder().withFamilyProduct(other.familyProduct).withName(other.name).withNameUnity(other.nameUnity).withQuantity(other.quantity).withPrice(other.price).build();
        }

        public Product build(){
            Product product = new Product();
            product.name = name;
            product.nameUnity = nameUnity;
            product.familyProduct = familyProduct;
            product.quantity = quantity;
            product.price = price;
            return product;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void rename(String newName) {
        name = newName;
    }

    /**
     * Décrémente la quantité du Produit de 1, sauf si la quantité est déjà égale à 1
     * @return True si la quantité à effectivement été décrémenté, False sinon
     */
    public boolean decreaseQuantity() {
        if (quantity == 1)
            return false;
        quantity--;
        return true;
    }

    /**
     * Augmente la quantité du Produit de 1
     */
    public void increaseQuantity(int quantity) {
        this.quantity+=quantity;
    }

    public void increaseQuantity(){
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

    public String getFamilyProduct() { return familyProduct; }

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


    public void setNameUnity(String nameUnity) {
        this.nameUnity = nameUnity;
    }

    @Override
    public String toString(){
        return name + " "+price+"€/$";
    }


    public void setPrice(double price) {
        this.price = price;
    }
}

