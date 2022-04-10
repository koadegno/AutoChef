package ulb.infof307.g01.model.db;

import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

/**
 * Classe utilitaire permettant de sort une Liste de Course
 */
public class ShoppingListSorter {

    /**
     * Convertit en {@link Vector}, puis trie une liste en fonction d'une clé de comparaison
     * @param shoppingList La {@link ShoppingList} a trié
     * @param comparator Clé utilisé pour la comparaison
     * @return Vecteur trié créé à partir  de {@code shoppingList}
     */
    public static Vector<Product> getSortedShoppingList(ShoppingList shoppingList, Comparator comparator) {
        Vector<Product> shoppingListVector = new Vector<>(shoppingList);
        shoppingListVector.sort(comparator);

        return shoppingListVector;
    }

    /**
     * Convertit en {@link Vector}, puis trie une liste en fonction du nom
     * @param shoppingList La {@link ShoppingList} a trié
     * @return Vecteur trié créé à partir  de {@code shoppingList}
     */
    public static Vector<Product> getSortedShoppingList(ShoppingList shoppingList) {
        return getSortedShoppingList(shoppingList, Comparator.comparing(Product::getName));
    }
}
