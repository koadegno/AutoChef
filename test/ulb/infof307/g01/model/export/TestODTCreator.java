package ulb.infof307.g01.model.export;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test de l'exportation en ODT
 */
class TestODTCreator {
    public static final String NAME_SHOPPING_LIST = "test/testShoppingList";
    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList(NAME_SHOPPING_LIST);
        Product testProduct = new Product.ProductBuilder().withName("Banane").withQuantity(3).withFamilyProduct(TestConstante.FAMILY_PRODUCT_FRUIT).withQuantity(1).withNameUnity("kg").build();
        Product testProduct2 = new Product.ProductBuilder().withName("Boeuf").withQuantity(7).withFamilyProduct(TestConstante.FAMILY_PRODUCT_MEAT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
        Product testProduct3 = new Product.ProductBuilder().withName("Mouton").withQuantity(7).withFamilyProduct(TestConstante.FAMILY_PRODUCT_MEAT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
        Product testProduct4 = new Product.ProductBuilder().withName("Dorade").withQuantity(7).withFamilyProduct(TestConstante.FAMILY_PRODUCT_FISH).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
        shoppingList.add(testProduct);
        shoppingList.add(testProduct2);
        shoppingList.add(testProduct3);
        shoppingList.add(testProduct4);
    }

    @AfterAll
    static void afterAll() throws IOException {
        Files.deleteIfExists(Path.of(NAME_SHOPPING_LIST+".odt"));
    }

    @Test
    void testCreateODT()  throws Exception {
        DocumentCreator odtCreator = new ODTCreator();
        odtCreator.createDocument(shoppingList);

        OdfTextDocument odtReader = OdfTextDocument.loadDocument (NAME_SHOPPING_LIST + ".odt");
        ShoppingList shoppingListRead =  readODT(odtReader, shoppingList.getName());
        ArrayList<Product> ShoppingListReadVector = new ArrayList<>(shoppingListRead);
        ArrayList<Product> ShoppingListVector = new ArrayList<>(shoppingList);
        ShoppingListVector.sort(Comparator.comparing(Product::getFamilyProduct));
        ShoppingListReadVector.sort(Comparator.comparing(Product::getFamilyProduct));



        assertEquals(ShoppingListVector.get(0).getName(),ShoppingListReadVector.get(0).getName());
        assertEquals(ShoppingListVector.get(0).getFamilyProduct(),
                ShoppingListReadVector.get(0).getFamilyProduct());
        assertEquals(ShoppingListVector.get(1).getName(),ShoppingListReadVector.get(1).getName());
        assertEquals(ShoppingListVector.get(1).getFamilyProduct(),
                ShoppingListReadVector.get(1).getFamilyProduct());
        odtReader.close();
    }

    /**
     * Parcours les enfants du fichier XML lié au fichier ODT et récupère les zones de text
     * @param odtDocument le fichier ODT
     * @return ShoppingList est une liste de course
     * @throws Exception si l'ouverture du fichier XML n'a pas pu se faire
     */
    ShoppingList readODT(@NotNull OdfTextDocument odtDocument, String documentTitle) throws Exception {
        ShoppingList shoppingList = new ShoppingList(odtDocument.getDocumentPath());
        OfficeTextElement contentRoot = odtDocument.getContentRoot();
        NodeList nodes = contentRoot.getChildNodes();
        String familyProduct= null;
        String productElements = null;
        boolean createProduct = false;
        for(int i =0; i< nodes.getLength(); i++){ // parcours des enfants
            if(nodes.item(i).hasChildNodes()){ // parcours des enfants des enfants
                NodeList nodeList = nodes.item(i).getChildNodes();
                for(int j =0; j< nodeList.getLength(); j++){
                    //correspond à une ligne de texte avec le produit
                    if (nodeList.item(j).getNextSibling() != null && nodeList.item(j).getNextSibling().getNodeType() == Node.TEXT_NODE){
                        productElements = nodeList.item(j).getNextSibling().getNodeValue();
                        createProduct = true;
                        j++; // tu passes le produit parce que c'est le next node
                    }
                    // corresponds à une ligne de texte avec la famille
                    else if(nodeList.item(j).getNodeName().equals("#text")){
                        familyProduct = nodeList.item(j).getNodeValue();
                        if(familyProduct.contains(documentTitle)){ // on est sur le titre de la page
                            continue;
                        }
                        familyProduct = familyProduct.replace(":","").strip();
                    }
                    if(createProduct){ // permet de savoir quand cree un produit
                        addProduct(shoppingList, familyProduct, productElements);
                        createProduct = false;
                    }
                }
            }
        }

        return shoppingList;
    }

    /**
     * ajoute un produit à la liste de course
     * @param shoppingList une liste de course
     * @param familyProduct la famille du produit
     * @param productElements l'élément XML qui contient le nom du produit
     */
    private void addProduct(@NotNull ShoppingList shoppingList, String familyProduct, @NotNull String productElements) {
        String[] productElementArray = productElements.split(" ");
        String nameUnity = productElementArray[2];
        int quantity = Integer.parseInt(productElementArray[1]);
        String name = productElementArray[0];
        shoppingList.add(new Product.ProductBuilder().withName(name).withQuantity(quantity).withNameUnity(nameUnity).withFamilyProduct(familyProduct).build());
    }
}