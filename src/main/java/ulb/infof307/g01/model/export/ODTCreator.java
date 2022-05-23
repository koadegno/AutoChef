package ulb.infof307.g01.model.export;


import org.odftoolkit.odfdom.doc.OdfTextDocument;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.exception.DocumentException;

/**
 * Classe d'exportation d'une recette en ODT
 */
public class ODTCreator extends DocumentCreator {

    String fileName;

    private OdfTextDocument file;

    protected void createFile(String fileName) throws DocumentException {

        this.fileName = fileName;

        try {
            file = OdfTextDocument.newTextDocument();
        }
        catch (Exception e) {
            file.close();
            throw new DocumentException(e);
        }
    }

    @Override
    protected void addTitle(String title) throws DocumentException {
        try {
            file.newParagraph().addContentWhitespace(title);
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    @Override
    protected void addChapter(String chapterTitle) throws DocumentException {
        try {
            file.newParagraph().addContentWhitespace(chapterTitle);
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    @Override
    protected void addProduct(Product item) throws DocumentException {
        try {
            String productString = String.format("\t%s %d %s", item.getName(), item.getQuantity(), item.getNameUnity());
            file.newParagraph().addContentWhitespace(productString);
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    @Override
    protected void saveFile() throws DocumentException {
        try {
            file.save(fileName + ".odt");
        } catch (Exception e) {
            throw new DocumentException(e);
        }
        file.close();
    }

}
