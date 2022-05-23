package ulb.infof307.g01.model.export;


import com.itextpdf.text.Font;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.exception.DocumentException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public abstract class DocumentCreator {

    protected static final String DOCUMENT_TITLE = "Liste de course : ";

    protected static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    protected static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);

    /**
     * Crée un fichier à partir d'une liste de course
     * @param shoppingList la liste de course
     * @throws DocumentException Erreur lors de l'écriture de l'exception
     */
    public void createDocument(ShoppingList shoppingList)             throws DocumentException {
        createFile(shoppingList.getName());
        addTitle(DOCUMENT_TITLE + shoppingList.getName());

        List<Product> sortedShoppingList = new ArrayList<>(shoppingList);
        sortedShoppingList.sort(Comparator.comparing(Product::getFamilyProduct));

        String nameFamilyProduct = sortedShoppingList.get(0).getFamilyProduct();

        addChapter(nameFamilyProduct);


        for (Product product : sortedShoppingList ) {
            if (!Objects.equals(product.getFamilyProduct(), nameFamilyProduct)) {
                nameFamilyProduct = product.getFamilyProduct();
                addChapter(nameFamilyProduct);
            }
            addProduct(product);
        }

        saveFile();
    };

    protected abstract void createFile(String fileName)               throws DocumentException;
    protected abstract void addTitle(String title)                    throws DocumentException;
    protected abstract void addChapter(String chapterTitle)           throws DocumentException;
    protected abstract void addProduct(Product item)                  throws DocumentException;

    protected abstract void saveFile()                                throws DocumentException;
}

