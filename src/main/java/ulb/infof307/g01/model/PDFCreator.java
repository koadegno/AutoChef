package ulb.infof307.g01.model;


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

/**
 * Classe qui permet d'exporter une liste de course en pdf
 */
public class PDFCreator {
    private static String FILE = null;


    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    public static void createPDF(ShoppingList shoppingList) {
        try {
            Vector<Product> sortedShoppingList = new Vector<>(shoppingList);
            sortedShoppingList.sort(Comparator.comparing(Product::getFamillyProduct));

            FILE = shoppingList.getName();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE+".pdf"));
            document.open();
            addContent(document, sortedShoppingList);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }}

    private static void addContent(Document document, Vector<Product> productList) throws DocumentException {
        if (!productList.isEmpty()){
            System.err.println("Warning: ShoppingList is empty!");
        }
        String nameFamilyProduct = productList.get(0).getFamillyProduct();

        Anchor anchor = new Anchor("Liste de courses : " + FILE , catFont); //catFont
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

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
