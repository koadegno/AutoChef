package ulb.infof307.g01.model;

//TODO: crée une class juste pour l'address
public class User {
    private Address address;
    private int ID = -1;
    private String name;
    private String familyName;

    private String pseudo;

    private String password;
    private Boolean isProfessional = false; // Est ce un paticulier ou un professionel?


    //constructors

    public User(int ID, String familyName, String name, String pseudo, String password, Address address, Boolean pro) {
        this(pseudo, password,pro);
        this.ID = ID;
        this.familyName = familyName;
        this.name = name;
        this.address = address;
    }
    public User(String pseudo, String password, Boolean isProfessional){
        this.pseudo = pseudo;
        this.password = password;
        this.isProfessional = isProfessional;
    }

    public User() {}



    //setter and getter TODO: supprimer celles qui ne sont pas utilisées

    public Boolean isProfessional(){return isProfessional;}
    public void setProUser(Boolean isPro){
        isProfessional =isPro;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getPseudo(){return pseudo;}
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return this.ID;
    }
    public void setAdress(Address address) {
        this.address = address;
    }
    public Address getAdress() {
        return address;
    }
    public String getFamilyName() {
        return familyName;
    }
}
