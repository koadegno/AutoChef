package ulb.infof307.g01.model.db.dao;

import ulb.infof307.g01.model.db.Database;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe d'accès à la base de données pour les données concernant les listes de courses
 */
public class ShoppingListDao extends Database implements Dao<ShoppingList> {

    public static final String LISTE_COURSE_TABLE_NAME = "ListeCourse";

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public ShoppingListDao(String nameDB) {
        super(nameDB);
    }

    private void insertIngredientInShoppingList(int shoppingListId, int ingredientId, int quantity) throws SQLException {
        String[] values = {String.format("%d",shoppingListId),String.format("%d",ingredientId),String.format("%d",quantity)};
        insert("ListeCourseIngredient",values);
    }

    @Override
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable(LISTE_COURSE_TABLE_NAME,"ORDER BY Nom ASC");
    }

    @Override
    public void insert(ShoppingList shoppingList) throws SQLException {
        String[] values = {"null",String.format("'%s'",shoppingList.getName())};
        insert(LISTE_COURSE_TABLE_NAME,values);
        int id = getGeneratedID();
        for(Product product : shoppingList){
            int productID = getIDFromName("Ingredient", product.getName(),"IngredientID");
            insertIngredientInShoppingList(id,productID, product.getQuantity());
        }
    }

    /**
     * Sauvegarde une liste de course modifiée, et la supprime si la liste ne contient rien
     */
    @Override
    public void update(ShoppingList shoppingList) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        int id = getIDFromName(LISTE_COURSE_TABLE_NAME, shoppingList.getName(), "ListeCourseID");
        constraint.add(String.format("%s = %d","ListeCourseID", id));
        delete("ListeCourseIngredient",constraint);
        updateName(LISTE_COURSE_TABLE_NAME, shoppingList.getName(),constraint );
        if(shoppingList.size() == 0){
            delete(LISTE_COURSE_TABLE_NAME,constraint);
        }
        else{
            for (Product product : shoppingList) {
                int productID = getIDFromName("Ingredient", product.getName(), "IngredientID");
                insertIngredientInShoppingList(id, productID, product.getQuantity());
            }
        }
    }

    @Override
    public ShoppingList get(String name) throws SQLException {
        int nameID = getIDFromName(LISTE_COURSE_TABLE_NAME,name,"ListeCourseID");
        ResultSet querySelectShoppingList = sendQuery(String.format("""
                SELECT S.Quantite,Ingredient.Nom,Unite.Nom,F.Nom
                FROM ListeCourseIngredient as S
                INNER JOIN Ingredient ON S.IngredientID = Ingredient.IngredientID
                INNER JOIN Unite ON Ingredient.UniteID = Unite.UniteID\s
                INNER JOIN FamilleAliment as F ON Ingredient.FamilleAlimentID = F.FamilleAlimentID
                WHERE S.ListeCourseID = %d""", nameID));
        ShoppingList shoppingList = new ShoppingList(name,nameID);
        while(querySelectShoppingList.next()){
            int productQuantity = querySelectShoppingList.getInt(1);
            String productName = querySelectShoppingList.getString(2);
            String productUnity = querySelectShoppingList.getString(3);
            String productFamily = querySelectShoppingList.getString(4);
            shoppingList.add(new Product(productName,productQuantity,productUnity,productFamily));
        }
        return shoppingList;
    }
}
