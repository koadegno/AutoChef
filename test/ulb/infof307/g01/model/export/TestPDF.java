package ulb.infof307.g01.model.export;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.TestConstante;
import ulb.infof307.g01.model.exception.DocumentException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test de l'exportation en PDF
 */
class TestPDF {
    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList("test/testShoppingList");
        Product testProduct = new Product.ProductBuilder().withName("Banane").withQuantity(3).withFamilyProduct(TestConstante.FOOD_CATEGORY_MEAT).withFamilyProduct(TestConstante.FRUIT).withQuantity(1).withNameUnity("kg").build();
        Product testProduct2 = new Product.ProductBuilder().withName("Carotte").withQuantity(7).withFamilyProduct(TestConstante.FOOD_CATEGORY_VEGE).withFamilyProduct(TestConstante.FOOD_CATEGORY_MEAT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
        shoppingList.add(testProduct);
        shoppingList.add(testProduct2);
    }

    @Test
     public void testCreatePDF() throws IOException, DocumentException {
        DocumentCreator pdfCreator = new PDFCreator();

        pdfCreator.createDocument(shoppingList);

        String text = """
                1. Liste de course : test/testShoppingList
                1.1. Fruit
                Banane 1kg
                1.2. Viande
                Carotte 1g""";

        PdfReader reader = new PdfReader("test/testShoppingList.pdf");
        String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);  // pageNumber = 1
        assertEquals(text, textFromPage);
        reader.close();
    }


}