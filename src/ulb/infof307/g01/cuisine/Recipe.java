package ulb.infof307.g01.cuisine;

public class Recipe extends UniqueProductList {

    private int id;
    private String name;
    private int duration;
    private String category;   //Exemple vegan
    private String type;       //Exemple mijoté, quiche
    private int nbrPerson;
    private String preparation;

    public Recipe (int id, String name, int duration, String category, String type, int nbrPerson, String preparation) {

        this.id = id; this.name = name; this.duration = duration; this.category = category; this.type = type;
        this.nbrPerson = nbrPerson;     this.preparation = preparation;
    }

    public Recipe(String name){
        this.id = 1; this.name = name; this.duration = 30; this.category = "cat"; this.type = "type";
        this.nbrPerson = 4;     this.preparation = "prep";
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
