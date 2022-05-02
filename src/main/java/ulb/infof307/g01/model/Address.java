package ulb.infof307.g01.model;

/**
 * Model représentant l'adresse d'un utilisateur
 */
public class Address {
    String country;
    String city;
    int postalCode;
    String streetName;
    int houseNumber;

    public Address(String country, String city, int postalCode, String streetName, int houseNumber) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
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
}