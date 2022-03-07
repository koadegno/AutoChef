package ulb.infof307.g01.cuisine;

import java.util.LinkedList;

public class Recipe {

    private int id;
    private String name;
    private int duration;
    private String category;   //Exemple vegan
    private String type;       //Exemple mijoté, quiche
    private int nbrPerson;
    private String preparation;
    private LinkedList <Product>  products;

    public Recipe (String name){
        this.name = name;
    }

    public Recipe (int id, String name, int duration, String category, String type, int nbrPerson, String preparation) {

        this.id = id;
        this.name = name;
        this.duration = duration;
        this.category = category;
        this.type = type;
        this.nbrPerson = nbrPerson;
        this.preparation = preparation;

    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        Recipe recipe = (Recipe) other;
        return this.getName().equals(recipe.getName());
    }
}
