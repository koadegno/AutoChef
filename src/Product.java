public class Product {
    private String name;
    private int quantity = 1;
    private String nameUnity;

    public Product(String productName) {
        name = productName;
    }

    public Product(String productName, int quantity) {
        name = productName;
        this.quantity = quantity;
    }

    public Product(String productName, int quantity, String _nameUnity) {
        name = productName;
        this.quantity = quantity;
        this.nameUnity = _nameUnity;
    }

    public void rename(String newName) {
        name = newName;
    }

    public boolean decrease() {
        if (quantity == 1)
            return false;
        quantity--;
        return true;
    }

    public void increase() {
        quantity++;
    }

    public void changeQuantity(int newQuantity) {
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

    public String getNameUnity(){return nameUnity;}
    @Override
    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        Product product = (Product)other;

        return this.getName().equals(product.getName());
    }
}

