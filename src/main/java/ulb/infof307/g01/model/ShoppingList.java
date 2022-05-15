package ulb.infof307.g01.model;


import java.util.Date;

/**
 * Classe représentant une Liste de Course
 */
public class ShoppingList extends ProductHashSet {

    private int id;
    private String name;
    private Date creationDate;
    private final boolean isArchived = false;

    public ShoppingList(String name) {this.name = name;}
    public ShoppingList(String name, int id) {
        this(name);
        this.id = id;
    }
    public ShoppingList(String name, int id, Date date) {
        this(name, id);
        this.creationDate = date;
    }

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || this.getClass() != other.getClass())
            return false;
        return this.getName().equals(((ShoppingList) other).getName());
    }
}
