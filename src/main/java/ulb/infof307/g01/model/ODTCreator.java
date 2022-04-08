package ulb.infof307.g01.model;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;

public class ODTCreator {


    public void createODT(ShoppingList shoppingList) throws Exception {
        OdfTextDocument odt = OdfTextDocument.newTextDocument();

        Vector<Product> sortedShoppingList = new Vector<>(shoppingList);
        sortedShoppingList.sort(Comparator.comparing(Product::getFamillyProduct));
        String nameFamilyProduct = sortedShoppingList.get(0).getFamillyProduct();
        writeODT(odt,"Liste de course : "+shoppingList.getName()+"\n");
        writeODT(odt, nameFamilyProduct+" : ");

        for (Product product : sortedShoppingList) {
            if (!Objects.equals(product.getFamillyProduct(), nameFamilyProduct)){
                nameFamilyProduct = product.getFamillyProduct();
                writeODT(odt,nameFamilyProduct +" : ");
            }
            String productString = String.format("\t%s %d %s",product.getName(),product.getQuantity(),product.getNameUnity());
            writeODT(odt,productString);
        }
        odt.save(shoppingList.getName()+".odt");
        odt.close();
    }

    private static void writeODT(OdfTextDocument odt, String string) throws Exception {
        String stringToWrite = String.format("%s", string);
        odt.newParagraph().addContentWhitespace(stringToWrite);
    }

    /**
     * Parcours les enfants du fichier XML lié au fichier ODT et récupère les zones de text
     * @param odtDocument le fichier ODT
     * @return ShoppingList est une liste de course
     * @throws Exception erreur ?
     */
    public ShoppingList readODT(OdfTextDocument odtDocument) throws Exception {
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
                    //correspond a une ligne de texte avec le produit
                    if (nodeList.item(j).getNextSibling() != null && nodeList.item(j).getNextSibling().getNodeType() == Node.TEXT_NODE){
                        productElements = nodeList.item(j).getNextSibling().getNodeValue();
                        createProduct = true;
                        //System.out.println(productElements);
                    }
                    // corresponds a une ligne de texte avec la famille
                    else if(nodeList.item(j).getNodeName().equals("#text")){
                        familyProduct = nodeList.item(j).getNodeValue();
                        if(familyProduct.contains("Liste de course")){
                            continue;
                        }
                        familyProduct = familyProduct.replace(":","").strip();
                    }
                    if(createProduct){ // permet de savoir quand cree un produit
                        String[] productElementArray = productElements.split(" ");
//                        System.out.println(familyProduct);
//                        System.out.println(Arrays.toString(productElementArray));
                        shoppingList.add(new Product(productElementArray[0],
                                Integer.parseInt(productElementArray[1]),
                                productElementArray[2],
                                familyProduct));
                        createProduct = false;
                    }

                }
            }
        }
    return shoppingList;
    }
}
