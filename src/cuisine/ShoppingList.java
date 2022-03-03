package cuisine;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ShoppingList extends Vector<Product> {

    private int id;
    private String name;

    private boolean isArchived = false;

    public ShoppingList(String name) {this.name = name;}

    ShoppingList(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public boolean isArchived() {return isArchived;}
    public void setArchived(boolean isArchived) {this.isArchived = isArchived;}

}
