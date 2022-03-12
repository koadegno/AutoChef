package ulb.infof307.g01.cuisine;

import java.util.Vector;

public class ShoppingList extends UniqueProductList {

    private int id;
    private String name;

    private boolean isArchived = false;

    public ShoppingList(String name) {this.name = name;}

    public ShoppingList(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public boolean isArchived() {return isArchived;}
    public void setArchived(boolean isArchived) {this.isArchived = isArchived;}

    public void setId(int id) {
       this.id = id;
    }
}
