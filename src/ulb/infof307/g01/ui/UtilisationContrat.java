package ulb.infof307.g01.ui;

import ulb.infof307.g01.cuisine.Recipe;

public interface UtilisationContrat <T> {
    public void add(T object);
    public void cancel();
}
