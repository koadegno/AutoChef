public class Produit {
    private String nom;
    private int quantite = 1;

    public Produit(String nomProduit) {
        nom = nomProduit;
    }

    public Produit(String nomProduit, int quantite) {
        nom = nomProduit;
        this.quantite = quantite;
    }

    public void renommer(String nouveauNom) {
        nom = nouveauNom;
    }

    public boolean reduire() {
        if (quantite == 1)
            return false;
        quantite--;
        return true;
    }

    public void augmenter() {
        quantite++;
    }

    public void changeQuantite(int newQuantite) {
        if (newQuantite > 0)
            quantite = newQuantite;
        else
            quantite = 1;
    }

    public int getQuantite() {
        return quantite;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object autre) {

        if (this == autre)
            return true;

        if (autre == null || this.getClass() != autre.getClass())
            return false;

        Produit produit = (Produit)autre;

        return this.getNom().equals(produit.getNom());
    }
}
