package ulb.infof307.g01.model.export;


import com.itextpdf.text.Font;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.exception.DocumentException;


public abstract class DocumentCreator {

    protected static String nameFile = null;

    protected static final String LISTE_DE_COURSE_TITLE = "Liste de course : ";

    protected static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    protected static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);

    /**
     * Crée un fichier à partir d'une liste de course
     * @param shoppingList la liste de course
     * @throws DocumentException Erreur lors de l'écriture de l'exception
     */
    abstract public void createDocument(ShoppingList shoppingList) throws DocumentException;

}

