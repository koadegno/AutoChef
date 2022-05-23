package ulb.infof307.g01.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test de la classe shoppingList
 */
class TestShoppingList {

    static private ShoppingList list;
    static private List<Product> testList;

    @BeforeAll
    static public void createList() {
        list = new ShoppingList("Liste de course pour Noël");
        testList = new ArrayList<>(2);

        testList.add(new Product.ProductBuilder().withName("Dinde").build());
        testList.add(new Product.ProductBuilder().withName("Orange").build());
    }

    @BeforeEach
    private void addElements() {
        list.add(testList.get(0));
        list.add(testList.get(1));
    }

    @AfterEach
    public void tearDown() {
        list.clear();
    }

    @Test
    void add() {
        Product product =  new Product.ProductBuilder().withName("Pomme").build();

        list.add(product);

        //Contient le nouveau produit et les produits rajoutés avant
        assertTrue(list.contains(product));
        assertTrue(list.contains(testList.get(0)));
        assertTrue(list.contains(testList.get(1)));
    }

    @Test
    void checkQuantityAdd(){
        list.add(testList.get(0));

        // Element en double
        for (Product p : list) {
            if (p.equals(testList.get(0)))
                assertEquals(p.getQuantity(), 2);
            if (p.equals(testList.get(1)))
                assertEquals(p.getQuantity(), 1);
        }
    }


    //TODO: Séparer en tests différents?
    // Arrange: produit qui existe (Dinde), act: remove, assert: il a été remove et contains la quantité attendue
    // Arrange: produit qui n'existe pas, act: remove, assert: erreur parce qu'il n'existe pas.

    @Test
    void remove() {
        addElements();

        boolean isRemoved = list.remove(testList.get(0));

        assertTrue(isRemoved, "ShoppingList.remove() n'a pas renvoyé True");
        assertTrue(list.contains(testList.get(0)), "L'élément a été supprimé alors qu'il n'aurait pas dû (Quantité > 1)");
    }

    @Test
    void checkQuantityRemove(){
        addElements();
        Product fakeProduct =  new Product.ProductBuilder().withName("false").build();

        list.remove(testList.get(0));

        //Quantité diminue
        for (Product p : list) {
            if (p.equals(testList.get(0)))
                assertEquals(1, p.getQuantity(), "La quantité du produit list[0] n'a pas été décrémenté après suppression");
        }
        //Produit effacé de la liste
        assertTrue(list.remove(testList.get(0)), "ShoppingList.remove() n'a pas renvoyé True");
        assertFalse(list.contains(testList.get(0)),"Le produit list[0] n'as pas été supprimé avec une quantité de 0");
        //On peut pas supprimer des éléments qui ne sont pas dans la liste
        assertFalse(list.remove(fakeProduct),"ShoppingList.remove() n'a pas renvoyé False avec un objet non présent dans la liste");
    }

}