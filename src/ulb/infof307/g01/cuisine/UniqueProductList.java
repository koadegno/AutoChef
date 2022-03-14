package ulb.infof307.g01.cuisine;

import java.util.Vector;

/**
 * Classe Abstraite représentant un Vecteur de {@code Product}, dans laquelle il n'y a pas de doublon possible
 * @see Product
 */
abstract class UniqueProductList extends Vector<Product> {

    /**
     *  Ajoute le {@code Product} p à la liste, si p est déjà dans la liste, le {@code Product} n'est pas ajouté en double,
     *  sa quantité est seulement incrémentée
     * @return Toujours True
     */
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

    /**
     *  Méthode inaccessible
     */
    @Override
    public void copyInto(Object[] anArray) {
        throw new RuntimeException("Not usable");
    }

    /**
     *  Méthode inaccessible
     */
    @Override
    public void insertElementAt(Product p, int index) {
        throw new RuntimeException("Not usable");
    }

    /**
     *  Méthode inaccessible
     */
    @Override
    public Product remove(int index) {
        throw new RuntimeException("Not usable");
    }

    /**
     * Si la quantité du produit est supérieure à 1, la quantité est décrémentée
     * si la quantité est à 1, le {@code Product} est supprimé de la liste
     * @return True si le produit était dans la liste, False sinon
     */
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
