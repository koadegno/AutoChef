package ulb.infof307.g01.ui;

/**
 * Cette interface est une sorte de contrat entre une windowController qui veut en utiliser une autre
 * tout en gardant son état.
 * @param <T> : Le type de l'objet que doit retourner la window avec laquelle
 * on a fait le contrat
 */
public interface UtilisationContrat <T> {
    /**
     * Permet à la classe appellant de prevenir qu'il a fini
     * @param object : la window appeler retourne l'objet demander avant de se fermer
     *
     */
    public void add(T object);

    /**
     * Permet de prevenir la window controller qui à passer le contract que
     * l'utilisateur à annuler la création de l'objet attend.
     */
    public void cancel();
}
