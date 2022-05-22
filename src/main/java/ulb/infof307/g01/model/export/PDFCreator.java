package ulb.infof307.g01.model.export;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.exception.DocumentException;

/**
 * Classe qui permet d'exporter une liste de course en PDF
 */
public class PDFCreator extends DocumentCreator {

    @Override
    public void createDocument(ShoppingList shoppingList) throws DocumentException {

        List<Product> sortedShoppingList = new ArrayList<>(shoppingList);
        sortedShoppingList.sort(Comparator.comparing(Product::getFamilyProduct));

        nameFile = shoppingList.getName();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(nameFile + ".pdf"));
            document.open();
            addContent(document, sortedShoppingList);
        } catch (DocumentException | FileNotFoundException | com.itextpdf.text.DocumentException e) {
            throw new DocumentException(e);
        }
        finally {
            document.close();
        }
    }


    private void addContent(Document document, List<Product> productList) throws DocumentException {
        String nameFamilyProduct = productList.get(0).getFamilyProduct();

        Anchor anchor = new Anchor(LISTE_DE_COURSE_TITLE + nameFile, catFont); //catFont
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
        Paragraph subPara = new Paragraph(nameFamilyProduct, subFont); //subFont
        Section subCatPart = catPart.addSection(subPara);

        for (Product product : productList ){
            if (!Objects.equals(product.getFamilyProduct(), nameFamilyProduct)){
                nameFamilyProduct = product.getFamilyProduct();
                subCatPart = catPart.addSection(new Paragraph(nameFamilyProduct, subFont)); //subFont
            }
            subCatPart.add(new Paragraph(product.getName() + " " +product.getQuantity() + product.getNameUnity()));
        }
        try {
            document.add(catPart);
        } catch (com.itextpdf.text.DocumentException e) {
            throw new DocumentException(e);
        }
    }
}
