package ulb.infof307.g01.model.export;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.exception.DocumentException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Classe qui permet d'exporter une liste de course en PDF
 */
public class PDFCreator extends DocumentCreator {

    Chapter chapter;
    Section currentSection;
    Document document = new Document();
    @Override
    protected void createFile(String fileName) throws DocumentException {

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName + ".pdf"));
            document.open();
        } catch ( FileNotFoundException | com.itextpdf.text.DocumentException e) {
            document.close();
            throw new DocumentException(e);
        }

    }

    @Override
    protected void addTitle(String title) {
        Anchor anchor = new Anchor(title, catFont);
        chapter = new Chapter(new Paragraph(anchor), 1);
    }

    @Override
    protected void addChapter(String chapterTitle) throws DocumentException {
        Paragraph paragraphChapterTitle = new Paragraph(chapterTitle, subFont);
        currentSection = chapter.addSection(paragraphChapterTitle);
    }

    @Override
    protected void addProduct(Product item) {
        currentSection.add(new Paragraph(item.getName() + " " + item.getQuantity() + item.getNameUnity()));
    }

    @Override
    protected void saveFile() throws DocumentException {
        try {
            document.add(chapter);
        } catch (com.itextpdf.text.DocumentException e) {
            throw new DocumentException(e);
        }
        document.close();
    }

}
