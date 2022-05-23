package ulb.infof307.g01.model.export;


import org.jetbrains.annotations.NotNull;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
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

    /**
     * Parcours les enfants du fichier XML lié au fichier ODT et récupère les zones de text
     * @param odtDocument le fichier ODT
     * @return ShoppingList est une liste de course
     * @throws Exception si l'ouverture du fichier XML n'a pas pu se faire
     */
    ShoppingList readODT(@NotNull OdfTextDocument odtDocument) throws Exception {
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
                        if(familyProduct.contains(DOCUMENT_TITLE)){ // on est sur le titre de la page
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
