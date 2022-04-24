package ulb.infof307.g01.model;

public class Adress {
    String country;
    String city;
    int postalCode;
    String streetName;
    int houseNumber;

    public Adress() {}
    public Adress(String country, String city, int postalCode, String streetName, int houseNumber) {
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
}