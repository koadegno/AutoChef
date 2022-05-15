package ulb.infof307.g01.model.export;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.exception.PDFException;

/**
 * Classe qui permet d'exporter une liste de course en PDF
 */
public class PDFCreator {
    private static String nameFile = null;

    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    public  void createPDF(ShoppingList shoppingList) throws PDFException {
        Vector<Product> sortedShoppingList = new Vector<>(shoppingList);
        sortedShoppingList.sort(Comparator.comparing(Product::getFamillyProduct));

        nameFile = shoppingList.getName();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(nameFile +".pdf"));
            document.open();
            addContent(document, sortedShoppingList);
        } catch (DocumentException | FileNotFoundException e) {
            throw new PDFException();
        }
        finally {
            document.close();
        }
    }

    private static void addContent(Document document, Vector<Product> productList) throws DocumentException {
        String nameFamilyProduct = productList.get(0).getFamillyProduct();

        Anchor anchor = new Anchor("Liste de courses : " + nameFile, catFont); //catFont
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
        Paragraph subPara = new Paragraph(nameFamilyProduct, subFont); //subFont
        Section subCatPart = catPart.addSection(subPara);

        for (Product product : productList ){
            if (!Objects.equals(product.getFamillyProduct(), nameFamilyProduct)){
                nameFamilyProduct = product.getFamillyProduct();
                subCatPart = catPart.addSection(new Paragraph(nameFamilyProduct, subFont));} //subFont
            subCatPart.add(new Paragraph(product.getName() + " " +product.getQuantity() + product.getNameUnity()));}
        document.add(catPart);
    }
}
