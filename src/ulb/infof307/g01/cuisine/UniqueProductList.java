package ulb.infof307.g01.cuisine;

import java.util.Vector;

abstract class UniqueProductList extends Vector<Product> {

    @Override
    public boolean add(Product p) {
        int indexProduct = this.indexOf(p);

        if (indexProduct < 0)
            super.add(p);
        else
            this.get(indexProduct).increase();
        return true;
    }

    @Override
    public void add(int index, Product product) {
        int indexProduct = this.indexOf(product);

        if (indexProduct < 0)
            super.add(index , product);
        else
            this.get(indexProduct).increase();
    }

    @Override
    public void copyInto(Object[] anArray) {
        throw new NotImplementedException("Not usable");
    }

    @Override
    public void insertElementAt(Product p, int index) {
        throw new NotImplementedException("Not usable");
    }

    @Override
    public Product remove(int index) {
        throw new NotImplementedException("Not usable");
    }

    @Override
    public boolean remove(Object p) {
        int indexProduct = this.indexOf(p);

        // Object not in list
        if (indexProduct < 0)
            return false;

        // If product.quantity == 1 : Removing product from list
        if(!this.get(indexProduct).decrease())
            super.remove(p);

        return true;
    }
}
