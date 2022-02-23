public class Produit {
    private String nom;

    public Produit(String nomProduit) {
        nom = nomProduit;
    }

    public void renommer(String nouveauNom) {
        nom = nouveauNom;
    }

    public String getNom() {
        return nom;
    }
}
