package ulb.infof307.g01.model;

/**
 * Classe représentant un utilisateur
 */
public class User {
    private Address address;
    private int id = -1;
    private String name;
    private String familyName;
    private String pseudo;
    private final String password;
    private Boolean isProfessional = false; // Est ce un paticulier ou un professionel?


    //constructors
    public User(int id, String lastName, String firstName, String pseudo, String password, Address address, Boolean pro) {
        this(pseudo, password,pro);
        this.id = id;
        this.familyName = lastName;
        this.name = firstName;
        this.address = address;
    }
    public User(String pseudo, String password, Boolean isProfessional){
        this.pseudo = pseudo;
        this.password = password;
        this.isProfessional = isProfessional;
    }

    public Boolean isProfessional(){return isProfessional;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getPseudo(){return pseudo;}
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    public Address getAddress() {
        return address;
    }
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Compare 2 objets
     */
    @Override
    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        User user = (User)other;

        return this.getPseudo().equals(user.getPseudo()) && this.getPassword().equals(user.getPassword()) && isProfessional.equals(user.isProfessional);
    }
}
