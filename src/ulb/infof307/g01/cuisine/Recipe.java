package ulb.infof307.g01.cuisine;

/**
 * Classe représentant les recettes
 */

public class Recipe extends UniqueProductList {
    
    private int id;
    private String name;
    private int duration;
    private String category;
    private String type;
    private int nbrPerson;
    private String preparation;

    public Recipe (String name){
        this.name = name;
    }

    public Recipe (int id, String name, int duration, String category, String type, int nbrPerson, String preparation) {
        this.id = id; this.name = name; this.duration = duration; this.category = category; this.type = type;
        this.nbrPerson = nbrPerson;     this.preparation = preparation;
    }

    public Recipe (String name, int duration, String category, String type, int nbrPerson, String preparation) {
        this.name = name; this.duration = duration; this.category = category; this.type = type;
        this.nbrPerson = nbrPerson;     this.preparation = preparation;
    }

    public int getId() {return id;}
    public String getName() {
        return name;
    }
    public int getDuration() { return duration; }
    public String getCategory() {
        return category;
    }
    public String getType() { return type; }
    public int getNbrPerson() { return nbrPerson; }
    public String getPreparation() { return preparation;}


    /**
     * Compare les recettes en fonction de leur Name
     * @param  other  Recipe
     * @return boolean
     */

    @Override
    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        return this.getName().equals(((Recipe) other).getName());
    }
}
