package ulb.infof307.g01.model;

import java.util.HashSet;


/**
 * Classe Abstraite représentant un Vecteur de {@code Product}, dans laquelle il n'y a pas de doublon possible
 * @see Product
 */
abstract class ProductHashSet extends HashSet<Product> {

    /**
     *  Ajoute le {@link Product} p à la liste, si p est déjà dans la liste, le {@link Product} n'est pas ajouté en double,
     *  sa quantité est seulement incrémentée d'une unité
     * @return Toujours True
     */
    @Override
    public boolean add(Product p) {
        if(contains(p)) {
            this.getProduct(p).increaseQuantity(p.getQuantity());
        }
        else {
            super.add(new Product.ProductBuilder().clone(p));
        }
        return true;
    }


    /**
     * Si la quantité du produit est supérieure à 1, la quantité est décrémentée
     * si la quantité est à 1, le {@link Product} est supprimé de la liste
     * @return True si le produit était dans la liste, False sinon
     */
    @Override
    public boolean remove(Object object) {
        if (contains(object)) {
            Product product = getProduct((Product)object);
            if(!product.decreaseQuantity())
                super.remove(object);
            return true;
        }
        return false;
    }

    /**
     * Recherche et renvoi un {@link Product} présent dans le {@link HashSet}.
     * Nécessaire car Product.equals() ne prend pas en compte la quantité
     * @param product Le produit qui sera recherché
     * @return Le {@link Product} s'il est trouvé, {@code null} sinon
     */
    private Product getProduct(Product product) {
        for (Product p : this) {
            if (p.equals(product))
                return p;
        }
        return null;
    }
}
