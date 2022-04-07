import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.PDFCreator;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestPDF {
    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList("test/testShoppingList");
        Product testProduct = new Product("Banane", 3, "kg", "Fruits");
        Product testProduct2 = new Product("Carotte", 7, "g", "Viande");
        shoppingList.add(testProduct);
        shoppingList.add(testProduct2);
    }

    @Test
     public void testCreatePDF() throws IOException {
        PDFCreator.createPDF(shoppingList);
        String text = """
                1. Liste de courses : test/testShoppingList
                1.1. Fruits
                Banane 3kg
                1.2. Viande
                Carotte 7g""";

        PdfReader reader = new PdfReader("test/testShoppingList.pdf");
        String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);  // pageNumber = 1
        assertEquals(text, textFromPage);
        reader.close();
    }
}