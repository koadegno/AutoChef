package ulb.infof307.g01.model;

/**
 * Classe représentant les recettes
 */

public class Recipe extends ProductHashSet {


    private int id;
    private String name;
    private int duration;
    private String category;
    private String type;
    private int nbrPerson;
    private String preparation;
    private  boolean isFavorite;

    /**
     * Builder pour construire des recettes
     */
    public static class RecipeBuilder{

        private int id = -1;
        private String name = "";
        private int duration = -1;
        private String category = "";
        private String type = "";
        private int nbrPerson = 1;
        private String preparation = "";
        private  boolean isFavorite = false;

        public RecipeBuilder withId(int id){
            this.id = id;
            return this;
        }
        public RecipeBuilder withName(String name){
            this.name = name;
            return this;
        }
        public RecipeBuilder withDuration(int duration){
            this.duration = duration;
            return this;
        }
        public RecipeBuilder withCategory(String category){
            this.category = category;
            return this;
        }
        public RecipeBuilder withType(String type){
            this.type = type;
            return this;
        }
        public RecipeBuilder withNumberOfPerson(int nbrPerson){
            this.nbrPerson = nbrPerson;
            return this;
        }
        public RecipeBuilder withPreparation(String preparation){
            this.preparation = preparation;
            return this;
        }
        public RecipeBuilder isFavorite(boolean isFavorite){
            this.isFavorite = isFavorite;
            return this;
        }

        public Recipe build(){
            Recipe recipe = new Recipe();
            recipe.id = id;
            recipe.name = name;
            recipe.duration = duration;
            recipe.category = category;
            recipe.type = type;
            recipe.nbrPerson = nbrPerson;
            recipe.preparation = preparation;
            recipe.isFavorite = isFavorite;
            return recipe;
        }
    }

    public int getId() { return id;}
    public Boolean isFavorite() {return isFavorite;}
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
    public void setName(String name) { this.name = name;}

    public void setId(int id) {this.id = id;}
    public void setType(String type) { this.type = type;}
    public void setPreparation(String preparation) { this.preparation = preparation;}


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

    public void setFavorite(Boolean isChecked) {
        isFavorite = isChecked;
    }

    public boolean getFavorite(){return isFavorite;}
}
