package ulb.infof307.g01.model;

//TODO: crée une class juste pour l'adress
public class User {
    private int ID;
    private String name;
    private String familyName;
    private String pseudo;
    private String password;
    private String country;
    private String city;
    private int postalCode;
    private String streetName;
    private int houseNumber;
    private Boolean pro = false; // Est ce un paticulier ou un professionel?


    public User(int ID, String familyName, String name, String pseudo, String password, String country, String city, int postalCode, String streetName, int houseNumber, Boolean pro) {
        this.ID = ID;
        this.familyName = familyName;
        this.name = name;
        this.pseudo = pseudo;
        this.password = password;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.pro = pro;
    }

    public User(String pseudo, String password){
        this.pseudo = pseudo;
        this.password = password;
    }

    public User() {}

    public Boolean isPasswordCorrect(String userPassword){return password == userPassword;}

    public Boolean isPseudoCorrect(String userPseudo){return pseudo == userPseudo;}
    public Boolean isValidUser(String userPseudo){return pseudo == userPseudo;}
    public void setAdress(String country, String city, int postalCode, String streetName, int houseNumber) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }




    //setter and getter TODO: supprimer celles qui ne sont pas utilisées
    public Boolean isProfessional(){return pro;}
    public void setProUser(Boolean isPro){pro=isPro;}

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
