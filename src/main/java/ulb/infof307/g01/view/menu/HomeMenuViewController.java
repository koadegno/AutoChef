package ulb.infof307.g01.view.menu;

import ulb.infof307.g01.view.ViewController;

/**
 * Contrôleur de vue pour la page principale des Menus.
 * Elle permet à d'accéder à la page de création de Menu, voir sa liste de Menus ou revenir à la page d'accueil
 * @see ulb.infof307.g01.model.Menu
 * */
public class HomeMenuViewController extends ViewController<HomeMenuViewController.Listener> {

    public void onUserMenusButtonClick() { listener.onUserMenusButtonClick(); }

    public void onCreateMenuButtonClick() { listener.onUserCreateMenuButtonClick(); }

    public void onBackButtonClick() { listener.onBackButtonClick(); }


    public void logout() {
        listener.logout();
    }

    public interface Listener {
        void onBackButtonClick();
        void onUserMenusButtonClick();
        void onUserCreateMenuButtonClick();

        void logout();
    }
}
