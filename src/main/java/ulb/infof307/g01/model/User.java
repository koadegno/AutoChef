package ulb.infof307.g01.model;

import java.util.ArrayList;

//TODO: crée une class juste pour l'adress
public class User {
    private Adress adress;
    private int ID;
    private String name;
    private String familyName;
    private String pseudo;
    private String password;
    private Boolean pro = false; // Est ce un paticulier ou un professionel?



    //constructors
    public User(int ID, String familyName, String name, String pseudo, String password,Adress adress, Boolean pro) {
        this(pseudo, password);
        this.ID = ID;
        this.familyName = familyName;
        this.name = name;
        this.adress = adress;
        this.pro = pro;
    }

    public User(String pseudo, String password){
        this.pseudo = pseudo;
        this.password = password;
    }
    public User() {}




    //setter and getter TODO: supprimer celles qui ne sont pas utilisées
    public Boolean isProfessional(){return pro;}
    public void setProUser(Boolean isPro){pro=isPro;}
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
    public void setAdress(Adress adress) {
        this.adress = adress;
    }
    public Adress getAdress() {
        return adress;
    }
}
